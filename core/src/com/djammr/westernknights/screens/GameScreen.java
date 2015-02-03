package com.djammr.westernknights.screens;

import com.badlogic.gdx.physics.box2d.Box2D;
import com.djammr.westernknights.WKGame;
import com.djammr.westernknights.WKWorld;
import com.djammr.westernknights.levels.TradingHub;

/**
 * Main Game screen
 */
public class GameScreen extends WKScreen {

    private WKWorld world;


    public GameScreen(WKGame game) {
        super(game);
        Box2D.init();
        setWorld(new TradingHub());
    }

    @Override
    public void load() {
        world.load();
        super.load();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        if (world != null) world.update(delta);
    }

    @Override
    public void dispose() {
        if (world != null) world.dispose();
    }

    public void setWorld(WKWorld world) {
        game.getScreens().setScreen("loading");
        if (this.world != null) this.world.dispose();
        this.world = world;
    }
}
