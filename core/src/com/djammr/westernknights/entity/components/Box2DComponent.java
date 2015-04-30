package com.djammr.westernknights.entity.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.physics.box2d.Body;
import com.djammr.westernknights.entity.Box2DUserData;

/**
 * Holds Box2D data
 */
public class Box2DComponent extends WKComponent {

    public Body body;


    @Override
    public void reset() {
        ((Box2DUserData)body.getUserData()).collidingSensor = "";
        ((Box2DUserData)body.getUserData()).footContacts = 0;
    }
}
