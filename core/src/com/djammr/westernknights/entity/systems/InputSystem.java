package com.djammr.westernknights.entity.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.djammr.westernknights.Assets;
import com.djammr.westernknights.WKGame;
import com.djammr.westernknights.entity.Box2DUserData;
import com.djammr.westernknights.entity.EntityStates;
import com.djammr.westernknights.entity.components.*;
import com.djammr.westernknights.util.input.keybindings.GameActions;
import com.djammr.westernknights.util.observers.InputObserver;
import com.djammr.westernknights.util.observers.Observable;
import com.djammr.westernknights.util.observers.Observer;
import com.djammr.westernknights.util.observers.ObserverKeys;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Executes commands best on input events from an {@link com.djammr.westernknights.util.observers.InputObservable} <br/>
 * Dispatches messages on certain input events after checks have been performed
 */
public class InputSystem extends IteratingSystem implements InputObserver, Observable {

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
    public void update(float deltaTime) {
        super.update(deltaTime);
        removeObservers();
        notifyObservers();
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
                        mvc.right = false;
                        break;
                    case GameActions.PLAYER_RIGHT:
                        mvc.right = true;
                        mvc.left = false;
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
                            addObserverData(ObserverKeys.CHANGE_WORLD, Assets.lvlRivertownID);
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

    // Observer stuff
    private List<Observer> observers = new ArrayList<>();
    private List<Observer> observerRemoveQueue = new ArrayList<>();
    private final Map<String, Object> data = new HashMap<String, Object>();
    public void registerObserver(Observer o) {
        observers.add(o);
    }
    public void removeObserver(Observer o) {
        //observers.remove(o);
        observerRemoveQueue.add(o);
    }

    private void removeObservers() {
        for (Observer observer : observerRemoveQueue) {
            observers.remove(observer);
        }
    }

    /**
     * Sends this instance and a data HashMap<String, Object> to all Observers, use for a push implementation.<br/>
     * The data map is cleared once all observers have been notified.
     */
    public void notifyObservers() {
        for (Observer observer : observers) {
            if (!observerRemoveQueue.contains(observer)) observer.update(this, data);
        }
        data.clear();
    }

    /**
     * Add data item to be sent to observers when notifyObservers() is called
     */
    public void addObserverData(String key, Object data) {
        this.data.put(key, data);
    }
}
