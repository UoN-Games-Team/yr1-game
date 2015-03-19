package tests;

import box2dLight.*;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;


public class DayNightTest implements ApplicationListener {
    static final float viewportWidth = 48;
    static final float viewportHeight = 32;
    OrthographicCamera camera;
    SpriteBatch batch;
    Box2DDebugRenderer debugRenderer;
    World world;
    Body box;
    RayHandler rayHandler;
    Light sun;
    Sprite skyDay;
    Sprite skyNight;
    float skyDayAlpha = 1;
    Texture bg;
    float gameTime = 0.25f;
    float sunDirection = -90f;
    Color ambientLight = new Color(0.2f, 0.2f, 0.2f, 0.1f);


    @Override
    public void create() {
        camera = new OrthographicCamera(viewportWidth, viewportHeight);
        camera.position.set(camera.viewportWidth/2, camera.viewportHeight/2, 0);
        camera.update();
        batch = new SpriteBatch();
        debugRenderer = new Box2DDebugRenderer();
        createPhysicsWorld();

        skyDay = new Sprite();
        skyDay.setRegion(new Texture(Gdx.files.internal("images/sky_day.png")));
        skyDay.setSize(30, 30);
        skyDay.setOriginCenter();
        skyDay.setPosition(camera.viewportWidth/2 - skyDay.getWidth()/2, camera.viewportHeight/2 - skyDay.getHeight()/2);
        skyNight = new Sprite();
        skyNight.setRegion(new Texture(Gdx.files.internal("images/sky_night.png")));
        skyNight.setSize(30, 30);
        skyNight.setOriginCenter();
        skyNight.setPosition(camera.viewportWidth/2 - skyNight.getWidth()/2, camera.viewportHeight/2 - skyNight.getHeight()/2);
        skyNight.setAlpha(1);

        bg = new Texture(Gdx.files.internal("images/background.png"));

        rayHandler = new RayHandler(world);
        rayHandler.setAmbientLight(ambientLight);
        initDirectionalLight();
    }

    @Override
    public void render() {
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        //Gdx.gl.glClearColor(58/ 255f, 144/ 255f, 224 / 255f, 0);
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        gameTime += Gdx.graphics.getDeltaTime() / 8;
        if (gameTime > 1f) gameTime = 0;

        // Rotation
        sun.setDirection(-(360 * gameTime));
        skyDay.setRotation(-(360 * gameTime));
        skyNight.setRotation(-(360 * gameTime));

        // Skybox Fading
        if (skyDayAlpha < 0) skyDayAlpha = 0;
        if (skyDayAlpha > 1) skyDayAlpha = 1;
        if (gameTime >= 0.4f && gameTime < 0.85f && skyDayAlpha > 0) {
            skyDay.setAlpha((skyDayAlpha > 0 + Gdx.graphics.getDeltaTime())? skyDayAlpha -= Gdx.graphics.getDeltaTime() : 0);
            System.out.println("to night");
        } else if (gameTime >= 0.85f && skyDayAlpha < 1) {
            System.out.println("to day");
            skyDay.setAlpha((skyDayAlpha < 1f - Gdx.graphics.getDeltaTime())? skyDayAlpha += Gdx.graphics.getDeltaTime() : 1);
        }

        // Night shadows off
        if (gameTime > 0.5) sun.setXray(true);
        else if (sun.isXray()) sun.setXray(false);

        // Sun alpha
        float alpha;
        if (gameTime > 0.75) alpha = gameTime - 0.5f;
        else if (gameTime < 0.25) alpha = gameTime + 0.5f;
        else alpha = 1 - gameTime;
        sun.setColor(sun.getColor().r, sun.getColor().b, sun.getColor().g, alpha);


        fixedStep(Gdx.graphics.getDeltaTime());
        batch.begin();
        {
            skyNight.draw(batch);
            skyDay.draw(batch);
            //batch.draw(bg, 0, -5, viewportWidth, viewportHeight);
        }
        batch.end();

        debugRenderer.render(world, camera.combined);

        rayHandler.setCombinedMatrix(camera);
        rayHandler.updateAndRender();
    }

    /*void initPointLights() {
        PointLight sun = new PointLight(rayHandler, 128, null, 10f, 0f, 0f);
        sun.setColor(
                MathUtils.random(),
                MathUtils.random(),
                MathUtils.random(),
                1f);
    }*/

    void initDirectionalLight() {
        sunDirection = MathUtils.random(0f, 360f);
        sun = new DirectionalLight(
                rayHandler, 4 * 128, new Color(1, 1, 1, 0.75f), sunDirection);
        sun.setDistance(1f);
    }

    private final static int MAX_FPS = 30;
    private final static int MIN_FPS = 15;
    public final static float TIME_STEP = 1f / MAX_FPS;
    private final static float MAX_STEPS = 1f + MAX_FPS / MIN_FPS;
    private final static float MAX_TIME_PER_FRAME = TIME_STEP * MAX_STEPS;
    private final static int VELOCITY_ITERS = 6;
    private final static int POSITION_ITERS = 2;
    float physicsTimeLeft;
    long aika;
    int times;
    private boolean fixedStep(float delta) {
        physicsTimeLeft += delta;
        if (physicsTimeLeft > MAX_TIME_PER_FRAME)
            physicsTimeLeft = MAX_TIME_PER_FRAME;
        boolean stepped = false;
        while (physicsTimeLeft >= TIME_STEP) {
            world.step(TIME_STEP, VELOCITY_ITERS, POSITION_ITERS);
            physicsTimeLeft -= TIME_STEP;
            stepped = true;
        }
        return stepped;
    }

    private void createPhysicsWorld() {
        world = new World(new Vector2(0, 0), true);

        PolygonShape rect = new PolygonShape();
        rect.setAsBox(1, 1);
        box = world.createBody(new BodyDef());
        box.createFixture(rect, 0);
        box.setTransform(viewportWidth/2, viewportHeight/2, 0);
        rect.dispose();
    }

    @Override
    public void dispose() {
        rayHandler.dispose();
        world.dispose();
    }

    @Override
    public void pause() {
    }
    @Override
    public void resize(int arg0, int arg1) {
    }
    @Override
    public void resume() {
    }
}
