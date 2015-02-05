package com.djammr.westernknights;

import com.djammr.westernknights.entity.EntityManager;
import com.djammr.westernknights.entity.systems.Box2DSystem;
import com.djammr.westernknights.entity.systems.RenderingSystem;
import com.djammr.westernknights.entity.systems.VisualSystem;

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
            entities.getEngine().addSystem(new RenderingSystem(20, 11.25f)); // 20m x 11.25m (16x9)
            entities.getEngine().addSystem(new Box2DSystem());
            entities.getEngine().addSystem(new VisualSystem());
            entities.getEngine().getSystem(Box2DSystem.class).setCamera(entities.getEngine().getSystem(RenderingSystem.class).getCamera());

            // load subclass
            load();
            loaded = true;
        }
    }

    /**
     * Load assets and entities here. Will only run once during the world's life cycle. <br/>
     * Asset.manager.finishloading() is called automatically
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
