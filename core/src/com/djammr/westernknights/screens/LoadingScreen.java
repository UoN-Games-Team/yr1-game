package com.djammr.westernknights.screens;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.djammr.westernknights.Assets;
import com.djammr.westernknights.WKGame;
import com.djammr.westernknights.ui.Loading;
import com.djammr.westernknights.util.controllers.LoadingController;

/**
 * Loading Screen
 */
public class LoadingScreen extends WKScreen {

    private String targetScreen;


    public LoadingScreen(WKGame game) {
        super(game);
    }

    @Override
    public void load() {
        uiControllers.put("loading", new LoadingController(this));
        uiViews.put("loading", new Loading((LoadingController)uiControllers.get("loading")));
        uiControllers.get("loading").setView(uiViews.get("loading"));
    }

    @Override
    public void loadComplete() {
        super.loadComplete();

    }

    @Override
    public void render(float delta) {
        super.render(delta);
        if (Assets.manager.update()) {
            if (targetScreen == null) WKGame.logger.logDebug("Target screen is null!");
            else {
                game.getScreens().setScreen(targetScreen, false);
            }
        }
    }

    /** Set the target screen to load */
    public LoadingScreen setTarget(String screen) {
        targetScreen = screen;
        return this;
    }
}
