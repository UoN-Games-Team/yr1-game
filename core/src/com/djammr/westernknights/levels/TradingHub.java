package com.djammr.westernknights.levels;

import com.badlogic.gdx.graphics.Texture;
import com.djammr.westernknights.Assets;
import com.djammr.westernknights.WKWorld;
import com.djammr.westernknights.util.assetloaders.Overlap2DMapLoader;
import com.djammr.westernknights.util.assetloaders.settings.Overlap2DMapSettings;


/**
 * Trading Hub Level
 */
public class TradingHub extends WKWorld {

    public TradingHub() {
        super(); // Make sure you call super() on all levels!
    }


    @Override
    public void load() {
        // Box texture
        Assets.load(Assets.testTexture, Texture.class);

        // Level
        Overlap2DMapLoader.Parameters params = new Overlap2DMapLoader.Parameters();
        params.set(Assets.overlap2DProject, Assets.lvlTestScene, Assets.overlap2DAtlas, getEntities());
        Assets.manager.load(Assets.lvlTest, Overlap2DMapSettings.class, params);
    }
}
