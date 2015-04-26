package com.djammr.westernknights;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import com.djammr.westernknights.screens.GameScreen;
import com.djammr.westernknights.screens.MainMenuScreen;
import com.djammr.westernknights.screens.ScreenManager;
import com.djammr.westernknights.screens.SplashScreen;
import com.djammr.westernknights.util.ConsoleLogger;
import com.djammr.westernknights.util.Logger;
import com.djammr.westernknights.util.SaveManager;
import com.djammr.westernknights.util.input.keybindings.KeyMapManager;

import javax.naming.ldap.Control;

/**
 * Game entry point
 */
public class WKGame extends Game {

	public static final String VERSION = "0.0.5";
	public static final int SCREEN_WIDTH = 1280;
	public static final int SCREEN_HEIGHT = 720;
	public static final float METERS_TO_PIXELS = 60f;
	public static final float PIXELS_TO_METERS = 1/METERS_TO_PIXELS;
	public static final int DEBUG_KEY = Input.Keys.APOSTROPHE;
	public static final Logger logger = new ConsoleLogger();
	public static final SaveManager saveManager = new SaveManager();
	public static final KeyMapManager keyMaps = new KeyMapManager();
	public static double gameTime = 0;
    public static boolean debugEnabled = false;

	private ScreenManager screenManager;


	@Override
	public void create () {
		logger.setLogLevel(Logger.LOG_DEBUG);
		logger.logDebug("Starting game");

		Assets.init();
		keyMaps.loadDefaultKeyMap();
		keyMaps.loadDefaultControllerMap();

		screenManager = new ScreenManager(this);
		screenManager.addScreen("splash_screen", new SplashScreen(this));
		screenManager.addScreen("main_menu", new MainMenuScreen(this));
		screenManager.addScreen("game", new GameScreen(this));

		screenManager.getScreen("splash_screen").load();
		screenManager.setScreen("splash_screen", false);
		screenManager.getScreen("main_menu").load();
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

	/**
	 * Closes the game
	 */
	public void exit() {
		Gdx.app.exit();
	}

	public ScreenManager getScreens() {
		return screenManager;
	}
}
