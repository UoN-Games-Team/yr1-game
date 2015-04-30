package com.djammr.westernknights.entity.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.Animation;

import java.util.HashMap;
import java.util.Map;

/**
 * Component for Parallax Settings
 */
public class ParallaxComponent extends WKComponent {

    public float intensity = 0;

    public ParallaxComponent(float intensity) {
        this.intensity = intensity;
    }
    public ParallaxComponent() {
        this(0);
    }

    @Override
    public void reset() {

    }
}
