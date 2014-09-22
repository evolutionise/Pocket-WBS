package com.example.pocket_wbs;

import com.example.pocket_wbs.R;
import com.example.pocket_wbs.R.id;
import com.example.pocket_wbs.R.layout;
import com.example.pocket_wbs.R.menu;
import com.example.pocket_wbs.gui.MyCanvas;

import android.support.v7.app.ActionBarActivity;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class GUImain extends ActionBarActivity {
	private int MaxHorizontalElements = 10;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guimain);
		//populateTable(5,5, MaxHorizontalElements);
		
		
		
		DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels;
		//Initializes screen to scroll to center of canvas
		final HorizontalScrollView hsv = (HorizontalScrollView)findViewById(R.id.horizontal_scroll_view);
		final MyCanvas myCanvas = (MyCanvas)findViewById(R.id.myCanvas);
		final int canvasWidth = myCanvas.getLayoutParams().width;
		
		//Algorithm to calculate screen to scroll to middle of page at start
		final int scrollToX= (int) ((canvasWidth/2)-(dpWidth/2));
		
		hsv.post(new Runnable() {
		    @Override
		    public void run() {
		        hsv.scrollTo(scrollToX, 0);
		    }
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.another, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	
	/*
	 * Decomposes an element automatically if it detected to have children
	 * Requires number of children
	 */
	public void autoDecomposeElement()
	{
		//Test data : No. of Children (Odd or Even)
		
		int noChild = 2;
		
		//If even number
		if (noChild%2==0)
		{
			
		}
	}
	
	public void breakDownElement2(View view)
	{
		MyCanvas myCanvas = (MyCanvas)findViewById(R.id.myCanvas);
		myCanvas.numElementsLvlOne = 2;
		myCanvas.invalidate();
		
	}
	
	public void breakDownElement3(View view)
	{
		MyCanvas myCanvas = (MyCanvas)findViewById(R.id.myCanvas);
		myCanvas.numElementsLvlOne = 3;
		myCanvas.invalidate();
		
	}
	
	public void breakDownElement4(View view)
	{
		MyCanvas myCanvas = (MyCanvas)findViewById(R.id.myCanvas);
		myCanvas.numElementsLvlOne = 4;
		myCanvas.invalidate();
		
	}
	
	public void breakDownElement5(View view)
	{
		MyCanvas myCanvas = (MyCanvas)findViewById(R.id.myCanvas);
		int numElementsLvlTwo = myCanvas.getNumElementsLvlTwo();
		
		if(numElementsLvlTwo==0)
		{
			myCanvas.setNumElementsLvlTwo(2);
		}

		myCanvas.invalidate();
		
	}
	
	public void breakDownElement6(View view)
	{
		MyCanvas myCanvas = (MyCanvas)findViewById(R.id.myCanvas);
		myCanvas.setNumElementsLvlTwo(0);
		myCanvas.invalidate();
	}

}