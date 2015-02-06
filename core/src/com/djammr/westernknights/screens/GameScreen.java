package com.djammr.westernknights.screens;

import com.badlogic.gdx.physics.box2d.Box2D;
import com.djammr.westernknights.WKGame;
import com.djammr.westernknights.WKWorld;
import com.djammr.westernknights.levels.TradingHub;
import com.djammr.westernknights.ui.DebugUI;
import com.djammr.westernknights.util.Controllers.DebugController;
import com.djammr.westernknights.util.Controllers.UIController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Main Game screen
 */
public class GameScreen extends WKScreen {

    private List<UIController> uiControllers = new ArrayList<UIController>();
    private Map<String, WKWorld> worlds = new HashMap<String, WKWorld>();
    private WKWorld currentWorld;


    public GameScreen(WKGame game) {
        super(game);
        Box2D.init();
        addWorld("trading_hub", new TradingHub());
    }

    @Override
    public void load() {
        setWorld("trading_hub");
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
        if (currentWorld != null) currentWorld.update(delta);

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

        for (WKWorld world : worlds.values()) {
            world.dispose();
        }
    }

    /**
     * Changes the world to the registered world identified by name. The world will be loaded if it hasn't been already
     * @param name name/ID the world was registered as
     */
    public void setWorld(String name) {
        game.getScreens().setScreen("loading");
        currentWorld = worlds.get(name);
        currentWorld.doLoad();
    }

    /**
     * Adds a world to the list of worlds for retrieval later
     * @param name name/ID to identify the world
     * @param world {@link WKWorld} instance
     */
    public void addWorld(String name, WKWorld world) {
        worlds.put(name, world);
    }

    public WKWorld getWorld() {
        return currentWorld;
    }

    public Map<String, WKWorld> getWorlds() {
        return worlds;
    }
}
