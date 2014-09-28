package com.example.pocket_wbs.gui;

import java.util.ArrayList;
import java.util.Iterator;
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
    		//be added to the WBS Element array list for population
    		WBSElement wbe = project.addChildElement(parent, name, startxTemp, startyTemp);
    		wbe.setName(wbe.getElementKey());
    		addElement(wbe);
    		adjustElements(wbe, startxTemp, hGapLvlOne);
    		startxTemp+=elementWidth+hGapLvlOne;
    	}
    }

    public void toastMessage(String message){
    	Toast.makeText(this.getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
    	gDetector.onTouchEvent(event);
    	this.gDetector.setIsLongpressEnabled(true);

    	 return true;
    }
    
    /*
     * Method to handle a doubleTap event - for now : Decompose element
     * @author Adrian
     */
    public void doubleTap(MotionEvent event) {
    	for (int i = WBSElements.size()-1; i >= 0; i--) {
    	    WBSElement wbse = WBSElements.get(i);
    	    if (wbse.isCollition(event.getX(),event.getY())) {
    	   	 
    	       	 //Check disallows an element to be decomposed if it already has children
    	       	 if(wbse.hasChildren()){
    	       		 String tempString = "Can't Decompose, Already has children";
    	       		 toastMessage(tempString);
    	       	 }
    	       	 else{
    	       		 decomposeElement(wbse, wbse.getX(),wbse.getY());
    	       		 this.invalidate();	
    	       	 }
    	    }
    	}
    }
    
    
    /*
     * Method to automatically adjust/shift elements to maintain a fixed distance between them
     * @author adrian
     */
    public void adjustElements(WBSElement wbs)
    {
    	int elementLevel = wbs.getElementLevel();
    	WBSElement parent = wbs.getParent();
    	String orientation;
    	
    	//Determine the Element's orientation (LEFT, MIDDLE, or RIGHT)
    	if(parent.getMidX()<rootMidPoint)
    		orientation="left";
    	else if (parent.getMidX()>rootMidPoint)
    		orientation="right";
    	else if (parent.getMidX()==rootMidPoint)
    		orientation="middle";
    		
    	//This check only runs for elements level 2 and above
    	if(elementLevel>=2) {
    		
    	}
    }
    
    public void adjustElements(WBSElement wbs, int startx, int gap)
    {
    	int level = wbs.getElementLevel();
    	int hGap = gap;
    	List<WBSElement> LevelElements = new ArrayList<WBSElement>();
    	List<WBSElement> LevelElementsParent = new ArrayList<WBSElement>();
    	
    	//Get all elements that are on the same level
        for (WBSElement element : WBSElements) 
        {
        	if(element.getElementLevel()==level) {
            	LevelElements.add(element);
        	}
        	else if (element.getElementLevel()==(level-1)) {
        		LevelElementsParent.add(element);
        	}
        }
        

        
        int count=LevelElements.size();
        int distanceFromRootMid = (elementWidth*count + ((count-1)*hGap))/2;
        int startX=project.getRootElement().getMidX()-distanceFromRootMid;
        
        //Iterate through level elements and adjust their positions
        //For level 0, 1, 2
        if (level<3 && count>=3) {
        	adjustElementsLevel012(count, distanceFromRootMid, startX, LevelElements, LevelElementsParent);
        } 
        //Special case for level 3 onwards, will try to seperate them to the right/left
        else if (level>=3) {
        	
        	int distanceFromMid;
        	
            for (WBSElement levelElement : LevelElements) {
            	//Find out if the level 1 node is to the left (before root Mid) or right (after)
            	//If before then to the left
            	if(levelElement.getParent().getParent().getX()<project.getRootElement().getMidX()) {
            		distanceFromMid=levelElement.getX()+elementWidth-project.getRootElement().getMidX();
                	if(distanceFromMid<0) {
                		//toastMessage("LEFT branch crossing over");
                	}
            	}
            	//If not then to the right
            	else {
            		if(levelElement.getX()<project.getRootElement().getMidX()) {
            			//toastMessage("RIGHT branch crossing over");
            		}
            		
            	}


            	
            }
        }

    }
    
    public void adjustElementsLevel012(int count, int distanceFromRootMid,int startX,List<WBSElement> LevelElements, List<WBSElement> LevelElementsParent ) {
    	
        for (WBSElement levelElement : LevelElements) {
        	levelElement.setX(startX);
        	startX = startX+elementWidth+hGapLvlOne;
        	
        }
        
        //Adjust parents accordingly (Counts number of children and centres parents
        for (WBSElement parentElement : LevelElementsParent) {
        	if(parentElement.hasChildren()) {
            	int numChild = parentElement.getNumChildren();
            	int midPoint = parentElement.getChildByIndex(0).getX() + (elementWidth*numChild + ((numChild-1)*hGapLvlOne))/2;
            	int parentStartX = midPoint-(elementWidth/2);
            	parentElement.setX(parentStartX);
        	}

        }
    }
    
    /*
     * Method to handle a longPress event - for now : Rename element
     */
    public void longPress(MotionEvent event) {

    	for (int i = WBSElements.size()-1; i >= 0; i--) {
    	    WBSElement wbse = WBSElements.get(i);
    	    if (wbse.isCollition(event.getX(),event.getY())) {
    	    	//String newName = main.renameElement();
    	    	getElementLevel(wbse);
    	    }
    	}

    }
    
    public void renameElement(WBSElement wbs, String newName) {
    	this.tempName=newName;
    }
    
    /*
     * returns the selected element's level
     */
    public int getElementLevel(WBSElement element)
    {
    	return element.getElementLevel();
    }
    
    
    public void testRename()
    {
    	WBSElement root = project.getRootElement();
    	root.setX(100);
    	WBSElements.add(root);
    	toastMessage(project.getProjectName() + "");
    	
    	this.invalidate();
    }
    
    public ProjectTree getTree()
    {
    	return this.project;
    }
    
    public List<WBSElement> getArray() {
    	return this.WBSElements;
    }
    
}

