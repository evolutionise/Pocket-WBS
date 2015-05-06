package com.example.pocket_wbs;

import java.util.List;

import com.example.pocket_wbs.R;
import com.example.pocket_wbs.R.id;
import com.example.pocket_wbs.R.layout;
import com.example.pocket_wbs.R.menu;
import com.example.pocket_wbs.gui.MyCanvas;
import com.example.pocket_wbs.gui.WBSSurfaceView;
import com.example.pocket_wbs.model.ProjectTree;
import com.example.pocket_wbs.model.WBSElement;
import com.example.pocket_wbs.model.WBSFileManager;

import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class GUImain extends ActionBarActivity {
	private int MaxHorizontalElements = 10;
	private MyCanvas myCanvas2;
	public ProjectTree pt;
	String newName;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guimain);

	//Initializes screen to scroll to center of canvas
		DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels;
		final HorizontalScrollView hsv = (HorizontalScrollView)findViewById(R.id.horizontal_scroll_view);

	//Will receive ProjectTree object from previous activity, create manually for now
		Intent intent = getIntent();
		// If I made a new activity from the screen
		if(intent.hasExtra("com.example.pocket_wbs.NEW_TREE")){
			this.pt = (ProjectTree) intent.getSerializableExtra("com.example.pocket_wbs.NEW_TREE");
		}
		// If I came from the zoom view
		else if(intent.hasExtra("com.example.pocket_wbs.TREE_TO_OVERVIEW")){
			this.pt = (ProjectTree) intent.getSerializableExtra("com.example.pocket_wbs.TREE_TO_OVERVIEW");
		}
	
	//Set textView to display project name
		//TextView tv = (TextView)findViewById(R.id.projectTitle);
		//tv.setText(pt.getProjectName());
		
		int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 800, getResources().getDisplayMetrics());
		int pxW = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 2000, getResources().getDisplayMetrics());
	//Creates instance of MyCanvas onto the XML Layout
		this.myCanvas2 = new MyCanvas(this.getApplicationContext(), pt, pxW, this);
		LinearLayout myContainer = (LinearLayout)findViewById(R.id.container1);
		myCanvas2.setBackgroundColor(Color.parseColor("#FFFFFF"));
		myContainer.addView(myCanvas2, pxW, px);

	//Algorithm to calculate screen to scroll to middle of page at start
		final int scrollToX= (int) ((pxW/2)-(dpWidth/2));
		
		hsv.post(new Runnable() {
		    @Override
		    public void run() {
		        hsv.scrollTo(scrollToX, 0);
		    }
		});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu, menu);
		final Context context = this;
		final ProjectTree tree = this.pt;
		MenuItem saveButton = menu.add("Save");
		MenuItem saveAsButton = menu.add("Save As");
		MenuItem exportButton = menu.add("Export");
		saveButton.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
			
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				WBSFileManager wbsManager = new WBSFileManager();
				if(tree.treeSavedToFile()){
					wbsManager.saveTreeToFile(context, tree);
					return false;
				}
				else{
					wbsManager.showSaveAsDialog(context, tree);
				}
				return false;
			}
		});
		saveAsButton.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
			
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				WBSFileManager wbsManager = new WBSFileManager();
				wbsManager.showSaveAsDialog(context, tree);
				return false;
			}
		});
		exportButton.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
			
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				WBSFileManager wbsManager = new WBSFileManager();
				wbsManager.exportFile(context, tree);
				return false;
			}
		});
		
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
	
	public void renameElement(WBSElement element){ // Probably gonna need to change the name of this at some point
		Intent intent = new Intent(this, ViewElementOverview.class);
		ProjectTree tree = this.pt;
		String key = element.getElementKey();
		intent.putExtra("com.example.pocket_wbs.PROJECT_TREE", tree);
		intent.putExtra("com.example.pocket_wbs.ELEMENT_KEY", key);
		startActivity(intent);
		finish();
	}
	
	public String renameElementAlert()
	{	

		AlertDialog.Builder alertDialog = new AlertDialog.Builder(GUImain.this);

        // Setting Dialog Title
        alertDialog.setTitle("Rename Element");

        // Setting Dialog Message
        alertDialog.setMessage("Enter name");
        final EditText input = new EditText(GUImain.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.MATCH_PARENT);
          input.setLayoutParams(lp);
          alertDialog.setView(input);


        // Setting Icon to Dialog
        alertDialog.setIcon(R.drawable.ic_launcher);

        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int which) {
                        // Write your code here to execute after dialog
                    	newName = input.getText().toString();
                    }
                });
        // Setting Negative "NO" Button
        alertDialog.setNegativeButton("CANCEL",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to execute after dialog
                        dialog.cancel();
                    }
                });

        // closed

        // Showing Alert Message
        alertDialog.show();
        
        return newName;
	}
	
	public void addChild(View view){
		ProjectTree pt = myCanvas2.getTree();
		List<WBSElement> lt = myCanvas2.getArray();
		WBSElement w = pt.addNewLastSibling(pt.getRootElement().getChildByIndex(0), "Hello");
		w.setName(w.getElementKey());
		lt.add(w);
		myCanvas2.invalidate();
	}
	
	public void checkOrientation(View view){
		myCanvas2.displayOrientation();
	}
	
}