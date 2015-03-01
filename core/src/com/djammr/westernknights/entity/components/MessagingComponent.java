package com.djammr.westernknights.entity.components;

import com.badlogic.ashley.core.Component;
import com.djammr.westernknights.util.observers.Observable;
import com.djammr.westernknights.util.observers.Observer;

import java.util.*;

/**
 * Observable based component for event messaging
 */
public class MessagingComponent extends Component implements Observable{

    public List<Observer> observers = new ArrayList<>();
    public final Map<String, Object> data = new HashMap<String, Object>();


    public void registerObserver(Observer o) {
        observers.add(o);
    }

    public void removeObserver(Observer o) {
        observers.remove(o);
    }


    public void notifyObservers() {
        throw new UnsupportedOperationException("MessagingSystem handles the notifying of ECS oberservers");
    }

    /**
     * Add data item to be sent to observers when notifyObservers() is called
     */
    public void addObserverData(String key, Object data) {
        this.data.put(key, data);
    }
}
