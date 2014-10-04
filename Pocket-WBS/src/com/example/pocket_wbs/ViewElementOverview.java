package com.example.pocket_wbs;

import com.example.pocket_wbs.model.ProjectTree;
import com.example.pocket_wbs.model.WBSElement;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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
	    this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_view_element_overview);
		
		updateTextViews();
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
