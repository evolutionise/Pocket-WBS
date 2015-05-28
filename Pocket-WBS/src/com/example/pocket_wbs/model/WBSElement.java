/**
 * Class for an object representing a WBS element
 * @author Alix, Jamie, Adrian
 */

package com.example.pocket_wbs.model;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

public class WBSElement implements Serializable{

	private String name;
	private int workHours = 0;
	private double budget = 0.00;
	private String responsibleStaff = "";
	private WBSElement parent;
	private LinkedList<WBSElement> children;
	private List<WBSActivity> activities = new LinkedList<WBSActivity>();
	private WBSAttributes attributes;

	private int startx;
	public int starty;
	private int midx;
	public int elementWidth=120;
	public int elementHeight=100;
	public int verticalGap=30;
	int verticalGapHalf=15;
	int horizontalGap=elementWidth/4;
	private boolean selected;
	String orientation;
	
	/**
	 * Default constructor (for root element only) - should only ever be called by Project Tree!
	 * @param name
	 * @author Alix, Jamie
	 */
	public WBSElement(String name){
		this.name = name;
		this.children = new LinkedList<WBSElement>();
		setOrientation();
	}
	
	/**
	 * Method to check whether an element has been selected
	 * @param true if the element was selected
	 */
	public void isSelected(boolean selected){
		this.selected=selected;
	}
	
	/**
	 * Constructor for an element object
	 * @param name
	 * @param parent
	 */
	protected WBSElement(String name, WBSElement parent){
		this.name = name;
		this.parent = parent;
		this.children = new LinkedList<WBSElement>();
		setOrientation();
	}
	
	/**
	 * Alternative Constructor for an element object
	 * @author adrian
	 */
	public WBSElement(int startx, int starty){
		this.startx=startx;
		this.starty=starty;
		setOrientation();

	}
	
	
	public WBSAttributes getAttributes() {
		if (attributes == null) {
			attributes = new WBSAttributes(this);
		}
		return attributes;
	}
	
    /**
     * Method used to draw an element onto the project tree
     * display in the GUI.
     * @param canvas - the canvas to which the element is being drawn
     */
	public void onDraw(Canvas canvas) 
    {

    	Paint rectangleP = new Paint();
    	Paint rectangleS = new Paint();
    	Paint rectangleA = new Paint();
    	Paint textp = new Paint();
    	Paint textl = new Paint();
    	
    	rectangleS.setColor(Color.parseColor("#848484"));
    	rectangleP.setColor(Color.parseColor("#819FF7")); 

    	textp.setColor(Color.WHITE);
    	textl.setColor(Color.BLACK);
    	
    	//textp.setTypeface(font);
	    textp.setTextSize(14);
	    textl.setStrokeWidth(2);
	    
    	//If Element has activities, give indication
    	if(!this.getActivitiesAsStringArray().isEmpty())
    	{
    		rectangleA.setColor(Color.parseColor("#F78181"));
    	    RectF a = new RectF(startx+10, starty+10, startx+elementWidth+10, starty+elementHeight+10);
    	    canvas.drawRoundRect(a, 30, 30, rectangleA);
    	}
    	
    	//Rectangle shadow
	    RectF s = new RectF(startx+5, starty+5, startx+elementWidth+5, starty+elementHeight+5);
	    canvas.drawRoundRect(s, 30, 30, rectangleS);
    	//Set rectangle start position to be in the middle of the screen
    	RectF r = new RectF(startx, starty, startx+elementWidth, starty+elementHeight);
    	canvas.drawRoundRect(r, 30, 30, rectangleP);
    	
    	String textToDraw = getDrawableNameText();
    	int i = 0;
    	for(String textLine : textToDraw.split("\n")){
    		float newLineSpace = textp.getTextSize();
    		canvas.drawText(textLine, startx+(elementWidth/6), starty + (elementHeight/2) + (newLineSpace*i), textp);
    		i++;
    	}
    	//canvas.drawText(getDrawableNameText(), startx+(elementWidth/6), starty+(elementHeight/2), textp);
    	
    	//If Element is selected, change colour

    	//Draw branches from child to parent if parent exists
    	if(hasParent()){
    	//Draw branch upwards from child
    	canvas.drawLine(midx, starty, midx, starty-verticalGapHalf, textl);
    	
    	//Draw branch horizontal towards parent
    	if(startx>parent.getX()){
    		canvas.drawLine(midx, starty-verticalGapHalf, midx-(midx-parent.getMidX()),starty-verticalGapHalf, textl);
    	}
    	else{
    		canvas.drawLine(midx, starty-verticalGapHalf, midx+(parent.getMidX()-midx),starty-verticalGapHalf, textl);
    	}
    	
    	//Draw branch downwards from parent
    		canvas.drawLine(parent.getMidX(), parent.getY()+elementHeight, parent.getMidX(), parent.getY()+elementHeight+verticalGapHalf, textl);
    	
    	}
    }
    
