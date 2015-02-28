package com.djammr.westernknights.entity.components.ai;

import com.badlogic.ashley.core.Component;
import com.djammr.westernknights.entity.ai.controllers.AIController;

/**
 * Component for AI entities that holds behaviour data
 */
public class BehaviourComponent extends Component {

    public AIController controller;
}
