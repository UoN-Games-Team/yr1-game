package com.djammr.westernknights.util.loaders;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Json;
import com.djammr.westernknights.WKGame;
import com.djammr.westernknights.entity.EntityFactory;
import com.djammr.westernknights.entity.EntityManager;
import com.djammr.westernknights.entity.components.Box2DComponent;
import com.djammr.westernknights.entity.components.TransformComponent;
import com.djammr.westernknights.entity.systems.Box2DSystem;
import com.djammr.westernknights.util.MeshData;
import com.uwsoft.editor.renderer.data.*;
import com.uwsoft.editor.renderer.utils.CustomVariables;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * Loads maps and UIs from an Overlap2D project
 * <br/><br/>
 * Features:
 *     <ul>
 *         <li>Texture loading</li>
 *         <li>Loading of non-universal Components from a 'components' array custom variable e.g. [AComponent, ExampleComponent]</li>
 *         <li>Loading of Box2D Meshes created in Overlap2D</li>
 *     </ul>
 */
public class Overlap2DLoader {

    private static Json json = new Json();
    private static ComponentMapper<TransformComponent> transm = ComponentMapper.getFor(TransformComponent.class);


    /**
     * Loads an Overlap2D Scene, creating the entities and adding them to the {@link com.djammr.westernknights.entity.EntityManager}
     * @param projectPath FileHandle for Overlap2D project file
     * @param scenePath FileHandle for Overlap2D scene file to load
     * @param atlas path to Overlap2D project texture atlas
     * @param entityManager {@link com.djammr.westernknights.entity.EntityManager} instance to add entities to
     */
    public static void loadMap(FileHandle projectPath, FileHandle scenePath, TextureAtlas atlas, EntityManager entityManager) {
        WKGame.logger.logDebug("Loading Overlap2D Scene: " + scenePath);
        SceneVO sceneVO = json.fromJson(SceneVO.class, scenePath.readString());
        ProjectInfoVO projectVO = json.fromJson(ProjectInfoVO.class, projectPath.readString());
        CustomVariables customVars = new CustomVariables();

        for (SimpleImageVO item : sceneVO.composite.sImages) {
            WKGame.logger.logDebug(item.layerName);
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

            // Box2D Mesh
            MeshData meshData = null;
            if (item.physicsBodyData != null) {
                meshData = loadMesh(projectVO, item);
            }

            // Create the entity
            Entity entity = EntityFactory.createEntity(entityManager.getEngine().getSystem(Box2DSystem.class), meshData, components);
            TransformComponent transc = transm.get(entity);
            transc.x = item.x * WKGame.PIXELS_TO_METERS;
            transc.y = item.y * WKGame.PIXELS_TO_METERS;
            transc.z = item.zIndex;
            transc.rotation = item.rotation;
            if (entity.getComponent(Box2DComponent.class) != null) {
                entity.getComponent(Box2DComponent.class).body.setTransform(transc.x, transc.y, transc.rotation * MathUtils.degRad);
            }
            // Add the entity to the world
            entityManager.addEntity(entity, item.itemIdentifier);
        }
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
        bodyDef.angularDamping = item.physicsBodyData.damping;

        MassData massData = new MassData();
        massData.I = item.physicsBodyData.rotationalInertia;
        massData.mass = item.physicsBodyData.mass;
        massData.center.set(item.physicsBodyData.centerOfMass);

        MeshVO meshVO = projectVO.meshes.get(item.meshId+"");
        List<float[]> polygonVerts = new ArrayList<float[]>();
        for (int i = 0; i < meshVO.minPolygonData.length; i++) {
            float[] verts = new float[meshVO.minPolygonData[i].length * 2];
            for (int j = 0; j < verts.length; j += 2) {
                verts[j] = meshVO.minPolygonData[i][j / 2].x  * WKGame.PIXELS_TO_METERS;
                verts[j + 1] = meshVO.minPolygonData[i][j / 2].y * WKGame.PIXELS_TO_METERS;
                //WKGame.logger.logDebug(String.valueOf(verts[j]));
                //WKGame.logger.logDebug(String.valueOf(verts[j + 1]));
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
