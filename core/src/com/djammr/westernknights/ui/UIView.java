package com.djammr.westernknights.ui;

import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.djammr.westernknights.Assets;
import com.djammr.westernknights.WKGame;
import com.djammr.westernknights.util.assetloaders.Overlap2DUILoader;
import com.djammr.westernknights.util.assetloaders.settings.Overlap2DUISettings;
import com.djammr.westernknights.util.controllers.UIController;
import com.djammr.westernknights.util.View;

import java.util.HashMap;
import java.util.Map;

/**
 * Abstract View class for UIs
 */
public abstract class UIView extends View {

    protected Map<String, Actor> actors;
    protected Stage stage;
    private boolean visible = true;


    public UIView(UIController controller) {
        super(controller);
        stage = new Stage();
        actors = new HashMap<String, Actor>();
        controller.addInputProcessor(stage);
    }

    /**
     * @param scenePath path to Overlap2D scene
     * @param sceneID AssetManager ID for the loaded scene
     */
    public UIView(UIController controller, String scenePath, String sceneID) {
        this(controller);
        createUI(scenePath, sceneID);
    }

    public void createUI(String scenePath, String sceneID) {
        Overlap2DUILoader.Parameters params = new Overlap2DUILoader.Parameters();
        params.set(Assets.overlap2DUIProject, scenePath,  Assets.overlap2DUIAtlas, Assets.overlap2DUIFonts, stage, actors);
        params.loadedCallback = new AssetLoaderParameters.LoadedCallback() {
            @Override
            public void finishedLoading(AssetManager assetManager, String fileName, Class type) {
                loadUI();
            }
        };
        Assets.manager.load(sceneID, Overlap2DUISettings.class, params);
    };

    /**
     * Load your UI here
     */
    public abstract void loadUI();

    /**
     * Should be called each frame from the {@link UIController}. Make sure you call super.render()!
     */
    public void render(float delta) {
        stage.act(delta);
        stage.draw();
    }

    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    public void dispose() {
        stage.dispose();
    }

    /**
     * Sets visible property for all actors on the stage
     */
    public void setVisible(boolean visible) {
        for (Actor actor : stage.getActors()) {
            actor.setVisible(visible);
        }
        this.visible = visible;
    }

    /**
     * @return Whether the stage is visible
     */
    public boolean isVisible() {
        return visible;
    }
}
