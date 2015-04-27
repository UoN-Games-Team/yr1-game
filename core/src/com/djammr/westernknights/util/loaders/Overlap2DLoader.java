package com.djammr.westernknights.util.loaders;

import box2dLight.ConeLight;
import box2dLight.Light;
import box2dLight.PointLight;
import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.graphics.ParticleEmitterBox2D;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.brashmonkey.spriter.Data;
import com.brashmonkey.spriter.Player;
import com.brashmonkey.spriter.PlayerTweener;
import com.brashmonkey.spriter.SCMLReader;
import com.djammr.westernknights.Assets;
import com.djammr.westernknights.WKGame;
import com.djammr.westernknights.WKWorld;
import com.djammr.westernknights.entity.Box2DUserData;
import com.djammr.westernknights.entity.EntityFactory;
import com.djammr.westernknights.entity.EntityManager;
import com.djammr.westernknights.entity.PhysicsFilters;
import com.djammr.westernknights.entity.ai.controllers.NPCController;
import com.djammr.westernknights.entity.components.*;
import com.djammr.westernknights.entity.components.ai.BehaviourComponent;
import com.djammr.westernknights.entity.components.ai.NodeComponent;
import com.djammr.westernknights.entity.systems.Box2DSystem;
import com.djammr.westernknights.entity.systems.RenderingSystem;
import com.djammr.westernknights.ui.Actors.ParticleEffectActor;
import com.djammr.westernknights.ui.Actors.SpriteAnimationActor;
import com.djammr.westernknights.util.spriter.LibGdxDrawer;
import com.djammr.westernknights.util.spriter.LibGdxLoader;
import com.uwsoft.editor.renderer.Overlap2D;
import com.uwsoft.editor.renderer.actor.SpriteAnimation.Animation;
import com.uwsoft.editor.renderer.data.*;
import com.uwsoft.editor.renderer.utils.CustomVariables;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * Loads maps and UIs from an Overlap2D project
 * <br/><br/>
 * Features:
 *     <ul>
 *         <li>Texture loading</li>
 *         <li>Loading of non-universal Entity Components from a 'components' array custom variable e.g. [AComponent, ExampleComponent]</li>
 *         <li>Loading of Box2D Meshes created in Overlap2D</li>
 *         <li>Loading of image objects</li>
 *         <li>Loading of Stage objects into a Stage instance</li>
 *     </ul>
 * <br/><br/>
 * The way this class is structured is pretty gross, but at least it's easy to add new features and extend functionality, so that's good, right?...
 */
public class Overlap2DLoader {

    public enum SceneType {
        UI, MAP
    }

    private static Json json = new Json();
    private static ComponentMapper<TransformComponent> transm = ComponentMapper.getFor(TransformComponent.class);
    private static ComponentMapper<VisualComponent> vism = ComponentMapper.getFor(VisualComponent.class);

    // Nasty time saving way of giving methods access the loadMap arguments.
    private static String projectPath;
    private static SceneVO sceneVO;
    private static ProjectInfoVO projectVO;
    private static CustomVariables customVars;
    private static Box2DSystem b2dSystem;
    private static TextureAtlas atlas;
    private static String spriteAnimationsPath;
    private static String spriterAnimationsPath;
    private static String particlesPath;
    private static EntityManager entityManager;
    private static String fontPath;
    private static Stage stage;
    private static Map<String, Actor> actors;
    private static List<String> layers;


    private static void setHandles(FileHandle projectPath, FileHandle scenePath) {
        Overlap2DLoader.projectPath = projectPath.parent().path();
        sceneVO = json.fromJson(SceneVO.class, scenePath.readString());
        projectVO = json.fromJson(ProjectInfoVO.class, projectPath.readString());
        spriteAnimationsPath = Overlap2DLoader.projectPath + "/orig/sprite_animations";
        spriterAnimationsPath = Overlap2DLoader.projectPath + "/orig/spriter_animations";
        particlesPath = Overlap2DLoader.projectPath + "/particles";
        customVars = new CustomVariables();
    }

