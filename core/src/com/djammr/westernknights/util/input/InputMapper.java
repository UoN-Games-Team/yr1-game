package com.djammr.westernknights.util.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.controllers.PovDirection;
import com.djammr.westernknights.WKGame;

/**
 * Input Processor for Kbd & Mouse and Controllers with keymap support
 */
public class InputMapper extends WKInput {

    public InputMapper() {
        // Register with controllers
        for (Controller controller : Controllers.getControllers()) {
            controller.addListener(this);
        }
    }

    @Override
    public boolean keyDown(int keycode) {
        WKGame.logger.logDebug(Input.Keys.toString(keycode) + " pressed");
        return true;
    }

    /**
     * XBox One: Joysticks, Triggers
     */
    @Override
    public boolean axisMoved(Controller controller, int axisCode, float value) {
        WKGame.logger.logDebug(controller.getName() + ", moved axis: " + axisCode + " to " + value);
        return true;
    }

    /**
     * XBox One: D-Pad
     */
    @Override
    public boolean povMoved(Controller controller, int povCode, PovDirection value) {
        WKGame.logger.logDebug(controller.getName() + ", moved pov: " + povCode + " to " + value);
        return true;
    }

    @Override
    public boolean buttonDown(Controller controller, int buttonCode) {
        WKGame.logger.logDebug(controller.getName() + ", pressed: " + buttonCode);
        return true;
    }
}
