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
import com.djammr.westernknights.util.observers.Observable;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.Date;
import java.util.Map;

/**
 * Debug Overlay to display key information
 */
public class DebugUI extends UIView {

    private ThreadMXBean TMB;
    private long time = new Date().getTime() * 1000000;
    private long cpuTime = 0;
    private int cpuPercent = -1;

    private DebugController controller;
    public Label txtCodename;
    public Label txtFPS;
    public Label txtCPU;
    public Label txtHeap;
    public Label txtNHeap;
    public Label txtEntites;


    public DebugUI(DebugController controller) {
        super(controller, Assets.uiDebug, Assets.uiDebugID);
        this.controller = controller;
    }

    public void loadUI() {
        TMB = ManagementFactory.getThreadMXBean();

        txtCodename = (Label)actors.get("txtCodename");
        txtFPS = (Label)actors.get("txtFPS");
        txtCPU = (Label)actors.get("txtCPU");
        txtHeap = (Label)actors.get("txtHeap");
        txtNHeap = (Label)actors.get("txtNHeap");
        txtEntites = (Label)actors.get("txtEntities");

        txtCodename.setText(txtCodename.getText() + WKGame.VERSION);
        WKGame.logger.logDebug("Debug overlay enabled, toggle with ' (apostrophe)");
    }

    @Override
    public void render(float delta) {
        if (controller.debugEnabled()) {
            super.render(delta);
            txtFPS.setText("FPS: " + Gdx.graphics.getFramesPerSecond());
            txtCPU.setText("CPU: " + getCPUUsage() + "%");
            txtHeap.setText("Java Heap: " + String.valueOf(Math.round(Gdx.app.getJavaHeap() * 0.000001)) + "MB");
            txtNHeap.setText("Native Heap: " + String.valueOf(Math.round(Gdx.app.getNativeHeap() * 0.000001)) + "MB");
            txtEntites.setText("World Entities:" + controller.getEntities());
        }
    }

    @Override
    public void update(Observable obs, Map<String, Object> data) {

    }

    public int getCPUUsage() {
        if(TMB.isThreadCpuTimeSupported() )
        {
            if(new Date().getTime() * 1000000 - time > 1000000000) //Reset once per second
            {
                time = new Date().getTime() * 1000000;
                cpuTime = TMB.getCurrentThreadCpuTime();
            }

            if(!TMB.isThreadCpuTimeEnabled())
            {
                TMB.setThreadCpuTimeEnabled(true);
            }

            if(new Date().getTime() * 1000000 - time != 0)
                cpuPercent = (int)((TMB.getCurrentThreadCpuTime() - cpuTime) / (new Date().getTime() * 1000000.0 - time) * 100.0);
        }
        else
        {
            cpuPercent = -2;
        }

        return cpuPercent;
    }
}