    /**
     * Method to check whether the on-click is within the element's bounds
     */
    public boolean isCollition(float x2, float y2) 
    {
        return x2 > startx && x2 < startx + elementWidth && y2 > starty && y2 < starty + elementHeight;
    }
    
    
	/**
	 * This method changes the name of an element
	 * @param name - the new name for the element
	 * @author Alix, Jamie
	 */
	public void setName(String name){
		if(name.equals(null)){
			throw new IllegalArgumentException("The name cannot be null");
		}
		else if(this.parent == null){
			throw new IllegalArgumentException("The root element is always named after the project");
		}
		else{
			this.name = name;			
		}
	}
	
	public void updateNameOfRootElement(String name){
		if(name.equals(null)){
			throw new IllegalArgumentException("The name cannot be null");
		}
		else{
			if(this.isRoot()){
				this.name = name;
			}
		}
	}
	
	/**
	 * Checks if the WBS element has child elements decomposed from it
	 * @return true if the element has children
	 */
	public boolean hasChildren(){
		boolean childrenExist = false;
		if(!this.children.isEmpty()){
			childrenExist = true;
		}
		return childrenExist;
	}
	
	/**
	 * This method adds a child element to the end of the list
	 * @param child - the element to add
	 */
	public WBSElement addChild(String name){
		WBSElement child = new WBSElement(name, this);
		this.children.add(child);
		child.setX(this.getChildByIndex(this.getChildren().size()-2).getX()+elementWidth+horizontalGap);
		child.setY(this.getChildByIndex(this.getChildren().size()-2).getY());
		//this.children.add(child);
		return child;
	}
	
	/**
	 * This method adds a child element to the end of the list
	 * @param name - name for the element
	 * @param startx - starting x coordinates for element
	 * @param starty - starting y coordinates for element
	 * @author adrian
	 */
	public WBSElement addChild(String name, int startx, int starty){
		WBSElement child = new WBSElement(name, this);
		child.setX(startx);
		child.setY(starty);
		child.setMidX(startx+(elementWidth/2));
		child.setOrientation();
		this.children.add(child);
		return child;
	}
	
	// Legacy Method
	public void addChild(WBSElement child){
		this.children.add(child);
	}
	
	/**
	 * Adds a child element to the specified index in the elements
	 * list of children
	 * @param name - the name of the new element
	 * @param index - where to place the new element
	 * @param startx - the x location of the new element in the GUI
	 * @param starty - the y location of the new element in the GUI
	 * @return
	 */
	protected WBSElement addChildByIndex(String name, int index, int startx, int starty){
		WBSElement child = new WBSElement(name, this);
		this.children.add(index, child);
		child.setX(startx);
		child.setY(starty);
		child.setMidX(startx+(elementWidth/2));
		child.setOrientation();
		//TODO calls method to re-arrange children
		return child;
	}
	
