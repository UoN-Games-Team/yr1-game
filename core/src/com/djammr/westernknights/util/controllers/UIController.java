package com.djammr.westernknights.util.controllers;

import com.badlogic.gdx.InputProcessor;
import com.djammr.westernknights.screens.WKScreen;
import com.djammr.westernknights.ui.UIView;

/**
 * Suggested base for User Interface Controllers, regardless of whether the view is a fancy GUI or a humble text based system
 */
public class UIController implements Controller {

    protected UIView view;
    protected WKScreen screen;


    public UIController(WKScreen screen) {
        this.screen = screen;
    }

    /** If your view is an observer, register it with an observable: observable.addObserver(view) */
    public void setView(UIView view) {
        this.view = view;
    }

    public void update(float delta) {
        view.render(delta);
    }

    public void resize(int width, int height) {
        view.resize(width, height);
    }

    public void dispose() {
        view.dispose();
    }

    /**
     * Changes to the specified screen
     * @param screen Screen to change to
     */
    public void setScreen(String screen) {
        this.screen.setScreen(screen);
    }

    /**
     * Adds an InputProcessor to the {@link WKScreen}'s InputMultiplexer
     * @param processor InputProcessor to add
     */
    public void addInputProcessor(InputProcessor processor) {
        // Add the UI input processor at the top of the stack
        screen.getInputMultiplexer().addProcessor(0, processor);
    }
}