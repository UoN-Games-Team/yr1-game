package com.djammr.westernknights.util.input.keybindings;

import com.badlogic.gdx.utils.IntMap;
import com.djammr.westernknights.Assets;
import com.djammr.westernknights.WKGame;

import java.util.HashMap;
import java.util.Map;

/**
 * Manages saving, loading and binding of keys
 */
public class KeyMapManager {

    /** Map for Keyboard & Mouse */
    //private IntMap<Integer> keyMap = new IntMap<Integer>();
    private Map<String, Float> keyMap;
    /** Map for a Controller */
    //private IntMap<Integer> controllerMap = new IntMap<Integer>();
    private Map<String, Float> controllerMap;



    /**
     * Loads a keyboard & mouse map from a file
     * @param localPath local path to the map file
     */
    @SuppressWarnings("unchecked")
    public void loadKeyMap(String localPath) {
        keyMap = WKGame.saveManager.load(HashMap.class, localPath);
    }

    /**
     * Loads a controller map from a file
     * @param localPath local path to the map file
     */
    @SuppressWarnings("unchecked")
    public void loadControllerMap(String localPath) {
        controllerMap = WKGame.saveManager.load(HashMap.class, localPath);
    }

    /**
     * Loads the default keyboard & mouse map file
     */
    public void loadDefaultKeyMap() {
        loadKeyMap(Assets.keyMapDefault);
    }

    /**
     * Loads the default controller map file
     */
    public void loadDefaultControllerMap() {
        loadControllerMap(Assets.controllerMapDefault);
    }

    /**
     * @return the current keyboard & mouse map
     */
    public Map<String, Float> getKeyMap() {
        return keyMap;
    }

    /**
     * @return the current controller map
     */
    public Map<String, Float> getControllerMap() {
        return controllerMap;
    }

}
