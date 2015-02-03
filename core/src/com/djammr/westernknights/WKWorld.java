package com.djammr.westernknights;

import com.djammr.westernknights.entity.EntityManager;
import com.djammr.westernknights.entity.systems.Box2DSystem;
import com.djammr.westernknights.entity.systems.RenderingSystem;

/**
 * Holds a Box2D world read from an Overlap2D file. Each level should be a World and set in the GameScreen
 */
public abstract class WKWorld {

    protected boolean loaded = false;
    protected EntityManager entities;


    public WKWorld() {

    }

    /**
     * Call externally to load the world
     */
    public final void doLoad() {
        if (!loaded) {
            WKGame.logger.logDebug("Loading World");
            entities = new EntityManager();
            entities.getEngine().addSystem(new RenderingSystem(1280f * WKGame.PIXELS_TO_METERS, 720f * WKGame.PIXELS_TO_METERS));
            entities.getEngine().addSystem(new Box2DSystem());
            entities.getEngine().getSystem(Box2DSystem.class).setDebugCamera(entities.getEngine().getSystem(RenderingSystem.class).getCamera());

            // load subclass
            load();
            loaded = true;
        }
    }

    /**
     * Load assets and entities here. Will only run once during the world's life cycle. <br/>
     * Asset.manager.finishloading() is called by the screen automatically
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
