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
	int elementWidth=140;
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
	
	
    
    /*
     * ***********************************************************************
     */

    
    private List<WBSElement> WBSElements = new ArrayList<WBSElement>();
    ProjectTree project;
    
    public MyCanvas(Context context) {
        super(context);

    }
    
    public MyCanvas(Context context, ProjectTree project, int width) {
        super(context);
    	this.width=width;

        this.rootMidPoint = width/2;
        this.rootStartxPoint=rootMidPoint-(elementWidth/2);   
        this.rootEndxPoint=rootStartxPoint + elementWidth; 
        this.project = project;
        WBSElement root = project.getRootElement();
        root.setX(rootStartxPoint);
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
    
    public void initializeWBS()
    {
        WBSElement rootElement = new WBSElement(rootStartxPoint,0);
        WBSElements.add(rootElement);
    }
    
    public void addElement(WBSElement wbe)
    {
    	WBSElements.add(wbe);
    }
    
    public void decomposeElement(WBSElement parent, int startx, int starty)
    {
    	hGapLvlOne=elementWidth/2;
    	int startxTemp=startx-(elementWidth*3/4);
    	int startyTemp=starty+(elementHeight+verticalGap);

    	for (int count=0; count<2; count++)
    	{
    		WBSElement wbe = new WBSElement(startxTemp, startyTemp);
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
                	 decomposeElement(wbse, wbse.getX(),wbse.getY());
                	 this.invalidate();
                	 }

                		 
                		
                 }
          }
          return super.onTouchEvent(event);
    }
    

}

