package com.djammr.westernknights.entity.components;

import com.badlogic.ashley.core.Component;
import com.djammr.westernknights.entity.EntityStates;

/**
 * Holds state data
 */
public class StateComponent extends Component {

    public int state = EntityStates.IDLE;
    public boolean onGround = false;
}
