package com.djammr.westernknights.util;

import com.djammr.westernknights.util.controllers.Controller;
import com.djammr.westernknights.util.observers.Observer;

/**
 * Base class for things that display to the screen, interactively or not, such as a GUI or a Renderer
 */
public abstract class View implements Observer {

    /** Your controller that will translate requests to the model */
    //protected Controller controller;


    public View(Controller controller) {
        //this.controller = controller;
    }

}