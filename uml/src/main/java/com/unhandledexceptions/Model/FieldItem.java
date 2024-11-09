package com.unhandledexceptions.Model;

public class FieldItem implements UMLObject {
	private String fieldName;
	private String type;

	public FieldItem() {
	} // blank constructor for IO serialization

	// constructor for fieldItem
	// fieldItem type is a string for the CLI because at this point that's all it
	// needs to be
	public FieldItem(String fieldName, String type) {
		this.fieldName = fieldName;
		this.type = type;
	}

	// getter and setter methods
	public String getName() {
		return this.fieldName;
	}

	public String getType() {
		return this.type;
	}

	public void setName(String fieldName) {
		this.fieldName = fieldName;
	}

	public void setType(String type) {
		this.type = type;
	}

	// toString method returns "type name"
	@Override
	public String toString() {
		return this.type + " " + this.fieldName;
	}
}
