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
	private WBSAttributes attributes;
	
	/**
	 * Constructor for a new WBS activity
	 * @param description
	 * @param element
	 */
	protected WBSActivity(String description, WBSElement element){
		this.description = description;
		this.containingElement = element;
	}

	
	public WBSAttributes getAttributes() {
		if (attributes == null) {
			attributes = new WBSAttributes(this);
		}
		return attributes;
	}
	
	/**
	 * Method that gets an activities description
	 * @return String - the activities description
	 */
	public String getDescription(){
		return description;
	}
	
	/**
	 * Method that changes the activities description to the specified input
	 * @param newDescription - the new descripition of the activity
	 */
	public void setDescription(String newDescription){
		description = newDescription;
	}
	
	/**
	 * A override to make the toString() method convert an activity to its description
	 * @return String - the activities description
	 */
	@Override
	public String toString(){
		return description;
	}
	
	/**
	 * Method to get the element in which the activity is contained
	 * @return
	 */
	public WBSElement getContainingElement(){
		return containingElement;
	}

	/**
	 * Method to get an activities allocated budget
	 * @return double - the activities budget
	 */
	public double getBudget() {
		return budget;
	}

	/**
	 * Method to set the activities budget to the specified value
	 * @param budget - the new value for the budget
	 * @throws throws an IllegalArgumentException when budget is below zero
	 */
	public void setBudget(double budget) {
		if(budget < 0) {
			throw new IllegalArgumentException("Budget can not be less than zero");
		}
		else {
			this.budget = budget;
		}
	}
	
	/**
	 * Method to get an activities actual cost
	 * @return double - the activities cost
	 */
	public double getActualCost() {
		return actualCost;
	}

	/**
	 * Method to change an activities actual cost
	 * @param actualCost
	 */
	public void setActualCost(double actualCost) {
		if(actualCost < 0) {
			throw new IllegalArgumentException("Actual cost can not be less than zero");
		}
		this.actualCost = actualCost;
	}

	/**
	 * Method to get the start date of an activity
	 * @return String - the start date in the format dd/mm/yyyy
	 */
	public String getStartDate() {
		return startDate;
	}

	/**
	 * Method for changing the start date of an activity so long
	 * as it is an actual date
	 * @param day - the numerical value of the day
	 * @param month - the numerical value of the month 
	 * @param year - the numerical value of the year
	 */
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

	/**
	 * Method to get an activities finish date
	 * @return String - the activities finish date
	 */
	public String getFinishDate() {
		return finishDate;
	}

	/**
	 * Method for changing the finish date of an activity so long
	 * as it is an actual date
	 * @param day - the numerical value of the day
	 * @param month - the numerical value of the month 
	 * @param year - the numerical value of the year
	 */
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
	
	/**
	 * Method to check that a given date is an actual date
	 * @param day - the day in the date
	 * @param month - the month in the date
	 * @param year - the year in the date
	 * @return true if the date is a valid one
	 */
	private boolean isDateValid(int day, int month, int year){
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
	
	/**
	 * Method for getting the days in the start date as a number
	 * @return int - a numerical representation of the start day
	 */
	public int getStartDays(){
		String[] date = startDate.split("/");
		int days = Integer.parseInt(date[0]);
		return days;
	}
	
	/**
	 * Method for getting the months in the start date as a number
	 * @return int - a numerical representation of the start month
	 */
	public int getStartMonths(){
		String[] date = startDate.split("/");
		int months = Integer.parseInt(date[1]);
		return months;
	}
	
	/**
	 * Method for getting the years in the start date as a number
	 * @return int - a numerical representation of the start year
	 */
	public int getStartYears(){
		String[] date = startDate.split("/");
		int years = Integer.parseInt(date[2]);
		return years;
	}
	
	/**
	 * Method for getting the days in the finish date as a number
	 * @return int - a numerical representation of the finish day
	 */
	public int getFinishDays(){
		String[] date = finishDate.split("/");
		int days = Integer.parseInt(date[0]);
		return days;
	}
	
	/**
	 * Method for getting the months in the finish date as a number
	 * @return int - a numerical representation of the finish day
	 */
	public int getFinishMonths(){
		String[] date = finishDate.split("/");
		int months = Integer.parseInt(date[1]);
		return months;
	}
	
	/**
	 * Method for getting the years in the finish date as a number
	 * @return int - a numerical representation of the finish year
	 */
	public int getFinishYears(){
		String[] date = finishDate.split("/");
		int years = Integer.parseInt(date[2]);
		return years;
	}
	
	/**
	 * Method for printing out errors messages in the attribute form for
	 * an activity.
	 * @param description - description attribute to check
	 * @param budget - budget attribute to check
	 * @param cost - cost attribute to check
	 * @return String - a message containing any error descriptions
	 */
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
	
	/**
	 * Method for printing out error messages regarding the start date
	 * @param day - the start date's day
	 * @param month - the start date's month 
	 * @param year - the start date's year
	 * @return String - a message containing any error descriptions
	 */
	public String validateStartDate(int day, int month, int year){
		String output = "";
		if(!isDateValid(day, month, year)){
			output = "The start date is not a valid date\n";
		}
		return output;
	}
	
	/**
	 * Method for printing out error messages regarding the finish date
	 * @param day - the finish date's day
	 * @param month - the finish date's month 
	 * @param year - the finish date's year
	 * @return String - a message containing any error descriptions
	 */
	public String validateFinishDate(int day, int month, int year){
		String output = "";
		if(!isDateValid(day, month, year)){
			output = "The finish date is not a valid date\n";
		}
		return output;
	}
	
	/**
	 * Method that presents an error message if the start date is after
	 * the finish date
	 * @param sDay - the day of the start date
	 * @param sMonth - the month of the start date
	 * @param sYear - the year of the start date
	 * @param fDay - the day of the finish date
	 * @param fMonth - the month of the finish date
	 * @param fYear - the year of the finish date
	 * @return String - a message containing error descriptions
	 */
	public String validateTimeFrame(int sDay, int sMonth, int sYear, int fDay, int fMonth, int fYear){
		String output = "";
		if(!(sYear <= fYear)){
			output = "The start date cannot be later than the finish date";
		}
		else if(!(sMonth <= fMonth)){
			output = "The start date cannot be later than the finish date";
		}
		else if(!(sDay <= fDay)){
			output = "The start date cannot be later than the finish date";
		}
		return output; 
	}
}
