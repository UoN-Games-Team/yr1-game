package com.djammr.westernknights.entity.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.djammr.westernknights.entity.components.Box2DComponent;
import com.djammr.westernknights.entity.components.TransformComponent;

/**
 * Manages the Box2D World
 */
public class Box2DSystem extends IteratingSystem {

    private ComponentMapper<TransformComponent> transm = ComponentMapper.getFor(TransformComponent.class);
    private ComponentMapper<Box2DComponent> b2dm = ComponentMapper.getFor(Box2DComponent.class);

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
        super(Family.all(Box2DComponent.class, TransformComponent.class).get());
        b2World = new World(new Vector2(0, -10), true);
        debugRenderer = new Box2DDebugRenderer();
    }

    @Override
    public void update(float deltaTime) {
        debugRenderer.render(b2World, debugCamera.combined);

        super.update(deltaTime);
        accumulator += deltaTime;
        loops = 0;
        while (accumulator > physicsTimeStep && loops < maxFrameSkip) {
            b2World.step(physicsTimeStep, velocityIterations, positionIterations);
            accumulator -= physicsTimeStep;
            loops++;
        }
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        TransformComponent posc = transm.get(entity);
        Box2DComponent b2dc = b2dm.get(entity);
        b2dc.body.setTransform(posc.x, posc.y, posc.rotation * MathUtils.degRad);
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

    /**
     * @param camera camera to use for the Box2D debug renderer
     */
    public void setDebugCamera(OrthographicCamera camera) {
        this.debugCamera = camera;
    }
}
