package com.djammr.westernknights.util.Observer;

public interface Observable {
    public abstract void registerObserver(Observer o);
    public abstract void removeObserver(Observer o);
    public abstract void notifyObservers();
}