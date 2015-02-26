package tests.ai;


import com.badlogic.gdx.ai.steer.behaviors.Wander;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import tests.ai.behaviours.B2DSteeringEntity;

import java.util.Random;

public class NPC {

    private Random random = new Random();

    private Sprite sprite;
    private B2DSteeringEntity behaviour;
    private boolean working;
    private int workTime = 120;
    private int workTicks = workTime;


    public NPC(Body body, float boundingRadius) {
        behaviour = new B2DSteeringEntity(body, boundingRadius);
    }

    public void update(float deltaTime) {
        if (behaviour.getPosition().x >= behaviour.getMaxX()-0.5f && workTicks >= workTime && random.nextFloat() < 0.0025f) {
            workTicks = 0;
            working = true;
            System.out.println("Started working");
            sprite.setFlip(sprite.isFlipX(), true);
        }
        if (!working) behaviour.update(deltaTime);
        else {
            workTicks++;
            if (workTicks >= workTime) {
                working = false;
                sprite.setFlip(sprite.isFlipX(), false);
            }
        }

        sprite.setPosition(behaviour.getPosition().x - sprite.getWidth()/2, behaviour.getPosition().y - sprite.getWidth()/2);
        if (behaviour.getLinearVelocity().x < -behaviour.getMaxLinearSpeed()/4) sprite.setFlip(false, false);
        else if (behaviour.getLinearVelocity().x > behaviour.getMaxLinearSpeed()/4) sprite.setFlip(true, false);
    }

    public void render(SpriteBatch batch) {
        sprite.draw(batch);
    }

    public void debugRender(ShapeRenderer renderer) {
        renderer.circle(((Wander<Vector2>) behaviour.getSteeringBehavior()).getWanderCenter().x,
                ((Wander<Vector2>) behaviour.getSteeringBehavior()).getWanderCenter().y,
                0.8f);
        renderer.circle(((Wander<Vector2>) behaviour.getSteeringBehavior()).getInternalTargetPosition().x,
                ((Wander<Vector2>) behaviour.getSteeringBehavior()).getInternalTargetPosition().y,
                0.5f);
    }

    public void updateAndRender(float deltaTime, SpriteBatch batch) {
        update(deltaTime);
        render(batch);
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }

    public B2DSteeringEntity getBehaviour() {
        return behaviour;
    }

    public Body getBody() {
        return behaviour.getBody();
    }
}
