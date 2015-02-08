package com.djammr.westernknights.util.input;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.controllers.PovDirection;
import com.djammr.westernknights.WKGame;

/**
 * Input Processor for Kbd & Mouse and Controllers that translates input to mapped events
 */
public class InputMapper extends WKInput {

    public InputMapper() {
        // Register with controllers
        Controllers.addListener(this);
    }

    @Override
    public boolean keyDown(int keycode) {
        WKGame.logger.logDebug(Input.Keys.toString(keycode) + " pressed");
        if (WKGame.keyMaps.getKeyMap().get(""+keycode) != null) {
            notifyObservers(WKGame.keyMaps.getKeyMap().get(""+keycode).intValue());
        }
        return true;
    }

    /**
     * XBox 360 & One: Joysticks, Triggers
     */
    @Override
    public boolean axisMoved(Controller controller, int axisCode, float value) {
        value = controller.getAxis(axisCode);
        //WKGame.logger.logDebug(controller.getName() + " axis " + axisCode + " at " + value);

        if ((value > WKGame.keyMaps.getControllerMap().get("deadzone") || value < -WKGame.keyMaps.getControllerMap().get("deadzone")) &&
                WKGame.keyMaps.getControllerMap().get("axis-"+axisCode+((value > 0)? "+" : "-")) != null) {
            notifyObservers(WKGame.keyMaps.getControllerMap().get("axis-"+axisCode+((value > 0)? "+" : "-")).intValue());
        }
        return true;
    }

    /**
     * XBox 360 & One: D-Pad
     */
    @Override
    public boolean povMoved(Controller controller, int povCode, PovDirection value) {
        //WKGame.logger.logDebug(controller.getName() + ", moved pov: " + povCode + " to " + value);

        if (WKGame.keyMaps.getControllerMap().get("pov-"+value.ordinal()) != null) {
            notifyObservers(WKGame.keyMaps.getControllerMap().get("pov-" + value.ordinal()).intValue());
        }
        return true;
    }

    @Override
    public boolean buttonDown(Controller controller, int buttonCode) {
        //WKGame.logger.logDebug(controller.getName() + ", pressed: " + buttonCode);

        if (WKGame.keyMaps.getControllerMap().get("btn-"+buttonCode) != null) {
            notifyObservers(WKGame.keyMaps.getControllerMap().get("btn-"+buttonCode).intValue());
        }
        return true;
    }

    @Override
    public void connected(Controller controller) {
        controller.addListener(this);
    }
}
