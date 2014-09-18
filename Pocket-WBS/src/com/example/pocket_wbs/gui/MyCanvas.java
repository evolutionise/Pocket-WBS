package com.example.pocket_wbs.gui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Shader.TileMode;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
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
        

    }
    
    protected void drawRectangle(Paint p, Canvas canvas)
    {
    	DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
    	
    	float dpHeight = displayMetrics.heightPixels / displayMetrics.density;
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        
    	p.setColor(Color.LTGRAY); 
    	RectF r = new RectF(dpWidth/2, 100, (dpWidth/2)+150, 200);
    	canvas.drawRoundRect(r, 30, 30, p);
    	//canvas.drawRect(new RectF(dpWidth/2, 100, (dpWidth/2)+150, 200), p); 
    }
    
    protected void drawBranch(Canvas canvas, Paint paint)
    {
    	DisplayMetrics displayMetrics = getResources().getDisplayMetrics();

        float dpHeight = displayMetrics.heightPixels / displayMetrics.density;
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        
        
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(3);
        int startx = (int) (dpWidth/2);
        int starty = 100;
        
        //Draw line downwards
        int nexty = starty+50;
        canvas.drawLine(startx, starty, startx, nexty, paint);
        //Draw line across
        canvas.drawLine(startx-100, nexty, startx+100, nexty, paint);
    }
    
    protected void getScreenMeasurements()
    {
    	DisplayMetrics displayMetrics = getResources().getDisplayMetrics();

        float dpHeight = displayMetrics.heightPixels / displayMetrics.density;
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
    }
}
