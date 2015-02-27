package com.djammr.westernknights.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.djammr.westernknights.Assets;
import com.djammr.westernknights.WKGame;
import com.djammr.westernknights.util.assetloaders.Overlap2DUILoader;
import com.djammr.westernknights.util.assetloaders.settings.Overlap2DUISettings;
import com.djammr.westernknights.util.controllers.DebugController;
import com.djammr.westernknights.util.controllers.PlayerHUDController;
import com.djammr.westernknights.util.observers.Observable;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.Date;
import java.util.Map;

/**
 * Debug Overlay to display key information
 */
public class PlayerHUD extends UIView {

    private PlayerHUDController controller;

    public PlayerHUD(PlayerHUDController controller) {
        super(controller);
        this.controller = controller;
    }

    @Override
    public void createUI() {
        Overlap2DUILoader.Parameters params = new Overlap2DUILoader.Parameters();
        params.set(Assets.overlap2DUIProject, Assets.uiHud, Assets.overlap2DUIFonts, stage, actors);
        params.loadedCallback = new AssetLoaderParameters.LoadedCallback() {
            @Override
            public void finishedLoading(AssetManager assetManager, String fileName, Class type) {
                loadUI();
            }
        };
        Assets.manager.load(Assets.uiHud, Overlap2DUISettings.class, params);
    }

    public void loadUI() {
        /*txtCodename = (Label)actors.get("txtCodename");
        txtFPS = (Label)actors.get("txtFPS");
        txtCPU = (Label)actors.get("txtCPU");
        txtHeap = (Label)actors.get("txtHeap");
        txtNHeap = (Label)actors.get("txtNHeap");
        txtEntites = (Label)actors.get("txtEntities");*/

        //txtCodename.setText(txtCodename.getText() + WKGame.VERSION);
        //WKGame.logger.logDebug("Debug overlay enabled, toggle with ' (apostrophe)");
    }

    @Override
    public void render(float delta) {
        super.render(delta);
    }

    @Override
    public void dispose() {
        super.dispose();
    }

    @Override
    public void update(Observable obs, Map<String, Object> data) {

    }
}
