package tests.generation;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import tests.generation.item.Item;
import tests.generation.item.ItemFactory;
import tests.generation.item.ItemIcon;
import tests.generation.weapon.Weapon;
import tests.generation.weapon.WeaponFactory;
import tests.generation.weapon.WeaponIcon;

/**
 * Weapon generation test
 */
public class GenerationTest implements ApplicationListener {

    private Stage stage;
    private Weapon weapon;
    private WeaponIcon weaponIcon;
    private Item item;
    private ItemIcon itemIcon;


    @Override
    public void create() {
        stage = new Stage(new ScreenViewport());
        weapon = WeaponFactory.createWeapon();
        weaponIcon = new WeaponIcon();
        weaponIcon.setFrom(weapon);

        item = ItemFactory.createItem();
        itemIcon = new ItemIcon();
        itemIcon.setFrom(item);

        weaponIcon.setPosition(stage.getWidth()/2 - weaponIcon.getWidth() - 40, stage.getHeight()/2 - weaponIcon.getHeight()/2);
        itemIcon.setPosition(stage.getWidth()/2 + 40, stage.getHeight()/2 - itemIcon.getHeight()/2);
        stage.addActor(weaponIcon);
        stage.addActor(itemIcon);
    }

    @Override
    public void render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();

        if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
            weapon = WeaponFactory.createWeapon();
            weaponIcon.setFrom(weapon);
            System.out.println(weapon);

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
