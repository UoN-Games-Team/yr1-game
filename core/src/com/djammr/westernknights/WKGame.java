package com.djammr.westernknights;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Input;
import com.djammr.westernknights.screens.GameScreen;
import com.djammr.westernknights.screens.ScreenManager;
import com.djammr.westernknights.util.ConsoleLogger;
import com.djammr.westernknights.util.Logger;

/**
 * Game entry point
 */
public class WKGame extends Game {

	public static final String VERSION = "0.0.1";
	public static final float METERS_TO_PIXELS = 60f;
	public static final float PIXELS_TO_METERS = 1/METERS_TO_PIXELS;
	public static final int DEBUG_KEY = Input.Keys.APOSTROPHE;
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
