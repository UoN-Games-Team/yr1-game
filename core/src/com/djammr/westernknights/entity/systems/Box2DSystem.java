package com.djammr.westernknights.entity.systems;

import box2dLight.RayHandler;
import com.badlogic.ashley.core.*;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.djammr.westernknights.WKGame;
import com.djammr.westernknights.WKWorld;
import com.djammr.westernknights.entity.Box2DUserData;
import com.djammr.westernknights.entity.components.Box2DComponent;
import com.djammr.westernknights.entity.components.TransformComponent;

/**
 * Manages the Box2D World
 */
public class Box2DSystem extends IteratingSystem implements ContactListener {

    private ComponentMapper<TransformComponent> transm = ComponentMapper.getFor(TransformComponent.class);
    private ComponentMapper<Box2DComponent> b2dm = ComponentMapper.getFor(Box2DComponent.class);

    private final float physicsTimeStep = 1/45f;
    private final int maxFrameSkip = 3;
    private final int velocityIterations = 6;
    private final int positionIterations = 2;
    private float accumulator = 0;
    private int loops;
    private boolean stepped = false;
    private boolean debugEnabled = true;
    private World b2World;
    private RayHandler rayHandler;
    private Box2DDebugRenderer debugRenderer;
    private OrthographicCamera camera;

    public Box2DSystem() {
        super(Family.all(Box2DComponent.class, TransformComponent.class).get());
        b2World = new World(new Vector2(0, -14), true);
        b2World.setContactListener(this);
        rayHandler = new RayHandler(b2World, 320, 180);
        debugRenderer = new Box2DDebugRenderer();
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        if (Gdx.input.isKeyJustPressed(WKGame.DEBUG_KEY)) {
            debugEnabled = !debugEnabled;
        }
        if (debugEnabled) debugRenderer.render(b2World, camera.combined);
        rayHandler.setCombinedMatrix(camera.combined);
        rayHandler.render();

        accumulator += deltaTime;
        loops = 0;
        stepped = false;
        while (accumulator > physicsTimeStep && loops < maxFrameSkip) {
            b2World.step(physicsTimeStep, velocityIterations, positionIterations);
            accumulator -= physicsTimeStep;
            loops++;
            stepped = true;
        }
        if (stepped) rayHandler.update();
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        TransformComponent posc = transm.get(entity);
        Box2DComponent b2dc = b2dm.get(entity);
        //b2dc.body.setTransform(posc.x, posc.y, posc.rotation * MathUtils.degRad);
    }

    @Override
    public void removedFromEngine(Engine engine) {
        super.removedFromEngine(engine);
        debugRenderer.dispose();
        rayHandler.dispose();
        b2World.dispose();
    }

    public World getB2World() {
        return b2World;
    }

    public RayHandler getRayHandler() {
        return rayHandler;
    }

    /**
     * @param camera camera to use for the Box2D debug renderer and lights
     */
    public void setCamera(OrthographicCamera camera) {
        this.camera = camera;
    }


    // Contact Listener Methods TODO: Better way of detecting collisions?
    Body bodyA;
    Body bodyB;
    Box2DUserData userDataA;
    Box2DUserData userDataB;
    @Override
    public void beginContact(Contact contact) {
        bodyA = contact.getFixtureA().getBody();
        bodyB = contact.getFixtureB().getBody();
        userDataA = (Box2DUserData)bodyA.getUserData();
        userDataB = (Box2DUserData)bodyB.getUserData();


        if (userDataA != null && userDataA.id.equals(WKWorld.GROUND_IDENTIFIER)) {
            if (userDataB != null) userDataB.onGround = true;
        } else if (userDataB != null && userDataB.id.equals(WKWorld.GROUND_IDENTIFIER)) {
            if (userDataA != null) userDataA.onGround = true;
        }
    }

    @Override
    public void endContact(Contact contact) {
        bodyA = contact.getFixtureA().getBody();
        bodyB = contact.getFixtureB().getBody();
        userDataA = (Box2DUserData)bodyA.getUserData();
        userDataB = (Box2DUserData)bodyB.getUserData();

        if (userDataA != null && userDataA.id.equals(WKWorld.GROUND_IDENTIFIER)) {
            if (userDataB != null) userDataB.onGround = false;
        } else if (userDataB != null && userDataB.id.equals(WKWorld.GROUND_IDENTIFIER)) {
            if (userDataA != null) userDataA.onGround = false;
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
