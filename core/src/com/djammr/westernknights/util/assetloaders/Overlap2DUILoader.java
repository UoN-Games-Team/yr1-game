package com.djammr.westernknights.util.assetloaders;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.djammr.westernknights.util.assetloaders.settings.Overlap2DUISettings;
import com.djammr.westernknights.util.loaders.Overlap2DLoader;

import java.util.Map;

/**
 * Loads and Overlap2D Scene to a Stage and returns a setting file.<br/>
 * Paths should be given as internal strings.
 */
public class Overlap2DUILoader extends AsynchronousAssetLoader<Overlap2DUISettings, Overlap2DUILoader.Parameters> {

    /** Paths should be given as internal strings.*/
    public static class Parameters extends AssetLoaderParameters<Overlap2DUISettings> {
        String projectPath;
        String scenePath;
        String fontFolderPath;
        String spriteAnimationsPath;
        Stage stage;
        Map<String, Actor> actors;

        public void set(String projectPath, String scenePath, String fontFolderPath, String spriteAnimationsPath, Stage stage, Map<String, Actor> actors) {
            this.projectPath = projectPath;
            this.scenePath = scenePath;
            this.fontFolderPath = fontFolderPath;
            this.spriteAnimationsPath = spriteAnimationsPath;
            this.stage = stage;
            this.actors = actors;
        }
    }

    Overlap2DUISettings settings;

    public Overlap2DUILoader() {
        super(new InternalFileHandleResolver());
    }
    public Overlap2DUILoader(FileHandleResolver resolver) {
        super(resolver);
    }

    @Override
    public void loadAsync(AssetManager manager, String fileName, FileHandle file, Parameters parameter) {
        settings = new Overlap2DUISettings();
        Overlap2DLoader.loadUI(resolve(parameter.projectPath), resolve(parameter.scenePath), parameter.fontFolderPath, parameter.spriteAnimationsPath, parameter.stage, parameter.actors);
    }

    @Override
    public Overlap2DUISettings loadSync(AssetManager manager, String fileName, FileHandle file, Parameters parameter) {
        Overlap2DUISettings settings = this.settings;
        this.settings = null;
        return settings;
    }

    @Override
    public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file, Parameters parameter) {
        return null;
    }
}
