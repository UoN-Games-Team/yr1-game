package tests.generation.weapon;

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

    public static String createEnchantment() {
    	int enchantment = rand.nextInt(20);
        switch (enchantment) {
            case 0:
                return "Fire";
            case 1:
                return "Ice";
            case 2:
                return "Storm";
            case 3:
                return "Poision";
            default:
                return "None";
        }
    }
    
    public static String createQuality() {
    	int quality = rand.nextInt(10);
    	if (quality < 5){return "Bronze";}
    	else if (quality < 9){return "Silver";}
    	else {return "Gold";}
    }
    
    public static String createName() {
        return "Test";
    }
    
    public static String createPassive() {
    	int passive = rand.nextInt(20);
        switch (passive) {
            case 0:
                return "Ability Recharge";
            case 1:
                return "Attack Damage";
            case 2:
                return "Attack Speed";
            case 3:
                return "Health";
            case 4:
                return "Life Steal";
            case 5:
                return "Movement Speed";
            default:
                return "None";
        }
    }

    public static Weapon createWeapon() {
        Weapon weapon = new Weapon();

        weapon.setName(createName());
        weapon.setType(createType());
        weapon.setEnchantment(createEnchantment());
        weapon.setQuality(createQuality());
        weapon.setPassive(createPassive());

        return weapon;
    }
}
