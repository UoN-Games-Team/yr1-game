package tests.generation;


import tests.WKTest;
import tests.generation.weapon.WeaponTest;

public class Weapon extends WKTest {

    public static void main (String[] arg) {
        run("Generation Test - Weapon", new WeaponTest());
    }
}
