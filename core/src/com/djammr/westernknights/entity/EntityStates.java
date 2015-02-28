package com.djammr.westernknights.entity;

/**
 * Holds state enums
 */
public class EntityStates {

    // TODO: Change to bits 0x0001 etc. which will allow for the use of bitwise operators
    public static enum ACTIVE_STATE {
        IDLE, MOVING, JUMPING, ATTACKING_MELEE, ATTACKING_RANGED, DODGING
    }

    public static enum AI_STATE {

    }
}
