package com.djammr.westernknights.entity.components;

import com.badlogic.ashley.core.Component;

/**
 * Component that can reset itself
 */
public abstract class WKComponent extends Component {

    public abstract void reset();
}