    /**
     * Loads an Overlap2D Scene, creating the entities and adding them to the {@link com.djammr.westernknights.entity.EntityManager}
     * @param projectPath FileHandle for Overlap2D project file
     * @param scenePath FileHandle for Overlap2D scene file to load
     * @param atlas path to Overlap2D project texture atlas
     * @param entityManager {@link com.djammr.westernknights.entity.EntityManager} instance to add entities to
     */
    public static void loadMap(FileHandle projectPath, FileHandle scenePath, TextureAtlas atlas, EntityManager entityManager) {
        WKGame.logger.logDebug("Loading Overlap2D Scene: " + scenePath);
        setHandles(projectPath, scenePath);
        b2dSystem = entityManager.getEngine().getSystem(Box2DSystem.class);
        Overlap2DLoader.atlas = atlas;
        Overlap2DLoader.entityManager = entityManager;

        // Global Stuff
        //entityManager.getEngine().getSystem(RenderingSystem.class).bgColour = sceneVO.ambientColor;
        //b2dSystem.getRayHandler().setAmbientLight(0.2f, 0.2f, 0.2f, (scenePath.toString().contains("night"))? 0.1f : 0.8f); // TODO: no longer using the _night suffix
        WKWorld.AMBIENT_COLOUR.r = sceneVO.ambientColor[0];
        WKWorld.AMBIENT_COLOUR.g = sceneVO.ambientColor[1];
        WKWorld.AMBIENT_COLOUR.b = sceneVO.ambientColor[2];
        WKWorld.AMBIENT_COLOUR.a = (scenePath.toString().contains("night"))? WKWorld.AMBIENT_ALPHA_NIGHT : WKWorld.AMBIENT_ALPHA_DAY;
        b2dSystem.getRayHandler().setAmbientLight(WKWorld.AMBIENT_COLOUR);

        // Add Scene
        addScene(sceneVO, SceneType.MAP);
    }

    /**
     * Loads an Overlap2D Scene UI, creating the UI components and adding them to the Stage
     * @param projectPath FileHandle for Overlap2D project file
     * @param scenePath FileHandle for Overlap2D scene file to load
     * @param fontFolderPath Path to font folder containing Overlap2D ttfs
     * @param stage Stage instance to add components to
     * @param actors Map<String, Actor> to populate for receiving actors later. Keys are the identifiers set in Overlap2D.
     */
    public static void loadUI(FileHandle projectPath, FileHandle scenePath, TextureAtlas atlas, String fontFolderPath, Stage stage, Map<String, Actor> actors) {
        WKGame.logger.logDebug("Loading Overlap2D UI: " + scenePath);
        setHandles(projectPath, scenePath);
        Overlap2DLoader.fontPath = fontFolderPath;
        Overlap2DLoader.atlas = atlas;
        Overlap2DLoader.stage = stage;
        Overlap2DLoader.actors = actors;

        // Add Scene
        addScene(sceneVO, SceneType.UI);
    }

    /**
     * Parses a scene object and adds objects
     * @param sceneVO SceneVO to parse
     */
    private static void addScene(SceneVO sceneVO, SceneType sceneType) {
        // The index of the layer an item is on is prepended to the item's z-index to resolve layer clashes easily
        layers = new ArrayList<String>();
        for (LayerItemVO layerVO : sceneVO.composite.layers) {
            layers.add(layerVO.layerName);
        }
        addComposite(new CompositeItemVO(sceneVO.composite), sceneType);
    }

