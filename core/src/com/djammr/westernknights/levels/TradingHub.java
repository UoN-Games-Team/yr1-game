package com.djammr.westernknights.levels;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.djammr.westernknights.Assets;
import com.djammr.westernknights.WKWorld;
import com.djammr.westernknights.entity.EntityFactory;
import com.djammr.westernknights.entity.systems.Box2DSystem;
import com.djammr.westernknights.util.loaders.Overlap2DLoader;

/**
 * Trading Hub Level
 */
public class TradingHub extends WKWorld {

    public TradingHub() {
        super(); // Make sure you call super() on all levels!
    }

    @Override
    public void load() {
        Assets.load(Assets.testTexture, Texture.class);
        Assets.load(Assets.lvlsAtlas, TextureAtlas.class);
        Assets.manager.finishLoading();

        Overlap2DLoader.loadMap(Gdx.files.internal(Assets.lvlsProject),
                                Gdx.files.internal(Assets.lvlTestScene),
                                Assets.manager.get(Assets.lvlsAtlas, TextureAtlas.class), getEntities());
    }
}
