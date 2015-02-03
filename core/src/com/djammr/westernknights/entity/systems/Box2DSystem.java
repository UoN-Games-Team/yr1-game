package com.djammr.westernknights.entity.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.djammr.westernknights.WKGame;

/**
 * Manages the Box2D World
 */
public class Box2DSystem extends EntitySystem {

    private final float physicsTimeStep = 1/45f;
    private final int maxFrameSkip = 3;
    private final int velocityIterations = 8;
    private final int positionIterations = 3;
    private float accumulator = 0;
    private int loops;
    private World b2World;
    private Box2DDebugRenderer debugRenderer;
    private OrthographicCamera debugCamera;

    public Box2DSystem() {
        super();
        b2World = new World(new Vector2(0, -10), true);
        debugRenderer = new Box2DDebugRenderer();
    }

    @Override
    public void update(float deltaTime) {
        debugRenderer.render(b2World, debugCamera.combined);

        accumulator += deltaTime;
        loops = 0;
        while (accumulator > physicsTimeStep && loops < maxFrameSkip) {
            b2World.step(physicsTimeStep, velocityIterations, positionIterations);
            accumulator -= physicsTimeStep;
            loops++;
        }
    }

    @Override
    public void removedFromEngine(Engine engine) {
        super.removedFromEngine(engine);
        debugRenderer.dispose();
        b2World.dispose();
    }

    public World getB2World() {
        return b2World;
    }

    public void setDebugCamera(OrthographicCamera camera) {
        this.debugCamera = camera;
    }
}
