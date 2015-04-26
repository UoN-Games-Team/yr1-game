package com.djammr.westernknights.ui;

import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.djammr.westernknights.Assets;
import com.djammr.westernknights.WKGame;
import com.djammr.westernknights.util.assetloaders.Overlap2DUILoader;
import com.djammr.westernknights.util.assetloaders.settings.Overlap2DUISettings;
import com.djammr.westernknights.util.controllers.PlayerHUDController;
import com.djammr.westernknights.util.observers.Observable;
import com.djammr.westernknights.util.observers.ObserverKeys;

import java.util.Map;

/**
 * Play HUD View
 */
public class PlayerHUD extends UIView {

    private PlayerHUDController controller;
    private float healthBarFullWidth;
    private Image imgHealthBar;
    private Image imgXpBar;

    public PlayerHUD(PlayerHUDController controller) {
        super(controller, Assets.uiHud, Assets.uiHudID);
        this.controller = controller;
    }

    public void loadUI() {
        imgHealthBar = (Image)actors.get("health_bar");
        imgXpBar = (Image)actors.get("xp_bar");
        healthBarFullWidth = imgXpBar.getWidth();
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
        if (data.containsKey(ObserverKeys.PLAYER_HEALTH_PERCENT)) {
            imgHealthBar.setWidth(healthBarFullWidth * (float)data.get(ObserverKeys.PLAYER_HEALTH_PERCENT));
        }
    }
}
