package com.djammr.westernknights.entity;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.physics.box2d.*;
import com.djammr.westernknights.entity.components.Box2DComponent;
import com.djammr.westernknights.entity.components.TransformComponent;
import com.djammr.westernknights.entity.systems.Box2DSystem;
import com.djammr.westernknights.util.MeshData;

import java.util.List;

/**
 * Creates Entities based on a file or given criteria
 */
public class EntityFactory {


    public static Entity createEntity(Box2DSystem box2DSystem, Component... components) {
        Entity entity = new Entity();
        entity.add(new Box2DComponent());

        BodyDef bodyDef = new BodyDef();
        PolygonShape poly = new PolygonShape();
        poly.setAsBox(1, 1.8f);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = poly;

        Body body = box2DSystem.getB2World().createBody(bodyDef);
        body.createFixture(fixtureDef);
        body.setTransform(0, 0, 0);
        entity.getComponent(Box2DComponent.class).body = body;
        poly.dispose();

        for (Component component : components) {
            entity.add(component);
        }

        return entity;
    }

    /**
     * Creates an new Entity based on the follow:
     * @param box2DSystem the {@link Box2DSystem} with the Box2D World to add the Body to
     * @param meshData {@link MeshData} object to load from
     * @param components List of Components to add to the entity
     * @return the created Entity
     */
    public static Entity createEntity(Box2DSystem box2DSystem, MeshData meshData, List<Component> components) {
        Entity entity = new Entity();

        if (meshData != null) {
            if (entity.getComponent(Box2DComponent.class) == null) entity.add(new Box2DComponent());
            Body body = box2DSystem.getB2World().createBody(meshData.bodyDef);
            body.setMassData(meshData.massData);
            for (FixtureDef fixtureDef : meshData.fixtureDefs) {
                body.createFixture(fixtureDef);
            }
            entity.getComponent(Box2DComponent.class).body = body;
        }

        entity.add(new TransformComponent());
        for (Component component : components) {
            entity.add(component);
        }

        return entity;
    }
}
