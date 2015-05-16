package com.example.pocket_wbs;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import com.example.pocket_wbs.gui.WBSActivityArrayAdapter;
import com.example.pocket_wbs.model.ProjectTree;
import com.example.pocket_wbs.model.WBSActivity;
import com.example.pocket_wbs.model.WBSAttributes;
import com.example.pocket_wbs.model.WBSElement;
import com.example.pocket_wbs.model.WBSFileManager;

import android.support.v7.app.ActionBarActivity;
import android.text.InputFilter;
import android.util.Log;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

public class ViewElementActivity extends ActionBarActivity {

	public ProjectTree tree;
	public WBSElement selectedElement;
	private LinearLayout customAttLayout;
	ListView activitiesList;
	ScrollView scrollView, parentScroll, childScroll;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = this.getIntent();
		//this.setUpTest();
		this.getSupportActionBar().hide();
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
		
		//Create ListView to put WBSActivities into
		
		activitiesList = (ListView) findViewById(R.id.activitiesList);

		
		WBSActivityArrayAdapter adapter = new WBSActivityArrayAdapter(this, selectedElement, tree);
		activitiesList.setAdapter(adapter);
		
		//Find layout for adding custom attributes
				customAttLayout = (LinearLayout) findViewById(R.id.attributesLayout);
				
		//GET custom attributes for Element level
				displayCustomAttributes();
				

	}
	
	@Override
	public void onBackPressed(){
		saveFieldChanges();
		moveToViewElementOverview();
	};
	
	private void saveFieldChanges(){
		
		EditText elementName = (EditText) findViewById(R.id.editElementName);
		EditText budget = (EditText) findViewById(R.id.budgetEditText);
		EditText duration = (EditText) findViewById(R.id.durationEditText);
		EditText manager = (EditText) findViewById(R.id.managerEditText);
		
		String newName = elementName.getText().toString();
		double newBudget = Double.parseDouble(budget.getText().toString());
		int newWorkHours = Integer.parseInt(duration.getText().toString());
		String newManager = manager.getText().toString();
		
		if(!this.selectedElement.isRoot()){
			if(!newName.equals("")){
				this.selectedElement.setName(newName);
			}
		}
		else{
			if(!newName.equals("")){
				this.tree.setProjectName(newName);
			}
		}
		if(newBudget >= 0){
			this.selectedElement.setBudget(newBudget);
		}
		else{
			this.selectedElement.setBudget(0);
		}
		if(newWorkHours >= 0){
			this.selectedElement.setWorkHours(newWorkHours);
		}
		else{
			this.selectedElement.setWorkHours(0);
		}
		this.selectedElement.setResponsibleStaff(newManager);
	}
	
	private void moveToViewElementOverview(){
		Intent intent = new Intent(this, ViewElementOverview.class);
		ProjectTree tree = this.tree;
		String key = this.selectedElement.getElementKey();
		intent.putExtra("com.example.pocket_wbs.TREE", tree);
		intent.putExtra("com.example.pocket_wbs.KEY", key);
		startActivity(intent);
		finish();
	}

	/**@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu, menu);
		final Context context = this;
		final ProjectTree tree = this.tree;
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
	}*/

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
		Button exitButton = (Button) findViewById(R.id.exitButtonEditWBS);
		exitButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				moveToViewElementOverview();			
			}
		});
	}
	
	/*
	 * Method that adds a custom attribute to its collection when button is pressed
	 */
	public LinearLayout addCustomAttribute(final String customAttributeName, String customAttributeValue)
	{
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
		LinearLayout newLayout = new LinearLayout(this);
		newLayout.setOrientation(LinearLayout.HORIZONTAL);
		newLayout.setPadding(0, 5, 0, 5);
		Log.d("LinearLayout", "addCustomAttribute : " + customAttributeName);
		customAttLayout.addView(newLayout);
		
		final TextView attributeNameLabel = new TextView(this);
		attributeNameLabel.setLayoutParams(params);
		attributeNameLabel.setText(customAttributeName + ": ");
		newLayout.addView(attributeNameLabel);
		  
		final EditText attributeValueField = new EditText(this);
		attributeValueField.setLayoutParams(params2);
		attributeValueField.setBackgroundColor(Color.parseColor("#FFFFFF"));
		attributeValueField.setEms(10);
		attributeValueField.setTextColor(Color.parseColor("#585858"));
		attributeValueField.setIncludeFontPadding(false);
		attributeValueField.setTextSize(15);
		attributeValueField.setTypeface(Typeface.DEFAULT);
		attributeValueField.setText(customAttributeValue);
		attributeValueField.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (event.getAction() == KeyEvent.ACTION_DOWN) {
					String attributeValue = attributeValueField.getText().toString();
					selectedElement.getAttributes().put(customAttributeName, attributeValue);
				}
				return false;
			}
		});
		attributeValueField.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (!hasFocus) {
					String attributeValue = attributeValueField.getText().toString();
					selectedElement.getAttributes().put(customAttributeName, attributeValue);
				}
			}
		});
		newLayout.addView(attributeValueField);
		
		return newLayout;
	}
	
	/*
	 * Method that displays all the current custom attributes in the collection 
	 */
	public void displayCustomAttributes()
	{
		Map<String, String> attributeMap = selectedElement.getAttributes().getAttributesAndNames();
		
		if (attributeMap != null) {
			for (Map.Entry<String, String> attribute : attributeMap.entrySet()) {
				addCustomAttribute(attribute.getKey(), attribute.getValue());
			}
		}
	}
	
	public void nameCustomAlert(View view)
	{	

		AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        // Setting Dialog Title
        alertDialog.setTitle("New Custom Attribute");

        // Setting Dialog Message
        alertDialog.setMessage("Enter name");
        final EditText input = new EditText(this);
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
                    	String customAttributeName = input.getText().toString();
                    	if(customAttributeName.equals("")){
                    		toastMessage("Please Enter a Valid Name");
                    	} else if (!selectedElement.getAttributes().addName(customAttributeName)) {
                    		// If statement checks if attribute name exists and adds it if it doesn't
                    		toastMessage("Attribute Name '" + customAttributeName + "' Already Exists");
                    	} else {
                    		LinearLayout newLayout = addCustomAttribute(customAttributeName, "");
                    		newLayout.requestFocus();
                    	}
                    }
                });
        // Setting Negative "NO" Button
        alertDialog.setNegativeButton("CANCEL",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        // closed

        // Showing Alert Message
        alertDialog.show();

	}
	
    public void toastMessage(String message){
    	Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
    
    
}
