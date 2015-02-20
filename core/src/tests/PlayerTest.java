package tests;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.brashmonkey.spriter.*;
import com.djammr.westernknights.WKGame;
import com.djammr.westernknights.util.input.WKInput;
import com.djammr.westernknights.util.input.keybindings.GameActions;
import com.djammr.westernknights.util.input.keybindings.KeyMapManager;
import com.djammr.westernknights.util.observers.InputObserver;
import com.djammr.westernknights.util.spriter.LibGdxDrawer;
import com.djammr.westernknights.util.spriter.LibGdxLoader;


/**
 * Combined test of physics, animation and actions
 */
public class PlayerTest extends WKInput implements ApplicationListener, InputObserver, ContactListener, Player.PlayerListener {

    private World world;
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    private Box2DDebugRenderer debugRenderer;
    private OrthographicCamera camera;
    private KeyMapManager keyMapManager;

    private Player spriterPlayer;
    private Player swordPlayer;
    private PlayerTweener spriterTweener;
    private LibGdxLoader spriterLoader;
    private LibGdxDrawer spriterDrawer;
    private Player.Attachment sword;

    private boolean playerRight = false;
    private boolean playerLeft = false;
    private boolean playerOnGround = false;
    private int playerSpeed = 10;
    private int playerJumpForce = 7;
    private Body player;
    private Body enemy;
    private Body swordSensor;
    private Body ground;

    @Override
    public void create() {
        Gdx.input.setInputProcessor(this);
        Controllers.addListener(this);
        keyMapManager = new KeyMapManager();
        keyMapManager.loadDefaultKeyMap();
        keyMapManager.loadDefaultControllerMap();
        registerObserver(this);

        camera = new OrthographicCamera(20, 11.25f);
        camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0);
        camera.update();
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setAutoShapeType(true);
        Box2D.init();
        debugRenderer = new Box2DDebugRenderer();
        world = new World(new Vector2(0, -10), true);
        world.setContactListener(this);

