/**
 * Class for an object representing a WBS element
 * @author Alix, Jamie
 */

package com.example.pocket_wbs.model;

import java.io.Serializable;
import java.util.LinkedList;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.MotionEvent;

public class WBSElement implements Serializable{

	private String name;
	private WBSElement parent;
	private LinkedList<WBSElement> children;

	private int startx;
	private int starty;
	private int midx;
	private int elementWidth=120;
	private int elementHeight=100;
	int verticalGap=30;
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
	}
	
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
	}
	
	/*
	 * Alternative Constructor for an element object
	 * @author adrian
	 */
	public WBSElement(int startx, int starty){
		this.startx=startx;
		this.starty=starty;

	}
	
    public void onDraw(Canvas canvas) 
    {

    	Paint rectangleP = new Paint();
    	Paint rectangleS = new Paint();
    	Paint textp = new Paint();
    	Paint textl = new Paint();
    	
    	
    	rectangleS.setColor(Color.parseColor("#848484"));
    	rectangleP.setColor(Color.parseColor("#819FF7")); 
    	
    	textp.setColor(Color.WHITE);
    	textl.setColor(Color.BLACK);
    	
    	//textp.setTypeface(font);
	    textp.setTextSize(14);
	    
    	//Rectangle shadow
	    RectF s = new RectF(startx+5, starty+5, startx+elementWidth+5, starty+elementHeight+5);
	    canvas.drawRoundRect(s, 30, 30, rectangleS);
    	//Set rectangle start position to be in the middle of the screen
    	RectF r = new RectF(startx, starty, startx+elementWidth, starty+elementHeight);
    	canvas.drawRoundRect(r, 30, 30, rectangleP);
    	canvas.drawText(name, startx+(elementWidth/6), starty+(elementHeight/2), textp);
    	
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
    
    /*
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
		child.setX(this.getChildByIndex(this.getChildren().size()-1).getX()+elementWidth+horizontalGap);
		child.setY(this.getChildByIndex(this.getChildren().size()-1).getY());
		this.children.add(child);
		return child;
	}
	
	/*
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
	
	public void addChild(WBSElement child){
		this.children.add(child);
	}
	

	
	protected WBSElement addChildByIndex(String name, int index){
		WBSElement child = new WBSElement(name, this);
		this.children.add(index, child);
		//TODO calls method to re-arrange children
		return child;
	}
	
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
	
	public WBSElement getParent(){
		return this.parent;
	}
	
	public WBSElement getChildByIndex(int i) {
		return this.children.get(i);
	}
	
	public int getNumChildren()
	{
		return this.children.size();
	}
	/*
	 * Check whether element has parent or not - mostly used for branch drawing
	 * @author adrian
	 */
	protected boolean hasParent(){
		return this.parent!=null;
	}
	
	public LinkedList<WBSElement> getChildren(){
		return this.children;
	}
	
	
	public boolean isRoot(){
		boolean root = false;
		if(this.parent == null){
			root = true;
		}
		return root;
	}

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
	
	/*
	 * Method to return this WBS Element's Level One Parent, for the purpose of
	 * determining whether this element is branching left, right or down the middle
	 * @return The level one parent of this element if it isn't a Level one element
	 * @author adrian
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
	
	public WBSElement getRoot()
	{
		WBSElement tempElement;
		tempElement = this;
		
		while(tempElement.hasParent()) {
			tempElement=tempElement.getParent();
		}
		
		return tempElement;
	}
	
	/*
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
	/*
	 * Getter and setter methods for Element Coordinate variables 
	 * @author adrian
	 */
    public int getX()
    {
    	return startx;
    }
    
    public int getY()
    {
    	return starty;
    }
    
    /*
     * Method to set starting x-coordinate for element. Also changes the Mid x-coordinate when this happens
     */
    public void setX(int x)
    {
    	this.startx=x;
    	this.midx=x+(elementWidth/2);
	}
    
    public void moveX(int x){
    	this.startx+=x;
    	this.midx=startx+(elementWidth/2);
    	this.setOrientation();
    	
    	if(!this.getParent().isRoot())
    		this.moveParent(x);
    	
    	
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
    
	public int getIndex(){
		int index = 0;
		if(this.hasParent()){
			index = this.getParent().getChildren().indexOf(this);
		}
		return index;
	}
	
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
	
	public void moveParent(int x) {
		this.parent.parentMove(x);
		//this.parent.arrangeChildren();

	}
	
	public void parentMove(int x) {
		this.startx+=x;
    	this.midx=startx+(elementWidth/2);
    	this.arrangeChildren();
    	LinkedList<WBSElement> parentSiblings = this.getSiblings();
    	for(WBSElement ps : parentSiblings){
    		if(ps.orientation==this.orientation && ps!=this){
    			ps.siblingMove(x);
    		}
    	}
    	
    	this.setOrientation();

	}
	
	public void siblingMove(int x){
		this.startx+=x;
		this.midx=startx+(elementWidth/2);
		this.setOrientation();
	}
	/*
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
	
	
}