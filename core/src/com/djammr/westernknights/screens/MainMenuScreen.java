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

    private InputMapper inputMapper = new InputMapper();
    private Map<String, UIController> uiControllers = new HashMap<String, UIController>();


    public MainMenuScreen(WKGame game) {
        super(game);
    }

    @Override
    public void load() {
        MainMenuController menuController = new MainMenuController(this);
        uiControllers.put("main_menu", menuController);
    }

    @Override
    public void loadComplete() {
        uiControllers.get("main_menu").setView(new MainMenu((MainMenuController)uiControllers.get("main_menu")));
        getInputMultiplexer().addProcessor(inputMapper);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        for (UIController controller : uiControllers.values()) {
            controller.update(delta);
        }
    }

    @Override
    public void resize(int width, int height) {
        for (UIController controller : uiControllers.values()) {
            controller.resize(width, height);
        }
    }

    @Override
    public void dispose() {
        for (UIController controller : uiControllers.values()) {
            controller.dispose();
        }
        uiControllers.clear();
    }
}
