package tests.ai.behaviours;

import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.SteeringAcceleration;
import com.badlogic.gdx.ai.steer.SteeringBehavior;
import com.badlogic.gdx.ai.steer.behaviors.Wander;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;

import java.util.Random;

/**
 * Steerable Box2D Entity, based on https://github.com/libgdx/gdx-ai/blob/master/tests/src/com/badlogic/gdx/ai/tests/steer/box2d/Box2dSteeringEntity.java
 */
public class B2DSteeringEntity implements Steerable<Vector2> {

    Body body;
    float boundingRadius;

    float maxLinearSpeed = 2;
    float maxLinearAccel = maxLinearSpeed/2f;
    float maxAngularSpeed = 0f;
    float maxAngularAcceleration = 0f;
    Float minX = null;
    Float maxX = null;
    Random random = new Random();

    protected SteeringBehavior<Vector2> steeringBehavior;
    private static final SteeringAcceleration<Vector2> steeringOutput = new SteeringAcceleration<Vector2>(new Vector2());


    public B2DSteeringEntity(Body body, float boundingRadius) {
        this.body = body;
        this.boundingRadius = boundingRadius;
    }

    @Override
    public Vector2 getPosition() {
        return body.getPosition();
    }

    @Override
    public float getOrientation() {
        return body.getAngle();
    }

    @Override
    public Vector2 getLinearVelocity() {
        return body.getLinearVelocity();
    }

    @Override
    public float getAngularVelocity() {
        return body.getAngularVelocity();
    }

    @Override
    public float getBoundingRadius() {
        return boundingRadius;
    }

    @Override
    public boolean isTagged() {
        return false;
    }

    @Override
    public void setTagged(boolean tagged) {

    }

    @Override
    public Vector2 newVector() {
        return new Vector2();
    }

    @Override
    public float vectorToAngle(Vector2 vector) {
        return (float)Math.atan2(-vector.x, vector.y);
    }

    @Override
    public Vector2 angleToVector(Vector2 outVector, float angle) {
        outVector.x = -(float)Math.sin(angle);
        outVector.y = (float)Math.cos(angle);
        return outVector;
    }

    public SteeringBehavior<Vector2> getSteeringBehavior() {
        return steeringBehavior;
    }

    public void setSteeringBehavior(SteeringBehavior<Vector2> steeringBehavior) {
        this.steeringBehavior = steeringBehavior;
    }

    public void update(float deltaTime) {
        if (steeringBehavior != null) {
            boolean applySteering = true;

            if (random.nextFloat() > 0.5f) {
                steeringBehavior.calculateSteering(steeringOutput); // 50% chance to stop moving
            } else {
                steeringOutput.setZero();
            }

            // boundaries
            if (minX != null && body.getPosition().x + steeringOutput.linear.x < minX) applySteering = false;
            if (maxX != null && body.getPosition().x + steeringOutput.linear.x > maxX) applySteering = false;
            if (applySteering) {
                applySteering(steeringOutput, deltaTime);
            } else {
                stop();
                steeringOutput.setZero();
                ((Wander)steeringBehavior).setWanderOrientation(0);
            }
        }
    }

    protected void applySteering(SteeringAcceleration<Vector2> steering, float deltaTime) {

        // Update position and velocity
        if(!steeringOutput.linear.isZero()) {
            Vector2 force = steeringOutput.linear;
            force.y = 0;
            if ((force.x > 0 && body.getLinearVelocity().x < maxLinearSpeed) || (force.x < 0 && body.getLinearVelocity().x > -maxLinearSpeed)) {
                body.applyLinearImpulse(force, body.getWorldCenter(), true);
            }
        }
    }

    public void stop() {
        body.setLinearVelocity(0, body.getLinearVelocity().y);
    }

    public Body getBody() {
        return body;
    }

    public void setSensor(boolean isSensor) {
        for (Fixture fixture : body.getFixtureList()) {
            fixture.setSensor(isSensor);
        }
    }

    // --- Limiters

    public void setMaxX(Float maxX) {
        this.maxX = maxX;
    }

    public void setMinX(Float minX) {
        this.minX = minX;
    }

    public void setBoundaries(Float minX, Float maxX) {
        setMinX(minX);
        setMaxX(maxX);
    }

    public float getMinX() {
        return minX;
    }
    public float getMaxX() {
        return maxX;
    }

    @Override
    public float getMaxLinearSpeed() {
        return maxLinearSpeed;
    }

    @Override
    public void setMaxLinearSpeed(float maxLinearSpeed) {
        this.maxLinearSpeed = maxLinearSpeed;
    }

    @Override
    public float getMaxLinearAcceleration() {
        return maxLinearAccel;
    }

    @Override
    public void setMaxLinearAcceleration(float maxLinearAcceleration) {
        this.maxLinearAccel = maxLinearAcceleration;
    }

    @Override
    public float getMaxAngularSpeed() {
        return maxAngularSpeed;
    }

    @Override
    public void setMaxAngularSpeed(float maxAngularSpeed) {
        this.maxAngularSpeed = maxAngularSpeed;
    }

    @Override
    public float getMaxAngularAcceleration() {
        return maxAngularAcceleration;
    }

    @Override
    public void setMaxAngularAcceleration(float maxAngularAcceleration) {
        this.maxAngularAcceleration = maxAngularAcceleration;
    }
}
