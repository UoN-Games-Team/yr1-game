package com.djammr.westernknights.entity.components;

import com.badlogic.ashley.core.Component;

/**
 * Holds flags for movement
 */
public class MovementComponent extends Component {

    public float speed = 6;
    public float jumpForce = 8;

    public boolean stop = false;
    public boolean left = false;
    public boolean right = false;
    public boolean jump = false;
}
