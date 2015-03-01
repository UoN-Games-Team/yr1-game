package tests.generation.item;


public class Item {

	private String type;
	private String size;
	
	
	//Setters
	public void setType(String type) {
        this.type = type;
    }
	
	public void setSize(String size) {
        this.size = size;
    }
	
	
	//Getters
	public String getType() {
        return type;
    }
	
	public String getSize() {
        return size;
    }
	
	
	//Output
	public String toString(){
		return size + " " + type;
	}
}