	// Legacy Method
	protected WBSElement addChildByIndex(String name, int index, String direction){
		WBSElement child = new WBSElement(name, this);
		
		//TODO calls method to re-arrange children
		
		//Checks direction to see if the added child is to the left/right/front
		if(direction.equals("front")){
			
		} else if(direction.equals("left")){
			int startXTemp = this.getChildByIndex(index).startx;
			int startYTemp = this.getChildByIndex(index).starty;
			child.setX(startXTemp-horizontalGap-elementWidth);
			child.setY(startYTemp);
			this.children.add(index, child);

			//TODO can have renaming code here? All elements index would have changed.
			
			//Move the index to the sibling on the left
			index=index-1;
			//Loop to make sure every element to the left of the newly added one is shifted
			
			while(index>0){
				WBSElement temp = this.getChildByIndex(index);
				temp.setX(temp.startx-horizontalGap-elementWidth);
				index--;
			}
		} else if(direction.equals("right")){
			int startXTemp = this.getChildByIndex(index-1).startx;
			int startYTemp = this.getChildByIndex(index-1).starty;
			child.setX(startXTemp+horizontalGap+elementWidth);
			child.setY(startYTemp);
			this.children.add(index, child);
			
			//Move the index to the sibling on the right (if applicable)
			index=index+1;
			
			//Loop to make sure every element to the right of the newly added one is shifted
			while(index<this.children.size()){
				WBSElement temp = this.getChildByIndex(index);
				temp.setX(temp.startx+horizontalGap+elementWidth);
				index++;
			}
		}
		
		//this.children.add(index, child);
		return child;
	}
	
	/**
	 * Method used to find the index of a specific child element
	 * within an elements list of children.
	 */
	public int getIndexOfChild(WBSElement child){
		return this.children.indexOf(child);
	}
	
	/**
	 * This method is used to get the name of an element
	 * @return String - the name of the element
	 * @author Alix, Jamie
	 */
	public String getName(){
		return this.name;
	}
	
	/**
	 * Method used to find the parent element to which the
	 * element is assigned
	 * @return WBSElement - the elements parent
	 */
	public WBSElement getParent(){
		return this.parent;
	}
	
	/**
	 * Method used to get the child element at a specific index
	 * in the elements child list
	 * @param i - the index to look for
	 * @return - WBSElement - the child element at that index
	 */
	public WBSElement getChildByIndex(int i) {
		// Check for IndexOutOfBoundsException?
		return this.children.get(i);
	}
	
	/**
	 * Method for finding the number of children that an element 
	 * currently has
	 * @return int - the number of children that an element has
	 */
	public int getNumChildren()
	{
		return this.children.size();
	}
	
	/**
	 * Check whether element has parent or not - mostly used for branch drawing
	 * @author adrian
	 */
	protected boolean hasParent(){
		return this.parent!=null;
	}
	
	/**
	 * Method used to get the list of children for an element
	 * @return LinkedList<WBSElement> - an elements list of children
	 */
	public LinkedList<WBSElement> getChildren(){
		return this.children;
	}
	
	/**
	 * Method to check if the selected element is the root element
	 * @return true when the element is the root element
	 */
	public boolean isRoot(){
		boolean root = false;
		if(this.parent == null){
			root = true;
		}
		return root;
	}

	/**
	 * Method used to find a string representation of an elements location
	 * within the data structure (0, 1, 1.1, 1.1.1, 2, 2.1 etc).
	 * @return String - the location of the element within the data structure
	 */
	public String getElementKey(){
		String key = "";
		if(this.isRoot()){ // If the key is the root element
			key = "0";
		}
		else
		{
			if(this.getParent().isRoot()){ // If the elements parent is root
				key = "" + (this.getParent().getIndexOfChild(this)+1);
			}
			else{
				key = this.getParent().getElementKey() + "." + (this.getParent().getIndexOfChild(this)+1);
			}
		}
		return key;
	}
	
	/**
	 * Method used to find the decomposition level of the element
	 * @return
	 */
	public int getElementLevel() {
		int level=0;
		WBSElement tempElement;
		tempElement = this;
		
		while(tempElement.hasParent()) {
			tempElement=tempElement.getParent();
			level++;
		}
		return level;
	}
	
