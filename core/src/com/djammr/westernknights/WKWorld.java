package com.djammr.westernknights;

import com.djammr.westernknights.entity.EntityManager;
import com.djammr.westernknights.entity.systems.Box2DSystem;
import com.djammr.westernknights.entity.systems.RenderingSystem;

/**
 * Holds a Box2D world read from an Overlap2D file. Each level should be a World and set in the GameScreen
 */
public abstract class WKWorld {

    protected EntityManager entities;


    public WKWorld() {
        entities = new EntityManager();

        entities.getEngine().addSystem(new RenderingSystem(16, 9));
        entities.getEngine().addSystem(new Box2DSystem());

        entities.getEngine().getSystem(Box2DSystem.class).setDebugCamera(entities.getEngine().getSystem(RenderingSystem.class).getCamera());
    }

    /**
     * Load assets and entities here. Asset.manager.finishloading() is called by the screen automatically
     */
    public abstract void load();

    public void update(float delta) {
        entities.update(delta);
    }

    public void dispose() {
        entities.dispose();
    }

    public EntityManager getEntities() {
        return entities;
    }
}
