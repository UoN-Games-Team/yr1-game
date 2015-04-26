package com.djammr.westernknights.screens;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.djammr.westernknights.Assets;
import com.djammr.westernknights.WKGame;
import com.djammr.westernknights.ui.UIView;
import com.djammr.westernknights.util.controllers.UIController;

import java.util.HashMap;
import java.util.Map;

/**
 * Abstract screen implementation for Western Knights screens
 */
public abstract class WKScreen implements Screen {

    protected InputMultiplexer inputMultiplexer;
    protected Map<String, UIController> uiControllers = new HashMap<String, UIController>();
    protected Map<String, UIView> uiViews = new HashMap<String, UIView>();
    protected WKGame game;
    private boolean loaded = false;
    private boolean paused = false;


    public WKScreen(WKGame game) {
        this.game = game;
        inputMultiplexer = new InputMultiplexer();
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(inputMultiplexer);
    }

    /**
     * Define assets to load (Assets.manager.load(path, type)) then call super.load()
     */
    public void load() {
        //Assets.manager.finishLoading();
    }

    /**
     * Call when loading has finished
     */
    public void loadComplete() {
        loaded = true;
    }

    @Override
    public void render(float delta) {
        for (UIController controller : uiControllers.values()) {
            controller.update(delta);
        }
    }

    @Override
    public void resize(int width, int height) {
        for (UIController controller : uiControllers.values()) {
            controller.resize(width, height);
        }
    }

    @Override
    public void pause() {
        paused = true;
    }

    @Override
    public void resume() {
        paused = false;
    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        for (UIController controller : uiControllers.values()) {
            controller.dispose();
        }
        uiControllers.clear();
    }

    public boolean isPaused() {
        return paused;
    }

    public boolean isLoaded() {
        return loaded;
    }

    public void setScreen(String screen) {
        getGame().getScreens().setScreen(screen);
    }

    public WKGame getGame() { return game; }

    /**
     * Gets the InputMultiplexer for this Screen. You should add any InputProcessors to this.
     * @return this Screen's InputMultiplexer instance
     */
    public InputMultiplexer getInputMultiplexer() {
        return inputMultiplexer;
    }
}
