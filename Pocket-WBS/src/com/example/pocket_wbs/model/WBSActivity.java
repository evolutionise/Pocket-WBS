package com.example.pocket_wbs.model;

public class WBSActivity {
	
	private String description = "";
	
	protected WBSActivity(String description, WBSElement element){
		this.description = description;
	}

	public String getDescription(){
		return description;
	}
	
	public void setDescription(String newDescription){
		description = newDescription;
	}
	

	
}
