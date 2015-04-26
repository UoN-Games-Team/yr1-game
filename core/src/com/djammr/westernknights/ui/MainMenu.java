package com.djammr.westernknights.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.djammr.westernknights.Assets;
import com.djammr.westernknights.WKGame;
import com.djammr.westernknights.util.assetloaders.Overlap2DUILoader;
import com.djammr.westernknights.util.assetloaders.settings.Overlap2DUISettings;
import com.djammr.westernknights.util.controllers.MainMenuController;
import com.djammr.westernknights.util.observers.Observable;

import java.util.ArrayList;
import java.util.Map;

/**
 * Main Menu View
 */
public class MainMenu extends UIView {

    private MainMenuController controller;

    private Image imgBg;
    private Label btnContinue;
    private Label btnSave;
    private Label btnLoad;
    private Label btnOptions;
    private Label btnExtras;
    private Label btnExit;
    private Label btnVideo;
    private Label btnAudio;
    private Label btnControls;
    private Label btnCredits;
    private Label btnAchievements;
    private Label btnPlayerStats;

    private ArrayList<Label> buttons = new ArrayList<Label>();



    public MainMenu(MainMenuController controller) {
        super(controller, Assets.uiMainMenu, Assets.uiMainMenuID);
        this.controller = controller;
    }

    public void loadUI() {
        imgBg = (Image)actors.get("bg");
        btnContinue = (Label)actors.get("lbContinue");
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

        buttons.add(btnContinue);
        buttons.add(btnLoad);
        buttons.add(btnOptions);
        buttons.add(btnExtras);
        buttons.add(btnExit);
        buttons.add(btnVideo);
        buttons.add(btnAudio);
        buttons.add(btnControls);
        buttons.add(btnCredits);
        buttons.add(btnAchievements);
        buttons.add(btnPlayerStats);
        for (Label button : buttons)
            button.setColor(button.getColor().r, button.getColor().g, button.getColor().b, 0.5f);

        stage.addListener(new InputListener() {
            @Override
            public boolean mouseMoved(InputEvent event, float x, float y) {
                for (Label button : buttons)
                    button.setColor(button.getColor().r, button.getColor().g, button.getColor().b, 0.5f);
                event.getTarget().setColor(event.getTarget().getColor().r, event.getTarget().getColor().g, event.getTarget().getColor().b, 1f);
                return true;
            }
        });

        btnContinue.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                controller.setScreen("game");
            }
        });

        btnExit.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                controller.exitGame();
            }
        });

        btnVideo.setVisible(false);
        btnAudio.setVisible(false);
        btnControls.setVisible(false);
        btnCredits.setVisible(false);
        btnAchievements.setVisible(false);
        btnPlayerStats.setVisible(false);
    }

    @Override
    public void update(Observable obs, Map<String, Object> data) {
    }
}
