package com.djammr.westernknights.screens;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.djammr.westernknights.Assets;
import com.djammr.westernknights.WKGame;

/**
 * Loading Screen
 */
public class LoadingScreen extends WKScreen {

    private String targetScreen;
    private SpriteBatch batch;
    //private Texture loadingTexture;
    private Sprite loadingSprite;
    private float rotation = 0;


    public LoadingScreen(WKGame game) {
        super(game);
        load();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        batch.begin();
        //batch.draw(loadingSprite, loadingSprite.getWidth()/2f, loadingSprite.getHeight()/2f);
        loadingSprite.draw(batch);
        batch.end();

        if (Assets.manager.update()) {
            if (targetScreen == null) WKGame.logger.logDebug("Target screen is null!");
            else game.getScreens().setScreen(targetScreen, false);
        }
        loadingSprite.rotate(rotation--);
    }

    @Override
    public void load() {
        batch = new SpriteBatch();
        Assets.load(Assets.loadingTexture, Texture.class);
        Assets.manager.finishLoading();

        loadingSprite = new Sprite(Assets.manager.get(Assets.loadingTexture, Texture.class));
        loadingSprite.setPosition(Gdx.graphics.getWidth()/2 - loadingSprite.getWidth()/2, Gdx.graphics.getHeight()/2 - loadingSprite.getHeight()/2);
        //loadingTexture = Assets.manager.get(Assets.loadingTexture, Texture.class);
        super.load();
    }

    /** Set the target screen to load */
    public LoadingScreen setTarget(String screen) {
        targetScreen = screen;
        return this;
    }

    @Override
    public void dispose() {
        batch.dispose();
        //loadingTexture.dispose();
    }
}
