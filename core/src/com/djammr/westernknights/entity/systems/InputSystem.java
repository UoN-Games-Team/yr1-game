package com.djammr.westernknights.entity.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.djammr.westernknights.WKGame;
import com.djammr.westernknights.entity.Box2DUserData;
import com.djammr.westernknights.entity.EntityStates;
import com.djammr.westernknights.entity.components.*;
import com.djammr.westernknights.util.input.keybindings.GameActions;
import com.djammr.westernknights.util.observers.InputObserver;
import com.djammr.westernknights.util.observers.ObserverKeys;

/**
 * Executes commands best on input events from an {@link com.djammr.westernknights.util.observers.InputObservable} <br/>
 * This should ideally act as a Controller type object for specific Components
 */
public class InputSystem extends IteratingSystem implements InputObserver {

    private ComponentMapper<MovementComponent> mvm = ComponentMapper.getFor(MovementComponent.class);
    private ComponentMapper<StatComponent> statm = ComponentMapper.getFor(StatComponent.class);
    private ComponentMapper<StateComponent> stm = ComponentMapper.getFor(StateComponent.class);
    //private ComponentMapper<MessagingComponent> msgm = ComponentMapper.getFor(MessagingComponent.class);
    private MovementComponent mvc;
    private StatComponent statc;
    private StateComponent stc;
    private Box2DUserData playerUserData;
    //private MessagingComponent msgc;


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
            // Events that can be process when paused
            switch (event) {
                case GameActions.PLAYER_MOVE_NONE:
                    mvc.stop = true;
                    mvc.left = false;
                    mvc.right = false;
                    break;
            }
            // Events that should only be process when not paused
            if (checkProcessing()) {
                switch (event) {
                    case GameActions.PLAYER_LEFT:
                        mvc.left = true;
                        break;
                    case GameActions.PLAYER_RIGHT:
                        mvc.right = true;
                        break;
                    case GameActions.PLAYER_ATTACK_LIGHT:
                        stc.state = EntityStates.ATTACKING_MELEE;
                        break;
                    case GameActions.PLAYER_JUMP:
                        mvc.jump = true;
                        break;

                    case GameActions.PLAYER_INTERACT:
                        WKGame.logger.logDebug(playerUserData.collidingSensor);
                        if (playerUserData.collidingSensor.equals("bounty_board")) {
                            WKGame.logger.logDebug("Loading RiverTown");
                        }
                        break;

                    case GameActions.DAMAGE:
                        statc.healthChange -= 50;
                        break;
                    case GameActions.HEAL:
                        statc.healthChange += 50;
                        break;
                    case GameActions.XP_GAIN:
                        statc.xpChange += 15;
                        break;
                }
            }
        }
    }

    /**
     * Sets the Entity that will be controlled by this InputSystem
     */
    public void setControllable(Entity entity) {
        mvc = mvm.get(entity);
        statc = statm.get(entity);
        stc = stm.get(entity);
        playerUserData = (Box2DUserData)entity.getComponent(Box2DComponent.class).body.getUserData();
        //msgc = msgm.get(entity);
    }
}
