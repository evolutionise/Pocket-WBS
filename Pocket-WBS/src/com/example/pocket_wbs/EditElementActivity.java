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
import android.widget.EditText;
import android.widget.TextView;

public class EditElementActivity extends ActionBarActivity {

	public ProjectTree tree;
	public WBSElement selectedElement;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		this.tree = (ProjectTree) intent.getSerializableExtra("com.example.pocket_wbs.TREE");
		String elementKey = intent.getStringExtra("com.example.pocket_wbs.ELEMENT");
		this.selectedElement = this.tree.getProjectElements().get(elementKey); 
		setContentView(R.layout.activity_edit_element);
		this.setUpEventListeners();
		this.updateTextViews();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.edit_element, menu);
		return true;
	}
	
	public void setUpEventListeners(){
		Button confirm = (Button) findViewById(R.id.editConfimationButton);
		confirm.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				EditElementActivity activity = (EditElementActivity) v.getContext();
				EditText nameField = (EditText) findViewById(R.id.editElementNameField);
				String name = nameField.getText().toString();
				activity.selectedElement.setName(name);
				activity.updateTextViews();
				activity.confirmChanges(v);
			}
		});
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
	
	public void updateTextViews(){
		TextView elementKey = (TextView) findViewById(R.id.elementIDEditPage);
		TextView elementName = (TextView) findViewById(R.id.elementNameEditPage);
		elementKey.setText("Element ID: " + selectedElement.getElementKey());
		elementName.setText("Element Name: " + selectedElement.getName());
	}
	
	public void confirmChanges(View v){
		Intent intent = new Intent(this, ViewElementActivity.class);
		ProjectTree tree = this.tree;
		String elementKey = this.selectedElement.getElementKey();
		intent.putExtra("com.example.pocket_wbs.TREE", tree);
		intent.putExtra("com.example.pocket_wbs.KEY", elementKey);
		startActivity(intent);
	}
}
