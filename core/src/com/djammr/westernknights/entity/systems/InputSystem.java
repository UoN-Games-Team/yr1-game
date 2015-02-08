package com.djammr.westernknights.entity.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import com.djammr.westernknights.WKGame;
import com.djammr.westernknights.entity.components.Box2DComponent;
import com.djammr.westernknights.util.input.keybindings.GameActions;
import com.djammr.westernknights.util.observers.InputObserver;

/**
 * Executes commands best on input events from an {@link com.djammr.westernknights.util.observers.InputObservable}
 */
public class InputSystem extends IteratingSystem implements InputObserver {

    private ComponentMapper<Box2DComponent> b2dm = ComponentMapper.getFor(Box2DComponent.class);


    public InputSystem() {
        super(Family.all(Box2DComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {

    }

    @Override
    public void inputEvent(int event) {
        WKGame.logger.logDebug("Received event: " + GameActions.toString(event));
        switch (event) {
            case GameActions.PLAYER_LEFT:
                for (Entity entity : getEntities()) {
                    b2dm.get(entity).body.applyForce(new Vector2(-5, 0), b2dm.get(entity).body.getWorldCenter(), true);
                }
                break;
            case GameActions.PLAYER_RIGHT:
                for (Entity entity : getEntities()) {
                    b2dm.get(entity).body.applyForce(new Vector2(5, 0), b2dm.get(entity).body.getWorldCenter(), true);
                }
                break;
            case GameActions.PLAYER_JUMP:
                for (Entity entity : getEntities()) {
                    b2dm.get(entity).body.applyLinearImpulse(new Vector2(0, 20), b2dm.get(entity).body.getWorldCenter(), true);
                }
                break;
        }
    }
}
