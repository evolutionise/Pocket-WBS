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
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
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
	 * Method to populate the screen/tableLayout with WBS elements
	 * @author Adrian
	 
	
	
	public void populateTable(int NUM_ROWS, int NUM_COLS, int MaxHE)
	{
		/*
		 * Default table starts with 5 columns (All to be equal size)
		 * Main(First) WBS Element should be in 3rd Column (Centered)
		 * Algorithm for expansion of table columns according to highest no. of WBS Elements in a row yet to be decided
		 
		
		//If The Maximum number of WBS Elements that we have is more than 5
		//Expand the NUM_COLS to 10 (twice the size for now)
		if (MaxHE>5)
			NUM_COLS=10;
		
		TableLayout table = (TableLayout)findViewById(R.id.tableForElements);
		
		for (int row=0; row<NUM_ROWS; row++)
		{
			TableRow tableRow = new TableRow(this);
			//TableRow.LayoutParams fieldparams = new TableRow.LayoutParams(400, 5, 0.2f);
			
			
			table.addView(tableRow);
			//tableRow.setLayoutParams(fieldparams);
			
			for (int col=0; col<NUM_COLS; col++)
			{
				Button button = new Button(this);
				
				//Sets fixed size for button
				LayoutParams lp = new TableRow.LayoutParams(90,100,0.2f);
				button.setLayoutParams(lp);
				
				//Set text size of button 
				button.setTextSize(10.0f);
				
				//If space in the table shouldn't have an Element make button invisible
				if(row!=0 || col!=2)
					button.setVisibility(Button.INVISIBLE);
				
				//IF THIS IS A WBS ELEMENT THEN IMPLEMENT
				else
				{
					button.setText("Pocket WBS");
					
					//Implement onClick function
					button.setOnClickListener(new View.OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							
						}
					});
				}
				
				tableRow.addView(button);

				
			}
		}
	}
	*/
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