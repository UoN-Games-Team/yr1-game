package com.djammr.westernknights.util.controllers;

import com.djammr.westernknights.WKWorld;
import com.djammr.westernknights.entity.components.MessagingComponent;
import com.djammr.westernknights.screens.GameScreen;
import com.djammr.westernknights.ui.UIView;

/**
 * Controller for the PlayerHUD
 */
public class PlayerHUDController extends UIController {


    public PlayerHUDController(GameScreen screen) {
        super(screen);
        this.screen = screen;
    }

    @Override
    public void setView(UIView view) {
        super.setView(view);
        ((GameScreen)screen).getWorld().getEntities().getEntity(WKWorld.PLAYER_IDENTIFIER).getComponent(MessagingComponent.class).registerObserver(this.view);
    }

    @Override
    public void resetView() {
        ((GameScreen)screen).getWorld().getEntities().getEntity(WKWorld.PLAYER_IDENTIFIER).getComponent(MessagingComponent.class).removeObserver(this.view);
    }
}
