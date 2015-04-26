package com.djammr.westernknights.screens;

import com.djammr.westernknights.WKGame;
import com.djammr.westernknights.ui.MainMenu;
import com.djammr.westernknights.ui.PlayerHUD;
import com.djammr.westernknights.util.controllers.MainMenuController;
import com.djammr.westernknights.util.controllers.PlayerHUDController;
import com.djammr.westernknights.util.controllers.UIController;
import com.djammr.westernknights.util.input.InputMapper;

import java.util.HashMap;
import java.util.Map;

/**
 * Main Menu screen
 */
public class MainMenuScreen extends WKScreen {


    public MainMenuScreen(WKGame game) {
        super(game);
    }

    @Override
    public void load() {
        uiControllers.put("main_menu", new MainMenuController(this));
        uiViews.put("main_menu", new MainMenu((MainMenuController) uiControllers.get("main_menu")));
    }

    @Override
    public void loadComplete() {
        super.loadComplete();
        uiControllers.get("main_menu").setView(uiViews.get("main_menu"));
    }
}
