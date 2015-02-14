package com.djammr.westernknights.entity.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.SortedIteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.djammr.westernknights.entity.components.AnimationComponent;
import com.djammr.westernknights.entity.components.PlayerComponent;
import com.djammr.westernknights.entity.components.TransformComponent;
import com.djammr.westernknights.entity.components.VisualComponent;
import com.djammr.westernknights.util.comparators.ZIndexComparator;


/**
 * Renders entity sprites
 */
public class RenderingSystem extends SortedIteratingSystem {

    private ComponentMapper<VisualComponent> vism = ComponentMapper.getFor(VisualComponent.class);

    public float[] bgColour = {0, 0, 0, 0};
    private OrthographicCamera camera;
    private SpriteBatch batch;


    /**
     * //@param camWidth Camera frustum width in meters
     * //@param camHeight Camera frustum height in meters
     */
    public RenderingSystem() {
        super(Family.all(TransformComponent.class, VisualComponent.class).get(), new ZIndexComparator());
        batch = new SpriteBatch();
    }

    @Override
    public void update(float deltaTime) {
        Gdx.gl.glClearColor(bgColour[0], bgColour[1], bgColour[2], bgColour[3]);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
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
        if (entity.getComponent(PlayerComponent.class) != null) {
            //batch.draw(entity.getComponent(AnimationComponent.class).currentAnim.getKeyFrames()[0], 0, 2f, 5f, 5f);
        }
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
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

}
