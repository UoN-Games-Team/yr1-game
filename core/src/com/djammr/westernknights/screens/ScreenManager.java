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
    private String prevScreen = "";
    private String currentScreen = "";


    public ScreenManager(WKGame game) {
        this.game = game;
        // Default loading screen, users can overwrite this with their own
        addScreen("loading", new LoadingScreen(game));
    }

    /**
     * Changes the game screen to the requested screen
     * @param screen Name of the registered screen to set
     * @param preLoad Whether to call load on the target screen
     */
    public void setScreen(String screen, boolean preLoad) {
        if (preLoad && !currentScreen.equals("loading")) {
            WKGame.logger.logDebug("Setting Screen: loading");
            game.setScreen(registeredScreens.get("loading"));
        }
        if (registeredScreens.containsKey(screen)) {
            WKGame.logger.logDebug("Loading Screen: " + screen);

            if (preLoad) {
                if (!registeredScreens.get(screen).isLoaded()) registeredScreens.get(screen).load();
                ((LoadingScreen) game.getScreen()).setTarget(screen);
            } else {
                WKGame.logger.logDebug("Setting Screen: " + screen);
                if (!registeredScreens.get(screen).isLoaded()) registeredScreens.get(screen).loadComplete();
                game.setScreen(registeredScreens.get(screen));
                prevScreen = currentScreen;
                currentScreen = screen;
            }
        } else {
            WKGame.logger.logError("Screen does not exist! Use addScreen()", new NullPointerException());
        }
    }

    /**
     * Changes the game screen to the requested screen. Shows the loading screen while loading the target screen.<br/>
     * @param screen Name of the registered screen to set
     */
    public void setScreen(String screen) {
        setScreen(screen, true);
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
     * @return currently set WKScreen instance
     */
    public WKScreen getScreen() {
        return (WKScreen)game.getScreen();
    }

    /**
     * @return WKScreen with ID screenID
     */
    public WKScreen getScreen(String screenID) {
        return registeredScreens.get(screenID);
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