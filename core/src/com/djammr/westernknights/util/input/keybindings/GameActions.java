package com.djammr.westernknights.util.input.keybindings;

/**
 * Game actions available to map. Events dispatched by {@link com.djammr.westernknights.util.input.InputMapper} correspond to the actions found in this class.
 */
public class GameActions {

    public static final int PLAYER_LEFT = 0;
    public static final int PLAYER_RIGHT = 1;
    public static final int PLAYER_JUMP = 2;
    public static final int PLAYER_DODGE = 3;
    public static final int PLAYER_ATTACK_LIGHT = 4;
    public static final int PLAYER_ATTACK_HEAVY = 5;
    public static final int PLAYER_ATTACK_RANGED = 6;
    public static final int PLAYER_ABILITY_1 = 7;
    public static final int PLAYER_ABILITY_2 = 8;
    public static final int PLAYER_ABILITY_SPECIAL = 9;
    public static final int PLAYER_BLOCK = 10;
    public static final int PLAYER_INTERACT = 11;
    public static final int PLAYER_AIM = 12;
    public static final int GAME_MENU = 13;
    public static final int ESC_MENU = 14;
    public static final int FAVS_LEFT = 15;
    public static final int FAVS_UP = 16;
    public static final int FAVS_RIGHT = 17;
    public static final int FAVS_DOWN = 18;
    public static final int PAN_LEFT = 19;
    public static final int PAN_RIGHT = 20;



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
            case PLAYER_DODGE:
                return "Player Dodge";
            case PLAYER_ATTACK_LIGHT:
                return "Player Attack Light";
            case PLAYER_ATTACK_HEAVY:
                return "Player Attack Heavy";
            case PLAYER_ATTACK_RANGED:
                return "Player Attack Ranged";
            case PLAYER_ABILITY_1:
                return "Player Ability 1";
            case PLAYER_ABILITY_2:
                return "Player Ability 2";
            case PLAYER_ABILITY_SPECIAL:
                return "Player Special Ability";
            case PLAYER_BLOCK:
                return "Player Block";
            case PLAYER_INTERACT:
                return "Player Interact";
            case PLAYER_AIM:
                return "Player Aim";
            case GAME_MENU:
                return "Game Menu";
            case ESC_MENU:
                return "ESC/Pause Menu";
            case FAVS_LEFT:
                return "Favourites Left";
            case FAVS_UP:
                return "Favourites Up";
            case FAVS_RIGHT:
                return "Favourites Right";
            case FAVS_DOWN:
                return "Favourites Down";
            case PAN_LEFT:
                return "Pan Camera Left";
            case PAN_RIGHT:
                return "Pan Camera Right";
            default:
                return null;
        }
    }
}
