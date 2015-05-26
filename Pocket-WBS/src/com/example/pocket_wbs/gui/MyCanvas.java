package com.example.pocket_wbs.gui;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.example.pocket_wbs.GUImain;
import com.example.pocket_wbs.model.MyGestureListener;
import com.example.pocket_wbs.model.ProjectTree;
import com.example.pocket_wbs.model.WBSElement;
 
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
    private String tempName="tempName";
    private GUImain main;
    private WBSElement selected;
    //Gestures attributes
    private GestureDetector gDetector;
    private float mScaleFactor = 1.f;
    private float dx,dy = 1.f;


    
    public MyCanvas(Context context) {
        super(context);

    }
    
    /*
     * Constructor that gets called when the MyCanvas class is instantiated in GUImain
     */
    public MyCanvas(Context context, ProjectTree project, int width, GUImain main) {
        super(context);
    	this.width=width;
    	this.main=main;
    	MyGestureListener gestureListener = new MyGestureListener(this);
        gDetector = new GestureDetector(context, gestureListener);


        this.rootMidPoint = width/2;
        this.rootStartxPoint=rootMidPoint-(elementWidth/2);   
        this.rootEndxPoint=rootStartxPoint + elementWidth; 
        this.project = project;
        
        //Retrieves root element from ProjectTree type project to initialize some values
        WBSElement root = project.getRootElement();
        root.setX(rootStartxPoint);
        root.setY(10);
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
    @Override
	protected void onDraw(Canvas canvas) {

        // TODO Auto-generated method stub
        super.onDraw(canvas);
        
        canvas.translate(dx,dy);
        canvas.scale(mScaleFactor, mScaleFactor);
        Paint p = new Paint();
        this.canvas = canvas;
        
        WBSElements = project.getProjectElementsAsArray();
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
    	hGapLvlOne=elementWidth/4;
    	int startxTemp=parent.getMidX()-((hGapLvlOne/2)+elementWidth);
    	int startyTemp=starty+(elementHeight+verticalGap);
    	WBSElement wbe = new WBSElement("tempName");
    	
    	//For decompose, we will be creating 2 elements, so the loop runs twice 
    	//Adjusts start x for the elements accordingly
    	for (int count=0; count<2; count++)
    	{
    		String name = "Child " + count;
    		//Adds child to the project with reference to its parent - returns the child to
    		//be added to the WBS Element array list for population
    		wbe = project.addChildElement(parent, name, startxTemp, startyTemp);
    		wbe.setName("New Element");
    		addElement(wbe);
    		startxTemp+=elementWidth+hGapLvlOne;
    	}
    	
    }
    
    public void toastMessage(String message){
    	Toast.makeText(this.getContext(), message, Toast.LENGTH_SHORT).show();
    }

    /*
     * Method to automatically adjust/shift elements to maintain a fixed distance between them
     * @author adrian
     */


    @Override
    public boolean onTouchEvent(MotionEvent event) {
    	gDetector.onTouchEvent(event);
    	this.gDetector.setIsLongpressEnabled(true);
    	
    	 return true;
    }
    

    /*
     * Method to handle a longPress event - Decompose element
     */
    public void longPress(MotionEvent event) {

    	for (int i = WBSElements.size()-1; i >= 0; i--) {
    	    WBSElement wbse = WBSElements.get(i);
    	    if (wbse.isCollition(event.getX(),event.getY())) {
    	    	
   	       	 //Check disallows an element to be decomposed if it already has children
   	       	 if(wbse.hasChildren()){
   	       		  WBSElement w=project.addNewLastSibling(wbse.getChildByIndex(0), "New Element");
   	       	      treeAlgorithm();
   	       	      this.invalidate();
   	       	 }
   	       	 else{
   	       		 if(!wbse.hasActivities()){
   	       			 decomposeElement(wbse, wbse.getX(),wbse.getY());
      	       	     treeAlgorithm();
   	       			 this.invalidate();
   	       		 }	
   	       	 }
    	    	
    	    }
    	}

    }
    
    /*
     * Method to handle a doubleTap event - Open/View Element
     * @author Adrian
     */
    public void doubleTap(MotionEvent event) {
    	for (int i = WBSElements.size()-1; i >= 0; i--) {
    	    WBSElement wbse = WBSElements.get(i);
    	    if (wbse.isCollition(event.getX(),event.getY())) {
    	    	main.renameElement(wbse);
    	    }
    	}
    }
    
    public void singleTap(MotionEvent event){
    	boolean yes=false;
    	for (int i = WBSElements.size()-1; i >= 0; i--) {
    	    WBSElement wbse = WBSElements.get(i);
    	    if (wbse.isCollition(event.getX(),event.getY())) {
    	    	/*
    	    	yes=true;
    	    	if(this.selected==null){
    	    		this.selected=wbse;
    	    		wbse.isSelected(true);
    	    	}
    	    	else{
    	    		this.selected.isSelected(false);
    	    		this.selected=wbse;
    	    		wbse.isSelected(true);
    	    	}
    	    	this.invalidate();*/	
    	    }
    	}
    }
    
    
    /*
     * returns the selected element's level
     */
    public int getElementLevel(WBSElement element)
    {
    	return element.getElementLevel();
    }
    
    
    public ProjectTree getTree()
    {
    	return this.project;
    }
    
    public List<WBSElement> getArray() {
    	return this.WBSElements;
    }
    
    
    public void displayOrientation()
    {
    	if(selected!=null) {
    		toastMessage("Orientation: " + selected.getOrientation());
    	}
    	else
    		toastMessage("no element selected");
    }
    
    
    public void setScaleFactor(float scale){
    	this.mScaleFactor = scale;
    }
    
    public float getScaleFactor(){
    	return this.mScaleFactor;
    }
    

    /*
     * Experimental method to recalculate/regenerate tree display algorithm each time a new element is added/removed
     */
    public void treeAlgorithm() {
    		int highestLevel=0;
    		//Get last element of the tree & find out what level the tree is up to
	    	WBSElements = project.getProjectElementsAsArray();
	        for (WBSElement wbs : WBSElements) 
	        {
	            if(wbs.getElementLevel()>highestLevel) {
	            	highestLevel = wbs.getElementLevel();
	            }
	        }
    		
    		int treeLevel = highestLevel;
    		
    		//Iterate through each level
    		for(int level=0; level<=highestLevel; level++)
    		{
    			
    			
    			ArrayList<WBSElement> tempElementArray = new ArrayList<WBSElement>();
    			//Add all elements of the same level into temporary element array 
    			for (WBSElement e : project.getProjectElementsAsArray()) 
    			{
    				if (e.getElementLevel()==level) {
    						tempElementArray.add(e);
    				}
    			}

    			
		    	//Get index of the last element to know when the end of the list is reached
		    	int lastElementIndex = tempElementArray.indexOf(tempElementArray.get(tempElementArray.size()-1));
	    			
				//Now iterate through all the elements of the same level and adjust accordingly
				for (WBSElement e : tempElementArray) 
				{	
					int currentIndex = tempElementArray.indexOf(e);
					
					//Check if there is an element to the left of this element
					if(currentIndex-1>=0)
					{
						WBSElement elementToLeft = tempElementArray.get(currentIndex-1);
						
						while(e.getLeftThreshold()<elementToLeft.getRightThreshold())
						{
							elementToLeft.moveNew(-5);
							e.moveNew(5);
						}
						
						//For when elements are removed
						while((e.getLeftThreshold()-elementToLeft.getRightThreshold())>10)
						{
							elementToLeft.moveNew(5);
							e.moveNew(-5);
						}
					}
					
					//Check if there is an element to the right of this element
					if(tempElementArray.indexOf(e)!=lastElementIndex)
					{
						WBSElement elementToRight = tempElementArray.get(currentIndex+1);
						
						while(e.getRightThreshold()>elementToRight.getLeftThreshold())
						{
							elementToRight.moveNew(5);
							e.moveNew(-5);
						}
						
						//For when elements are removed
						while((elementToRight.getLeftThreshold()-e.getRightThreshold())>10)
						{
							elementToRight.moveNew(-5);
							e.moveNew(5);
						}
					}
					
				}
				
    			
    		}
    		
    		this.invalidate();
    		
    }
    
    
}

