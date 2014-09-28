/**
 * Class for an object representing a WBS element
 * @author Alix, Jamie
 */

package com.example.pocket_wbs.model;

import java.util.LinkedList;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.MotionEvent;

public class WBSElement {

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
	int horizontalGap=elementWidth/2;
	private boolean selected;
	
	/**
	 * Default constructor (for root element only) - should only ever be called by Project Tree!
	 * @param name
	 * @author Alix, Jamie
	 */
	protected WBSElement(String name){
		this.name = name;
		this.children = new LinkedList<WBSElement>();
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
    	Paint textp = new Paint();
    	Paint textl = new Paint();
    	
    	rectangleP.setColor(Color.parseColor("#E0E0F8")); 
    	textp.setColor(Color.BLACK);
    	textl.setColor(Color.BLACK);
    	
    	//textp.setTypeface(font);
	    textp.setTextSize(14);
    	
    	//Set rectangle start position to be in the middle of the screen
    	RectF r = new RectF(startx, starty, startx+elementWidth, starty+elementHeight);
    	canvas.drawRoundRect(r, 30, 30, rectangleP);
    	canvas.drawText(name, startx+(elementWidth/6), starty+(elementHeight/2), textp);
    	
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
		this.children.add(child);
		this.arrangeChildren();
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
	
	protected LinkedList<WBSElement> getChildren(){
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
		WBSElement lvlOneParent=null;
		
		if(getElementLevel()!=1) {
			for(int i=0; i<getElementLevel()-1; i++) {
				lvlOneParent=this.getParent();
			}
		}
		else
			lvlOneParent=this;
		
		return lvlOneParent;
	}
	
	/*
	 * Method that re-arranges children whenever a child is added (after decomposed)
	 * @author adrian
	 */
	public void arrangeChildren()
	{
		//only re-arranges if there are more than 2 children
		if(this.getNumChildren()>2) {
			
			int childStartY=starty+(elementHeight+verticalGap);
			
			//This value * ElementWidth will give us the starting position for 2 Children
			double value = 1.25;
			
			//Initialize what 'value' should be based on the number of children
			for (int i=0; i<getNumChildren()-2; i++) {
				value = value + 0.75;
			}
			
			int childStartX=(int) (this.midx-(value*elementWidth));
			LinkedList<WBSElement> children = this.getChildren();
			
			for (WBSElement child: children) {
				child.setX(childStartX);
				child.setY(childStartY);
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
    

}