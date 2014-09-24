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
	/*
	 * *********************************Adrians**********************************************
	 */
	private int startx;
	private int starty;
	private int elementWidth=120;
	private int elementHeight=100;
	/*
	 * *********************************Adrians**********************************************
	 */
	
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
	 * *********************************Adrians**********************************************
	 */
	public WBSElement(int startx, int starty){
		this.startx=startx;
		this.starty=starty;
	}
	
    public void onDraw(Canvas canvas) 
    {

    	Paint p = new Paint();
    	p.setColor(Color.LTGRAY); 
    	
    	//Set rectangle start position to be in the middle of the screen
    	RectF r = new RectF(startx, starty, startx+elementWidth, starty+elementHeight);
    	canvas.drawRoundRect(r, 30, 30, p);
    }
    
    public boolean isCollition(float x2, float y2) 
    {
        return x2 > startx && x2 < startx + elementWidth && y2 > starty && y2 < starty + elementHeight;
    }
    
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
	/*
	 * *********************************Adrians**********************************************
	 */
    
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
	
}