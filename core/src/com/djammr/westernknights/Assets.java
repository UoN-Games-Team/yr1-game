package com.djammr.westernknights;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.djammr.westernknights.util.AssetLoaders.Overlap2DMapLoader;
import com.djammr.westernknights.util.AssetLoaders.Overlap2DUILoader;
import com.djammr.westernknights.util.AssetLoaders.Settings.Overlap2DMapSettings;
import com.djammr.westernknights.util.AssetLoaders.Settings.Overlap2DUISettings;

import java.util.HashMap;
import java.util.Map;

/**
 * Manages a libGDX AssetManager. Load and request assets through this class.<br/>
 * Use Assets.get(path, type) to load an asset or retrieve it if it exists.
 * See https://github.com/libgdx/libgdx/wiki/Managing-your-assets for more info
 */
public class Assets {

    public static AssetManager manager;
    public static Map<String, BitmapFont> fonts = new HashMap<String, BitmapFont>();

    // Add all asset paths here and reference when loading assets
    public static String testTexture = "images/test.png";
    public static String loadingTexture = "images/loading.png";

    // UI
    public static Skin skinDefault;
    public static String skinDefaultJson = "images/ui/skins/default/uiskin.json";

    // Overlap2D Project
    public static String overlap2DProject = "levels/test/project.dt";
    public static String overlap2DAtlas = "levels/test/orig/pack.atlas";
    public static String overlap2DFonts = "levels/test/freetypefonts";
    // Scenes
    public static String uiDebugScene = "levels/test/scenes/DebugUI.dt";
    public static String lvlTestScene = "levels/test/scenes/MainScene.dt";

    // Loaded Overlap2D scene from Overlap2DLoaders. TODO: Add ability to load them again from get (or just cache)
    public static String lvlTest = "lvlTest";
    public static String uiDebug = "uiDebug";


    /**
     * Creates the AssetManager instance
     */
    public static void init() {
        manager = new AssetManager();
        manager.setLoader(Overlap2DMapSettings.class, new Overlap2DMapLoader());
        manager.setLoader(Overlap2DUISettings.class, new Overlap2DUILoader());
        // Put assets you want to cache at the start of the game here
        skinDefault = new Skin(Gdx.files.internal(skinDefaultJson));
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
     * Gets a font from the cache if it exists otherwise creates it based on the following arguments
     * @param fontID id of the font, must be in the format: {ttf-file-name}-{size}
     * @param defaultPath path to the ttf file used to create the font if it doesn't exist
     * @return the BitmapFont or null if it doesn't exist and no path is provided
     */
    public static BitmapFont getFont(String fontID, String defaultPath) {
        if (fonts.containsKey(fontID)) {
            return fonts.get(fontID);
        }
        else if (defaultPath != null) {
            FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(defaultPath));
            FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
            parameter.size = Integer.parseInt(fontID.split("-")[1]);
            BitmapFont font = generator.generateFont(parameter);
            generator.dispose();
            fonts.put(fontID, font);
            return font;
        }
        return null;
    }

    /**
     * see {@link #getFont}
     */
    public static BitmapFont getFont(String fontID) {
        return getFont(fontID, null);
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
