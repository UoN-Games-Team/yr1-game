package com.djammr.westernknights.entity.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.djammr.westernknights.WKGame;
import com.djammr.westernknights.entity.components.Box2DComponent;
import com.djammr.westernknights.util.input.keybindings.GameActions;
import com.djammr.westernknights.util.observers.InputObserver;

/**
 * Executes commands best on input events from an {@link com.djammr.westernknights.util.observers.InputObservable}
 */
public class InputSystem extends IteratingSystem implements InputObserver {

    public InputSystem() {
        super(Family.all(Box2DComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {

    }

    @Override
    public void inputEvent(int event) {
        WKGame.logger.logDebug("Received event: " + GameActions.toString(event));
    }
}
