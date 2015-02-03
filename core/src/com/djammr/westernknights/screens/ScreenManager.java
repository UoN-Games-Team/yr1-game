package com.djammr.westernknights.screens;

import com.djammr.westernknights.WKGame;

import java.util.HashMap;
import java.util.Map;

/**
 * Screen Manager/Pooler, for easily caching and loading screens with automatic loading screen support. <br/>
 * Call game.getScreens().setScreen(name) to change screens
 */
public class ScreenManager {

    private final WKGame game;
    private Map<String, WKScreen> registeredScreens = new HashMap<String, WKScreen>();
    private String prevScreen;
    private String currentScreen;


    public ScreenManager(WKGame game) {
        this.game = game;
        // Default loading screen, users can overwrite this with their own
        addScreen("loading", new LoadingScreen(game));
        setScreen("loading");
    }

    /**
     * Changes the game screen to the requested screen
     * @param screen Name of the registered screen
     */
    public void setScreen(String screen) {
        if (!screen.equals("loading") && !currentScreen.equals("loading")) game.setScreen(registeredScreens.get("loading"));
        if (registeredScreens.containsKey(screen)) {
            WKGame.logger.logDebug("Setting screen: " + screen);
            prevScreen = currentScreen;
            currentScreen = screen;
            registeredScreens.get(screen).load();
            game.setScreen(registeredScreens.get(screen));
        } else {
            WKGame.logger.logError("Screen does not exist! Use addScreen()", new NullPointerException());
        }
    }

    /**
     * Register and cache a screen for setting later
     * @param ID name/ID for the screen (needed to retrieve it later)
     * @param screen the ZbeScreen instance
     */
    public void addScreen(String ID, WKScreen screen) {
        registeredScreens.put(ID, screen);
    }

    /**
     * Disposes and removes a registered screen from this manager.
     * Use when you no longer want to cache a screen.
     * @param screen the name of the screen to dispose and remove
     */
    public void removeScreen(String screen) {
        if (registeredScreens.containsKey(screen)) {
            registeredScreens.get(screen).dispose();
            registeredScreens.remove(screen);
        }
    }

    public void dispose() {
        WKGame.logger.logDebug("Disposing");
        for (WKScreen screen : registeredScreens.values()) {
            try {
                screen.dispose();
            } catch (NullPointerException ex) {
                WKGame.logger.logError("NullPointer disposing screen: " + screen + "\nTrace: " + ex.getCause());
            }
        }
        registeredScreens.clear();
    }

    /**
     * @return currently set ZbeScreen instance
     */
    public WKScreen getScreen() {
        return (WKScreen)game.getScreen();
    }

    /**
     * @return ID/Name of the previous screen
     */
    public String getPrevScreenID() {
        return prevScreen;
    }

    /**
     * @return ID/Name of the current screen
     */
    public String getCurrentScreenID() {
        return currentScreen;
    }
}