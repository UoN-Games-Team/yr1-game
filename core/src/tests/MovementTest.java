package tests;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJoint;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.djammr.westernknights.WKGame;
import com.djammr.westernknights.entity.EntityFactory;
import com.djammr.westernknights.util.SaveManager;
import com.djammr.westernknights.util.input.WKInput;
import com.djammr.westernknights.util.input.keybindings.GameActions;
import com.djammr.westernknights.util.input.keybindings.KeyMapManager;
import com.djammr.westernknights.util.observers.InputObserver;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class MovementTest extends WKInput implements ApplicationListener, InputObserver, ContactListener {

    private World world;
    private Box2DDebugRenderer debugRenderer;
    private OrthographicCamera camera;
    private SaveManager saveManager;
    private KeyMapManager keyMapManager;

    private boolean playerRight = false;
    private boolean playerLeft = false;
    private boolean playerJump = false;
    private boolean playerOnGround = false;
    private int playerSpeed = 10;
    private int playerJumpForce = 10;
    private Body player;
    private RevoluteJoint playerJoint;
    private Body ground;

    @Override
    public void create() {
        Gdx.input.setInputProcessor(this);
        Controllers.addListener(this);
        saveManager = new SaveManager();
        keyMapManager = new KeyMapManager();
        keyMapManager.loadDefaultKeyMap();
        keyMapManager.loadDefaultControllerMap();
        registerObserver(this);

        camera = new OrthographicCamera(16 * 1.25f, 9 * 1.25f);
        camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0);
        camera.update();
        Box2D.init();
        debugRenderer = new Box2DDebugRenderer();
        world = new World(new Vector2(0, -10), true);
        world.setContactListener(this);

        createBodies();
    }

    private void createBodies() {
        // Player
        float width = 0.8f, height = 1.8f;
        float boxOffset = (width/2)/2, boxHeight = height - width/2;
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.active = true;

        PolygonShape poly = new PolygonShape();
        poly.setAsBox(width/2, boxHeight/2, new Vector2(0, boxOffset), 0);
        FixtureDef fixtureDefB = new FixtureDef();
        fixtureDefB.shape = poly;
        fixtureDefB.density = 1f;
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
        fixtureDefC.density = 1f;
        fixtureDefC.friction = 0;

        Body wheel = world.createBody(bodyDef);
        wheel.createFixture(fixtureDefC);
        //wheel.setFixedRotation(true);

        RevoluteJointDef motor = new RevoluteJointDef();
        //motor.enableMotor = true;
        //motor.maxMotorTorque = 50;
        motor.bodyA = box;
        motor.bodyB = wheel;
        motor.collideConnected = false;
        motor.localAnchorA.set(0, -boxHeight/2 + boxOffset);
        motor.localAnchorB.set(0, 0);
        playerJoint = (RevoluteJoint)world.createJoint(motor);

        player = wheel;
        //box.setTransform(10, 5, 0);
        wheel.setTransform(10, 5, 0);

        poly.dispose();
        circle.dispose();

        // Ground
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
        //fixtureDef.density = 3f;
        fixtureDef.friction = 0.5f;

        ground = world.createBody(bodyDef);
        ground.createFixture(fixtureDef);

        poly.dispose();
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
        debugRenderer.render(world, camera.combined);

        gameLogic();

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
        }
        else if (playerLeft) {
            if (player.getLinearVelocity().x > -playerSpeed) {
                //player.applyForceToCenter(new Vector2(-10, 0), true);
                player.applyLinearImpulse(new Vector2(-playerSpeed / 2, 0), player.getWorldCenter(), true);
            }
            //playerJoint.setMotorSpeed(playerSpeed*2);
        }
        else {
            resetPlayerSpeed();
        }
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

    }

    @Override
    public boolean keyDown(int keycode) {
        //WKGame.logger.logDebug(Input.Keys.toString(keycode) + " pressed");
        if (keyMapManager.getKeyMap().get(""+keycode) != null) {
            notifyObservers(keyMapManager.getKeyMap().get(""+keycode).intValue());
        }
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (keyMapManager.getKeyMap().get(""+keycode) != null &&
                (keyMapManager.getKeyMap().get(""+keycode).intValue() == GameActions.PLAYER_LEFT || keyMapManager.getKeyMap().get(""+keycode).intValue() == GameActions.PLAYER_RIGHT)) {
            notifyObservers(GameActions.PLAYER_MOVE_NONE);
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
        //WKGame.logger.logDebug(controller.getName() + ", pressed: " + buttonCode);

        if (keyMapManager.getControllerMap().get("btn-"+buttonCode) != null) {
            notifyObservers(keyMapManager.getControllerMap().get("btn-"+buttonCode).intValue());
        }
        return true;
    }

    @Override
    public void inputEvent(int event) {
        switch (event) {
            case GameActions.PLAYER_MOVE_NONE:
                resetPlayerSpeed();
                playerLeft = false;
                playerRight = false;
                break;
            case GameActions.PLAYER_LEFT:
                resetPlayerSpeed();
                playerLeft = true;
                break;
            case GameActions.PLAYER_RIGHT:
                resetPlayerSpeed();
                playerRight = true;
                break;
            case GameActions.PLAYER_JUMP:
                if (playerOnGround) player.applyLinearImpulse(new Vector2(0, playerJumpForce), player.getWorldCenter(), true);
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
}
