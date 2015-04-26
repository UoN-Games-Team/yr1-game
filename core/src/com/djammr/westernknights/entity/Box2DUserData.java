package com.djammr.westernknights.entity;

import com.djammr.westernknights.entity.ai.SteeringEntity;
import com.djammr.westernknights.entity.components.StateComponent;

/**
 * User Data for Box2D bodies. Simulates a component for box2D related collision handling and states
 */
public class Box2DUserData {

    public String id = "";
    public StateComponent stateComponent;
    public int footContacts = 0;
    public SteeringEntity steeringEntity = null;
    public String collidingSensor = "";
}
