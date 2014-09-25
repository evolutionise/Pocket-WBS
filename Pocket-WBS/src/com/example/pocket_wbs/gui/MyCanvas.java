package com.example.pocket_wbs.gui;

import java.util.ArrayList;
import java.util.List;

import com.example.pocket_wbs.model.ProjectTree;
import com.example.pocket_wbs.model.WBSElement;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;
 
public class MyCanvas extends View {
	
	//Screen attributes
	int width;
	
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
	int hGapLvlOne=0;

    //ArrayList to store all WBS Elements for population
    private List<WBSElement> WBSElements = new ArrayList<WBSElement>();
    private ProjectTree project;
    
    public MyCanvas(Context context) {
        super(context);

    }
    
    /*
     * Constructor that gets called when the MyCanvas class is instantiated in GUImain
     */
    public MyCanvas(Context context, ProjectTree project, int width) {
        super(context);
    	this.width=width;

        this.rootMidPoint = width/2;
        this.rootStartxPoint=rootMidPoint-(elementWidth/2);   
        this.rootEndxPoint=rootStartxPoint + elementWidth; 
        this.project = project;
        
        //Retrieves root element from ProjectTree type project to initialize some values
        WBSElement root = project.getRootElement();
        root.setX(rootStartxPoint);
        root.setMidX(rootStartxPoint+(elementWidth/2));
        
        //Adds the root element to our WBSElement type ArrayList for population in onDraw()
        WBSElements.add(root);


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

        // TODO Auto-generated method stub
        super.onDraw(canvas);
        Paint p = new Paint();
        this.canvas = canvas;
        
        for (WBSElement wbs : WBSElements) 
        {
            wbs.onDraw(canvas);
        }


    }
    
    /*
     * Method to add elements to the WBS Elements array list
     */
    public void addElement(WBSElement wbe)
    {
    	WBSElements.add(wbe);
    }
    
    public void decomposeElement(WBSElement parent, int startx, int starty)
    {
    	//Initializes child element's starting x&y positions based on the parent
    	hGapLvlOne=elementWidth/2;
    	int startxTemp=startx-(elementWidth*3/4);
    	int startyTemp=starty+(elementHeight+verticalGap);
    	
    	//For decompose, we will be creating 2 elements, so the loop runs twice 
    	//Adjusts start x for the elements accordingly
    	for (int count=0; count<2; count++)
    	{
    		String name = "Child " + count;
    		//Adds child to the project with reference to its parent - returns the child to
    		//be added to the WBS Element arraylist for population
    		WBSElement wbe = project.addChildElement(parent, name, startxTemp, startyTemp);
    		addElement(wbe);
    		startxTemp+=elementWidth+hGapLvlOne;
    	}
    }
    

    @Override
    public boolean onTouchEvent(MotionEvent event) {
          for (int i = WBSElements.size()-1; i >= 0; i--) {
                 WBSElement wbse = WBSElements.get(i);
                 if (wbse.isCollition(event.getX(),event.getY())) {

                	 if(wbse.isRoot())
                	 {
                		 	
                	 }
                	 //Should sit in IF check, but for testing purposes will be outside for now
                	 decomposeElement(wbse, wbse.getX(),wbse.getY());
                	 this.invalidate();	
                 }
          }
          return super.onTouchEvent(event);
    }
    

}

