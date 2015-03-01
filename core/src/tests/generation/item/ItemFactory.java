package tests.generation.item;

import java.util.Random;

public class ItemFactory {

	private static Random rand = new Random();
	
	public static String createType() {
        int type = rand.nextInt(4);
        switch (type) {
            case 0:
                return "Health";
            case 1:
                return "Strength";
            case 2:
                return "Attack Speed";
            default:
                return "Movement";
        }
    }
	
	public static String createSize() {
        int size = rand.nextInt(3);
        switch (size) {
            case 0:
                return "Minor";
            case 1:
                return "Medium";
            default:
                return "Major";
        }
    }	
	
	
	
	public static Item createItem(){
		Item item = new Item();
		
		item.setType(createType());
		item.setSize(createSize());
		
		return item;
	}
}
