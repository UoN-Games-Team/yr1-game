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
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Json;
import com.djammr.westernknights.Assets;
import com.djammr.westernknights.WKGame;
import com.djammr.westernknights.entity.EntityFactory;
import com.djammr.westernknights.entity.EntityManager;
import com.djammr.westernknights.entity.components.Box2DComponent;
import com.djammr.westernknights.entity.components.TransformComponent;
import com.djammr.westernknights.entity.components.VisualComponent;
import com.djammr.westernknights.entity.systems.Box2DSystem;
import com.djammr.westernknights.entity.systems.RenderingSystem;
import com.djammr.westernknights.util.MeshData;
import com.uwsoft.editor.renderer.data.*;
import com.uwsoft.editor.renderer.utils.CustomVariables;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
 * The way this class is structured is pretty gross, but at least it's easy to add new features and extend functionality, so that's good, right?
 */
public class Overlap2DLoader {

    private static Json json = new Json();
    private static ComponentMapper<TransformComponent> transm = ComponentMapper.getFor(TransformComponent.class);
    private static ComponentMapper<VisualComponent> vism = ComponentMapper.getFor(VisualComponent.class);

    // Nasty time saving way of giving methods access the loadMap arguments.
    private static SceneVO sceneVO;
    private static ProjectInfoVO projectVO;
    private static CustomVariables customVars;
    private static Box2DSystem b2dSystem;
    private static TextureAtlas atlas;
    private static EntityManager entityManager;
    private static String fontPath;
    private static Stage stage;
    private static Map<String, Actor> actors;


    private static void setHandles(FileHandle projectPath, FileHandle scenePath) {
        sceneVO = json.fromJson(SceneVO.class, scenePath.readString());
        projectVO = json.fromJson(ProjectInfoVO.class, projectPath.readString());
        customVars = new CustomVariables();
    }
    /** Makes me feel a little bit sick, but 'tis the way for now */
    private static void clearInstances() {
        sceneVO = null;
        projectVO = null;
        customVars = null;
        b2dSystem = null;
        atlas = null;
        entityManager = null;
        fontPath = null;
        stage = null;
        actors = null;
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
        entityManager.getEngine().getSystem(RenderingSystem.class).bgColour = sceneVO.ambientColor;
        b2dSystem.getRayHandler().setAmbientLight(255, 255, 255, 0.4f);

        // Add Scene composite
        addComposite(new CompositeItemVO(sceneVO.composite));
    }

    /**
     * Loads an Overlap2D Scene UI, creating the UI components and adding them to the Stage
     * @param projectPath FileHandle for Overlap2D project file
     * @param scenePath FileHandle for Overlap2D scene file to load
     * @param fontFolderPath Path to font folder containing Overlap2D ttfs
     * @param stage Stage instance to add components to
     * @param actors Map<String, Actor> to populate for receiving actors later. Keys are the identifiers set in Overlap2D.
     */
    public static void loadUI(FileHandle projectPath, FileHandle scenePath, String fontFolderPath, Stage stage, Map<String, Actor> actors) {
        WKGame.logger.logDebug("Loading Overlap2D UI: " + scenePath);
        setHandles(projectPath, scenePath);
        Overlap2DLoader.fontPath = fontFolderPath;
        Overlap2DLoader.stage = stage;
        Overlap2DLoader.actors = actors;

        // Add Scene composite
        addComposite(new CompositeItemVO(sceneVO.composite));
    }

    /**
     * Recursively add objects from a CompositeItemVO including child composites
     * @param composite CompositeVO to add
     */
    private static void addComposite(CompositeItemVO composite) {
        for (MainItemVO item : composite.composite.getAllItems(false)) {
            item.x += composite.x;
            item.y += composite.y;
            item.scaleX += composite.scaleX - 1f;
            item.scaleY += composite.scaleY - 1f;
            item.rotation += composite.rotation;

            if (item instanceof SimpleImageVO) {
                addEntity((SimpleImageVO)item);
            }
            else if (item instanceof LightVO) {
                addLight((LightVO)item);
            }
            else if (item instanceof LabelVO) {
                addLabel((LabelVO)item);
            }
            else if (item instanceof CompositeItemVO) {
                addComposite((CompositeItemVO)item);
            }
        }
    }

