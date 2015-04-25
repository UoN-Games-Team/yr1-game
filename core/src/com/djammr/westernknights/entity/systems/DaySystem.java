package com.djammr.westernknights.entity.systems;

import box2dLight.RayHandler;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.djammr.westernknights.WKGame;
import com.djammr.westernknights.WKWorld;
import com.djammr.westernknights.entity.components.MessagingComponent;
import com.djammr.westernknights.entity.components.TransformComponent;
import com.djammr.westernknights.entity.components.VisualComponent;
import com.djammr.westernknights.util.observers.Observer;

/**
 * Manages day and night cycle
 */
public class DaySystem extends EntitySystem {

    private float skyDayAlpha = 1;
    private double alpha;
    private double gameTime = 0;
    private int speed = 256;
    private boolean toDay = false; // Transition: to day=1, to night=0
    private Entity skyboxDay;
    private Entity skyboxNight;
    private RayHandler rayHandler;

    private ComponentMapper<TransformComponent> transm = ComponentMapper.getFor(TransformComponent.class);
    private ComponentMapper<VisualComponent> vism = ComponentMapper.getFor(VisualComponent.class);
    private TransformComponent day_transc;
    private TransformComponent night_transc;
    private VisualComponent day_visc;
    private VisualComponent night_visc;


    public DaySystem() {
    }

    @Override
    public void update(float deltaTime) {
        double gameTimeStep = deltaTime / speed;
        if (!toDay) gameTime += gameTimeStep;
        else gameTime -= gameTimeStep;

        // Rotation
        //sun.setDirection(-(360 * gameTime));
        //day_transc.rotation = (toDay)? 360 * (float)gameTime : -360 * (float)gameTime;
        //night_transc.rotation = (toDay)? 360 * (float)gameTime : -360 * (float)gameTime;
        double rotationStep = 180 * gameTimeStep;
        day_transc.rotation -= rotationStep;
        night_transc.rotation -= rotationStep;
        if (day_transc.rotation >= 360) day_transc.rotation = 0;
        if (night_transc.rotation >= 360) night_transc.rotation = 0;
        if (gameTime <= 0 && toDay) toDay = false;
        else if (gameTime >= 1 && !toDay) toDay = true;


        // Skybox Fading
        /*if (skyDayAlpha < 0) skyDayAlpha = 0;
        if (skyDayAlpha > 1) skyDayAlpha = 1;
        if (gameTime >= 0.4f && gameTime < 0.85f && skyDayAlpha > 0) {
            day_visc.sprite.setAlpha((skyDayAlpha > 0 + Gdx.graphics.getDeltaTime())? skyDayAlpha -= Gdx.graphics.getDeltaTime() : 0);
            //WKGame.logger.logDebug("to night");
        } else if (gameTime >= 0.85f && skyDayAlpha < 1) {
            //WKGame.logger.logDebug("to day");
            day_visc.sprite.setAlpha((skyDayAlpha < 1f - Gdx.graphics.getDeltaTime())? skyDayAlpha += Gdx.graphics.getDeltaTime() : 1);
        }*/
        skyDayAlpha = 1 - (float)gameTime;
        if (skyDayAlpha < 0) skyDayAlpha = 0;
        if (skyDayAlpha > 1) skyDayAlpha = 1;
        day_visc.sprite.setAlpha(skyDayAlpha);


        // Sun alpha
        alpha = 1 - gameTime;
        if (alpha < WKWorld.AMBIENT_ALPHA_NIGHT) alpha = WKWorld.AMBIENT_ALPHA_NIGHT;
        else if (alpha > WKWorld.AMBIENT_ALPHA_DAY) alpha = WKWorld.AMBIENT_ALPHA_DAY;
        //if (alpha >= WKWorld.AMBIENT_ALPHA_DAY && toDay) toDay = false;
        //else if (alpha <= WKWorld.AMBIENT_ALPHA_NIGHT && !toDay) toDay = true;
        if (rayHandler != null) rayHandler.setAmbientLight(WKWorld.AMBIENT_COLOUR.r, WKWorld.AMBIENT_COLOUR.g, WKWorld.AMBIENT_COLOUR.b, (float)alpha);
    }

    public void setRayHandler(RayHandler rayHandler) {
        this.rayHandler = rayHandler;
    }

    public void setSkyBoxes(Entity skyboxDay, Entity skyboxNight) {
        this.skyboxDay = skyboxDay;
        this.skyboxNight = skyboxNight;

        day_transc = transm.get(skyboxDay);
        night_transc = transm.get(skyboxNight);
        day_visc = vism.get(skyboxDay);
        night_visc = vism.get(skyboxNight);

        day_transc.x += day_visc.sprite.getWidth()/2 * day_visc.sprite.getScaleX();
        day_transc.y += day_visc.sprite.getHeight()/2 * day_visc.sprite.getScaleY();
        day_visc.sprite.setOriginCenter();
        day_visc.sprite.setPosition(day_transc.x, day_transc.y);

        night_transc.x += night_visc.sprite.getWidth()/2 * night_visc.sprite.getScaleX();
        night_transc.y += night_visc.sprite.getHeight()/2 * night_visc.sprite.getScaleX();
        night_visc.sprite.setOriginCenter();
        night_visc.sprite.setPosition(night_transc.x, night_transc.y);
    }
}
