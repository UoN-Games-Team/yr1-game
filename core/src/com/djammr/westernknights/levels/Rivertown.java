package com.djammr.westernknights.levels;

import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.djammr.westernknights.Assets;
import com.djammr.westernknights.WKWorld;
import com.djammr.westernknights.entity.components.Box2DComponent;
import com.djammr.westernknights.entity.systems.CameraSystem;
import com.djammr.westernknights.entity.systems.InputSystem;
import com.djammr.westernknights.util.assetloaders.Overlap2DMapLoader;
import com.djammr.westernknights.util.assetloaders.settings.Overlap2DMapSettings;


/**
 * Rivertown Level
 */
public class Rivertown extends WKWorld {

    public Rivertown() {
        super(); // Make sure you call super() on all levels!
    }


    @Override
    public void load() {
        // Level
        Overlap2DMapLoader.Parameters params = new Overlap2DMapLoader.Parameters();
        params.set(Assets.overlap2DLevelProject, Assets.lvlRivertown, Assets.overlap2DLevelAtlas, getEntities());
        params.loadedCallback = new AssetLoaderParameters.LoadedCallback() {
            @Override
            public void finishedLoading(AssetManager assetManager, String fileName, Class type) {
                getEntities().getEngine().getSystem(InputSystem.class).setControllable(getEntities().getEntity(WKWorld.PLAYER_IDENTIFIER));
                /*getEntities().getEngine().getSystem(CameraSystem.class).follow(getEntities().getEntity(WKWorld.PLAYER_IDENTIFIER),
                        getEntities().getEntity(WKWorld.GROUND_IDENTIFIER).getComponent(Box2DComponent.class).body.getPosition().x,
                        getEntities().getEntity("border_right").getComponent(Box2DComponent.class).body.getPosition().x,
                        0f, null);*/
            }
        };

        Assets.manager.load(Assets.lvlRivertownID, Overlap2DMapSettings.class, params);
    }
}
