package com.djammr.westernknights.levels;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Texture;
import com.djammr.westernknights.Assets;
import com.djammr.westernknights.WKWorld;
import com.djammr.westernknights.entity.EntityFactory;
import com.djammr.westernknights.entity.systems.Box2DSystem;

/**
 * Trading Hub Level
 */
public class TradingHub extends WKWorld {

    public TradingHub() {
        super(); // Make sure you call super() on all levels!
        Entity testEntity = EntityFactory.createEntity(entities.getEngine().getSystem(Box2DSystem.class));
        entities.addEntity(testEntity, "test");
    }

    @Override
    public void load() {
        Assets.load(Assets.testTexture, Texture.class);
    }
}
