package tests.generation.item;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import tests.generation.weapon.Weapon;

/**
 * Multi-layered Image
 */
public class ItemIcon extends Actor {


    private float iconWidth = 256;
    private float iconHeight = iconWidth;

    private String path = "test/icons/items/";
    private Texture imgQuality;

    public ItemIcon() {
        setWidth(iconWidth);
        setHeight(iconHeight);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (imgQuality != null) batch.draw(imgQuality, getX(), getY(), iconWidth, iconHeight);
    }

    public void setFrom(Item item) {
        imgQuality = new Texture(Gdx.files.internal(path+parseQuality(item.getSize())));
    }

    public String parseQuality(String type) {
        if (type.equals("Minor")) return "minor health icon.png";
        if (type.equals("Medium")) return "medium health icon.png";
        if (type.equals("Major")) return "major health icon.png";
        return null;
    }
}
