package tests.weapon;

import java.util.Random;


public class WeaponFactory {

    private static Random rand = new Random();


    public static String createType() {
        int type = rand.nextInt(4);
        switch (type) {
            case 0:
                return "Short Sword";
            case 1:
                return "2 Handed Sword";
            case 2:
                return "Flail";
            case 3:
                return "Axe";
            default:
                return "";
        }
    }

    public static String createName() {
        return "Test";
    }

    public static Weapon createWeapon() {
        Weapon weapon = new Weapon();

        weapon.setName(createName());
        weapon.setType(createType());

        return weapon;
    }
}
