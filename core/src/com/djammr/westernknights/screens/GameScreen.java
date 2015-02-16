package com.djammr.westernknights.screens;

import com.badlogic.gdx.physics.box2d.Box2D;
import com.djammr.westernknights.Assets;
import com.djammr.westernknights.WKGame;
import com.djammr.westernknights.WKWorld;
import com.djammr.westernknights.entity.systems.InputSystem;
import com.djammr.westernknights.levels.TradingHub;
import com.djammr.westernknights.ui.DebugUI;
import com.djammr.westernknights.util.controllers.DebugController;
import com.djammr.westernknights.util.controllers.UIController;
import com.djammr.westernknights.util.input.InputMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Main Game screen
 */
public class GameScreen extends WKScreen {

    private InputMapper inputMapper = new InputMapper();
    private List<UIController> uiControllers = new ArrayList<UIController>();
    private Map<String, WKWorld> worlds = new HashMap<String, WKWorld>();
    private WKWorld currentWorld;


    public GameScreen(WKGame game) {
        super(game);
        Box2D.init();
        addWorld(Assets.lvlTradingHub, new TradingHub());
        getInputMultiplexer().addProcessor(inputMapper);
    }

    @Override
    public void load() {
        setWorld(Assets.lvlTradingHub);
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
        if (currentWorld != null) {
            inputMapper.removeObserver(currentWorld.getEntities().getEngine().getSystem(InputSystem.class));
        }
        currentWorld = worlds.get(name);
        currentWorld.doLoad();
        inputMapper.registerObserver(currentWorld.getEntities().getEngine().getSystem(InputSystem.class));
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
