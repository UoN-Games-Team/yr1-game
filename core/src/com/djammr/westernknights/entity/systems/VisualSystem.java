package com.djammr.westernknights.entity.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.MathUtils;
import com.djammr.westernknights.entity.components.Box2DComponent;
import com.djammr.westernknights.entity.components.TransformComponent;
import com.djammr.westernknights.entity.components.VisualComponent;

/**
 * CUpdates animations and sprite positions
 */
public class VisualSystem extends IteratingSystem {

    private ComponentMapper<TransformComponent> transm = ComponentMapper.getFor(TransformComponent.class);
    private ComponentMapper<Box2DComponent> b2dm = ComponentMapper.getFor(Box2DComponent.class);
    private ComponentMapper<VisualComponent> vism = ComponentMapper.getFor(VisualComponent.class);


    public VisualSystem() {
        super(Family.all(VisualComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        TransformComponent transc = transm.get(entity);
        Box2DComponent b2dc = b2dm.get(entity);
        VisualComponent visc = vism.get(entity);

        if (b2dc != null) {
            visc.sprite.setPosition(b2dc.body.getPosition().x, b2dc.body.getPosition().y);
            visc.sprite.setRotation(b2dc.body.getAngle() * MathUtils.radDeg);
        }
        else if (transc != null) {
            visc.sprite.setPosition(transc.x, transc.y);
            visc.sprite.setRotation(transc.rotation);
        }
    }
}
