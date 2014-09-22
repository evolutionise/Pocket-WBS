package com.example.pocket_wbs.gui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Shader.TileMode;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
 
public class MyCanvas extends View {
	
	//Screen attributes
	float dpHeight;
	float dpWidth;
	
	//General attributes
	int verticalGap=30;
	int verticalGapHalf=15;
	int elementWidth=120;
	int elementHeight=100;
	
	//Root element attributes
	int rootMidPoint;
	int rootStartxPoint;
	int rootEndxPoint;
	int rootStartyPoint=0;
	int rootEndyPoint=rootStartyPoint+elementHeight;
	
	Canvas canvas;
	
	//Level One attributes
	public int numElementsLvlOne=0;
	public boolean hasChildren=false;
	int hGapLvlOne=0;
	int startXLvlOne=0;
	
	//Level Two attributes
	public int numElementsLvlTwo=0;
	int hGapLvlTwo=0;
	int startXLvlTwo=0;
	
	//Level Three attributes
	int numElementsLvlThree=0;
	
    public MyCanvas(Context context) {
        super(context);
        // TODO Auto-generated constructor stub


    }
 
    public MyCanvas(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
    }
 
    public MyCanvas(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }
 
    protected void onDraw(Canvas canvas) {
    	
    	DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
    	
    	float dpHeight = displayMetrics.heightPixels;
        float dpWidth = displayMetrics.widthPixels;
        
        this.dpHeight = dpHeight;
        this.dpWidth = dpWidth;
        this.canvas = canvas;
        this.rootMidPoint = (int)dpWidth/2;
        this.rootStartxPoint=rootMidPoint-(elementWidth/2);   
        
        this.rootEndxPoint=rootStartxPoint + elementWidth;
        
        // TODO Auto-generated method stub
        super.onDraw(canvas);
        Paint p = new Paint();

        
        
        createLevelOneChildren(numElementsLvlOne);
        createLevelOneBranches(numElementsLvlOne);
        createLevelTwoChildren(numElementsLvlTwo);
        drawRootElement(p, canvas);
        
    }
    
    /*
     * Most of these methods are still preliminary and are used for testing to decide
     * the most suitable means of generating a WBS GUI
     * 
     * @Author - Adrian
     */
    
    protected void drawRootElement(Paint p, Canvas canvas)
    {
    	if(numElementsLvlOne>2)
    	{
    		rootStartxPoint=startXLvlOne+elementWidth+hGapLvlOne;
    		rootEndxPoint=rootStartxPoint+elementWidth;
    	}
        p.setColor(Color.LTGRAY); 
    	//Set rectangle start position to be in the middle of the screen
    	RectF r = new RectF(rootStartxPoint, 0, rootEndxPoint, elementHeight);

    	canvas.drawRoundRect(r, 30, 30, p);
    	//canvas.drawRect(new RectF(dpWidth/2, 100, (dpWidth/2)+150, 200), p); 
    	
    	//Width of box is currently 120px
    	//Height of box is currently 100px
    }
    

    
    protected void drawDecomposedElements(RectF rect)
    {
    	int startx = (int) rect.centerX();
    	int starty = (int) rect.centerY();
    	starty = starty+100;
    	
    	RectF newRect1 = new RectF();
    }
   
    
    //Creates branches for level one
    public void createLevelOneBranches(int numChildren)
    {
    	int startXDown=rootMidPoint;
    	int endXDown=startXDown;
    	int startYDown=rootEndyPoint;
    	int endYDown=rootEndyPoint+verticalGapHalf;
    	
    	if (numChildren==2)
    	{
    		//The line across starts in the middle of the first element
    		int startXAcross=startXLvlOne+(elementWidth/2);
    		int startYAcross=rootEndyPoint+verticalGapHalf;
    		
    		//Line across ends in the middle of the last element
    		int endXAcross=startXAcross+((elementWidth+hGapLvlOne)*(numElementsLvlOne-1));
    		//int endXAcross=rootStartxPoint+(elementWidth*5/4);
    		int endYAcross=startYAcross;
    		
    		//draw line down
    		drawBranch(startXDown, startXDown, startYDown, endYDown);
    		//draw line across
    		drawBranch(startXAcross, endXAcross, startYAcross,endYAcross);
    		//draw line down from horizontal line
    		endYAcross+=verticalGapHalf;
    		drawBranch(startXAcross, startXAcross, startYAcross,endYAcross);
    		drawBranch(endXAcross, endXAcross, startYAcross,endYAcross);
    	}
    	else if(numChildren>=3)
    	{
    		//The line across starts in the middle of the first element
    		int startXAcross=startXLvlOne+(elementWidth/2);
    		int startYAcross=rootEndyPoint+verticalGapHalf;
    		//Line across ends in the middle of the last element
    		int endXAcross=startXAcross+((elementWidth+hGapLvlOne)*(numElementsLvlOne-1));
    		int endYAcross=startYAcross;
    		
    		startXDown=startXAcross+elementWidth+hGapLvlOne;
    		
    		//draw line down from Root Element
    		drawBranch(startXDown, startXDown, startYDown, endYDown);

    		//draw line down from horizontal line
    		endYAcross+=verticalGapHalf;
    		
    		
    		for (int count=0; count<numChildren; count++)
    		{
    			//draw line across from one element to the next
    			if(count<numChildren)
    			drawBranch(startXAcross, elementWidth+hGapLvlOne, startYAcross,startYAcross);
    			
    			//Draw line down from horizontal line to each element
    			drawBranch(startXAcross, startXAcross, startYAcross,endYAcross);
    			startXAcross+=(elementWidth+hGapLvlOne);
    		}
    	}
    }
    
