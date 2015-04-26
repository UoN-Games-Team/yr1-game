package com.djammr.westernknights.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.djammr.westernknights.Assets;
import com.djammr.westernknights.WKGame;
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
    private float xpBarFullWidth;
    private Image imgHealthBar;
    private Image imgXpBar;
    private Image crest;

    public PlayerHUD(PlayerHUDController controller) {
        super(controller, Assets.uiHud, Assets.uiHudID);
        this.controller = controller;
    }

    public void loadUI() {
        imgHealthBar = (Image)actors.get("hp_bar");
        imgXpBar = (Image)actors.get("xp_bar");
        healthBarFullWidth = imgHealthBar.getWidth();
        xpBarFullWidth = imgXpBar.getWidth();
        imgXpBar.setWidth(0);

        crest = (Image)actors.get("crest");
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
        // Health Bar
        if (data.containsKey(ObserverKeys.PLAYER_HEALTH_PERCENT)) {
            float widthChange = imgHealthBar.getWidth() - (healthBarFullWidth * (float) data.get(ObserverKeys.PLAYER_HEALTH_PERCENT));
            imgHealthBar.setWidth(imgHealthBar.getWidth() - widthChange);
            imgHealthBar.moveBy(widthChange*imgHealthBar.getScaleX(), 0);
        }
        // XP Bar
        if (data.containsKey(ObserverKeys.PLAYER_XP_PERCENT)) {
            float widthChange = imgXpBar.getWidth() - (xpBarFullWidth * (float) data.get(ObserverKeys.PLAYER_XP_PERCENT));
            imgXpBar.setWidth(imgXpBar.getWidth() - widthChange);
        }

        // Level Crest
        if (data.containsKey(ObserverKeys.PLAYER_LEVEL_UP)) {
            int level = (int)data.get(ObserverKeys.PLAYER_LEVEL_UP);
            try {
                TextureRegionDrawable newCrest = new TextureRegionDrawable(new TextureRegion(new Texture("images/ui/crests/"+level+".png")));
                crest.setDrawable(newCrest);
            }
            catch (GdxRuntimeException ex) {
                // No crest for target level
            }
        }
    }
}
