package com.djammr.westernknights.screens;


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

    }

    @Override
    public void dispose() {

    }
}
