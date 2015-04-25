package com.djammr.westernknights.ui;

import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.djammr.westernknights.Assets;
import com.djammr.westernknights.WKGame;
import com.djammr.westernknights.util.assetloaders.Overlap2DUILoader;
import com.djammr.westernknights.util.assetloaders.settings.Overlap2DUISettings;
import com.djammr.westernknights.util.controllers.MainMenuController;
import com.djammr.westernknights.util.observers.Observable;

import java.util.Map;

/**
 * Main Menu View
 */
public class MainMenu extends UIView {

    private MainMenuController controller;

    Image imgBg;
    Label btnContinue;
    Label btnSave;
    Label btnLoad;
    Label btnOptions;
    Label btnExtras;
    Label btnExit;
    Label btnVideo;
    Label btnAudio;
    Label btnControls;
    Label btnCredits;
    Label btnAchievements;
    Label btnPlayerStats;


    public MainMenu(MainMenuController controller) {
        super(controller);
        this.controller = controller;
    }

    @Override
    public void createUI() {
        Overlap2DUILoader.Parameters params = new Overlap2DUILoader.Parameters();
        params.set(Assets.overlap2DUIProject, Assets.uiMainMenu,  Assets.overlap2DUIAtlas, Assets.overlap2DUIFonts, stage, actors);
        params.loadedCallback = new AssetLoaderParameters.LoadedCallback() {
            @Override
            public void finishedLoading(AssetManager assetManager, String fileName, Class type) {
                loadUI();
            }
        };
        Assets.manager.load(Assets.uiMainMenuID, Overlap2DUISettings.class, params);
    }

    public void loadUI() {
        imgBg = (Image)actors.get("bg");
        btnContinue = (Label)actors.get("lbContinue");
        btnSave = (Label)actors.get("lbSave");
        btnLoad = (Label)actors.get("lbLoad");
        btnOptions = (Label)actors.get("lbOptions");
        btnExtras = (Label)actors.get("lbExtras");
        btnExit = (Label)actors.get("lbExit");
        btnVideo = (Label)actors.get("lbVideo");
        btnAudio = (Label)actors.get("lbAudio");
        btnControls = (Label)actors.get("lbControls");
        btnCredits = (Label)actors.get("lbCredits");
        btnAchievements = (Label)actors.get("lbAchievements");
        btnPlayerStats = (Label)actors.get("lbPlayerStats");

        btnContinue.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                WKGame.logger.logDebug("Continue");
            }
        });

        btnVideo.setVisible(false);
        btnAudio.setVisible(false);
        btnControls.setVisible(false);
        btnCredits.setVisible(false);
        btnAchievements.setVisible(false);
        btnPlayerStats.setVisible(false);

        controller.addInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
    }

    @Override
    public void dispose() {
        super.dispose();
    }

    @Override
    public void update(Observable obs, Map<String, Object> data) {
    }
}
