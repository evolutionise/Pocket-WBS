package com.example.pocket_wbs;

import com.example.pocket_wbs.model.ProjectTree;
import com.example.pocket_wbs.model.WBSElement;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ViewElementActivity extends ActionBarActivity {

	public ProjectTree tree;
	public WBSElement selectedElement;
	public boolean testSetUp = false; // For testing purposes
	
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
		setContentView(R.layout.activity_view_element);
		setUpEventListeners();
		updateActivity();
	}
	
	@Override
	public void onBackPressed(){
		Intent intent = new Intent(this, GUImain.class);
		ProjectTree tree = this.tree;
		intent.putExtra("com.example.pocket_wbs.TREE_TO_OVERVIEW", tree);
		startActivity(intent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.view_element, menu);
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
	
	public void setUpEventListeners(){
		
		Button leftButton = (Button) findViewById(R.id.moveLeft);
		Button rightButton = (Button) findViewById(R.id.moveRight);
		Button upButton = (Button) findViewById(R.id.moveUp);
		Button downButton = (Button) findViewById(R.id.moveDown);
		Button editButton = (Button) findViewById(R.id.editElementButton);
		
		leftButton.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				ViewElementActivity activity = (ViewElementActivity) v.getContext();
				int index = activity.selectedElement.getIndex() - 1;
				activity.selectedElement = selectedElement.getSiblings().get(index);
				activity.updateActivity();
			}
		});
		
		rightButton.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				ViewElementActivity activity = (ViewElementActivity) v.getContext();
				int index = activity.selectedElement.getIndex() + 1;
				activity.selectedElement = selectedElement.getSiblings().get(index);
				activity.updateActivity();
			}
		});
		
		upButton.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				ViewElementActivity activity = (ViewElementActivity) v.getContext();
				activity.selectedElement = selectedElement.getParent();
				activity.updateActivity();
			}
		});
		
		downButton.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				ViewElementActivity activity = (ViewElementActivity) v.getContext();
				activity.selectedElement = activity.selectedElement.getChildren().getFirst();
				activity.updateActivity();
			}
		});
		
		editButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ViewElementActivity activity = (ViewElementActivity) v.getContext();
				activity.goToEditActivity(v);
			}
		});
		
		
	}
	
	/**
	 * This method updates the activity based on the currently selected activity
	 */
	private void updateActivity(){
		updateTextViews();
		updateLeftButton();
		updateRightButton();
		updateUpButton();
		updateDownButton();
	}
	
	/**
	 * This method updates the text views based on the currently selected activity
	 */
	private void updateTextViews(){
		TextView elementKey = (TextView) findViewById(R.id.elementKeyView);
		TextView elementName = (TextView) findViewById(R.id.elementNameView);
		elementKey.setText("Element " + selectedElement.getElementKey());
		elementName.setText("Element Name: " + "\n" + selectedElement.getName());
	}
	
	/**
	 * This method updates the left button based on the currently selected activity
	 */
	private void updateLeftButton(){
		Button leftButton = (Button) findViewById(R.id.moveLeft);
		if(selectedElement.getIndex() == 0){
			leftButton.setVisibility(View.INVISIBLE);
		}
		else{
			leftButton.setVisibility(View.VISIBLE);
		}
	}
	
	
	/**
	 * This method updates the right button based on the currently selected activity
	 */
	private void updateRightButton(){
		Button rightButton = (Button) findViewById(R.id.moveRight);
		if(selectedElement.getIndex()+1 >= selectedElement.getSiblings().size()){
			rightButton.setVisibility(View.INVISIBLE);
		}
		else{
			rightButton.setVisibility(View.VISIBLE);
		}
	}
	
	/**
	 * This method updates the right button based on the currently selected activity
	 */
	private void updateUpButton(){
		Button upButton = (Button) findViewById(R.id.moveUp);
		if(selectedElement.isRoot()){
			upButton.setVisibility(View.INVISIBLE);
		}
		else{
			upButton.setVisibility(View.VISIBLE);
		}
	}
	
	/**
	 * This method updates the down button based on the currently selected activity
	 */
	private void updateDownButton(){
		Button downButton = (Button) findViewById(R.id.moveDown);
		if(!selectedElement.hasChildren()){
			downButton.setVisibility(View.INVISIBLE);
		}
		else{
			downButton.setVisibility(View.VISIBLE);
		}
	}
	
	public void goToEditActivity(View v){
		Intent intent = new Intent(this, EditElementActivity.class);
		String elementKey = this.selectedElement.getElementKey();
		ProjectTree tree = this.tree;
		intent.putExtra("com.example.pocket_wbs.ELEMENT", elementKey);
		intent.putExtra("com.example.pocket_wbs.TREE", tree);
		startActivity(intent);
		finish();
	}
}
