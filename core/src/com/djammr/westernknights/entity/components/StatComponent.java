package com.djammr.westernknights.entity.components;

import com.badlogic.ashley.core.Component;

/**
 * Holds stat data
 */
public class StatComponent extends Component {

    public float maxHealth = 1000;
    public float health = maxHealth;
    public float healthPercent = 1;


    public void changeHealth(float amount) {
        health += amount;
        healthPercent = health / maxHealth;
    }

    /**
     * Deducts health, capping at 0
     * @param amount amount of health to deduct
     */
    public void damage(float amount) {
        changeHealth(-((health > amount)? amount : health));
    }

    /**
     * Adds health, capping at max health
     * @param amount amount of health to add
     */
    public void heal(float amount) {
        changeHealth((health < maxHealth - amount)? amount : maxHealth - health);
    }
}
