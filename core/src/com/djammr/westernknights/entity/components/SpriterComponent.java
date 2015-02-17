package com.djammr.westernknights.entity.components;

import com.badlogic.ashley.core.Component;
import com.brashmonkey.spriter.Player;
import com.djammr.westernknights.util.spriter.LibGdxDrawer;
import com.djammr.westernknights.util.spriter.LibGdxLoader;

/**
 * Holds Spriter data
 */
public class SpriterComponent extends Component {

    public Player player;
    public LibGdxLoader loader;
    public LibGdxDrawer drawer;
}
