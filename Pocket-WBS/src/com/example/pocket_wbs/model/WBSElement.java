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

    	Paint p = new Paint();
    	Paint textp = new Paint();
    	Paint textl = new Paint();
    	
    	p.setColor(Color.LTGRAY); 
    	textp.setColor(Color.BLACK);
    	textl.setColor(Color.BLACK);
    	
    	//textp.setTypeface(font);
	    textp.setTextSize(12);
    	
    	//Set rectangle start position to be in the middle of the screen
    	RectF r = new RectF(startx, starty, startx+elementWidth, starty+elementHeight);
    	canvas.drawRoundRect(r, 30, 30, p);
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
	protected boolean hasChildren(){
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
		return child;
	}
	
	protected int getIndexOfChild(WBSElement child){
		return this.children.indexOf(child);
	}
	
	/**
	 * This method is used to get the name of an element
	 * @return String - the name of the element
	 * @author Alix, Jamie
	 */
	protected String getName(){
		return this.name;
	}
	
	protected WBSElement getParent(){
		return this.parent;
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

	protected String getElementKey(){
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
    
    public void setX(int x)
    {
    	this.startx=x;
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