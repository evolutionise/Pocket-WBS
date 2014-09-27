package com.example.pocket_wbs.gui;

import com.example.pocket_wbs.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class WBSSurfaceView extends SurfaceView {
	private Bitmap bmp;
	private SurfaceHolder holder;
	
	public WBSSurfaceView(Context context) {
		super(context);
		
		holder = getHolder();
		holder.addCallback(new SurfaceHolder.Callback() {
			
			@Override
			public void surfaceDestroyed(SurfaceHolder holder) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void surfaceCreated(SurfaceHolder holder) {
				Canvas c = holder.lockCanvas(null);
				onDraw(c);
				holder.unlockCanvasAndPost(c);
				
			}
			
			@Override
			public void surfaceChanged(SurfaceHolder holder, int format, int width,
					int height) {
				// TODO Auto-generated method stub
				
			}
		});
		bmp = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
	}
	
    public WBSSurfaceView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
    }
 
    public WBSSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }
    
	@Override
	protected void onDraw(Canvas canvas) 
	{
		  super.onDraw(canvas);
	      canvas.drawColor(Color.BLACK);
	      canvas.drawBitmap(bmp, 10, 10, null);
	}


}


