package com.djammr.westernknights.entity.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.PerformanceCounter;
import com.djammr.westernknights.WKGame;
import com.djammr.westernknights.entity.EntityStates;
import com.djammr.westernknights.entity.components.Box2DComponent;
import com.djammr.westernknights.entity.components.MovementComponent;
import com.djammr.westernknights.entity.components.PlayerComponent;
import com.djammr.westernknights.entity.components.StateComponent;
import com.djammr.westernknights.util.input.keybindings.GameActions;
import com.djammr.westernknights.util.observers.InputObserver;

/**
 * Executes commands best on input events from an {@link com.djammr.westernknights.util.observers.InputObservable} <br/>
 * This should ideally act as a Controller type object for specific Components
 */
public class InputSystem extends IteratingSystem implements InputObserver {

    private ComponentMapper<MovementComponent> mvm = ComponentMapper.getFor(MovementComponent.class);
    private MovementComponent mvc;


    public InputSystem() {
        super(Family.all(PlayerComponent.class, MovementComponent.class, Box2DComponent.class).get());
    }


    @Override
    protected void processEntity(Entity entity, float deltaTime) {

    }

    @Override
    public void inputEvent(int event) {
        WKGame.logger.logDebug("Received event: " + GameActions.toString(event));
        if (mvc != null) {
            switch (event) {
                case GameActions.PLAYER_MOVE_NONE:
                    mvc.stop = true;
                    mvc.left = false;
                    mvc.right = false;
                    break;
                case GameActions.PLAYER_LEFT:
                    mvc.left = true;
                    break;
                case GameActions.PLAYER_RIGHT:
                    mvc.right = true;
                    break;
                case GameActions.PLAYER_JUMP:
                    mvc.jump = true;
                    break;
            }
        }
    }

    /**
     * Sets the Entity that will be controlled by this InputSystem
     */
    public void setControllable(Entity entity) {
        mvc = mvm.get(entity);
    }
}
