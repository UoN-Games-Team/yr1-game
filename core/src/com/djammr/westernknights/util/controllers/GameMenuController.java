package com.djammr.westernknights.util.controllers;

import com.djammr.westernknights.WKWorld;
import com.djammr.westernknights.entity.components.MessagingComponent;
import com.djammr.westernknights.entity.systems.InputSystem;
import com.djammr.westernknights.screens.GameScreen;
import com.djammr.westernknights.ui.GameMenu;
import com.djammr.westernknights.ui.UIView;
import com.djammr.westernknights.util.observers.InputObserver;

/**
 * Controller for the Game Menu
 */
public class GameMenuController extends UIController {


    public GameMenuController(GameScreen screen) {
        super(screen);
        this.screen = screen;
    }

    @Override
    public void setView(UIView view) {
        super.setView(view);
        ((GameScreen)screen).getInputMapper().registerObserver((GameMenu)view);
    }

    @Override
    public void resetView() {
        ((GameScreen)screen).getInputMapper().removeObserver((GameMenu) view);
    }

    /**
     * Resumes or Pauses the game
     */
    public void togglePause() {
        if (!screen.isPaused()) screen.pause();
        else screen.resume();
    }
}
