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

    private String path = "test/icons/weapons/";
    private Texture imgQuality;
    private Texture imgEnchant;
    private Texture imgType;

    public WeaponIcon() {
        setWidth(iconWidth);
        setHeight(iconHeight);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (imgQuality != null) batch.draw(imgQuality, getX(), getY(), iconWidth, iconHeight);
        if (imgEnchant != null) batch.draw(imgEnchant, getX(), getY(), iconWidth, iconHeight);
        if (imgType != null) batch.draw(imgType, getX(), getY(), iconWidth, iconHeight);
    }

    public void setFrom(Weapon weapon) {
        imgQuality = new Texture(Gdx.files.internal(path+parseQuality(weapon.getQuality())));
        if (!weapon.getEnchantment().equals("None")) imgEnchant = new Texture(Gdx.files.internal(path+parseEnchant(weapon.getEnchantment())));
        else imgEnchant = null;
        imgType = new Texture(Gdx.files.internal(path+parseType(weapon.getType())));
    }

    public String parseQuality(String type) {
        if (type.equals("Bronze")) return "Bronze.png";
        if (type.equals("Silver")) return "Silver.png";
        if (type.equals("Gold")) return "Gold.png";
        return null;
    }

    public String parseEnchant(String type) {
        if (type.equals("Fire")) return "Fire.png";
        if (type.equals("Ice")) return "Frost.png";
        if (type.equals("Storm")) return "Storm.png";
        if (type.equals("Poison")) return "Poison.png";
        return null;
    }

    public String parseType(String type) {
        if (type.equals("Short Sword")) return "Weapon 1.png";
        if (type.equals("2 Handed Sword")) return "Weapon 2.png";
        if (type.equals("Flail")) return "Weapon 3.png";
        if (type.equals("Axe")) return "Weapon 4.png";
        return null;
    }
}
