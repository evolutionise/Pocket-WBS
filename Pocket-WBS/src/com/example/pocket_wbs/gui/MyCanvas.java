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
        // TODO Auto-generated method stub
        super.onDraw(canvas);
                //Getting width & height of the canvas which we configured his dimensions in the main.xml layout file.
        int w = canvas.getWidth();
        int h = canvas.getHeight();
                //With Paint class we are configuring the color of our pencil
        Paint p = new Paint();
        //drawBranch(canvas,p);
        drawRectangle(p, canvas);
        drawBranch(canvas,p);

    }
    
    /*
     * Most of these methods are still preliminary and are used for testing to decide
     * the most suitable means of generating a WBS GUI
     * 
     * @Author - Adrian
     */
    
    protected void drawRectangle(Paint p, Canvas canvas)
    {
    	DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
    	
    	float dpHeight = displayMetrics.heightPixels;
        float dpWidth = displayMetrics.widthPixels;
        
    	p.setColor(Color.LTGRAY); 
    	//Set rectangle start position to be in the middle of the screen
    	RectF r = new RectF((dpWidth/2)-70, 0, (dpWidth/2)+70, 100);
    	canvas.drawRoundRect(r, 30, 30, p);
    	//canvas.drawRect(new RectF(dpWidth/2, 100, (dpWidth/2)+150, 200), p); 
    	
    	//Width of box is currently 120px
    	//Height of box is currently 100px
    }
    
    protected void drawBranch(Canvas canvas, Paint paint)
    {
    	DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
    	
    	float dpHeight = displayMetrics.heightPixels;
        float dpWidth = displayMetrics.widthPixels;
    	 
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(1);
        int startx = (int) (dpWidth/2);
        int starty = 100;
        
        //Draw line downwards
        int nexty = starty+15;
        canvas.drawLine(startx, starty, startx, nexty, paint);
        //Draw line across
        canvas.drawLine(startx-100, nexty, startx+100, nexty, paint);
        //Draw branch lines
        canvas.drawLine(startx-100, nexty, startx-100, nexty+15, paint);
        canvas.drawLine(startx+100, nexty, startx+100, nexty+15, paint);
    }
    
    protected void drawDecomposedElements(RectF rect)
    {
    	int startx = (int) rect.centerX();
    	int starty = (int) rect.centerY();
    	starty = starty+100;
    	
    	RectF newRect1 = new RectF();
    }
    
    protected void getScreenMeasurements()
    {
    	DisplayMetrics displayMetrics = getResources().getDisplayMetrics();

        float dpHeight = displayMetrics.heightPixels / displayMetrics.density;
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
    }
    
}
