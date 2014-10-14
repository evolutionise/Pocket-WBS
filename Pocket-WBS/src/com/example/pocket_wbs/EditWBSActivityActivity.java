package com.example.pocket_wbs;

import com.example.pocket_wbs.model.ProjectTree;
import com.example.pocket_wbs.model.WBSActivity;
import com.example.pocket_wbs.model.WBSElement;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class EditWBSActivityActivity extends Activity{
	
	private WBSActivity activity;
	private EditText description;
	private ProjectTree tree;

	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		tree = (ProjectTree) getIntent().getSerializableExtra("com.example.pocket_wbs.model.PROJECT_TREE");
		String elementKey = getIntent().getStringExtra("com.example.pocket_wbs.ELEMENT_KEY");
		int position = getIntent().getIntExtra("com.example.pocket_wbs.model.ACTIVITY_INDEX", 0);
		
		WBSElement element = tree.getProjectElements().get(elementKey);
		activity = element.getActivityByIndex(position);		
		
		setContentView(R.layout.edit_wbsactivity_activity);
		description = (EditText) findViewById(R.id.activity_description_edit_text);
		description.setText(activity.getDescription());
    }
	
	public void saveActivity(View view){
		String newDescription = description.getText().toString();
		activity.setDescription(newDescription);
		onBackPressed();
	}
	
	public void cancelActivity(View view){
		onBackPressed();
	}
	
	public void deleteActivity(View view){
		WBSElement element = activity.getContainingElement();
		element.deleteActivity(activity);
		onBackPressed();
	}
	
	@Override
	public void onBackPressed(){
		Intent intent = new Intent(this, ViewElementActivity.class);
		WBSElement element = activity.getContainingElement();		
		intent.putExtra("com.example.pocket_wbs.ELEMENT_KEY", element.getElementKey());
		intent.putExtra("com.example.pocket_wbs.PROJECT_TREE", tree);
		startActivity(intent);
		finish();
	}
}
