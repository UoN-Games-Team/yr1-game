package com.djammr.westernknights.entity.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.djammr.westernknights.WKGame;
import com.djammr.westernknights.entity.Box2DUserData;
import com.djammr.westernknights.entity.components.Box2DComponent;
import com.djammr.westernknights.entity.components.MovementComponent;

/**
 * Handles movement via flags
 */
public class MovementSystem extends IteratingSystem {

    private ComponentMapper<MovementComponent> mvm = ComponentMapper.getFor(MovementComponent.class);
    private ComponentMapper<Box2DComponent> b2dm = ComponentMapper.getFor(Box2DComponent.class);
    private MovementComponent mvc;
    private Box2DComponent b2dc;
    private Box2DUserData b2dUserData;


    public MovementSystem() {
        super(Family.all(MovementComponent.class, Box2DComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        mvc = mvm.get(entity);
        b2dc = b2dm.get(entity);
        b2dUserData = (Box2DUserData)b2dc.body.getUserData();

        if (mvc.stop) {
            resetSpeed();
            mvc.stop = false;
        }

        if (mvc.right) {
            if (b2dc.body.getLinearVelocity().x < mvc.speed) {
                b2dc.body.applyLinearImpulse(mvc.speed / 2, 0,
                        b2dc.body.getWorldCenter().x, b2dc.body.getWorldCenter().y, true);
            }
        }
        else if (mvc.left) {
            if (b2dc.body.getLinearVelocity().x > -mvc.speed) {
                b2dc.body.applyLinearImpulse(-mvc.speed / 2, 0,
                        b2dc.body.getWorldCenter().x, b2dc.body.getWorldCenter().y, true);
            }
        }
        else {
            //resetSpeed(); //TODO: Test if resetting the speed is required when no movement should happen
        }

        if (mvc.jump) {
            if (b2dUserData.onGround) b2dc.body.applyLinearImpulse(0, mvc.jumpForce, b2dc.body.getWorldCenter().x, b2dc.body.getWorldCenter().y, true);
            mvc.jump = false;
        }
    }

    private void resetSpeed() {
        b2dc.body.setLinearVelocity(0, b2dc.body.getLinearVelocity().y);
    }
}
