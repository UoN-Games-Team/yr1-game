package com.djammr.westernknights.screens;

import com.badlogic.gdx.physics.box2d.Box2D;
import com.djammr.westernknights.Assets;
import com.djammr.westernknights.WKGame;
import com.djammr.westernknights.WKWorld;
import com.djammr.westernknights.entity.systems.InputSystem;
import com.djammr.westernknights.levels.Rivertown;
import com.djammr.westernknights.levels.TradingHub;
import com.djammr.westernknights.ui.DebugUI;
import com.djammr.westernknights.ui.PlayerHUD;
import com.djammr.westernknights.util.controllers.DebugController;
import com.djammr.westernknights.util.controllers.PlayerHUDController;
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
    private Map<String, WKWorld> worlds = new HashMap<String, WKWorld>();
    private WKWorld currentWorld;


    public GameScreen(WKGame game) {
        super(game);
        Box2D.init();
        addWorld(Assets.lvlTradingAreaID, new TradingHub());
        addWorld(Assets.lvlRivertownID, new Rivertown());
    }

    @Override
    public void load() {
        setWorld(Assets.lvlTradingAreaID);
        //setWorld(Assets.lvlRivertownID);

        uiControllers.put("debug", new DebugController(this));
        uiViews.put("debug", new DebugUI((DebugController)uiControllers.get("debug")));
        uiControllers.get("debug").setView(uiViews.get("debug"));

        uiControllers.put("player_hud", new PlayerHUDController(this));
        uiViews.put("player_hud", new PlayerHUD((PlayerHUDController)uiControllers.get("player_hud")));
    }

    @Override
    public void loadComplete() {
        super.loadComplete();
        uiControllers.get("player_hud").setView(uiViews.get("player_hud"));
        getInputMultiplexer().addProcessor(inputMapper);
    }

    @Override
    public void render(float delta) {
        if (currentWorld != null) currentWorld.update(delta);
        super.render(delta);
    }

    @Override
    public void dispose() {
        for (WKWorld world : worlds.values()) {
            world.dispose();
        }
    }

    /**
     * Changes the world to the registered world identified by name. The world will be loaded if it hasn't been already
     * @param name name/ID the world was registered as
     */
    public void setWorld(String name) {
        if (game.getScreens().getCurrentScreenID().equals("game")) game.getScreens().setScreen("loading");
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
