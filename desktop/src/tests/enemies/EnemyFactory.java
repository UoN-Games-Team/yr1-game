package tests.enemies;

import java.util.Random;

public class EnemyFactory {

	private static Random rand = new Random();
	
	
	public static String createFaction() {
        int faction = rand.nextInt(4);
        switch (faction) {
            case 0:
                return "Grunt";
            case 1:
                return "Bandit";
            default:
                return "Soldier";
        }
    }
	
	public static String createType() {
        int type = rand.nextInt(4);
        switch (type) {
            case 0:
                return "Light Infantry";
            case 1:
                return "Heavy Infantry";
            default:
                return "Ranged";
        }
    }
	
	public static int createLevel() {
		//get player level
		int playerLevel = 1;
		
		//Create Random Modifier Value
		int modifyvalue = rand.nextInt(20);
		int modify;
		if (modifyvalue <= 3){modify = 0;}
		else if (modifyvalue <= 8){modify = 1;}
		else if (modifyvalue <= 13){modify = 2;}
		else if (modifyvalue <= 16){modify = 3;}
		else if (modifyvalue <= 18){modify = 4;}
		else {modify = 5;}
		
		//Chance of adding/subtracting modify
		boolean operator;
		int operatorvalue = rand.nextInt(3);
		if (operatorvalue == 0) {operator = false;}
		else {operator = true;}

		//If false subtract modify
		if (!operator){ modify = modify - modify*2;}
		
		int level = playerLevel + modify;
		if (level < 0){level = 1;}
		return level;
    }
	
	public static int createDmgStat(int level) {
		int dmg = level * 4 + 7;
		return dmg;
    }
	
	
	
	
	public static Enemy createEnemy() {
		Enemy enemy = new Enemy();
		
		enemy.setFaction(createFaction());
		enemy.setType(createType());
		enemy.setLevel(createLevel());
		enemy.setDmgStat(EnemyFactory.createDmgStat(enemy.getLevel()));
		
		return enemy;
	}
}
