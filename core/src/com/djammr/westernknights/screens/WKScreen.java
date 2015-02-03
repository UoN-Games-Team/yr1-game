package com.djammr.westernknights.screens;


import com.badlogic.gdx.Screen;
import com.djammr.westernknights.Assets;
import com.djammr.westernknights.WKGame;

/**
 * Abstract screen implementation for Western Knights screens
 */
public abstract class WKScreen implements Screen {

    protected WKGame game;


    public WKScreen(WKGame game) {
        this.game = game;
    }

    /**
     * Define assets to load (Assets.manager.load(path, type)) then call super.load()
     */
    public void load() {
        Assets.manager.finishLoading();
    }

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
}