	/**
	 * Method to return this WBS Element's Level One Parent, for the purpose of
	 * determining whether this element is branching left, right or down the middle
	 * @return The level one parent of this element if it isn't a Level one element
	 * @author Adrian
	 */
	
	public WBSElement getLevelOneParent() {
		WBSElement lvlOneParent=this;
		
		if(getElementLevel()!=1) {
			for(int i=0; i<getElementLevel(); i++) {
				lvlOneParent=lvlOneParent.getParent();
			}
		}
		
		return lvlOneParent;
	}
	
	// Redundant - method available in project tree
	public WBSElement getRoot()
	{
		WBSElement tempElement;
		tempElement = this;
		
		while(tempElement.hasParent()) {
			tempElement=tempElement.getParent();
		}
		
		return tempElement;
	}
	
	/**
	 * Method that re-arranges children whenever a child is added (after decomposed)
	 * @author adrian
	 */
	public void arrangeChildren()
	{
		//only re-arranges if there are more than 2 children
		if(this.hasChildren()) {
			
			int childStartY=starty+(elementHeight+verticalGap);
			
			//This value * ElementWidth will give us the starting position for 2 Children
			//double value = 1.25;
			  double value = 0.5;
			  
			//Initialize what 'value' should be based on the number of children
			for (int i=0; i<getNumChildren()-1; i++) {
				value = value + 0.625;
			}
			
			int childStartX=(int) (this.midx-(value*elementWidth));
			LinkedList<WBSElement> children = this.getChildren();
			
			for (WBSElement child: children) {
				child.setX(childStartX);
				child.setY(childStartY);
				child.setOrientation();
				childStartX+=elementWidth+horizontalGap;
				
			}
		}
	}
	
	//====Beginning of element location methods section====//
	
	/**
	 * Getter and setter methods for Element Coordinate variables 
	 * @author adrian
	 */
    public int getX() {
    	return startx;
    }
    
    public int getY() {
    	return starty;
    }
    
    /**
     * Method to set starting x-coordinate for element. Also changes the Mid x-coordinate when this happens
     */
    public void setX(int x) {
    	this.startx=x;
    	this.midx=x+(elementWidth/2);
	}
    
    public void setY(int y)
    {
    	this.starty=y;
    }
	
    public int getMidX()
    {
    	return midx;
    }
    
    public void setMidX(int midx)
    {
    	this.midx=midx;
    }
    
    //====End of location methods sections===//
    
	/**
	 * Method used to find the elements index in the child array
	 * that it resides.
	 * @return int - the elements index
	 */
    public int getIndex(){
		int index = 0;
		if(this.hasParent()){
			index = this.getParent().getChildren().indexOf(this);
		}
		return index;
	}
	
	/**
	 * Method used to get a list of the elements siblings.
	 * @return LinkedList<WBSElement> - A list of the element and its sibilings
	 */
    public LinkedList<WBSElement> getSiblings(){
		LinkedList<WBSElement> siblings = new LinkedList<WBSElement>();
		if(this.isRoot()){
			siblings.add(this);
		}
		else{
			siblings = this.getParent().getChildren();
		}
		return siblings;
	}    
	
    public void moveNew(int x)
    {
    	this.startx+=x;
    	this.midx=startx+(elementWidth/2);
    	this.arrangeChildren();
    }
	
	public void siblingMove(int x){
		this.startx+=x;
		this.midx=startx+(elementWidth/2);
	}
	
	/**
	 * Method to determine where this element branches (LEFT or RIGHT)
	 */
	public void setOrientation(){
		WBSElement root = getRoot();
		
    	if(this.midx<root.getMidX())
    		orientation="left";
    	else if (this.midx>root.getMidX())
    		orientation="right";
    	else if (this.midx==root.getMidX())
    		orientation="middle";
	}
	
	
	public String getOrientation()
	{
		return this.orientation;
		
	}
	
