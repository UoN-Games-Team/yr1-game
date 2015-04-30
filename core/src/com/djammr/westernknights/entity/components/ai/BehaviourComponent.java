package com.djammr.westernknights.entity.components.ai;

import com.badlogic.ashley.core.Component;
import com.djammr.westernknights.entity.ai.controllers.AIController;
import com.djammr.westernknights.entity.components.WKComponent;

/**
 * Component for AI entities that holds behaviour data
 */
public class BehaviourComponent extends WKComponent {

    public AIController controller;

    @Override
    public void reset() {
        controller.reset();
    }
}
