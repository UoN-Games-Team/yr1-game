package com.djammr.westernknights.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.djammr.westernknights.util.Controllers.UIController;
import com.djammr.westernknights.util.View;

import java.util.HashMap;
import java.util.Map;

/**
 * Abstract View class for UIs
 */
public abstract class UIView extends View {

    protected Map<String, Actor> actors;
    protected Stage stage;

    public UIView(UIController controller) {
        super(controller);
        stage = new Stage();
        actors = new HashMap<String, Actor>();
        controller.addInputProcessor(stage);
        createUI();
    }

    /**
     * Create your UI here
     */
    public abstract void createUI();

    /**
     * Should be called each frame from the {@link UIController}. Make sure you call super.render()!
     */
    public void render(float delta) {
        stage.act(delta);
        stage.draw();
    }

    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    public void dispose() {
        stage.dispose();
    }
}
