package com.djammr.westernknights.screens;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.djammr.westernknights.WKGame;

/**
 * Loading Screen
 */
public class LoadingScreen extends WKScreen {


    public LoadingScreen(WKGame game) {
        super(game);
    }

    @Override
    public void show() {
        WKGame.logger.logDebug("Loading...");
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        WKGame.logger.logDebug("Loading...");
    }

    @Override
    public void dispose() {

    }
}
