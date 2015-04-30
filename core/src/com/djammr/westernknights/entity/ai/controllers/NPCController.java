package com.djammr.westernknights.entity.ai.controllers;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ai.steer.behaviors.Wander;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.djammr.westernknights.WKGame;
import com.djammr.westernknights.entity.ai.SteeringEntity;
import com.djammr.westernknights.entity.ai.behaviours.Wander2D;
import com.djammr.westernknights.util.Utils;

/**
 * Controller for NPCs
 */
public class NPCController extends AIController {

    private SteeringEntity steerable;


    public NPCController(Entity entity) {
        super(entity);
        steerable = new SteeringEntity(entity);
        steerable.setMaxLinearSpeed(steerable.getMaxLinearSpeed()/2.5f);
        steerable.setSteeringBehavior(new Wander2D(steerable));
    }

    @Override
    public void update(float deltaTime) {
        steerable.update(deltaTime);
    }

    @Override
    public void debugRender(ShapeRenderer renderer) {
        renderer.circle(((Wander2D) steerable.getSteeringBehavior()).getInternalTargetPosition(),
                steerable.getPosition().y, 0.2f);
    }

    @Override
    public void reset() {

    }

    public SteeringEntity getSteerable() {
        return steerable;
    }
}
