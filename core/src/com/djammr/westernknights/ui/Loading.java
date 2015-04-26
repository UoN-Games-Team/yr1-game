package com.djammr.westernknights.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.djammr.westernknights.Assets;
import com.djammr.westernknights.util.assetloaders.Overlap2DUILoader;
import com.djammr.westernknights.util.assetloaders.settings.Overlap2DUISettings;
import com.djammr.westernknights.util.controllers.LoadingController;
import com.djammr.westernknights.util.controllers.MainMenuController;
import com.djammr.westernknights.util.observers.Observable;

import java.util.ArrayList;
import java.util.Map;

/**
 * Loading View
 */
public class Loading extends UIView {

    private LoadingController controller;


    public Loading(LoadingController controller) {
        super(controller, Assets.uiLoading, Assets.uiLoadingID);
        this.controller = controller;
    }

    public void loadUI() {

    }

    @Override
    public void update(Observable obs, Map<String, Object> data) {
    }
}
