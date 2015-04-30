package com.djammr.westernknights.entity.components;

import com.badlogic.ashley.core.Component;
import com.djammr.westernknights.WKGame;

/**
 * Position and rotation component for all Entity types. Box2D entities have their positions mapped to this as well!
 */
public class TransformComponent extends WKComponent {

    // Original position values from level file
    public float origX = 0;
    public float origY = 0;
    public float origRotation = 0;

    public float x = 0;
    public float y = 0;
    public int z = 0;
    /** Rotation in degrees */
    public float rotation = 0;


    /** Resets the position and rotation to original loaded values */
    @Override
    public void reset() {
        x = origX;
        y = origY;
        rotation = origRotation;
    }
}
