package tests.ai;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ai.steer.behaviors.Wander;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import tests.ai.behaviours.B2DSteeringEntity;

/**
 * GDX AI Test
 */
public class AITest implements ApplicationListener {

    private World world;
    private Box2DDebugRenderer debugRenderer;
    private ShapeRenderer shapeRenderer;
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private float cameraSpeed = 0.1f;
    private boolean debugRender = true;

    private NPC npc1;
    private Body ground;

    private float actorWidth = 0.72f;
    private float actorHeight = 1.8f;


    @Override
    public void create() {
        camera = new OrthographicCamera(16 * 1.25f, 9 * 1.25f);
        camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0);
        camera.update();
        shapeRenderer = new ShapeRenderer();
        batch = new SpriteBatch();

        Box2D.init();
        debugRenderer = new Box2DDebugRenderer();
        world = new World(new Vector2(0, -10), true);

        createBodies();
    }

    private void createBodies() {
        // --- NPCs
        float width = actorWidth, height = actorHeight;

        // --- NPC1
        /*npc1 = new B2DSteeringEntity(createActor(width, height), width);
        npc1.getBody().setTransform(15, 5, 0);*/
        npc1 = new NPC(createActor(width, height), width);
        npc1.getBody().setTransform(15, 5, 0);
        Sprite sprite = new Sprite(new Texture(Gdx.files.internal("test/npc.png")));
        sprite.setSize(actorWidth, actorHeight);
        npc1.setSprite(sprite);

        B2DSteeringEntity target = createNode(20, 2.25f);
        //npc1.setSteeringBehavior(new Seek<Vector2>(npc1, target));
        npc1.getBehaviour().setSteeringBehavior(new Wander<Vector2>(npc1.getBehaviour())
                .setWanderOffset(0f)
                .setWanderRadius(1f)
                .setWanderRate(0.1f)
                .setFaceEnabled(false));
        npc1.getBehaviour().setBoundaries(0f, target.getPosition().x);

        // --- Ground
        BodyDef bodyDef = new BodyDef();
        PolygonShape poly = new PolygonShape();
        float[] vertArray = new float[]{
                0, 0,
                0, 2,
                5, 2,
                8, 2,
                10, 2,
                15, 2,
                30, 2,
                30, 0
        };
        poly.set(vertArray);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = poly;
        //fixtureDef.density = 3f;
        fixtureDef.friction = 0.5f;

        ground = world.createBody(bodyDef);
        ground.createFixture(fixtureDef);

        poly.dispose();
    }

    public Body createActor(float width, float height) {
        float boxOffset = (width/2)/2, boxHeight = height - width/2;
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.active = true;
        FixtureDef fixtureDefB = new FixtureDef();
        FixtureDef fixtureDefC = new FixtureDef();

        PolygonShape poly = new PolygonShape();
        poly.setAsBox(width/2, boxHeight/2, new Vector2(0, boxOffset), 0);
        fixtureDefB.shape = poly;
        fixtureDefB.density = 1f;
        fixtureDefB.friction = 0;

        Body box = world.createBody(bodyDef);
        box.createFixture(fixtureDefB);
        box.setFixedRotation(true);

        CircleShape circle = new CircleShape();
        circle.setRadius(width/2);
        fixtureDefC.shape = circle;
        fixtureDefC.density = 1f;
        fixtureDefC.friction = 0.6f;

        Body wheel = world.createBody(bodyDef);
        wheel.createFixture(fixtureDefC);
        wheel.setFixedRotation(true);

        RevoluteJointDef motor = new RevoluteJointDef();
        motor.bodyA = box;
        motor.bodyB = wheel;
        motor.collideConnected = false;
        motor.localAnchorA.set(0, -boxHeight/2 + boxOffset);
        motor.localAnchorB.set(0, 0);
        world.createJoint(motor);

        poly.dispose();
        circle.dispose();

        return wheel;
    }

    public B2DSteeringEntity createNode(float x, float y) {
        float width = 0.25f;
        float height = width;

        BodyDef bodyDef = new BodyDef();
        FixtureDef fixtureDef = new FixtureDef();

        PolygonShape poly = new PolygonShape();
        poly.setAsBox(width, height);
        fixtureDef.shape = poly;
        fixtureDef.isSensor = true;

        Body box = world.createBody(bodyDef);
        box.createFixture(fixtureDef);
        box.setFixedRotation(true);
        box.setTransform(x, y, 0);

        poly.dispose();

        return new B2DSteeringEntity(box, width);
    }

    @Override
    public void resize(int width, int height) {

    }

    private final float physicsTimeStep = 1/45f;
    private final int maxFrameSkip = 3;
    private final int velocityIterations = 6;
    private final int positionIterations = 2;
    private float accumulator = 0;
    private int loops;
    @Override
    public void render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();
        shapeRenderer.setProjectionMatrix(camera.combined);
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        {
            npc1.render(batch);
        }
        batch.end();

        if (debugRender) {
            debugRenderer.render(world, camera.combined);

            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            {
                npc1.debugRender(shapeRenderer);
            }
            shapeRenderer.end();
        }

        gameLogic(Gdx.graphics.getDeltaTime());

        accumulator += Gdx.graphics.getDeltaTime();
        loops = 0;
        while (accumulator > physicsTimeStep && loops < maxFrameSkip) {
            world.step(physicsTimeStep, velocityIterations, positionIterations);
            accumulator -= physicsTimeStep;
            loops++;
        }
    }

    private void gameLogic(float deltaTime) {
        // --- Camera Movement
        // L/R
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            camera.translate(-cameraSpeed, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            camera.translate(cameraSpeed, 0);
        }
        // U/D
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            camera.translate(0, cameraSpeed);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            camera.translate(0, -cameraSpeed);
        }
        // ---

        // --- AI
        npc1.update(deltaTime);

        // --- Debug
        if (Gdx.input.isKeyJustPressed(Input.Keys.APOSTROPHE)) {
            debugRender = !debugRender;
        }
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
        world.dispose();
        debugRenderer.dispose();
        batch.dispose();
    }
}