	/**
	 * Method used to add a new activity to an element
	 * @param description - the description of the new activity
	 * @return WBSActivity - the activity that was added to the element
	 */
	public WBSActivity addActivity(String description){
			WBSActivity newActivity = new WBSActivity(description, this);
			activities.add(newActivity);
			return newActivity;
	}
	
	/**
	 * Method used to remove a selected activity from an elements list of
	 * activities
	 * @param activity - the activity to be removed
	 */
	public void deleteActivity(WBSActivity activity){
		activities.remove(activity);
	}
	
	/**
	 * All activities including the one currently at the index parameter will have 1 added to their index.
	 */
	public void setActivityIndex(int index, WBSActivity activity){
		activities.remove(activity);
		activities.set(index, activity);
	}
	
	/**
	 * Method used to find the index of an activity within the elements
	 * activity list.
	 * @param activity - the activity to find the index of
	 * @return int - the index of the specified activity
	 */
	public int getActivityIndex(WBSActivity activity){
		return activities.indexOf(activity);
	}
	
	/**
	 * Method used to find a specific activity from the given position
	 * @param position - the index of the activity to find
	 * @return WBSActivity - the activity at the specified position
	 */
	public WBSActivity getActivityByIndex(int position){
		return activities.get(position);
	}
	
	/**
	 * This method may never be used but added it in here just in case.  Less reliable than setting the activity index directly. 
	 * @author Alix
	 */
	public void swapActivities(WBSActivity act1, WBSActivity act2){
		//take index of act1 to use later
		int index = activities.indexOf(act1);
		//remove act1 so it isn't duplicated
		activities.remove(act1);
		//replace act2 with act 1
		activities.set(activities.indexOf(act2), act1);
		//add in act2 where act1 previously was
		activities.add(index, act2);		
	}
	
	/**
	 * Method to get a string array of all of the WBSActivities for the ListView 
	 * Used in Sprint Two, may not be needed after adding WBSAttributes as the ListView will change type
	 * @author Alix
	 */
	public List<String> getActivitiesAsStringArray(){
		List<String> activitiesArray = new LinkedList<String>();
		for(int i = 0; i < activities.size(); i++){
			String activityDescription = activities.get(i).getDescription();
			activitiesArray.add(activityDescription);
		}
		
		return activitiesArray;
	}

	/**
	 * Method used to find the duration of an element
	 * @return int - the element's duration
	 */
	public int getWorkHours() {
		return workHours;
	}

	/**
	 * Method used to change the duration of an activity
	 * @throws IllegalArgumentException when the new duration is lower than zero
	 * @param duration
	 */
	public void setWorkHours(int hours) {
		if(hours < 0){
			throw new IllegalArgumentException("Work hours can not be less than zero");
		}
		else {
			this.workHours = hours;
		}
	}

	/**
	 * Method used to get an element's budget
	 * @return double - an element's assigned budget
	 */
	public double getBudget() {
		return budget;
	}

	/**
	 * Method used to change the budget of an activity
	 * @throws IllegalArgumentException when the new budget is lower than zero
	 * @param budget - the new value to assign the budget to
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
	 * Method used to get the name of the staff member assigned as
	 * the responsible manager for an activity.
	 * @return String - the name of the individual responsible for the activity
	 */
	public String getResponsibleStaff() {
		return responsibleStaff;
	}

	/**
	 * Method used to change the name of the responsible staff member
	 * @param responsibleStaff
	 */
	public void setResponsibleStaff(String responsibleStaff) {
		this.responsibleStaff = responsibleStaff;
	}
	
	/**
	 * Method used to check whether the elements has activities assigned
	 * to it
	 * @return true if the element has activities
	 */
	public boolean hasActivities(){
		boolean activitiesExist = !this.activities.isEmpty();
		return activitiesExist;
	}
	
	/**
	 * Method used to find the total budget assigned to an elements
	 * children
	 * @return double - the total budget of all children
	 */
	public double getBudgetTotalOfChildren(){
		double totalBudget = 0;
		
		if(this.hasChildren()){
			for(WBSElement element : this.children){
				totalBudget += element.getBudget();
			}
		}
		else{
			if(this.hasActivities()){
				for(WBSActivity activity : this.activities){
					totalBudget += activity.getBudget();
				}
			}
		}
		
		return totalBudget;
	}
	
