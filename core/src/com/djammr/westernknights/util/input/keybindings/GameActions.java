package com.djammr.westernknights.util.input.keybindings;

/**
 * Game actions available to map. Events dispatched by {@link com.djammr.westernknights.util.input.InputMapper} correspond to the actions found in this class.
 */
public class GameActions {

    public static final int PLAYER_LEFT = 0;
    public static final int PLAYER_RIGHT = 1;
    public static final int PLAYER_JUMP = 2;


    /**
     * Meaningful name for an action code
     */
    public static String toString(int actionCode) {
        switch (actionCode) {
            case PLAYER_LEFT:
                return "Player Left";
            case PLAYER_RIGHT:
                return "Player Right";
            case PLAYER_JUMP:
                return "Player Jump";
            default:
                return null;
        }
    }
}
