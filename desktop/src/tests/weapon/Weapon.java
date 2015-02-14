package tests.weapon;



public class Weapon {

    private String type;
    private String enchantment;
    private String quality;
    private String name;
    private String passive;

    //Setters

    public void setType(String type) {
        this.type = type;
    }
    
    public void setEnchantment(String enchantment) {
        this.enchantment = enchantment;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public void setPassive(String passive) {
        this.passive = passive;
    }
    
    
    //Getters
    public String getType() {
        return type;
    }
    
    public String getEnchantment() {
        return enchantment;
    }
    
    public String getQuality() {
        return quality;
    }
    
    public String getName() {
        return name;
    }
    
    public String getPassive() {
        return passive;
    }

    //Output
    public String toString() {
        return name + ", " + type + ", " + quality + ", " + enchantment + ", " + passive;
    }
}
