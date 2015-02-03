package com.djammr.westernknights;

import com.badlogic.gdx.assets.AssetManager;

/**
 * Manages a libGDX AssetManager. Load and request assets through this class.<br/>
 * Use Assets.get(path, type) to load an asset or retrieve it if it exists.
 * See https://github.com/libgdx/libgdx/wiki/Managing-your-assets for more info
 */
public class Assets {

    public static AssetManager manager;

    // Add all asset paths here and reference when loading assets
    public static String testTexture = "images/test.png";

    // Test Level
    public static String lvlsProject = "levels/test/project.dt";
    public static String lvlsAtlas = "levels/test/orig/pack.atlas";
    public static String lvlTestScene = "levels/test/scenes/MainScene.dt";


    /**
     * Creates the AssetManager instance
     */
    public static void init() {
        manager = new AssetManager();
        // Put assets you want to cache at the start of the game here
    }

    /**
     * Checks if an asset is loaded and if not, loads it.
     * @param path path to the asset
     * @param type class type of the asset
     */
    public static void load(String path, Class type) {
        if (!manager.isLoaded(path)) {
            manager.load(path, type);
        }
    }

    /**
     * Updates the AssetManager instance
     */
    public static void update() {
        manager.update();
    }

    public static void dispose() {
        manager.dispose();
    }
}
