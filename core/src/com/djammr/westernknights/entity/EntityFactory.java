package com.djammr.westernknights.entity;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.djammr.westernknights.entity.components.Box2DComponent;
import com.djammr.westernknights.entity.systems.Box2DSystem;

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
}
