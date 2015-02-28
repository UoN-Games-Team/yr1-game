package com.djammr.westernknights;

import com.badlogic.gdx.graphics.Color;
import com.djammr.westernknights.entity.EntityManager;
import com.djammr.westernknights.entity.systems.*;

/**
 * Holds a Box2D world read from an Overlap2D file. Each level should be a World and set in the GameScreen
 */
public abstract class WKWorld {

    // Lighting
    public static float AMBIENT_ALPHA_DAY = 0.8f;
    public static float AMBIENT_ALPHA_NIGHT = 0.1f;
    public static final Color AMBIENT_COLOUR = new Color();  // Update from Overlap2DLoader and RayHandler ambient light should be set from this.
    // Entities
    public static final float PLAYER_WIDTH = 0.5f;
    public static final float PLAYER_HEIGHT = 1.8f;
    public static final String PLAYER_IDENTIFIER = "player";
    public static final String GROUND_IDENTIFIER = "ground";
    public static final String NPC_TYPE = "npc";
    public static final String FOOT_SENSOR_IDENTIFIER = "foot_sensor";


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
            entities.getEngine().addSystem(new InputSystem());
            entities.getEngine().addSystem(new CameraSystem(20, 11.25f)); // 20m x 11.25m (16x9)
            entities.getEngine().addSystem(new MovementSystem());
            entities.getEngine().addSystem(new AnimationSystem());
            entities.getEngine().addSystem(new VisualSystem());
            entities.getEngine().addSystem(new RenderingSystem());
            entities.getEngine().addSystem(new Box2DSystem());

            entities.getEngine().getSystem(RenderingSystem.class).setCamera(entities.getEngine().getSystem(CameraSystem.class).getCamera());
            entities.getEngine().getSystem(Box2DSystem.class).setCamera(entities.getEngine().getSystem(CameraSystem.class).getCamera());

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