    /**
     * Adds an Entity from a SimpleImageVO
     * @param item SimpleImageVO to add
     */
    private static void addEntity(SimpleImageVO item) {
        // Texture
        TextureRegion region = atlas.findRegion(item.imageName);

        // Components
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
        // TODO: custom var to set invisible (no texture just Box2D mesh)
        if (region != null) {
            components.add(new VisualComponent());
        }

        // Box2D Mesh
        MeshData meshData = null;
        if (item.physicsBodyData != null) {
            meshData = loadMesh(projectVO, item);
        }

        // Create the entity
        Entity entity = EntityFactory.createEntity(b2dSystem, meshData, components);
        // Set Position
        TransformComponent transc = transm.get(entity);
        transc.x = item.x * WKGame.PIXELS_TO_METERS;
        transc.y = item.y * WKGame.PIXELS_TO_METERS;
        transc.z = item.zIndex;
        transc.rotation = item.rotation;
        if (entity.getComponent(Box2DComponent.class) != null) {
            entity.getComponent(Box2DComponent.class).body.setTransform(transc.x, transc.y, transc.rotation * MathUtils.degRad);
        }
        // Set Texture
        VisualComponent visc = vism.get(entity);
        if (visc != null) {
            visc.sprite.setRegion(region);
            visc.sprite.setSize(region.getRegionWidth() * WKGame.PIXELS_TO_METERS, region.getRegionHeight() * WKGame.PIXELS_TO_METERS);
            visc.sprite.setScale(item.scaleX, item.scaleY);
            visc.sprite.setRotation(transc.rotation);
            visc.sprite.setPosition(transc.x, transc.y);
        }
        // Add the entity to the world
        entityManager.addEntity(entity, item.itemIdentifier);
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
        }
    }

    private static void addLabel(final LabelVO label) {
        final Label nlabel = new Label(label.text, Assets.skinDefault);
        nlabel.setPosition(label.x, label.y);
        nlabel.setRotation(label.rotation);
        nlabel.setAlignment(label.align);
        //nlabel.setStyle(new Label.LabelStyle(Assets.getFont(label.style + "-" + label.size, fontPath+"/"+label.style+".ttf"), new Color(label.tint[0], label.tint[1], label.tint[2], label.tint[3])));
        stage.addActor(nlabel);
        actors.put(label.itemIdentifier, nlabel);

        // Queue adding Label style as it requires an OpenGL Context
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                nlabel.setStyle(new Label.LabelStyle(Assets.getFont(label.style + "-" + label.size, fontPath+"/"+label.style+".ttf"), new Color(label.tint[0], label.tint[1], label.tint[2], label.tint[3])));
            }
        });
    }

    /**
     * Loads Box2D Mesh data for a SimpleImageVO from an Overlap2D Project
     * @param projectVO the ProjectInfoVO to load from
     * @param item the SimpleImageVO to load the mesh for
     * @return created {@link MeshData} object
     */
    public static MeshData loadMesh(ProjectInfoVO projectVO, SimpleImageVO item) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.values()[item.physicsBodyData.bodyType];
        bodyDef.allowSleep = item.physicsBodyData.allowSleep;
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
        PolygonShape poly = new PolygonShape();
        for (int i = 0; i < polygonVerts.size(); i++) {
            poly.set(polygonVerts.get(i));
            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.shape = poly;
            fixtureDef.density = item.physicsBodyData.density;
            fixtureDef.friction = item.physicsBodyData.friction;
            fixtureDef.restitution = item.physicsBodyData.restitution;
            fixtureDefs[i] = fixtureDef;
        }

        return new MeshData(bodyDef, massData, fixtureDefs);
    }
}
