package com.example.pocket_wbs.gui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.example.pocket_wbs.GUImain;
import com.example.pocket_wbs.R;
import com.example.pocket_wbs.model.MyGestureListener;
import com.example.pocket_wbs.model.ProjectTree;
import com.example.pocket_wbs.model.WBSElement;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.support.v4.view.GestureDetectorCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
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
    private String tempName="tempName";
    private GUImain main;
    private WBSElement selected;
    //Gestures attributes
    private GestureDetector gDetector;

    
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
    protected void onDraw(Canvas canvas) {

        // TODO Auto-generated method stub
        super.onDraw(canvas);
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
    		wbe.setName(wbe.getElementKey());
    		addElement(wbe);
    		//adjustElements(wbe, startxTemp, hGapLvlOne);
    		startxTemp+=elementWidth+hGapLvlOne;
    	}
    	
    	adjustElements(wbe);
    }

    public void toastMessage(String message){
    	Toast.makeText(this.getContext(), message, Toast.LENGTH_SHORT).show();
    }

    /*
     * Method to automatically adjust/shift elements to maintain a fixed distance between them
     * @author adrian
     */
    public void adjustElements(WBSElement wbs)
    {
    	int elementLevel = wbs.getElementLevel();
    	WBSElement parent = wbs.getParent();
    	int parentLevel = parent.getElementLevel();
    	
    	ArrayList<WBSElement> parentSiblings = new ArrayList<WBSElement>();
    	String orientation="";
    	
    	//Determine the Element's orientation (LEFT, MIDDLE, or RIGHT)
    	if(parent.getMidX()<rootMidPoint)
    		orientation="left";
    	else if (parent.getMidX()>rootMidPoint)
    		orientation="right";
    	else if (parent.getMidX()==rootMidPoint)
    		orientation="middle";
    		
    	//This check only runs for elements level 2 and above
    	if(elementLevel>=2) {
    		if(orientation.equals("left")) {
    			//Check to see if there are other elements on the
    			//same level as parent, filter according to branch (LEFT)
    			
    			/*for (WBSElement sibling : parent.getSiblings()) {
    				//If sibling is on the same branch side, add to LinkedList
    				if (sibling.getMidX()<rootMidPoint && sibling.hasChildren()) {
    					parentSiblings.add(sibling);
    					toastMessage(sibling.getName());
    				}
    				
    			}*/
    			for (WBSElement parentSibling : project.getProjectElementsAsArray()) {
    				if (parentSibling.getElementLevel()==parentLevel && parentSibling.getMidX()<=rootMidPoint) {
    					if(parentSibling.hasChildren()) {
    						parentSiblings.add(parentSibling);
    					}
    						
    				}
    			}
    			
    			//Now that we have siblings with children.. adjust from right to left
    			int lastIndexParent = parentSiblings.size()-1;
    			
    			ArrayList<WBSElement> parentChildren = new ArrayList<WBSElement>();
    			//Get most right parent first
    			for (int count=lastIndexParent; count>=0; count--) {
    				
    				//Get last parent to first parent
    				WBSElement p = parentSiblings.get(count);
    				
    				//Get all children in the parent
    				LinkedList<WBSElement> children = p.getChildren();
    				int lastIndexChild = children.size()-1;
    				
    				//Get right most child to left most child
    				WBSElement c = children.get(lastIndexChild);
    				
    				//If right most child is further than root mid point, shift family left.
    				if (c.getX()+elementWidth>rootMidPoint) {
    					while(c.getX()+elementWidth>rootMidPoint) {
    						//Move this element left (negative integer?)
    						c.moveX(-10);	
    			
        					//TODO MOVE ANY ELEMENTS ON THE SAME LEVEL towards the left
        					//of this element to the left (negative integer)
    						/*for(WBSElement allChildren : children){
    							allChildren.moveX(-10, children.size());
    						}*/
    					}
    				//Check if this element's parent has a sibling towards the right
    				//If the current count is less than the last IndexParent = there's a parent sibling to the right
    				} 

    				if (count<lastIndexParent) {
    					//If family to the right has children... then check if left most
    					//element in that family clashes with right most in this one
    					if(parentSiblings.get(count+1).hasChildren()) {
    						//If right most element in this family crosses over the start of the left most element
    						//in the family on the right
    						if(c.getX()+elementWidth>=parentSiblings.get(count+1).getChildByIndex(0).getX()) {
    							while(c.getX()+elementWidth+(hGapLvlOne)>=parentSiblings.get(count+1).getChildByIndex(0).getX()) {
    								c.moveX(-10);

    								//TODO MOVE ANY ELEMENTS ON THE SAME LEVEL towards the left
    	        					//of this element to the left (negative integer)
    	    						/*for(WBSElement allChildren : children){
	    							allChildren.moveX(-10, children.size());
	    							}*/
    							}
    						}
    					}
    				}
    				//Now check if this element's parent has a sibling towards the left
    				//If count is bigger than 0 that means there is a parent to the left
    				if(count>0) {
    					//If the family on the left has children
    					if(parentSiblings.get(count-1).hasChildren()) {
    						//If the left most element in this family touches the right
    						//most element in the family on the left
    						WBSElement neighbourParent = parentSiblings.get(count-1);
    						LinkedList<WBSElement> neighbourChildren = neighbourParent.getChildren();
    						//This gets the right most child in the neighbouring family to the left of this family
    						WBSElement neighbour = neighbourParent.getChildByIndex(neighbourParent.getNumChildren()-1);
    						if(c.getX()-hGapLvlOne<neighbour.getX()+elementWidth){
    							while(c.getX()-hGapLvlOne<neighbour.getX()+elementWidth) {
    								neighbour.moveX(-10);
    	    						/*for(WBSElement Children : neighbourChildren){
    	    							Children.moveX(-10, neighbourChildren.size());
    	    						}*/
    							}
    						}
    					}
    				}
    				
    			}

    			
				
    		} else if (orientation.equals("right")) {
    			
    		}
    	}
    }


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
   	       		  WBSElement w=project.addNewLastSibling(wbse.getChildByIndex(0), "default");
   	       	      w.setName(w.getElementKey());
   	       	      
   	       	      //If an element is added, the children of this element should arrange their 
   	       	      //children accordingly
   	       	      /*int siblingLevel = w.getElementLevel();
   	       	      for (WBSElement sibling : project.getProjectElementsAsArray()){
   	       	    	  if(sibling.getElementLevel()==siblingLevel){
   	       	    		  adjustElements(sibling);
   	       	    		  if(sibling.hasChildren()){
   	       	    			  adjustElements(sibling.getChildByIndex(0));
   	       	    			  sibling.arrangeChildren();
   	       	    		  }
   	       	    			  
   	       	    	  }
   	       	      }*/
   	       	      adjustElements(w);
   	       	      this.invalidate();
   	       	 }
   	       	 else{
   	       		 decomposeElement(wbse, wbse.getX(),wbse.getY());
   	       		 this.invalidate();	
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
}

