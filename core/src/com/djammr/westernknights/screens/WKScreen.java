package com.djammr.westernknights.screens;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.djammr.westernknights.Assets;
import com.djammr.westernknights.WKGame;

/**
 * Abstract screen implementation for Western Knights screens
 */
public abstract class WKScreen implements Screen {

    protected InputMultiplexer inputMultiplexer;
    protected WKGame game;


    public WKScreen(WKGame game) {
        this.game = game;
        inputMultiplexer = new InputMultiplexer();
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
    public abstract void loadComplete();

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

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