        createBodies();
        createAnimation();
    }

    private void createBodies() {
        // Player
        float width = 0.6f, height = 1.8f;
        float boxOffset = (width/2)/2, boxHeight = height - width/2;
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.active = true;

        PolygonShape poly = new PolygonShape();
        poly.setAsBox(width/2, boxHeight/2, new Vector2(0, boxOffset), 0);
        FixtureDef fixtureDefB = new FixtureDef();
        fixtureDefB.shape = poly;
        fixtureDefB.density = 0.5f;
        fixtureDefB.friction = 0;

        Body box = world.createBody(bodyDef);
        box.createFixture(fixtureDefB);
        box.setFixedRotation(true);

        //bodyDef.position.set(box.getPosition());
        CircleShape circle = new CircleShape();
        circle.setRadius(width/2);
        //circle.setPosition(new Vector2(0, -boxHeight/2));
        FixtureDef fixtureDefC = new FixtureDef();
        fixtureDefC.shape = circle;
        fixtureDefC.density = 0.5f;
        fixtureDefC.friction = 0.6f;

        Body wheel = world.createBody(bodyDef);
        wheel.createFixture(fixtureDefC);
        wheel.setFixedRotation(true);

        RevoluteJointDef motor = new RevoluteJointDef();
        //motor.enableMotor = true;
        //motor.maxMotorTorque = 50;
        motor.bodyA = box;
        motor.bodyB = wheel;
        motor.collideConnected = false;
        motor.localAnchorA.set(0, -boxHeight/2 + boxOffset);
        motor.localAnchorB.set(0, 0);
        world.createJoint(motor);

        player = wheel;
        //box.setTransform(10, 5, 0);
        wheel.setTransform(10, 5, 0);

        poly.dispose();
        circle.dispose();

        // ---- Enemy
        bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        poly = new PolygonShape();
        poly.setAsBox(width, height * 0.75f);
        FixtureDef fd = new FixtureDef();
        fd.shape = poly;
        fd.density = 20f;
        fd.friction = 1f;
        enemy = world.createBody(bodyDef);
        enemy.createFixture(fd);
        enemy.setFixedRotation(true);
        enemy.setTransform(20, 4.35f, 0);
        poly.dispose();

        // --- Sword
        poly = new PolygonShape();
        poly.setAsBox(0.1f, 0.5f);
        fd = new FixtureDef();
        fd.shape = poly;
        fd.isSensor = true;
        swordSensor = world.createBody(new BodyDef());
        swordSensor.createFixture(fd);
        poly.dispose();

        // ---- Ground
        bodyDef = new BodyDef();
        poly = new PolygonShape();
        //poly.setAsBox(32, 0.5f);
        float[] vertArray = new float[]{
                0, 0,
                0, 1,
                5, 1,
                8, 2,
                10, 2,
                15, 3,
                30, 3,
                30, 0
        };
        poly.set(vertArray);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = poly;
        fixtureDef.friction = 0.5f;

        ground = world.createBody(bodyDef);
        ground.createFixture(fixtureDef);

        poly.set(new float[] {
                30, 3,
                60, 3,
                60, 0
        });
        fixtureDef.shape = poly;
        ground.createFixture(fixtureDef);

        poly.dispose();
    }

    private void createAnimation() {
        FileHandle handle = Gdx.files.internal("test/spriter/spriter_character.scml");
        Data data = new SCMLReader(handle.read()).getData();

        spriterLoader = new LibGdxLoader(data);
        spriterLoader.load(handle.file());

        spriterDrawer = new LibGdxDrawer(spriterLoader, batch, shapeRenderer);
        /*spriterPlayer = new Player(data.getEntity(0));
        spriterPlayer.addListener(this);
        spriterPlayer.setScale(WKGame.PIXELS_TO_METERS / 2.5f);
        spriterPlayer.speed = 4;

        spriterPlayer.setAnimation("idle");
        spriterPlayer.speed = 1;*/

        spriterTweener = new PlayerTweener(data.getEntity("player"));
        //spriterTweener.setPlayers(spriterPlayer, spriterPlayer);
        spriterTweener.setScale(WKGame.PIXELS_TO_METERS / 2.5f);
        spriterTweener.getFirstPlayer().speed = 4;
        spriterTweener.getSecondPlayer().speed = 4;
        spriterTweener.addListener(this);

        swordPlayer = new Player(data.getEntity("sword"));
        swordPlayer.setScale(spriterTweener.getScale());
        sword = new Player.Attachment(spriterTweener.getBone("bone_008")) {
            @Override
            protected void setPosition(float x, float y) {
                swordPlayer.setPosition(x, y);
                swordSensor.setTransform(x, y, swordSensor.getAngle());
            }

            @Override
            protected void setScale(float xscale, float yscale) {
                //swordPlayer.scale(Math.max(xscale, yscale));
            }

            @Override
            protected void setAngle(float angle) {
                swordPlayer.setAngle(angle);
                swordSensor.setTransform(swordSensor.getPosition().x, swordSensor.getPosition().y, angle * MathUtils.degRad);

            }
        };
        sword.setParent(spriterTweener.getBone("bone_008"));
        spriterTweener.attachments.add(sword);
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
        batch.setProjectionMatrix(camera.combined);
        shapeRenderer.setProjectionMatrix(camera.combined);

        gameLogic();

        batch.begin();
        {
            //spriterPlayer.update();
            spriterTweener.update();
            swordPlayer.update();
            spriterDrawer.draw(spriterTweener);
            spriterDrawer.draw(swordPlayer);
            spriterTweener.setBaseAnimation(spriterTweener.getSecondPlayer().getAnimation());
        }
        batch.end();
        debugRenderer.render(world, camera.combined);



        accumulator += Gdx.graphics.getDeltaTime();
        loops = 0;
        while (accumulator > physicsTimeStep && loops < maxFrameSkip) {
            world.step(physicsTimeStep, velocityIterations, positionIterations);
            accumulator -= physicsTimeStep;
            loops++;
        }
        camera.position.set(player.getPosition(), 0);
    }

    private void gameLogic() {
        // Movement
        if (playerRight) {
            if (player.getLinearVelocity().x < playerSpeed) {
                //player.applyForceToCenter(new Vector2(playerSpeed, 0), true);
                player.applyLinearImpulse(new Vector2(playerSpeed / 2, 0), player.getWorldCenter(), true);
            }
            //playerJoint.setMotorSpeed(-playerSpeed*2);
            if (playerOnGround)  {
                spriterTweener.getSecondPlayer().setAnimation("run");
                spriterTweener.getSecondPlayer().speed = 4;
            }
        }
        else if (playerLeft) {
            if (player.getLinearVelocity().x > -playerSpeed) {
                //player.applyForceToCenter(new Vector2(-10, 0), true);
                player.applyLinearImpulse(new Vector2(-playerSpeed / 2, 0), player.getWorldCenter(), true);
            }
            //playerJoint.setMotorSpeed(playerSpeed*2);
            if (playerOnGround)  {
                spriterTweener.getSecondPlayer().setAnimation("run");
                spriterTweener.getSecondPlayer().speed = 4;
            }
        }
        else {
            resetPlayerSpeed();
        }

        spriterTweener.setPosition(player.getPosition().x, player.getPosition().y - 0.3f);
    }

    private void resetPlayerSpeed() {
        //playerJoint.setMotorSpeed(0);
        player.setLinearVelocity(0, player.getLinearVelocity().y);
        if (playerOnGround) player.getFixtureList().get(0).setFriction(1);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
        batch.dispose();
        spriterLoader.dispose();
        world.dispose();
    }


    @Override
    public boolean keyDown(int keycode) {
        //WKGame.logger.logDebug(Input.Keys.toString(keycode) + " pressed");
        if (keyMapManager.getKeyMap().get("btn-"+keycode) != null) {
            notifyObservers(keyMapManager.getKeyMap().get("btn-"+keycode).intValue());
        }
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (keyMapManager.getKeyMap().get("btn-"+keycode) != null &&
                (keyMapManager.getKeyMap().get("btn-"+keycode).intValue() == GameActions.PLAYER_LEFT || keyMapManager.getKeyMap().get("btn-"+keycode).intValue() == GameActions.PLAYER_RIGHT)) {
            notifyObservers(GameActions.PLAYER_MOVE_NONE);
        }
        return true;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (keyMapManager.getKeyMap().get("btn-"+button) != null) {
            notifyObservers(keyMapManager.getKeyMap().get("btn-"+button).intValue());
        }
        return true;
    }

    /**
     * XBox 360 & One: Joysticks, Triggers
     */
    @Override
    public boolean axisMoved(Controller controller, int axisCode, float value) {
        value = controller.getAxis(axisCode);
        //WKGame.logger.logDebug(controller.getName() + " axis " + axisCode + " at " + value);

        String axis = "axis-"+axisCode+((value > 0)? "+" : "-");
        int action = -2;
        if (keyMapManager.getControllerMap().get(axis) != null) {
            action = keyMapManager.getControllerMap().get(axis).intValue();
        }
        float deadzone = keyMapManager.getControllerMap().get("deadzone");

        if ((value > deadzone || value < -deadzone) && action != -2) {
            notifyObservers(action);
        } else if (action == GameActions.PLAYER_LEFT || action == GameActions.PLAYER_RIGHT) {
            notifyObservers(GameActions.PLAYER_MOVE_NONE);
        }
        return true;
    }

    /**
     * XBox 360 & One: D-Pad
     */
    @Override
    public boolean povMoved(Controller controller, int povCode, PovDirection value) {
        //WKGame.logger.logDebug(controller.getName() + ", moved pov: " + povCode + " to " + value);

        if (keyMapManager.getControllerMap().get("pov-"+value.ordinal()) != null) {
            notifyObservers(keyMapManager.getControllerMap().get("pov-" + value.ordinal()).intValue());
        }
        return true;
    }

    @Override
    public boolean buttonDown(Controller controller, int buttonCode) {
        //System.out.println(controller.getName() + ", pressed: " + buttonCode);
        if (keyMapManager.getControllerMap().get("btn-"+buttonCode) != null) {
            notifyObservers(keyMapManager.getControllerMap().get("btn-"+buttonCode).intValue());
        }
        return true;
    }

    @Override
    public void inputEvent(int event) {
        switch (event) {
            case GameActions.PLAYER_ATTACK_LIGHT:
                spriterTweener.getFirstPlayer().setAnimation("swing");
                if (playerLeft || playerRight || !playerOnGround) {
                    spriterTweener.baseBoneName = "bone_002";
                }
                spriterTweener.setWeight(0f);
                spriterTweener.getFirstPlayer().speed = 1;
                break;
            case GameActions.PLAYER_MOVE_NONE:
                resetPlayerSpeed();
                playerLeft = false;
                playerRight = false;
                if (playerOnGround) {
                    spriterTweener.getSecondPlayer().setAnimation("idle");
                    spriterTweener.setWeight(1f);
                    spriterTweener.getSecondPlayer().speed = 1;
                }
                break;
            case GameActions.PLAYER_LEFT:
                resetPlayerSpeed();
                playerLeft = true;
                if (spriterTweener.flippedX() != -1) spriterTweener.flipX();
                break;
            case GameActions.PLAYER_RIGHT:
                resetPlayerSpeed();
                playerRight = true;
                if (spriterTweener.flippedX() != 1) spriterTweener.flipX();
                break;
            case GameActions.PLAYER_JUMP:
                if (playerOnGround) {
                    player.applyLinearImpulse(new Vector2(0, playerJumpForce), player.getWorldCenter(), true);
                    spriterTweener.getSecondPlayer().setAnimation("jump");
                    //spriterTweener.getSecondPlayer().speed = 4;
                    spriterTweener.setWeight(1f);
                }
                break;
        }
    }

    @Override
    public void beginContact(Contact contact) {
        if (contact.getFixtureB().getBody() == player) {
            if (contact.getFixtureA().getBody() == ground) {
                playerOnGround = true;
            }
        }

        if ((contact.getFixtureA().getBody() == swordSensor && contact.getFixtureB().getBody() == enemy) ||
                (contact.getFixtureB().getBody() == swordSensor && contact.getFixtureA().getBody() == enemy)) {
            System.out.println("hit!");
        }
    }

    @Override
    public void endContact(Contact contact) {
        if (contact.getFixtureB().getBody() == player) {
            if (contact.getFixtureA().getBody() == ground) {
                playerOnGround = false;
            }
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }


    @Override
    public void animationFinished(Animation animation) {
        //spriterTweener.getSecondPlayer().setAnimation("idle");
    }

    @Override
    public void animationChanged(Animation oldAnim, Animation newAnim) {
    }

    @Override
    public void preProcess(Player player) {

    }

    @Override
    public void postProcess(Player player) {

    }

    @Override
    public void mainlineKeyChanged(Mainline.Key prevKey, Mainline.Key newKey) {

    }
}