    /**
     * Recursively add objects from a CompositeItemVO including child composites
     * @param composite CompositeVO to add
     */
    private static void addComposite(CompositeItemVO composite, SceneType sceneType) {
        for (MainItemVO item : composite.composite.getAllItems(false)) {
            item.x += composite.x;
            item.y += composite.y;
            item.scaleX += composite.scaleX - 1f;
            item.scaleY += composite.scaleY - 1f;
            item.rotation += composite.rotation;

            if (item instanceof SimpleImageVO) {
                if (sceneType.equals(SceneType.MAP)) addEntity((SimpleImageVO)item);
                else if (sceneType.equals(SceneType.UI)) addUiImage((SimpleImageVO) item);
            }
            else if (item instanceof SpriteAnimationVO) {
                if (sceneType.equals(SceneType.MAP)) addEntity((SpriteAnimationVO)item);
                else if (sceneType.equals(SceneType.UI)) addUiSpriteAnimation((SpriteAnimationVO)item);
            }
            else if (item instanceof SpriterVO) {
                addEntity((SpriterVO)item);
            }
            else if (item instanceof LightVO) {
                addLight((LightVO) item);
            }
            else if (item instanceof ParticleEffectVO) {
                if (sceneType.equals(SceneType.MAP)) addParticle((ParticleEffectVO) item);
                else if (sceneType.equals(SceneType.UI)) addUiParticle((ParticleEffectVO) item);
            }
            else if (item instanceof LabelVO) {
                addUiLabel((LabelVO) item);
            }
            else if (item instanceof CompositeItemVO) {
                addComposite((CompositeItemVO) item, sceneType);
            }
        }
    }

    /**
     * Initialises a VisualComponent for an entity based on a MainItemVO
     */
    private static void initVisualComponent(MainItemVO item, Entity entity, TextureRegion region) {
        TransformComponent transc = transm.get(entity);
        VisualComponent visc = vism.get(entity);
        if (visc != null) {
            visc.sprite.setRegion(region);
            if (item.itemIdentifier.equals(WKWorld.PLAYER_IDENTIFIER)) {
                visc.sprite.setSize(WKWorld.PLAYER_WIDTH, WKWorld.PLAYER_HEIGHT);
            } else {
                visc.sprite.setSize(region.getRegionWidth() * WKGame.PIXELS_TO_METERS, region.getRegionHeight() * WKGame.PIXELS_TO_METERS);
                visc.sprite.setScale(item.scaleX, item.scaleY);
                visc.sprite.setRotation(transc.rotation);
            }
            visc.sprite.setPosition(transc.x, transc.y);
        }
    }

