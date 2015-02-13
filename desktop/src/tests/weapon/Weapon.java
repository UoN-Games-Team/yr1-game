package tests.weapon;



public class Weapon {


    private String name;
    private String type;


    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String toString() {
        return name + ", " + type;
    }
}