    //Draws lines
    protected void drawBranch(int startx, int endx, int starty, int endy)
    {
    	Paint p = new Paint();
        
        //Draw line downwards
        canvas.drawLine(startx, starty, endx, endy, p);
    }
    
    
    /*
     * Creates Elements for Level One
     * @author Adrian
     */
    public void createLevelOneChildren(int numChildren)
    {
    	
    	//Y-axis start position is 30px (standard vertical gap) after parent element
    	int starty=elementHeight+verticalGap;
    	int tempStartX=0;
    	
		if(numChildren==2)
		{
			//Horizontal gap between children = width of an element
			hGapLvlOne=elementWidth/2;
			//Start point for the first child element
			startXLvlOne=rootStartxPoint-(elementWidth*3/4);
		}
		//If number of elements is > 2 then their Starting position and gap should be the same
		else
		{
			//Re-sizes the size of horizontal-gap in Lvl One elements to accomodate 
			if(numElementsLvlTwo>0)
				hGapLvlOne=elementWidth/2;
			else
				hGapLvlOne=elementWidth/4;
			
			startXLvlOne=rootStartxPoint-(hGapLvlOne+elementWidth);
			
			//Max start point for left most element is set here
				//shifts the whole tree to the right, by moving the root node
		}

		tempStartX=startXLvlOne;
		
		for(int count=0; count<numChildren; count++)
		{
			drawElement(tempStartX, starty);
			tempStartX+=elementWidth+hGapLvlOne;
		}
    }
    
    //General Element drawer
    protected void drawElement(int startx, int starty)
    {
    	Paint p = new Paint();
    	p.setColor(Color.LTGRAY); 
    	
    	//Set rectangle start position to be in the middle of the screen
    	RectF r = new RectF(startx, starty, startx+elementWidth, starty+elementHeight);
    	canvas.drawRoundRect(r, 30, 30, p);

    }
    
    public void createLevelTwoChildren(int numChildren)
    {
    	//Y-axis start position is 30px (standard vertical gap) after parent element
    	int starty=(elementHeight+verticalGap)*2;
    	int tempStartX=0;
    	
		if(numChildren==2)
		{
			//Horizontal gap between children = width of an element
			hGapLvlTwo=elementWidth/2;
			//Start point for the first child element
			startXLvlTwo+=elementWidth+hGapLvlTwo;
		}
		//If number of elements is > 2 then their Starting position and gap should be the same
		else
		{
			//Re-sizes the size of horizontal-gap in Lvl One elements to accommodate 
			if(numElementsLvlThree>0)
				hGapLvlTwo=elementWidth/2;
			else
				hGapLvlTwo=elementWidth/4;
			
			startXLvlTwo+=elementWidth+hGapLvlTwo;
			
		}

		tempStartX=startXLvlTwo;
		
		for(int count=0; count<numChildren; count++)
		{
			drawElement(tempStartX, starty);
			tempStartX+=elementWidth+hGapLvlTwo;
		}
    }
    /*
     * Method to return the number of elements in level two
     * @return number of elements
     * @author Adrian
     */
    public int getNumElementsLvlTwo()
    {
    	return numElementsLvlTwo;
    }
    
    public void setNumElementsLvlTwo(int numElements)
    {
    	numElementsLvlTwo = numElements;
    }
}
