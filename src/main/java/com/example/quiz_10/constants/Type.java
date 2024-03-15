package com.example.quiz_10.constants;

public enum Type {
	
	SINGLE_CHOICE("single choice"), //
	MULTI_CHOICE("multi choice"), //
	TEXT("text");
	
	private String type;

	private Type(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}
	
	

}
