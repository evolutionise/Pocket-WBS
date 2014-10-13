package com.example.pocket_wbs.model;

import java.io.Serializable;

public class WBSActivity implements Serializable {
	
	private String description = "";
	private WBSElement containingElement;
	
	protected WBSActivity(String description, WBSElement element){
		this.description = description;
		this.containingElement = element;
	}

	public String getDescription(){
		return description;
	}
	
	public void setDescription(String newDescription){
		description = newDescription;
	}
	
	@Override
	public String toString(){
		return description;
	}
	
	public WBSElement getContainingElement(){
		return containingElement;
	}
	

	
}
