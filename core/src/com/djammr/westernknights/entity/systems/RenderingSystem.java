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
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.djammr.westernknights.WKGame;
import com.djammr.westernknights.entity.components.*;
import com.djammr.westernknights.entity.components.ai.BehaviourComponent;
import com.djammr.westernknights.entity.components.ai.NodeComponent;
import com.djammr.westernknights.util.comparators.ZIndexComparator;


/**
 * Renders entity sprites
 */
public class RenderingSystem extends SortedIteratingSystem {

    private ComponentMapper<VisualComponent> vism = ComponentMapper.getFor(VisualComponent.class);
    private ComponentMapper<SpriterComponent> sprtm = ComponentMapper.getFor(SpriterComponent.class);
    private ComponentMapper<BehaviourComponent> bvm = ComponentMapper.getFor(BehaviourComponent.class);
    private ComponentMapper<NodeComponent> nodem = ComponentMapper.getFor(NodeComponent.class);
    VisualComponent visc;
    SpriterComponent sprtc;
    BehaviourComponent bvc;
    NodeComponent nodec;

    public float[] bgColour = {0, 0, 0, 0};
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;


    /**
     * //@param camWidth Camera frustum width in meters
     * //@param camHeight Camera frustum height in meters
     */
    public RenderingSystem() {
        super(Family.all(VisualComponent.class).get(), new ZIndexComparator());
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
    }

    @Override
    public void update(float deltaTime) {
        Gdx.gl.glClearColor(bgColour[0], bgColour[1], bgColour[2], bgColour[3]);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        super.update(deltaTime);
        batch.end();

        if (WKGame.debugEnabled) {
            shapeRenderer.setProjectionMatrix(camera.combined);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
                for (Entity entity : getEntities()) debugRender(entity);
            shapeRenderer.end();
        }
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        render(entity);
    }

    private void render(Entity entity) {
        visc = vism.get(entity);
        sprtc = sprtm.get(entity);
        nodec = nodem.get(entity);
        if (sprtc != null) {
            sprtc.player.update();
            sprtc.drawer.draw(sprtc.player, batch);
        } else {
            if (nodec == null || WKGame.debugEnabled) visc.sprite.draw(batch);
        }
    }

    private void debugRender(Entity entity) {
        bvc = bvm.get(entity);
        if (bvc != null) bvc.controller.debugRender(shapeRenderer);
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
    }

    @Override
    public void removedFromEngine(Engine engine) {
        super.removedFromEngine(engine);
        batch.dispose();
        shapeRenderer.dispose();
    }

    /**
     * @param camera camera to use for the Box2D debug renderer and lights
     */
    public void setCamera(OrthographicCamera camera) {
        this.camera = camera;
    }

}
