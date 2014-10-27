package com.example.pocket_wbs.model;

import java.io.Serializable;

public class WBSActivity implements Serializable {
	
	private String description = "";
	private int duration = 0;
	private double budget = 0.00;
	private double actualCost = 0.00;
	private String startDate = "";
	private String finishDate = "";
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

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		if(duration < 0){
			throw new IllegalArgumentException("Duration can not be less than zero");
		}
		else {
			this.duration = duration;
		}
	}

	public double getBudget() {
		return budget;
	}

	public void setBudget(double budget) {
		if(budget < 0) {
			throw new IllegalArgumentException("Budget can not be less than zero");
		}
		else {
			this.budget = budget;
		}
	}

	public double getActualCost() {
		return actualCost;
	}

	public void setActualCost(double actualCost) {
		if(actualCost < 0) {
			throw new IllegalArgumentException("Actual cost can not be less than zero");
		}
		this.actualCost = actualCost;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getFinishDate() {
		return finishDate;
	}

	public void setFinishDate(String finishDate) {
		this.finishDate = finishDate;
	}
	

	
}
