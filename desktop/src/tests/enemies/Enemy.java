package tests.enemies;

public class Enemy {

	private String faction;
	private String type;
	private int level;
	private int dmg;
	
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
	
		
	//Output
	public String toString() {
        return "Faction: " + faction + ", Type: " + type + ", Level: " + level + ", Damage: " + dmg;
    }
}
