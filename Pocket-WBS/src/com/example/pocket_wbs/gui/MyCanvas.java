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
	
	float dpHeight;
	float dpWidth;
	int superMidPoint;
	int superStartxPoint;
	int superEndxPoint;
	int superEndyPoint;
	int verticalGap=30;
	int verticalGapHalf=15;
	int elementWidth=140;
	int elementHeight=100;
	Canvas canvas;
	
	public int numChildren=0;
	
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
        this.superMidPoint = (int)dpWidth/2;
        this.superEndyPoint = 100;
        this.superStartxPoint = superMidPoint-70;
        this.superEndxPoint = superStartxPoint + elementWidth;
        
        // TODO Auto-generated method stub
        super.onDraw(canvas);

        
        Paint p = new Paint();

        drawRectangle(p, canvas);

        createLevelOneChildren(numChildren);
        createLevelOneBranches(numChildren);
       
        
    }
    
    /*
     * Most of these methods are still preliminary and are used for testing to decide
     * the most suitable means of generating a WBS GUI
     * 
     * @Author - Adrian
     */
    
    protected void drawRectangle(Paint p, Canvas canvas)
    {
        p.setColor(Color.LTGRAY); 
    	//Set rectangle start position to be in the middle of the screen
    	RectF r = new RectF((dpWidth/2)-70, 0, (dpWidth/2)+70, 100);
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
    	int startXDown=superMidPoint;
    	int endXDown=startXDown;
    	int startYDown=superEndyPoint;
    	int endYDown=superEndyPoint+verticalGapHalf;
    	
    	if (numChildren==2)
    	{
    		//The line across starts in the middle of the first element
    		int startXAcross=superStartxPoint-(elementWidth/4);
    		int startYAcross=superEndyPoint+verticalGapHalf;
    		//Line across ends in the middle of the last element
    		int endXAcross=superStartxPoint+(elementWidth*5/4);
    		int endYAcross=startYAcross;
    		
    		//draw line down
    		drawBranch(startXDown, endXDown, startYDown, endYDown);
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
    		int startXAcross=superStartxPoint-(elementWidth*3/4);
    		int startYAcross=superEndyPoint+verticalGapHalf;
    		//Line across ends in the middle of the last element
    		int endXAcross=superEndxPoint+(elementWidth*3/4);
    		int endYAcross=startYAcross;
    		
    		//draw line down
    		drawBranch(startXDown, endXDown, startYDown, endYDown);
    		//draw line across
    		
    		//draw line down from horizontal line
    		endYAcross+=verticalGapHalf;
    		
    		for (int count=0; count<numChildren; count++)
    		{
    			//draw line across from one element to the next
    			if(count<numChildren-1)
    			drawBranch(startXAcross, startXAcross+(elementWidth*5/4), startYAcross,startYAcross);
    			
    			//Draw line down from horizontal line to each element
    			drawBranch(startXAcross, startXAcross, startYAcross,endYAcross);
    			startXAcross+=(elementWidth*5/4);
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
    
    
    //Creates elements for Level One
    public void createLevelOneChildren(int numChildren)
    {
    	int childGap=0;
    	int startx=0;
    	//Y-axis start position is 30px (standard vertical gap) after parent element
    	int starty=elementHeight+verticalGap;
    	
		if(numChildren==2)
		{
			//Horizontal gap between children = width of an element
			childGap=elementWidth/2;
			//Start point for the first child element
			startx=superStartxPoint-(elementWidth*3/4);
		}
		//If number of elements is > 2 then their Starting position and gap should be the same
		else
		{
			childGap=elementWidth/4;
			startx=superStartxPoint-(elementWidth*5/4);
		}

		
		for(int count=0; count<numChildren; count++)
		{
			drawElement(startx, starty);
			startx+=elementWidth+childGap;
		}
    }
    
    protected void drawElement(int startx, int starty)
    {
    	Paint p = new Paint();
    	p.setColor(Color.LTGRAY); 
    	
    	//Set rectangle start position to be in the middle of the screen
    	RectF r = new RectF(startx, starty, startx+elementWidth, starty+elementHeight);
    	canvas.drawRoundRect(r, 30, 30, p);

    }
    
}
