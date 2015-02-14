package tests.enemies;

import java.util.Random;

public class EnemyFactory {

	private static Random rand = new Random();
	
	
	public static String createType() {
        int type = rand.nextInt(4);
        switch (type) {
            case 0:
                return "Grunt";
            case 1:
                return "Bandit";
            default:
                return "Soldier";
        }
    }
	
	
	
	public static Enemy createEnemy() {
		Enemy enemy = new Enemy();
		
		enemy.setType(createType());
		
		return enemy;
	}
}
