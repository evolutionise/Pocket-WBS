package com.project_pocket_wbs.pocket_wbs.model;

import android.view.GestureDetector;
import android.view.MotionEvent;

import com.project_pocket_wbs.pocket_wbs.gui.MyCanvas;

public class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
    private static final String DEBUG_TAG = "Gestures"; 
    private MyCanvas myCanvas;
    
    public MyGestureListener(MyCanvas myCanvas)
    {
    	this.myCanvas = myCanvas;
    }
    
    @Override
    public boolean onDoubleTap(MotionEvent event) {
    	myCanvas.doubleTap(event);
        return true;
    }

    @Override
    public void onLongPress(MotionEvent event) {
    	myCanvas.longPress(event);
    }
    
    @Override
    public boolean onSingleTapConfirmed(MotionEvent event) {
        myCanvas.singleTap(event);
        return true;
    }
}
