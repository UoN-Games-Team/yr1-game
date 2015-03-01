package tests.generation.enemies;

public class Enemy {

	private String faction;
	private String type;
	private int level;
	private int dmg;
	private int health;
	
	//Setters
	 public void setFaction(String faction) {
	        this.faction = faction;
	    }
	
	 public void setType(String type) {
	        this.type = type;
	    }
	 
	 public void setLevel(int level) {
	        this.level = level;
	    }
	 
	 public void setDmgStat(int dmg) {
	        this.dmg = dmg;
	    }
	
	 public void setHealthStat(int health) {
	        this.health = health;
	    }
	
	
	//Getters
	public String getFaction() {
        return faction;
    }
	
	public String getType() {
        return type;
    }
	
	public int getLevel() {
        return level;
    }
	
	public int getDmgStat() {
        return dmg;
    }
	
	public int getHealth() {
        return health;
    }
		
	//Output
	public String toString() {
        return "Faction: " + faction + ", Type: " + type + ", Level: " + level + ", Health: " + health + ", Damage: " + dmg;
    }
}
