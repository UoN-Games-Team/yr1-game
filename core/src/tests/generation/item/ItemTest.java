package tests.generation.item;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import tests.generation.weapon.Weapon;
import tests.generation.weapon.WeaponFactory;
import tests.generation.weapon.WeaponIcon;

/**
 * Icon generation test
 */
public class ItemTest implements ApplicationListener {

    private Stage stage;
    private Item item;
    private ItemIcon itemIcon;


    @Override
    public void create() {
        stage = new Stage(new ScreenViewport());
        item = ItemFactory.createItem();
        itemIcon = new ItemIcon();
        itemIcon.setFrom(item);

        itemIcon.setPosition(stage.getWidth()/2 - itemIcon.getWidth()/2, stage.getHeight()/2 - itemIcon.getHeight()/2);
        stage.addActor(itemIcon);
    }

    @Override
    public void render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();

            if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
                item = ItemFactory.createItem();
            itemIcon.setFrom(item);
            System.out.println(item);
        }
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
