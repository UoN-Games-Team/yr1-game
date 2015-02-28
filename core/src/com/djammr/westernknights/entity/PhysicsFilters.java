package com.djammr.westernknights.entity;


public class PhysicsFilters {

    // Filter Categories
    public static final short CATEGORY_DEFAULT = 0x0001;
    public static final short CATEGORY_PLAYER = 0x0002;  // Player category
    public static final short CATEGORY_ACTOR = 0x0004;  // Actor category
    public static final short CATEGORY_ACTOR_GHOST = 0x0008;  // Anything an actor can walk

    // Groups
    public static final short GROUP_ACTORS = -2;

    // Mask Bits
    public static final short MASK_ACTOR = CATEGORY_DEFAULT;  // Default mask for actors
    public static final short MASK_PLAYER = CATEGORY_DEFAULT;  // Mask for the player


    public static short fromString(String categoryName) {
        switch (categoryName) {
            case "player":
                return CATEGORY_PLAYER;
            case "actor":
                return CATEGORY_ACTOR;
            case "actor_ghost":
                return CATEGORY_ACTOR_GHOST;
            default:
                return CATEGORY_DEFAULT;
        }
    }
}
