package tests.weapon;



public class WeaponTest {

    public static void main(String[] args) {
        /*testWeapon.setName(WeaponFactory.createName());
        testWeapon.setType(WeaponFactory.createType());*/

        Weapon testWeapon = WeaponFactory.createWeapon();

        System.out.println(testWeapon);

    }
}
