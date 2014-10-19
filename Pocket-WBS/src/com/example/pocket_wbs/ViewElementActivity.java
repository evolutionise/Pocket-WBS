package com.example.pocket_wbs;

import com.example.pocket_wbs.gui.WBSActivityArrayAdapter;
import com.example.pocket_wbs.model.ProjectTree;
import com.example.pocket_wbs.model.WBSActivity;
import com.example.pocket_wbs.model.WBSElement;

import android.support.v7.app.ActionBarActivity;
import android.content.Context;
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
	}
	
	/**
	 * This method updates the text views based on the currently selected activity
	 */
	private void updateTextViews(){
		TextView elementKey = (TextView) findViewById(R.id.elementKeyView);
		//TextView elementName = (TextView) findViewById(R.id.elementNameView);
		EditText editElementName = (EditText) findViewById(R.id.editElementName);
		elementKey.setText("Element " + selectedElement.getElementKey());
		//elementName.setText("Element Name: " + "\n" + selectedElement.getName());
		editElementName.setText(selectedElement.getName());
	}
	
	private void setUpEventListeners(){
		Button save = (Button) this.findViewById(R.id.elementSaveButton);
		Button cancel = (Button) this.findViewById(R.id.elementCancelButton);
		save.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ViewElementActivity activity = (ViewElementActivity) v.getContext();
				EditText elementName = (EditText) findViewById(R.id.editElementName);
				if(!activity.selectedElement.isRoot()){
					activity.selectedElement.setName(elementName.getText().toString());
					Context context = getApplicationContext();
					Toast saveMessage = new Toast(context);
					int displayTime = Toast.LENGTH_SHORT;
					CharSequence message = "Changes Saved";
					saveMessage.makeText(context, message, displayTime).show();
				}
			}
		});
		cancel.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ViewElementActivity activity = (ViewElementActivity) v.getContext();
				EditText elementName = (EditText) findViewById(R.id.editElementName);
				elementName.setText(activity.selectedElement.getName());
			}
		});
	}
	
}
