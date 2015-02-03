package com.djammr.westernknights.entity.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.djammr.westernknights.Assets;
import com.djammr.westernknights.entity.components.Box2DComponent;


/**
 * Renders a World
 */
public class RenderingSystem extends IteratingSystem {

    private ComponentMapper<Box2DComponent> b2DComp = ComponentMapper.getFor(Box2DComponent.class);
    private OrthographicCamera camera;
    private SpriteBatch batch;


    /**
     * @param camWidth Camera frutsum width in meters
     * @param camHeight Camera frustum height in meters
     */
    public RenderingSystem(int camWidth, int camHeight) {
        super(Family.all(Box2DComponent.class).get());
        camera = new OrthographicCamera(camWidth, camHeight);
        camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0);
        camera.update();
        batch = new SpriteBatch();
    }

    @Override
    public void update(float deltaTime) {
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        camera.update();
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
        batch.draw(Assets.manager.get(Assets.testTexture, Texture.class), b2DComp.get(entity).body.getPosition().x, b2DComp.get(entity).body.getPosition().y, 1f, 1.8f);
    }

    @Override
    public void removedFromEngine(Engine engine) {
        super.removedFromEngine(engine);
        batch.dispose();
    }

    public OrthographicCamera getCamera() {
        return camera;
    }
}