    /**
     * Adds an Entity from a MainItemVO
     * @param item MainItemVO to add
     * @param region initial TextureRegion to display, null for none
     * @param additionalComponents any additional  Entity Components to add
     */
    private static Entity addEntity(MainItemVO item, TextureRegion region, Component... additionalComponents) {
        // --- Components
        List<Component> components = new ArrayList<Component>();
        customVars.loadFromString(item.customVars);
        if (customVars.getStringVariable("components") != null) {
            for (String componentClass : customVars.getStringVariable("components").replaceAll("\\[|\\]", "").replace(" ", "").split(",")) {
                try {
                    components.add((Component) Class.forName("com.djammr.westernknights.entity.components." + componentClass).getConstructor().newInstance());
                } catch (NoSuchMethodException | ClassNotFoundException | InvocationTargetException | InstantiationException | IllegalAccessException ex) {
                    WKGame.logger.logDebug("Error loading component " + componentClass + ": " + ex.getLocalizedMessage());
                }
            }
        }
        components.addAll(Arrays.asList(additionalComponents));

        // --- Parallax
        if (customVars.getStringVariable("parallax") != null) {
            components.add(new ParallaxComponent(Float.valueOf(customVars.getStringVariable("parallax"))));
        }

        String entityType = customVars.getStringVariable("entity_type");
        Entity entity;
        // --- Box2D Mesh
        // --- Player
        if (item.itemIdentifier.equals(WKWorld.PLAYER_IDENTIFIER)) {
            entity = EntityFactory.createPlayer(item.itemIdentifier, b2dSystem, WKWorld.PLAYER_WIDTH, WKWorld.PLAYER_HEIGHT, components);
            customVars.setVariable("filter_category", "player");
            for (Fixture fixture : entity.getComponent(Box2DComponent.class).body.getFixtureList()) {
                Filter filter = fixture.getFilterData();
                filter.maskBits = PhysicsFilters.MASK_PLAYER;
                fixture.setFilterData(filter);
            }
        }
        // --- Enemy
        else if (entityType != null && entityType.equals(WKWorld.ENEMY_TYPE)) {
            float minX;
            float maxX;
            minX = entityManager.getEntity(item.itemIdentifier + "_node_" + 1).getComponent(TransformComponent.class).x;
            maxX = entityManager.getEntity(item.itemIdentifier + "_node_" + 2).getComponent(TransformComponent.class).x;
            entity = EntityFactory.createEnemy(item.itemIdentifier, b2dSystem, 2.3f, 3f, components, minX, maxX);
            customVars.setVariable("filter_category", "enemy");
        }
        // --- NPC
        else if (entityType != null && entityType.equals(WKWorld.NPC_TYPE)) {
            entity = EntityFactory.createNPC(item.itemIdentifier, b2dSystem, WKWorld.PLAYER_WIDTH, WKWorld.PLAYER_HEIGHT, components);
            // --- Node data
            if (customVars.getStringVariable("nodes") != null) {
                // Boundary nodes
                Float minX = null;
                Float maxX = null;
                int nodes = customVars.getStringVariable("nodes").replaceAll("\\[|\\]", "").replace(" ", "").split(",").length;
                if (nodes >= 1) minX = entityManager.getEntity(item.itemIdentifier + "_node_" + 1).getComponent(TransformComponent.class).x;
                if (nodes >= 2) maxX = entityManager.getEntity(item.itemIdentifier + "_node_" + 2).getComponent(TransformComponent.class).x;
                ((NPCController)entity.getComponent(BehaviourComponent.class).controller).getSteerable().setBoundaries(minX, maxX);
            }
            customVars.setVariable("filter_category", "actor");
        }
        else {
            MeshData meshData = null;
            if (item.physicsBodyData != null) {
                meshData = loadMesh(projectVO, item);
            }
            // --- Nodes
            if (entityType != null && entityType.equals(WKWorld.NODE_TYPE)) components.add(new NodeComponent());
            // Create the entity
            entity = EntityFactory.createEntity(item.itemIdentifier, b2dSystem, meshData, components);
        }

        // --- Physics Category
        if (customVars.getStringVariable("filter_category") != null) {
            for (Fixture fixture : entity.getComponent(Box2DComponent.class).body.getFixtureList()) {
                Filter filter = fixture.getFilterData();
                filter.categoryBits = PhysicsFilters.fromString(customVars.getStringVariable("filter_category"));
                fixture.setFilterData(filter);
            }
        }

        // --- Set Position
        TransformComponent transc = transm.get(entity);
        transc.x = item.x * WKGame.PIXELS_TO_METERS;
        transc.y = item.y * WKGame.PIXELS_TO_METERS;
        transc.z = Integer.parseInt(layers.indexOf(item.layerName) + "" + item.zIndex); // resolves z-index layer clashes
        transc.rotation = item.rotation;
        if (entity.getComponent(Box2DComponent.class) != null) {
            entity.getComponent(Box2DComponent.class).body.setTransform(transc.x, transc.y, transc.rotation * MathUtils.degRad);
            ((Box2DUserData)entity.getComponent(Box2DComponent.class).body.getUserData()).id = item.itemIdentifier;
        }

        // --- Set Texture
        if (region != null) {
            initVisualComponent(item, entity, region);
        }

        // --- Add the entity to the world
        entityManager.addEntity(entity, item.itemIdentifier);
        return entity;
    }

    /**
     * Adds an Entity from a SimpleImageVO
     * @param item SimpleImageVO to add
     */
    private static void addEntity(SimpleImageVO item) {
        // Texture
        TextureRegion region = atlas.findRegion(item.imageName);
        addEntity(item, region, ((region != null) ? new VisualComponent() : null));
    }

