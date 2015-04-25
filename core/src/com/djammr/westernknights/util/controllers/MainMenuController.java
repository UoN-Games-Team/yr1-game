package com.djammr.westernknights.util.controllers;

import com.djammr.westernknights.WKWorld;
import com.djammr.westernknights.entity.components.MessagingComponent;
import com.djammr.westernknights.screens.GameScreen;
import com.djammr.westernknights.screens.MainMenuScreen;
import com.djammr.westernknights.ui.UIView;

/**
 * Controller for the Main Menu
 */
public class MainMenuController extends UIController {


    public MainMenuController(MainMenuScreen screen) {
        super(screen);
        this.screen = screen;
    }

    @Override
    public void setView(UIView view) {
        super.setView(view);
    }
}
