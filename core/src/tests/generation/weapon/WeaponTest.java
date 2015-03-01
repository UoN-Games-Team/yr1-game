package tests.generation.weapon;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

/**
 * Weapon generation test
 */
public class WeaponTest implements ApplicationListener {

    private Stage stage;
    private Weapon weapon;
    private WeaponIcon weaponIcon;


    @Override
    public void create() {
        stage = new Stage(new ScreenViewport());
        weapon = WeaponFactory.createWeapon();
        weaponIcon = new WeaponIcon();
        weaponIcon.setFrom(weapon);

        weaponIcon.setPosition(stage.getWidth()/2 - weaponIcon.getWidth()/2, stage.getHeight()/2 - weaponIcon.getHeight()/2);
        stage.addActor(weaponIcon);
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
