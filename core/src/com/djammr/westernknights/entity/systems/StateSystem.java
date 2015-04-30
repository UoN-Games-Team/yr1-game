package com.djammr.westernknights.entity.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.djammr.westernknights.entity.components.StateComponent;

/**
 * Changes Entity state based on current entity activity
 */
public class StateSystem extends IteratingSystem {

    public StateSystem() {
        super(Family.all(StateComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {

    }
}