package com.djammr.westernknights.util.controllers;

import com.djammr.westernknights.screens.GameScreen;

/**
 * Controller for the DebugUI
 */
public class DebugController extends UIController {


    public DebugController(GameScreen screen) {
        super(screen);
        this.screen = (GameScreen)screen;
    }

    /**
     * @return number of entities in the current world
     */
    public int getEntities() {
        if (((GameScreen)screen).getWorld() != null) {
            return ((GameScreen) screen).getWorld().getEntities().getEntityCount();
        } else {
            return 0;
        }
    }
}
