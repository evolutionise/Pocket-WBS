package com.example.pocket_wbs;

import com.example.pocket_wbs.model.ProjectTree;
import com.example.pocket_wbs.model.WBSElement;
import com.example.pocket_wbs.model.WBSFileManager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ViewElementOverview extends ActionBarActivity {
	
	public ProjectTree tree;
	public WBSElement selectedElement;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = this.getIntent();
		//this.setUpTest();
		if(intent.hasExtra("com.example.pocket_wbs.TREE")){
			this.tree = (ProjectTree) intent.getSerializableExtra("com.example.pocket_wbs.TREE");
			String elementKey = intent.getStringExtra("com.example.pocket_wbs.KEY");
			this.selectedElement = this.tree.getProjectElements().get(elementKey);
		}
		else if(intent.hasExtra("com.example.pocket_wbs.PROJECT_TREE")){
			this.tree = (ProjectTree) intent.getSerializableExtra("com.example.pocket_wbs.PROJECT_TREE");
			String elementKey = intent.getStringExtra("com.example.pocket_wbs.ELEMENT_KEY");
			this.selectedElement = this.tree.getProjectElements().get(elementKey);
		}
		//Remove title bar
		setContentView(R.layout.activity_view_element_overview);
		setUpEventListeners();
		updateActivity();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu, menu);
		// Creates the save menu option
		final Context context = this;
		final ProjectTree tree = this.tree;
		MenuItem saveButton = menu.add("Save");
		saveButton.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
			
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				Toast saveMessage = new Toast(context);
				int displayTime = Toast.LENGTH_SHORT;
				CharSequence message = "File Saved";
				WBSFileManager wbsManager = new WBSFileManager();
				wbsManager.saveTreeToFile(context, tree);
				saveMessage.makeText(context, message, displayTime).show();
				return false;
			}
		});
		return true;
	}
	
	public void setUpEventListeners(){
		
		Button leftButton = (Button) findViewById(R.id.arrowLeft);
		Button rightButton = (Button) findViewById(R.id.arrowRight);
		Button upButton = (Button) findViewById(R.id.arrowUp);
		Button downButton = (Button) findViewById(R.id.arrowDown);
		//Button editButton = (Button) findViewById(R.id.editElementButton);
		
		leftButton.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				ViewElementOverview activity = (ViewElementOverview) v.getContext();
				if(selectedElement.getIndex() == 0){
					activity.tree.addNewLeftSibling(activity.selectedElement, "New Element");
				}
				if(!selectedElement.isRoot()){
					// There will be a new implementation for this in the new edit screen
					//EditText elementName = (EditText) findViewById(R.id.editElementName); 
					//activity.selectedElement.setName(elementName.getText().toString());
				}
				int index = selectedElement.getIndex() - 1;
				selectedElement = selectedElement.getSiblings().get(index);
				activity.updateActivity();
			}
		});
		
		rightButton.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				ViewElementOverview activity = (ViewElementOverview) v.getContext();
				if(activity.selectedElement.getIndex()+1 >= activity.selectedElement.getSiblings().size()){
					activity.tree.addNewRightSibling(activity.selectedElement, "New Element");
				}
				if(!activity.selectedElement.isRoot()){
					// There will be a new implementation for this in the new edit screen
					//EditText elementName = (EditText) findViewById(R.id.editElementName); 
					//activity.selectedElement.setName(elementName.getText().toString());
				}
				int index = activity.selectedElement.getIndex() + 1;
				activity.selectedElement = selectedElement.getSiblings().get(index);
				activity.updateActivity();				
			}
		});
		
		upButton.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				ViewElementOverview activity = (ViewElementOverview) v.getContext();
				if(!selectedElement.isRoot()){
					// There will be a new implementation for this in the new edit screen
					//EditText elementName = (EditText) findViewById(R.id.editElementName); 
					//e.setName(elementName.getText().toString());
				}
				selectedElement = selectedElement.getParent();
				activity.updateActivity();
			}
		});
		
		downButton.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				ViewElementOverview activity = (ViewElementOverview) v.getContext();
				if(!selectedElement.hasChildren()){
			    	int hGapLvlOne = selectedElement.elementWidth/4;
			    	int startxTemp = selectedElement.getMidX() - ((hGapLvlOne / 2) + selectedElement.elementWidth);
			    	int startyTemp = selectedElement.starty + (selectedElement.elementHeight + selectedElement.verticalGap);
					activity.tree.addChildElement(selectedElement, "New Element", startxTemp, startyTemp);
					startxTemp += selectedElement.elementWidth + hGapLvlOne;
					activity.tree.addChildElement(selectedElement, "New Element", startxTemp, startyTemp);
				}
				if(!selectedElement.isRoot()){
					// There will be a new implementation for this in the new edit screen
					//EditText elementName = (EditText) findViewById(R.id.editElementName); 
					//e.setName(elementName.getText().toString());
				}
				selectedElement = selectedElement.getChildren().getFirst();
				activity.updateActivity();				
			}
		});
	}
	
	/**
	 * This method updates the activity based on the currently selected element
	 */
	private void updateActivity(){
		updateTextViews();
		updateLeftButton();
		updateRightButton();
		updateUpButton();
		updateDownButton();
	}

	private void updateTextViews(){
		Button elementButton = (Button) findViewById(R.id.buttonElement);
		
	    String styledText = "<big> <font color='#FFFFFF'>"
	            + this.selectedElement.getName() + "</font> </big>" + "<br />" + "<br />"
	            + "<small>" + "Element Details:" + "</small>" + "<br />" + "<br />"
	            + "<small>" + this.selectedElement.getElementKey() + "</small>";
	    elementButton.setText(Html.fromHtml(styledText));
	    
		//EditText editElementName = (EditText) findViewById(R.id.editElementName);
		//elementKey.setText("Element " + selectedElement.getElementKey());

		//editElementName.setText(selectedElement.getName());
	}
	
	private void updateLeftButton(){
		Button leftButton = (Button) findViewById(R.id.arrowLeft);
		if(selectedElement.getIndex() == 0){
			leftButton.setVisibility(View.VISIBLE);
			leftButton.setText("Add Left");
			leftButton.setBackgroundResource(R.drawable.arrow_down);
		}
		else{
			leftButton.setVisibility(View.VISIBLE);
			String disp = selectedElement.getParent().getChildByIndex(selectedElement.getIndex()-1).getElementKey();
			leftButton.setText(disp);
			leftButton.setBackgroundResource(R.drawable.arrow_up);
		}
		if(selectedElement.isRoot()){
			leftButton.setVisibility(View.INVISIBLE);
		}
	}
	
	
	/**
	 * This method updates the right button based on the currently selected activity
	 */
	private void updateRightButton(){
		Button rightButton = (Button) findViewById(R.id.arrowRight);
		if(selectedElement.getIndex()+1 >= selectedElement.getSiblings().size()){
			rightButton.setVisibility(View.VISIBLE);
			rightButton.setText("Add Right");
			rightButton.setBackgroundResource(R.drawable.arrow_down);
		}
		else{
			rightButton.setVisibility(View.VISIBLE);
			String disp = selectedElement.getParent().getChildByIndex(selectedElement.getIndex()+1).getElementKey();
			rightButton.setText(disp);
			rightButton.setBackgroundResource(R.drawable.arrow_up);
		}
		
		if(selectedElement.isRoot()){
			rightButton.setVisibility(View.INVISIBLE);
		}
	}
	
	/**
	 * This method updates the right button based on the currently selected activity
	 */
	private void updateUpButton(){
		Button upButton = (Button) findViewById(R.id.arrowUp);
		if(selectedElement.isRoot()){
			upButton.setVisibility(View.INVISIBLE);
		}
		else{
			upButton.setVisibility(View.VISIBLE);
			upButton.setText("Up");
		}
	}
	
	/**
	 * This method updates the down button based on the currently selected activity
	 */
	private void updateDownButton(){
		Button downButton = (Button) findViewById(R.id.arrowDown);
		if(!selectedElement.hasChildren()){
			if(!selectedElement.hasActivities()){
				downButton.setText("Add Down");
				downButton.setBackgroundResource(R.drawable.arrow_down);
			}
			else{
				downButton.setVisibility(View.INVISIBLE);
			}
		}
		else{
			downButton.setText("Down");
			downButton.setBackgroundResource(R.drawable.arrow_up);
		}
	}
	
	@Override
	public void onBackPressed(){
		Intent intent = new Intent(this, GUImain.class);
		ProjectTree tree = this.tree;
		intent.putExtra("com.example.pocket_wbs.TREE_TO_OVERVIEW", tree);
		startActivity(intent);
		finish();
	}
	
	public void viewElementDetails(View view){
		Intent intent = new Intent(this, ViewElementActivity.class);
		ProjectTree tree = this.tree;
		String key = this.selectedElement.getElementKey();
		intent.putExtra("com.example.pocket_wbs.PROJECT_TREE", tree);
		intent.putExtra("com.example.pocket_wbs.ELEMENT_KEY", key);
		startActivity(intent);
		finish();
	}	
}
