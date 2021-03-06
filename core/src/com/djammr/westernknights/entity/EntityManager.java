package com.djammr.westernknights.entity;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.djammr.westernknights.WKGame;
import com.djammr.westernknights.WKWorld;
import com.djammr.westernknights.entity.components.Box2DComponent;
import com.djammr.westernknights.entity.components.TransformComponent;
import com.djammr.westernknights.entity.components.WKComponent;
import com.djammr.westernknights.entity.systems.Box2DSystem;
import com.djammr.westernknights.entity.systems.InputSystem;
import com.djammr.westernknights.entity.systems.MovementSystem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Loads and keeps references to World objects
 */
public class EntityManager {

    private Engine ashley;
    /** Entity Maps arranged by user defined type **/
    private Map<String, List<Entity>> entities = new HashMap<String, List<Entity>>();


    public EntityManager() {
        ashley = new Engine();
    }

    public void update(float delta) {
        ashley.update(delta);
    }

    /**
     * Adds an entity to the world grouped by type
     * @param entity Entity instance to add
     * @param type type to group this entity under. E.g. wall
     */
    public void addEntity(Entity entity, String type) {
        WKGame.logger.logDebug("Adding entity: " + type);
        if (!entities.containsKey(type)) {
            entities.put(type, new ArrayList<Entity>());
        }
        entities.get(type).add(entity);
        ashley.addEntity(entity);
    }

    /**
     * @return number of world entities
     */
    public int getEntityCount() {
        return ashley.getEntitiesFor(Family.all().get()).size();
    }

    /**
     * Gets a List of entities identified by the type used to add them
     * @param type type of entities to get
     * @return List of entities identified by 'type'
     */
    public List<Entity> getEntities(String type) {
        return entities.get(type);
    }

    /**
     * Gets the first entity of 'type' added to the World.
     * Useful if you have a single entity type such as 'player'
     * @param type type of entity to get (used in addEntity)
     * @return the first entity identified by 'type'
     */
    public Entity getEntity(String type) {
        return entities.get(type).get(0);
    }

    public void reset() {
        for (Entity entity : ashley.getEntitiesFor(Family.all().get())) {
            for (Component component : entity.getComponents()) {
                ((WKComponent)component).reset();
            }
            if (entity.getComponent(Box2DComponent.class) != null
                    && !((Box2DUserData)(entity.getComponent(Box2DComponent.class).body.getUserData())).id.equals(WKWorld.PLAYER_IDENTIFIER))
            {
                entity.getComponent(Box2DComponent.class).body.setAwake(true);
                entity.getComponent(Box2DComponent.class).body.setTransform(
                        entity.getComponent(TransformComponent.class).origX,
                        entity.getComponent(TransformComponent.class).origY,
                        entity.getComponent(TransformComponent.class).origRotation * MathUtils.degRad
                );
            }
        }
    }

    /**
     * Called when the parent World is disposed
     */
    public void dispose() {
        WKGame.logger.logDebug("Disposing entities");
        for (int i=0; i < ashley.getSystems().size(); i++) {
            ashley.removeSystem(ashley.getSystems().get(i));
        }
        ashley.removeAllEntities();
    }

    /**
     * @param pause whether to pause all Ashley systems. False will resume paused systems
     */
    public void pauseSystems(boolean pause) {
        for (int i=0; i < ashley.getSystems().size(); i++) {
            ashley.getSystems().get(i).setProcessing(!pause);
        }
    }

    /**
     * @return Ashley Engine instance for adding systems
     */
    public Engine getEngine() {
        return ashley;
    }
}
