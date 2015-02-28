package com.djammr.westernknights.entity.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.djammr.westernknights.entity.Box2DUserData;
import com.djammr.westernknights.entity.EntityStates;
import com.djammr.westernknights.entity.components.*;

/**
 * Manages Entity Animations
 */
public class AnimationSystem extends IteratingSystem {

    private ComponentMapper<VisualComponent> vism = ComponentMapper.getFor(VisualComponent.class);
    private ComponentMapper<AnimationComponent> animm = ComponentMapper.getFor(AnimationComponent.class);
    private ComponentMapper<SpriterComponent> sprtm = ComponentMapper.getFor(SpriterComponent.class);
    private ComponentMapper<StateComponent> stm = ComponentMapper.getFor(StateComponent.class);
    private VisualComponent visc;
    private AnimationComponent animc;
    private SpriterComponent sprtc;
    private StateComponent stc;


    public AnimationSystem() {
        super(Family.all(VisualComponent.class).one(AnimationComponent.class, SpriterComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        visc = vism.get(entity);
        animc = animm.get(entity);
        sprtc = sprtm.get(entity);
        stc = stm.get(entity);

        // TODO: Implement as an enum component and just query that for names e.g. setAnim(Animations.JUMP)
        // TODO: Add bitwise operators for checking combined states
        String animName = null;
        switch (stc.state) {
            case EntityStates.JUMPING:
                animName = "jump";
                break;
            case EntityStates.IDLE:
                animName = "idle";
                break;
            case EntityStates.MOVING:
                animName = "run";
                break;
        }
        if (!stc.onGround) {
            animName = "jump";
        }

        // Set the animation
        if (animc != null) {
            animc.currentAnim = animc.animations.get(animName);
            visc.sprite.setRegion(animc.currentAnim.getKeyFrame(animc.stateTime += deltaTime));
        }
        if (sprtc != null) {
            sprtc.setFirstAnim(animName, 0f);
        }
    }
}
