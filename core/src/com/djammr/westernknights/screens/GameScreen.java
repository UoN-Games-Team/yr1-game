package com.djammr.westernknights.screens;

import com.badlogic.gdx.physics.box2d.Box2D;
import com.djammr.westernknights.WKGame;
import com.djammr.westernknights.WKWorld;
import com.djammr.westernknights.levels.TradingHub;
import com.djammr.westernknights.ui.DebugUI;
import com.djammr.westernknights.util.Controllers.DebugController;
import com.djammr.westernknights.util.Controllers.UIController;

import java.util.ArrayList;
import java.util.List;

/**
 * Main Game screen
 */
public class GameScreen extends WKScreen {

    private List<UIController> uiControllers = new ArrayList<UIController>();
    private WKWorld world;


    public GameScreen(WKGame game) {
        super(game);
        Box2D.init();
        setWorld(new TradingHub());
    }

    @Override
    public void load() {
        world.doLoad();

        DebugController dbgController = new DebugController(this);
        dbgController.setView(new DebugUI(dbgController));
        uiControllers.add(dbgController);

        super.load();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        if (world != null) world.update(delta);

        for (UIController controller : uiControllers) {
            controller.update(delta);
        }
    }

    @Override
    public void resize(int width, int height) {
        for (UIController controller : uiControllers) {
            controller.resize(width, height);
        }
    }

    @Override
    public void dispose() {
        for (UIController controller : uiControllers) {
            controller.dispose();
        }
        uiControllers.clear();
        if (world != null) world.dispose();
    }

    public void setWorld(WKWorld world) {
        game.getScreens().setScreen("loading");
        if (this.world != null) this.world.dispose();
        this.world = world;
    }

    public WKWorld getWorld() {
        return world;
    }
}
