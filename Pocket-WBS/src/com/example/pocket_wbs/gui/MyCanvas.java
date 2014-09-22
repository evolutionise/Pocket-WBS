package com.example.pocket_wbs.gui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
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
    
    /*
     * (non-Javadoc)
     * @see android.view.View#onDraw(android.graphics.Canvas)
     * Acts as a constructor as it is initialized in the XML: activity_guimain.xml
     */
    protected void onDraw(Canvas canvas) {
    	int width=this.getLayoutParams().width;
    	
    	DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
    	
    	float dpHeight = displayMetrics.heightPixels;
        float dpWidth = displayMetrics.widthPixels;
        
        this.dpHeight = dpHeight;
        this.dpWidth = dpWidth;
        this.canvas = canvas;
        
        this.rootMidPoint = width/2;
        this.rootStartxPoint=rootMidPoint-(elementWidth/2);   
        this.rootEndxPoint=rootStartxPoint + elementWidth;
        
        // TODO Auto-generated method stub
        super.onDraw(canvas);
        Paint p = new Paint();

        
        createLevelTwoChildren(numElementsLvlTwo);
        createLevelOneChildren(numElementsLvlOne);
        drawRootElement(p, canvas);
        createLevelOneBranches(numElementsLvlOne);
        
    }

    
    /*
     * This method draws the root element in the middle of a 800x800 canvas
     * @param p - object type paint
     * @param canvas - object type canvas
     */
    protected void drawRootElement(Paint p, Canvas canvas)
    {
        p.setColor(Color.LTGRAY); 
    	RectF r = new RectF(rootStartxPoint, 0, rootEndxPoint, elementHeight);
    	canvas.drawRoundRect(r, 30, 30, p);
    }
    
    
    
    protected void drawDecomposedElements(RectF rect)
    {
    	int startx = (int) rect.centerX();
    	int starty = (int) rect.centerY();
    	starty = starty+100;
    	
    	RectF newRect1 = new RectF();
    }
   

    
    /*
     * Creates Elements for Level One
     * @param numChildren - total number of children in this level
     * @author Adrian
     */
    public void createLevelOneChildren(int numChildren)
    {
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
			//Re-sizes the size of horizontal-gap (Bigger gap) in Lvl One elements to accommodate more elements
			if(numElementsLvlTwo>0)
				hGapLvlOne=elementWidth/2;
			else
				hGapLvlOne=elementWidth/4;
			
			startXLvlOne=rootStartxPoint-(hGapLvlOne+elementWidth);
		}

		tempStartX=startXLvlOne;
		
		for(int count=0; count<numChildren; count++)
		{
			drawElement(tempStartX, starty);
			tempStartX+=elementWidth+hGapLvlOne;
		}
    }
    
    
    
    /*
     * Method that draws the WBS elements onto the screen
     * @param startx - starting x-axis position for the element
     * @param starty - starting y-axis position for the element
     * @author Adrian
     */
    protected void drawElement(int startx, int starty)
    {
    	Paint p = new Paint();
    	p.setColor(Color.LTGRAY); 
    	
    	//Set rectangle start position to be in the middle of the screen
    	RectF r = new RectF(startx, starty, startx+elementWidth, starty+elementHeight);
    	canvas.drawRoundRect(r, 30, 30, p);

    }
    
    
    
    /*
     * Method that creates WBS elements for Level Two
     * @param numChildren - total number of children in this level
     * @author Adrian
     */
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
			tempStartX=startXLvlOne-(elementWidth+hGapLvlTwo);
			
			startXLvlTwo=tempStartX;
				

		}
		//If number of elements is > 2 then their Starting position and gap should be the same
		else
		{
			//Re-sizes the size of horizontal-gap in Lvl One elements to accommodate 
			if(numElementsLvlThree>0)
				hGapLvlTwo=elementWidth/2;
			else
				hGapLvlTwo=elementWidth/4;
			
			startXLvlTwo=rootStartxPoint-(hGapLvlTwo+elementWidth);

			
		}

		tempStartX=startXLvlTwo;
		
		for(int count=0; count<numChildren; count++)
		{
			drawElement(tempStartX, starty);
			tempStartX+=elementWidth+hGapLvlTwo;
		}
    }
    
    
    /*
     * Method to create branches for Level One
     * @param numChildren - total number of children for this level
     */
    public void createLevelOneBranches(int numChildren)
    {
    	int startXDown=rootMidPoint;
    	int endXDown=startXDown;
    	int startYDown=rootEndyPoint;
    	int endYDown=rootEndyPoint+verticalGapHalf;
    	
    	if (numChildren>=2)
    	{
    		//The line across starts in the middle of the first element
    		int startXAcross=startXLvlOne+(elementWidth/2);
    		int startYAcross=rootEndyPoint+verticalGapHalf;
    		int endYAcross=startYAcross;
    		
    		//draw line down from Root Element
    		drawBranch(startXDown, endXDown, startYDown, endYDown);

    		//Set horizontal line across to end according to the vertical gap value
    		endYAcross+=verticalGapHalf;
    		
    		
    		for (int count=0; count<numChildren; count++)
    		{
    			//draw line across from one element to the next
    			if(count<numChildren-1)
    			drawBranch(startXAcross, startXAcross+elementWidth+hGapLvlOne, startYAcross,startYAcross);
    			
    			//Draw line down from horizontal line to each element
    			drawBranch(startXAcross, startXAcross, startYAcross,endYAcross);
    			startXAcross+=(elementWidth+hGapLvlOne);
    		}
    	}
    }
    
    
    
    /*
     * Method that draws out the lines 
     * @param startx - starting horizontal coordinates
     * @param endx - ending horizontal coordinates
     * @param starty - starting vertical coordinates
     * @param endy - ending vertical coordinates
     */
    protected void drawBranch(int startx, int endx, int starty, int endy)
    {
    	Paint p = new Paint();
        
        //Draw line downwards
        canvas.drawLine(startx, starty, endx, endy, p);
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
    
    public void repositionTree(int moveDistance)

    {
    	startXLvlOne+=moveDistance;
    }
    

}

