package com.djammr.westernknights;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.djammr.westernknights.screens.GameScreen;
import com.djammr.westernknights.screens.ScreenManager;
import com.djammr.westernknights.util.ConsoleLogger;
import com.djammr.westernknights.util.Logger;

/**
 * Game entry point
 */
public class WKGame extends Game {

	public static final Logger logger = new ConsoleLogger();

	private ScreenManager screenManager;


	@Override
	public void create () {
		logger.setLogLevel(Logger.LOG_DEBUG);
		logger.logDebug("Starting game");

		Assets.init();
		screenManager = new ScreenManager(this);
		screenManager.addScreen("game", new GameScreen(this));
		screenManager.setScreen("game");
	}

	@Override
	public void render() {
		super.render();
		Assets.update();
	}

	@Override
	public void dispose() {
		screenManager.dispose();
		Assets.dispose();
	}

	public ScreenManager getScreens() {
		return screenManager;
	}
}