    /**
     * Adds an Entity from a SpriterVO
     * @param item SpriterVO to add
     */
    private static void addEntity(final SpriterVO item) {
        final FileHandle spriterFile = Gdx.files.internal(spriterAnimationsPath + "/" + item.animationName + "/" + item.animationName + ".scml");
        final Data data = new SCMLReader(spriterFile.read()).getData();

        final Entity entity = addEntity(item, null, new VisualComponent(), new SpriterComponent());
        final SpriterComponent spriterComponent = entity.getComponent(SpriterComponent.class);

        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                spriterComponent.loader = new LibGdxLoader(data);
                spriterComponent.loader.load(spriterFile.file());
                spriterComponent.player = new PlayerTweener(data.getEntity(item.entity));
                spriterComponent.player.setScale(item.scale * WKGame.PIXELS_TO_METERS);
                spriterComponent.player.setPosition(item.x, item.y);
                //spriterComponent.player.setBaseAnimation(item.animation);
                spriterComponent.player.getFirstPlayer().setAnimation(item.animation);
                spriterComponent.drawer = new LibGdxDrawer(spriterComponent.loader, null, null);
            }
        });
    }

    /**
     * Adds an Entity from a SpriteAnimationVO
     * @param item SpriteAnimationVO to add
     */
    private static void addEntity(final SpriteAnimationVO item) {
        final Entity entity = addEntity(item, null, new VisualComponent(), new AnimationComponent());

        // Animations
        final HashMap<String, Animation> animations = new HashMap<String, Animation>();
        animations.putAll(Animation.constructJsonObject(item.animations));

        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                AnimationComponent ac = entity.getComponent(AnimationComponent.class);
                // Animation Atlas
                Assets.load(spriteAnimationsPath + "/" + item.animationName + "/" + item.animationName + ".atlas", TextureAtlas.class);
                Assets.manager.finishLoading();
                TextureAtlas ta = Assets.manager.get(spriteAnimationsPath + "/" + item.animationName + "/" + item.animationName + ".atlas", TextureAtlas.class);

                for (String animName : animations.keySet()) {
                    ac.animations.put(animName, Assets.createAnimation(1f/item.fps, ta, animations.get(animName).startFrame, animations.get(animName).endFrame));
                    WKGame.logger.logDebug(animName);
                }
                ac.currentAnim = ac.animations.get(animations.keySet().iterator().next());
                initVisualComponent(item, entity, ac.currentAnim.getKeyFrame(0));
            }
        });
    }

    /**
     * Adds a Box2D light from a LightVO
     * @param light LightVO to add
     */
    private static void addLight(LightVO light) {
        Light nlight = null;
        switch (light.type) {
            case POINT:
                nlight = new PointLight(b2dSystem.getRayHandler(), light.rays);
                break;
            case CONE:
                nlight = new ConeLight(b2dSystem.getRayHandler(), light.rays, null, 0, 0, 0, 0, light.coneDegree);
                break;
        }
        if (nlight != null) {
            float alpha = 1;
            customVars.loadFromString(light.customVars);
            if (customVars.getStringVariable("alpha") != null) {
                alpha = Float.parseFloat(customVars.getStringVariable("alpha"));
            }
            nlight.setColor(light.tint[0], light.tint[1], light.tint[2], alpha);
            nlight.setDistance(light.distance * WKGame.PIXELS_TO_METERS);
            nlight.setXray(light.isXRay);
            nlight.setStaticLight(light.isStatic);
            nlight.setPosition(light.x * WKGame.PIXELS_TO_METERS, light.y * WKGame.PIXELS_TO_METERS);
            //nlight.setDirection(light.directionDegree);
            nlight.setDirection(light.rotation);
            b2dSystem.registerLight(nlight);
        }
    }

    /**
     * Adds a ParticleEffect from a ParticleEffectVO. Uses ParticleEmitterBox2D
     * @param item ParticleEffectVO to add
     */
    private static void addParticle(ParticleEffectVO item) {
        ParticleEffect particleEffect = new ParticleEffect();
        particleEffect.load(Gdx.files.internal(particlesPath + "/" + item.particleName), atlas);
        particleEffect.scaleEffect(WKGame.PIXELS_TO_METERS * item.scaleX);

        for (int i=0; i < particleEffect.getEmitters().size; i++) {
            particleEffect.getEmitters().set(i, new ParticleEmitterBox2D(b2dSystem.getB2World(), particleEffect.getEmitters().get(i)));
            ParticleEmitter emitter = particleEffect.getEmitters().get(i);

            // Spawn scaling doesn't seem to alter the actual dimensions of the effect like in the editor
            /*emitter.getSpawnWidth().setHigh(emitter.getSpawnWidth().getHighMin() * item.scaleX, emitter.getSpawnWidth().getHighMax() * item.scaleX);
            emitter.getSpawnWidth().setLow(emitter.getSpawnWidth().getLowMin() * item.scaleX, emitter.getSpawnWidth().getLowMax() * item.scaleX);
            emitter.getSpawnHeight().setHigh(emitter.getSpawnHeight().getHighMin() * item.scaleY, emitter.getSpawnHeight().getHighMax() * item.scaleY);
            emitter.getSpawnHeight().setLow(emitter.getSpawnHeight().getLowMin() * item.scaleY, emitter.getSpawnHeight().getLowMax() * item.scaleY);*/

            emitter.setPosition(item.x * WKGame.PIXELS_TO_METERS, item.y * WKGame.PIXELS_TO_METERS);
        }
        entityManager.getEngine().getSystem(RenderingSystem.class).addParticle(particleEffect);
    }

    /**
     * Adds a ParticleEffect from a ParticleEffectVO to the Stage
     * @param item ParticleEffectVO to add
     */
    private static void addUiParticle(ParticleEffectVO item) {
        ParticleEffectActor particleEffect = new ParticleEffectActor(Gdx.files.internal(particlesPath + "/" + item.particleName), atlas);
        //particleEffect.getEffect().scaleEffect(item.scaleX);

        /*for (int i=0; i < particleEffect.getEffect().getEmitters().size; i++) {
            particleEffect.getEffect().getEmitters().set(i, new ParticleEmitter());
            ParticleEmitter emitter = particleEffect.getEffect().getEmitters().get(i);

            emitter.setPosition(item.x, item.y);
        }*/
        particleEffect.start();
        addUiItem(particleEffect, item);
    }

    /**
     * Adds a Sprite Animation from a SpriteAnimationVO to the Stage
     * @param item SpriteAnimationVO to add
     */
    private static void addUiSpriteAnimation(final SpriteAnimationVO item) {
        final SpriteAnimationActor spriteActor = new SpriteAnimationActor();
        // Animations
        final HashMap<String, Animation> animations = new HashMap<String, Animation>();
        animations.putAll(Animation.constructJsonObject(item.animations));
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                // Animation Atlas
                Assets.load(spriteAnimationsPath + "/" + item.animationName + "/" + item.animationName + ".atlas", TextureAtlas.class);
                Assets.manager.finishLoading();
                TextureAtlas ta = Assets.manager.get(spriteAnimationsPath + "/" + item.animationName + "/" + item.animationName + ".atlas", TextureAtlas.class);

                for (String animName : animations.keySet()) {
                    spriteActor.getAnimations().put(animName, Assets.createAnimation(1f / item.fps, ta, animations.get(animName).startFrame, animations.get(animName).endFrame));
                }
                spriteActor.setAnimation(spriteActor.getAnimations().get(animations.keySet().iterator().next()));
            }
        });

        addUiItem(spriteActor, item);
    }

    /**
     * Adds a Label to the stage from a LabelVO
     * @param item LabelVO to add
     */
    private static void addUiLabel(final LabelVO item) {
        final Label label = new Label(item.text, Assets.skinDefault);
        addUiItem(label, item);
        label.setAlignment(item.align);

        // Queue adding Label style as it requires an OpenGL Context
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                label.setStyle(new Label.LabelStyle(Assets.getFont(item.style + "-" + item.size, fontPath + "/" + item.style + ".ttf"), new Color(item.tint[0], item.tint[1], item.tint[2], item.tint[3])));
                label.setSize(item.width, item.height);
            }
        });
    }

    /**
     * Adds an Image to the stage from a SimpleImageVO
     * @param item SimpleImageVO to add
     */
    private static void addUiImage(SimpleImageVO item) {
        Image image = new Image(atlas.findRegion(item.imageName));
        addUiItem(image, item);
    }

    /**
     * Configures and adds an Actor to the stage. The actor is also added to the actors map
     * @param actor Actor to add
     * @param item MainItemVO to configure from
     */
    private static void addUiItem(Actor actor, MainItemVO item) {
        float screenWidthScale = stage.getWidth() / WKGame.SCREEN_WIDTH;
        float screenHeightScale = stage.getHeight() / WKGame.SCREEN_HEIGHT;
        actor.setPosition(item.x * screenWidthScale, item.y * screenHeightScale);
        actor.setRotation(item.rotation);
        actor.setScaleX(item.scaleX * screenWidthScale);
        actor.setScaleY(item.scaleY * screenHeightScale);
        actor.setZIndex(Integer.parseInt(layers.indexOf(item.layerName) + "" + item.zIndex));

        stage.addActor(actor);
        actors.put(item.itemIdentifier, actor);
    }

    /**
     * Loads Box2D Mesh data for a SimpleImageVO from an Overlap2D Project
     * @param projectVO the ProjectInfoVO to load from
     * @param item the SimpleImageVO to load the mesh for
     * @return created {@link MeshData} object
     */
    public static MeshData loadMesh(ProjectInfoVO projectVO, MainItemVO item) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.values()[item.physicsBodyData.bodyType];
        bodyDef.allowSleep = true; //item.physicsBodyData.allowSleep;
        bodyDef.gravityScale = item.physicsBodyData.gravityScale;
        bodyDef.awake = item.physicsBodyData.awake;
        bodyDef.bullet = item.physicsBodyData.bullet;
        bodyDef.linearDamping = item.physicsBodyData.damping;


        MassData massData = new MassData();
        massData.I = item.physicsBodyData.rotationalInertia;
        massData.mass = item.physicsBodyData.mass;
        massData.center.set(item.physicsBodyData.centerOfMass);

        MeshVO meshVO = projectVO.meshes.get(item.meshId+"");
        List<float[]> polygonVerts = new ArrayList<float[]>();
        for (int i = 0; i < meshVO.minPolygonData.length; i++) {
            float[] verts = new float[meshVO.minPolygonData[i].length * 2];
            for (int j = 0; j < verts.length; j += 2) {
                verts[j] = meshVO.minPolygonData[i][j / 2].x  * WKGame.PIXELS_TO_METERS * item.scaleX;
                verts[j + 1] = meshVO.minPolygonData[i][j / 2].y * WKGame.PIXELS_TO_METERS * item.scaleY;
            }
            polygonVerts.add(verts);
        }

        FixtureDef[] fixtureDefs = new FixtureDef[polygonVerts.size()];
        PolygonShape poly;
        for (int i = 0; i < polygonVerts.size(); i++) {
            poly = new PolygonShape();
            poly.set(polygonVerts.get(i));
            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.shape = poly;
            fixtureDef.density = item.physicsBodyData.density;
            fixtureDef.friction = item.physicsBodyData.friction;
            fixtureDef.restitution = item.physicsBodyData.restitution;
            if (customVars.getStringVariable("body_type") != null) {
                if (customVars.getStringVariable("body_type").equals("sensor"))
                    fixtureDef.isSensor = true;
            }
            fixtureDefs[i] = fixtureDef;
        }

        return new MeshData(bodyDef, massData, fixtureDefs);
    }
}
