package com.djammr.westernknights.entity.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.djammr.westernknights.entity.components.TransformComponent;
import com.djammr.westernknights.entity.components.VisualComponent;


/**
 * Renders entity sprites
 */
public class RenderingSystem extends IteratingSystem {

    private ComponentMapper<TransformComponent> transm = ComponentMapper.getFor(TransformComponent.class);
    private ComponentMapper<VisualComponent> vism = ComponentMapper.getFor(VisualComponent.class);

    public float[] bgColour = {0, 0, 0, 0};
    private OrthographicCamera camera;
    private SpriteBatch batch;


    /**
     * //@param camWidth Camera frustum width in meters
     * //@param camHeight Camera frustum height in meters
     */
    public RenderingSystem(/*float camWidth, float camHeight*/) {
        super(Family.all(TransformComponent.class, VisualComponent.class).get());
        //camera = new WKCamera(camWidth, camHeight);
        //camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0);
        //camera.update();
        batch = new SpriteBatch();
    }

    @Override
    public void update(float deltaTime) {
        Gdx.gl.glClearColor(bgColour[0], bgColour[1], bgColour[2], bgColour[3]);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        //camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        super.update(deltaTime);
        batch.end();
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        render(entity);
    }

    private void render(Entity entity) {
        VisualComponent visc = vism.get(entity);
        visc.sprite.draw(batch);
    }

    @Override
    public void removedFromEngine(Engine engine) {
        super.removedFromEngine(engine);
        batch.dispose();
    }

    /**
     * @param camera camera to use for the Box2D debug renderer and lights
     */
    public void setCamera(OrthographicCamera camera) {
        this.camera = camera;
    }

    /**
     * Sets the camera to follow this entity
     * @param entity Entity for the camera to follow
     */
    /*public void setCameraFollow(Entity entity) {
        camera.follow(entity);
    }

    public WKCamera getCamera() {
        return camera;
    }*/
}
