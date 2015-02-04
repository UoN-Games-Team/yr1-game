package com.djammr.westernknights.levels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.djammr.westernknights.Assets;
import com.djammr.westernknights.WKWorld;
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
        Overlap2DLoader.loadMap(Gdx.files.internal(Assets.overlap2DProject),
                Gdx.files.internal(Assets.lvlTestScene),
                Assets.manager.get(Assets.overlap2DAtlas, TextureAtlas.class), getEntities());
    }
}
