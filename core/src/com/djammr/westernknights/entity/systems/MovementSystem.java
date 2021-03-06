package com.djammr.westernknights.entity.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.djammr.westernknights.WKGame;
import com.djammr.westernknights.entity.Box2DUserData;
import com.djammr.westernknights.entity.EntityStates;
import com.djammr.westernknights.entity.components.Box2DComponent;
import com.djammr.westernknights.entity.components.MovementComponent;
import com.djammr.westernknights.entity.components.StateComponent;
import com.djammr.westernknights.entity.components.TransformComponent;

/**
 * Handles movement via flags
 */
public class MovementSystem extends IteratingSystem {

    private ComponentMapper<MovementComponent> mvm = ComponentMapper.getFor(MovementComponent.class);
    private ComponentMapper<Box2DComponent> b2dm = ComponentMapper.getFor(Box2DComponent.class);
    private ComponentMapper<StateComponent> stm = ComponentMapper.getFor(StateComponent.class);
    private MovementComponent mvc;
    private Box2DComponent b2dc;
    private StateComponent stc;


    public MovementSystem() {
        super(Family.all(MovementComponent.class, Box2DComponent.class, StateComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        mvc = mvm.get(entity);
        b2dc = b2dm.get(entity);
        stc = stm.get(entity);

        if (mvc.stop) {
            resetSpeed();
            mvc.stop = false;
            //stc.state = (stc.state != EntityStates.IDLE)? stc.state & EntityStates.IDLE : EntityStates.IDLE;
            stc.state = EntityStates.IDLE;
        }

        if (mvc.right) {
            if (b2dc.body.getLinearVelocity().x < mvc.speed) {
                b2dc.body.applyLinearImpulse(mvc.speed / 2, ((stc.onGround)? -1f : 0),
                        b2dc.body.getWorldCenter().x, b2dc.body.getWorldCenter().y, true);
            }

            //stc.state = (stc.state != EntityStates.MOVING)? stc.state & EntityStates.MOVING : EntityStates.MOVING;
            stc.state = EntityStates.MOVING;
        }
        else if (mvc.left) {
            if (b2dc.body.getLinearVelocity().x > -mvc.speed) {
                b2dc.body.applyLinearImpulse(-mvc.speed / 2, ((stc.onGround)? -1f : 0),
                        b2dc.body.getWorldCenter().x, b2dc.body.getWorldCenter().y, true);
            }
            //stc.state = (stc.state != EntityStates.MOVING)? stc.state & EntityStates.MOVING : EntityStates.MOVING;
            stc.state = EntityStates.MOVING;
        }
        else {
            resetSpeed();
        }

        if (mvc.jump) {
            if (stc.onGround) b2dc.body.applyLinearImpulse(0, mvc.jumpForce, b2dc.body.getWorldCenter().x, b2dc.body.getWorldCenter().y, true);
            mvc.jump = false;
        }
    }

    private void resetSpeed() {
        b2dc.body.setLinearVelocity(0, b2dc.body.getLinearVelocity().y);
    }
}
