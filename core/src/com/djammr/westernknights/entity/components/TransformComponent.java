package com.djammr.westernknights.entity.components;

import com.badlogic.ashley.core.Component;

/**
 * Position and rotation component for all Entity types. Box2D entities have their positions mapped to this as well!
 */
public class TransformComponent extends Component {

    public float x = 0;
    public float y = 0;
    public int z = 0;
    /** Rotation in degrees */
    public float rotation = 0;
}
