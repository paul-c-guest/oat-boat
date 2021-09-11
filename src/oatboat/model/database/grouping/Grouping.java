package oatboat.model.database.grouping;

public class Grouping {
	
	public String description, displayText;
	private final int index;
	
	public Grouping(int index, String description, String displayText) {
		this.index = index;
		this.description = description;
		this.displayText = displayText;
	}
	
	public int getIndex() {
		return index;
	}

}
