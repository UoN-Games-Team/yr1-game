package com.djammr.westernknights.ui;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.djammr.westernknights.Assets;
import com.djammr.westernknights.util.controllers.GameMenuController;
import com.djammr.westernknights.util.controllers.MainMenuController;
import com.djammr.westernknights.util.input.keybindings.GameActions;
import com.djammr.westernknights.util.observers.InputObserver;
import com.djammr.westernknights.util.observers.Observable;

import java.util.ArrayList;
import java.util.Map;

/**
 * Game Menu View
 */
public class GameMenu extends UIView implements InputObserver {

    private GameMenuController controller;

    private Image imgBg;
    private Label btnMap;
    private Label btnInventory;
    private Label btnAbilities;
    private Label btnBounties;
    private Label btnLore;
    private Label btnPlayerStats;

    private ArrayList<Label> buttons = new ArrayList<Label>();


    public GameMenu(GameMenuController controller) {
        super(controller, Assets.uiInventory, Assets.uiInventoryID);
        this.controller = controller;
    }

    public void loadUI() {
        imgBg = (Image)actors.get("bg");
        btnMap = (Label)actors.get("lbMap");
        btnInventory = (Label)actors.get("lbInventory");
        btnAbilities = (Label)actors.get("lbAbilities");
        btnBounties = (Label)actors.get("lbBounties");
        btnLore = (Label)actors.get("lbLore");
        btnPlayerStats = (Label)actors.get("lbPlayerStats");

        buttons.add(btnMap);
        buttons.add(btnInventory);
        buttons.add(btnAbilities);
        buttons.add(btnBounties);
        buttons.add(btnLore);
        buttons.add(btnPlayerStats);
        for (Label button : buttons) {
            button.setColor(button.getColor().r, button.getColor().g, button.getColor().b, 0.6f);
            button.setName("inactive");
        }
        btnInventory.setColor(btnInventory.getColor().r, btnInventory.getColor().g, btnInventory.getColor().b, 1f);
        btnInventory.setName("active");

        stage.addListener(new InputListener() {
            @Override
            public boolean mouseMoved(InputEvent event, float x, float y) {
                for (Label button : buttons)
                    if (!button.getName().equals("active")) button.setColor(button.getColor().r, button.getColor().g, button.getColor().b, 0.6f);
                event.getTarget().setColor(event.getTarget().getColor().r, event.getTarget().getColor().g, event.getTarget().getColor().b, 1f);
                return true;
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                for (Label touchButton : buttons) {
                    touchButton.setName("inactive");
                    touchButton.setColor(touchButton.getColor().r, touchButton.getColor().g, touchButton.getColor().b, 0.6f);
                }
                event.getTarget().setName("active");
                event.getTarget().setColor(event.getTarget().getColor().r, event.getTarget().getColor().g, event.getTarget().getColor().b, 1f);
                return false;
            }
        });

        /*btnExit.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

            }
        });*/
        setVisible(false);
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
    }

    @Override
    public void inputEvent(int event) {
        switch (event) {
            case GameActions.GAME_MENU:
                controller.togglePause();
                setVisible(!isVisible());
                break;
        }
    }
}
