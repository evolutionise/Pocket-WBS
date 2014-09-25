package com.example.pocket_wbs;

import com.example.pocket_wbs.R;
import com.example.pocket_wbs.R.id;
import com.example.pocket_wbs.R.layout;
import com.example.pocket_wbs.R.menu;
import com.example.pocket_wbs.gui.MyCanvas;
import com.example.pocket_wbs.gui.WBSSurfaceView;
import com.example.pocket_wbs.model.ProjectTree;
import com.example.pocket_wbs.model.WBSElement;

import android.support.v7.app.ActionBarActivity;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
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
	
		
	//Initializes screen to scroll to center of canvas
		DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels;
		final HorizontalScrollView hsv = (HorizontalScrollView)findViewById(R.id.horizontal_scroll_view);

	//Will receive ProjectTree object from previous activity, create manually for now
		ProjectTree pt = new ProjectTree("Adrian's Project");
		
		int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 800, getResources().getDisplayMetrics());
		
	//Creates instance of MyCanvas onto the XML Layout
		MyCanvas myCanvas2 = new MyCanvas(this.getApplicationContext(), pt, px);
		LinearLayout myContainer = (LinearLayout)findViewById(R.id.container1);
		myCanvas2.setBackgroundColor(Color.parseColor("#E0E6F8"));
		myContainer.addView(myCanvas2, px, px);

	//Algorithm to calculate screen to scroll to middle of page at start
		final int scrollToX= (int) ((px/2)-(dpWidth/2));
		
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

}