package com.djammr.westernknights.entity.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * For holding texture and animation data
 */
public class VisualComponent extends WKComponent {

    public Sprite sprite = new Sprite();
    public boolean flipX = false;

    @Override
    public void reset() {

    }
}
