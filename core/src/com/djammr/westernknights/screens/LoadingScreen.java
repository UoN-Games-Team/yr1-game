package com.djammr.westernknights.screens;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.djammr.westernknights.Assets;
import com.djammr.westernknights.WKGame;

/**
 * Loading Screen
 */
public class LoadingScreen extends WKScreen {

    private SpriteBatch batch;
    private Texture loadingTexture;


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
        batch.draw(loadingTexture, Gdx.graphics.getWidth()/2 - loadingTexture.getWidth()/2, Gdx.graphics.getHeight()/2 - loadingTexture.getHeight()/2);
        batch.end();
    }

    @Override
    public void load() {
        batch = new SpriteBatch();
        Assets.load(Assets.loadingTexture, Texture.class);
        Assets.manager.finishLoading();

        loadingTexture = Assets.manager.get(Assets.loadingTexture, Texture.class);
        super.load();
    }

    @Override
    public void dispose() {
        batch.dispose();
        loadingTexture.dispose();
    }
}
