package com.example.pocket_wbs;

import java.text.DecimalFormat;

import com.example.pocket_wbs.gui.WBSActivityArrayAdapter;
import com.example.pocket_wbs.model.ProjectTree;
import com.example.pocket_wbs.model.WBSActivity;
import com.example.pocket_wbs.model.WBSElement;

import android.support.v7.app.ActionBarActivity;
import android.text.InputFilter;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ViewElementActivity extends ActionBarActivity {

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
	    this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_view_element);
		setUpEventListeners();
		updateActivity();
		
		//Create ListView to put WBSActivities into
		
		ListView activitiesList = (ListView) findViewById(R.id.activitiesList);
		
		WBSActivityArrayAdapter adapter = new WBSActivityArrayAdapter(this, selectedElement, tree);
		activitiesList.setAdapter(adapter);
	}
	
	@Override
	public void onBackPressed(){
		Intent intent = new Intent(this, ViewElementOverview.class);
		ProjectTree tree = this.tree;
		String key = this.selectedElement.getElementKey();
		intent.putExtra("com.example.pocket_wbs.TREE", tree);
		intent.putExtra("com.example.pocket_wbs.KEY", key);
		startActivity(intent);
		finish();
	};

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
	
	/**
	 * This method updates the activity based on the currently selected activity
	 */
	private void updateActivity(){
		updateTextViews();
		updateWBSActivityView();
		updateDeleteButton();
	}
	
	private void updateWBSActivityView(){
		TextView activitiesText = (TextView) findViewById(R.id.activityHeadingText);
		ListView activitiesList = (ListView) findViewById(R.id.activitiesList);
		if(selectedElement.hasChildren()){
			activitiesText.setVisibility(View.INVISIBLE);
			activitiesList.setVisibility(View.INVISIBLE);
		}
		else{
			activitiesText.setVisibility(View.VISIBLE);
			activitiesList.setVisibility(View.VISIBLE);
		}
	}
	
	/**
	 * This method updates the text views based on the currently selected activity
	 */
	private void updateTextViews(){
		DecimalFormat df = new DecimalFormat("0.00");
		
		TextView elementKey = (TextView) findViewById(R.id.elementKeyView);
		TextView childAllocatedBudget = (TextView) findViewById(R.id.childBudgetTotalText);
		TextView remainingBudget = (TextView) findViewById(R.id.remainingBudgetText);
		
		EditText editElementName = (EditText) findViewById(R.id.editElementName);
		EditText budget = (EditText) findViewById(R.id.budgetEditText);
		EditText duration = (EditText) findViewById(R.id.durationEditText);
		EditText manager = (EditText) findViewById(R.id.managerEditText);
		
		elementKey.setText("Element " + selectedElement.getElementKey());
		childAllocatedBudget.setText("Sub-Element Budget Total: " + df.format(selectedElement.getBudgetTotalOfChildren()));
		remainingBudget.setText("Remaining Budget: " + df.format(selectedElement.getRemainingBudget()));
		
		budget.setText(df.format(selectedElement.getBudget()));
		duration.setText(Integer.toString(selectedElement.getDuration()));
		manager.setText(selectedElement.getResponsibleStaff());
		editElementName.setText(selectedElement.getName());
	}
	
	private void setUpEventListeners(){
		Button save = (Button) this.findViewById(R.id.elementSaveButton);
		Button delete = (Button) this.findViewById(R.id.deleteElementButton);
		Button cancel = (Button) this.findViewById(R.id.elementCancelButton);
		save.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ViewElementActivity activity = (ViewElementActivity) v.getContext();
				Context context = getApplicationContext();
				EditText elementName = (EditText) findViewById(R.id.editElementName);
				EditText budget = (EditText) findViewById(R.id.budgetEditText);
				EditText duration = (EditText) findViewById(R.id.durationEditText);
				EditText manager = (EditText) findViewById(R.id.managerEditText);
				
				String newName = elementName.getText().toString();
				double newBudget = Double.parseDouble(budget.getText().toString());
				int newDuration = Integer.parseInt(duration.getText().toString());
				String newManager = manager.getText().toString();
				
				if(activity.selectedElement.validateFormInputs(newName, newBudget, newDuration).equals("")){
					if(!activity.selectedElement.isRoot()){
						activity.selectedElement.setName(newName);
					}
					activity.selectedElement.setBudget(newBudget);
					activity.selectedElement.setDuration(newDuration);
					activity.selectedElement.setResponsibleStaff(newManager);
					Toast saveMessage = new Toast(context);
					int displayTime = Toast.LENGTH_SHORT;
					CharSequence message = "Changes Saved";
					updateActivity();
					saveMessage.makeText(context, message, displayTime).show();
				}
				else{
					Toast saveMessage = new Toast(context);
					int displayTime = Toast.LENGTH_LONG;
					CharSequence message = "You need to fix the following errors...\n" 
								+ activity.selectedElement.validateFormInputs(newName, newBudget, newDuration);
					for (int i=0; i < 2; i++)
					{
					    saveMessage.makeText(activity, message, Toast.LENGTH_LONG).show();
					}
				}
			}
		});
		cancel.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ViewElementActivity activity = (ViewElementActivity) v.getContext();
				activity.updateTextViews();				
			}
		});
		
		delete.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				final ViewElementActivity activity = (ViewElementActivity) v.getContext();
				new AlertDialog.Builder(activity)
		        .setIcon(android.R.drawable.ic_dialog_alert)
		        .setTitle("Delete Element")
		        .setMessage("Are you sure you want to delete this element?\n(This will delete the other sibling if there are only two elements)")
		        .setPositiveButton("Yes", new DialogInterface.OnClickListener()
		        {
		        	@Override
		        	public void onClick(DialogInterface dialog, int which) {
		        		activity.selectedElement.getParent().removeChild(selectedElement.getIndex());
		        		Intent intent = new Intent(activity, GUImain.class);
		        		ProjectTree tree = activity.tree;
		        		intent.putExtra("com.example.pocket_wbs.TREE_TO_OVERVIEW", tree);
		        		startActivity(intent);		    
		        		finish();
		        	}

		        })
		        .setNegativeButton("No", null)
		        .show();
			}
		});
	}
	
	private void updateDeleteButton(){
		Button delete = (Button) findViewById(R.id.deleteElementButton);
		if(this.selectedElement.isRoot()){
			delete.setVisibility(View.INVISIBLE);
		}
		else{
			delete.setVisibility(View.VISIBLE);
		}
	}
}
