package utils;

import java.io.Serializable;

public class SelectItem implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private int index;
	private String value;
	private String description;
	
	public SelectItem() {
	}
	
	public SelectItem(int index, String value, String description) {
		this.index = index;
		this.description = description;
		this.value = value;
	}
	
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
}
