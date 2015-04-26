package com.djammr.westernknights.util.controllers;

import com.djammr.westernknights.screens.LoadingScreen;
import com.djammr.westernknights.screens.MainMenuScreen;
import com.djammr.westernknights.ui.UIView;

/**
 * Controller for the Loading Screen
 */
public class LoadingController extends UIController {


    public LoadingController(LoadingScreen screen) {
        super(screen);
        this.screen = screen;
    }

    @Override
    public void setView(UIView view) {
        super.setView(view);
    }
}
