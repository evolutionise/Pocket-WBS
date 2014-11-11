package com.example.pocket_wbs.model;

import java.io.Serializable;
import java.util.Date;

public class WBSActivity implements Serializable {
	
	private String description = "";
	private double budget = 0.00;
	private double actualCost = 0.00;
	private String startDate = "01/01/2015";
	private String finishDate = "01/01/2015";
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

	public void setStartDate(int day, int month, int year) {
		if(isDateValid(day, month, year)){
			String stringDay = Integer.toString(day);
			String stringMonth = Integer.toString(month);
			String stringYear = Integer.toString(year);
			if(stringDay.length() == 1){
				stringDay = "0" + day;
			}
			if(stringMonth.length() == 1){
				stringMonth = "0" + month;
			}
			if(stringYear.length() == 2 ){
				stringYear = "20" + year;
			}
			if(stringYear.length() == 3 ){
				stringYear = "2" + year;
			}
			this.startDate = stringDay + "/" + stringMonth + "/" + stringYear;
		}
	}

	public String getFinishDate() {
		return finishDate;
	}

	public void setFinishDate(int day, int month, int year) {
		if(isDateValid(day, month, year)){
			String stringDay = Integer.toString(day);
			String stringMonth = Integer.toString(month);
			String stringYear = Integer.toString(year);
			if(stringDay.length() == 1){
				stringDay = "0" + day;
			}
			if(stringMonth.length() == 1){
				stringMonth = "0" + month;
			}
			if(stringYear.length() == 1){
				stringYear = "200" + year;
			}
			if(stringYear.length() == 2 ){
				stringYear = "20" + year;
			}
			if(stringYear.length() == 3 ){
				stringYear = "2" + year;
			}
			this.finishDate = stringDay + "/" + stringMonth + "/" + stringYear;
		}
	}
	
	public boolean isDateValid(int day, int month, int year){
		boolean valid = true;
		if(year < 0){
			valid = false;
		}
		if(month <= 0 || month > 12){
			valid = false;
		}
		if(month == 4 || month == 6 || month == 9 || month == 11){
			if(day <= 0 || day > 30){
				valid = false;
			}
		}
		else if(month == 2){
			if((year % 400 == 0) || ((year % 100 != 0 && year % 4 == 0))){
				if(day <= 0 || day > 29){
					valid = false;
				}
			}
			else{
				if(day <= 0 || day > 28){
					valid = false;
				}
			}
		}
		else{
			if(day <= 0 || day > 31){
				valid = false;
			}
		}
		return valid;
	}
	
	public int getStartDays(){
		String[] date = startDate.split("/");
		int days = Integer.parseInt(date[0]);
		return days;
	}
	
	public int getStartMonths(){
		String[] date = startDate.split("/");
		int months = Integer.parseInt(date[1]);
		return months;
	}
	
	public int getStartYears(){
		String[] date = startDate.split("/");
		int years = Integer.parseInt(date[2]);
		return years;
	}
	
	public int getFinishDays(){
		String[] date = finishDate.split("/");
		int days = Integer.parseInt(date[0]);
		return days;
	}
	
	public int getFinishMonths(){
		String[] date = finishDate.split("/");
		int months = Integer.parseInt(date[1]);
		return months;
	}
	
	public int getFinishYears(){
		String[] date = finishDate.split("/");
		int years = Integer.parseInt(date[2]);
		return years;
	}
	
	public String validateFormInputs(String description, double budget, double cost){
		String output = "";
		
		if(description.equals("")){
			output += "You need to enter a description for the activity.\n";
		}
		if(budget < 0){
			output += "The budget you set was less than zero.\n";
		}
		if(cost < 0){
			output += "The actual cost you set was less than zero\n";
		}
		
		return output;
	}
	
	public String validateStartDate(int day, int month, int year){
		String output = "";
		if(!isDateValid(day, month, year)){
			output = "The start date is not a valid date\n";
		}
		return output;
	}
	
	public String validateFinishDate(int day, int month, int year){
		String output = "";
		if(!isDateValid(day, month, year)){
			output = "The finish date is not a valid date\n";
		}
		return output;
	}
	
	public String validateTimeFrame(int sDay, int sMonth, int sYear, int fDay, int fMonth, int fYear){
		String output = "";
		if(!(sDay <= fDay && sMonth <= fMonth && sYear <= fYear)){
			output = "The start date cannot be later than the finish date";
		}
		return output; 
	}
}
