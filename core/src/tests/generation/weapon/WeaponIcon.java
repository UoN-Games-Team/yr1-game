package tests.generation.weapon;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Multi-layered Image
 */
public class WeaponIcon extends Actor {

    private float iconWidth = 128;
    private float iconHeight = iconWidth;

    private Texture imgBase;
    private Texture imgEnchant;
    private Texture imgType;

    public WeaponIcon() {
        setWidth(iconWidth);
        setHeight(iconHeight);
    }


    public void setFrom(Weapon weapon) {
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (imgBase != null) batch.draw(imgBase, getX(), getY(), iconWidth, iconHeight);
        if (imgEnchant != null) batch.draw(imgBase, getX(), getY(), iconWidth, iconHeight);
        if (imgType != null) batch.draw(imgBase, getX(), getY(), iconWidth, iconHeight);
    }
}
