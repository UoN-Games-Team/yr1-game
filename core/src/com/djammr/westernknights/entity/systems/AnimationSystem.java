package com.djammr.westernknights.entity.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.djammr.westernknights.entity.Box2DUserData;
import com.djammr.westernknights.entity.components.AnimationComponent;
import com.djammr.westernknights.entity.components.Box2DComponent;
import com.djammr.westernknights.entity.components.StateComponent;
import com.djammr.westernknights.entity.components.VisualComponent;

/**
 * Manages Entity Animations
 */
public class AnimationSystem extends IteratingSystem {

    private ComponentMapper<VisualComponent> vism = ComponentMapper.getFor(VisualComponent.class);
    private ComponentMapper<AnimationComponent> animm = ComponentMapper.getFor(AnimationComponent.class);
    private ComponentMapper<StateComponent> stm = ComponentMapper.getFor(StateComponent.class);
    private VisualComponent visc;
    private AnimationComponent animc;
    private StateComponent stc;


    public AnimationSystem() {
        super(Family.all(AnimationComponent.class, VisualComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        visc = vism.get(entity);
        animc = animm.get(entity);
        stc = stm.get(entity);

        switch (stc.state) {
            case JUMPING:
                animc.currentAnim = animc.animations.get("jump");
                break;
            case IDLE:
                animc.currentAnim = animc.animations.get("idle");
                break;
            case MOVING:
                animc.currentAnim = animc.animations.get("walk");
                break;
        }
        if (!stc.onGround) {
            animc.currentAnim = animc.animations.get("jump");
        }

        if (animc.currentAnim != null) {
            visc.sprite.setRegion(animc.currentAnim.getKeyFrame(animc.stateTime += deltaTime));
        }
    }
}
