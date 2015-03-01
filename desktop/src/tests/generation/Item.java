package tests.generation;

import tests.WKTest;
import tests.generation.item.ItemTest;

public class Item extends WKTest{

    public static void main (String[] arg) {
        run("Generation Test - Items", new ItemTest());
    }

}
