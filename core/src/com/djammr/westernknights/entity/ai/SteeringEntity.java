package com.djammr.westernknights.entity.ai;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.SteeringAcceleration;
import com.badlogic.gdx.ai.steer.SteeringBehavior;
import com.badlogic.gdx.ai.steer.behaviors.Wander;
import com.badlogic.gdx.math.Vector2;
import com.djammr.westernknights.WKGame;
import com.djammr.westernknights.entity.components.Box2DComponent;
import com.djammr.westernknights.entity.components.MovementComponent;

/**
 * Steerable Entity
 */
public class SteeringEntity implements Steerable<Vector2> {

    private Box2DComponent b2dc;
    private MovementComponent mvc;
    Float minX = null;
    Float maxX = null;
    float boundingRadius;
    boolean tagged = false;

    protected SteeringBehavior<Vector2> steeringBehavior;
    private final SteeringAcceleration<Vector2> steeringOutput = new SteeringAcceleration<Vector2>(new Vector2());


    public SteeringEntity(Entity entity, float boundingRadius) {
        this.boundingRadius = boundingRadius;
        b2dc = ComponentMapper.getFor(Box2DComponent.class).get(entity);
        mvc = ComponentMapper.getFor(MovementComponent.class).get(entity);
    }
    public SteeringEntity(Entity entity) {
        this(entity, 1f);
    }

    public void update(float deltaTime) {
        if (steeringBehavior != null) {
            boolean applySteering = true;
            steeringBehavior.calculateSteering(steeringOutput);

            mvc.right = false;
            mvc.left = false;
            if (minX != null && steeringOutput.linear.x < 0 && getPosition().x - 0.1 < minX) applySteering = false;
            else if (maxX != null && steeringOutput.linear.x > 0 && getPosition().x + 0.1 > maxX) applySteering = false;
            if (applySteering) {
                applySteering(steeringOutput, deltaTime);
            } else {
                stop();
            }
        }
    }

    protected void applySteering(SteeringAcceleration<Vector2> steering, float deltaTime) {
        // Update position and velocity
        if(!steeringOutput.linear.isZero()) {
            Vector2 force = steeringOutput.linear;
            if (force.x > 0) mvc.right = true;
            else if (force.x < 0) mvc.left = true;
        }
    }

    public void stop() {
        mvc.right = false;
        mvc.left = false;
        mvc.stop = true;
        steeringOutput.setZero();
    }

    public SteeringBehavior<Vector2> getSteeringBehavior() {
        return steeringBehavior;
    }

    public void setSteeringBehavior(SteeringBehavior<Vector2> steeringBehavior) {
        this.steeringBehavior = steeringBehavior;
    }

    public SteeringAcceleration<Vector2> getSteeringOutput() {
        return steeringOutput;
    }

    @Override
    public Vector2 getPosition() {
        return b2dc.body.getPosition();
    }

    @Override
    public float getOrientation() {
        return b2dc.body.getAngle();
    }

    @Override
    public Vector2 getLinearVelocity() {
        return b2dc.body.getLinearVelocity();
    }

    @Override
    public float getAngularVelocity() {
        return b2dc.body.getAngularVelocity();
    }

    @Override
    public float getBoundingRadius() {
        return boundingRadius;
    }

    @Override
    public boolean isTagged() {
        return tagged;
    }

    @Override
    public void setTagged(boolean tagged) {
        this.tagged = tagged;
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

    // --- Limiters
    /**
     * Set the maximum boundary for this NPC
     * @param maxX maximum X position of this NPC
     */
    public void setMaxX(Float maxX) {
        this.maxX = maxX;
    }

    /**
     * Set the minimum boundary for this NPC
     * @param minX minimum X position of this NPC
     */
    public void setMinX(Float minX) {
        this.minX = minX;
    }

    /**
     * Set the boundaries for this NPC
     * @param minX minimum X position of this NPC
     * @param maxX maximum X position of this NPC
     */
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
        return mvc.speed;
    }

    @Override
    public void setMaxLinearSpeed(float maxLinearSpeed) {
        mvc.speed = maxLinearSpeed;
    }

    @Override
    public float getMaxLinearAcceleration() {
        return mvc.speed;
    }

    @Override
    public void setMaxLinearAcceleration(float maxLinearAcceleration) {

    }

    @Override
    public float getMaxAngularSpeed() {
        return mvc.speed;
    }

    @Override
    public void setMaxAngularSpeed(float maxAngularSpeed) {

    }

    @Override
    public float getMaxAngularAcceleration() {
        return mvc.speed;
    }

    @Override
    public void setMaxAngularAcceleration(float maxAngularAcceleration) {

    }
}