	/**
	 * Method used to find the remaining budget of an element
	 * @return double - the amount of money left for assignment
	 */
	public double getRemainingBudget(){
		double remainingBudget = this.getBudget() - this.getBudgetTotalOfChildren();
		return remainingBudget;
	}
	
	/**
	 * Method used to find whether the total budget of on element's children
	 * is over the element's assigned budget
	 * @return true if the element is over budget
	 */
	public boolean elementOverBudget(){
		boolean overBudget = false;
		if(this.getBudget() - this.getBudgetTotalOfChildren() < 0){
			overBudget = true;
		}
		return overBudget;
	}
	
	/**
	 * Method used to remove a child element at a specific index in the
	 * elements list of children
	 * @param childIndex - the index of the child to be removed
	 */
	public void removeChild(int childIndex){
		if(this.getNumChildren() == 2){
			this.children.clear();
		}
		else{
			this.children.remove(childIndex);
		}		
	}
	
	
	public void deleteActivityByIndex(int index){
		if(this.hasActivities()){
				this.activities.remove(index);
		}
	}
	
	
	/*
	 * Method to determine how much space this element needs according to the number of children it has 
	 * @return int - number of space in pixels that this element needs for its children
	 */
	public int getChildSpaceNeeded() {
		
		int returnValue;
		
		if(this.hasChildren())
		{
			returnValue = ((this.getNumChildren()*elementWidth)+((this.getNumChildren()-1)*horizontalGap));
		}
		else
			returnValue = elementWidth;
		
		return returnValue;
			
	}
	
	/*
	 * Method to determine x-threshold space required to the left of this element
	 * @return the left x-coordinate for space required
	 */
	public int getLeftThreshold() {
		
		return (this.getMidX()-(getChildSpaceNeeded()/2)-(elementWidth/4));
	}
	
	/*
	 * Method to determine x-threshold space required to the right of this element
	 * @return the right x-coordinate for space required
	 */
	
	public int getRightThreshold() {
		return (this.getMidX()+(getChildSpaceNeeded()/2)+(elementWidth/4));
	}
	
	public String getDrawableNameText(){
		String drawableText = "";
		String elementName = this.getName();
		if(elementName.length() > 14){
			boolean spaceFound = false;
			for(int x = 13; x > 0; x--){
				char c = elementName.charAt(x);
				if(c == ' '){
					drawableText += elementName.substring(0, x+1) + "\n";
					if(elementName.substring(x+1, elementName.length()).length() > 14){
						drawableText += elementName.substring(x+1, x+11) + "...";
					}
					else{
						drawableText += elementName.substring(x+1, elementName.length());
					}
					spaceFound = true;
					break;
				}
			}
			if(!spaceFound){
				drawableText += elementName.substring(0, 14) + "\n";
				if(elementName.substring(14, elementName.length()).length() > 14){
					drawableText += "-" + elementName.substring(14, 25) + "...";
				}
				else{
					drawableText += "-" + elementName.substring(14, elementName.length());
				}
						
			}
		}
		else{
			drawableText = elementName;
		}
		return drawableText;
	}
	
	public void removeChildren(){
		this.children.clear();
	}
	
	public void deleteElementFromParent(){
		if(!this.isRoot()){
			this.clearHierarchy();
			this.getParent().removeChild(this.getIndex());
		}
	}
	
	public void clearHierarchy(){
		if(this.hasChildren()){
			if(this.elementHasNoSubChildren()){
				this.removeChildren();
			}
			else{
				for(WBSElement child : this.children){
					child.clearHierarchy();
				}
				this.removeChildren();
			}
		}
	}
	
	public boolean elementHasNoSubChildren(){
		boolean result = true;
		for(WBSElement child : this.children){
			if(child.hasChildren()){
				result = false;
				break;
			}
		}
		return result;
	}
}