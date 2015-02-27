package com.djammr.westernknights.util.controllers;

import com.djammr.westernknights.screens.GameScreen;

/**
 * Controller for the PlayerHUD
 */
public class PlayerHUDController extends UIController {


    public PlayerHUDController(GameScreen screen) {
        super(screen);
        this.screen = (GameScreen)screen;
    }
}
