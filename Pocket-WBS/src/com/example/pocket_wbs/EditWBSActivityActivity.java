package com.example.pocket_wbs;

import java.text.DecimalFormat;
import java.util.Map;

import com.example.pocket_wbs.model.ProjectTree;
import com.example.pocket_wbs.model.WBSActivity;
import com.example.pocket_wbs.model.WBSElement;
import com.example.pocket_wbs.model.WBSFileManager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;

public class EditWBSActivityActivity extends ActionBarActivity{
	
	private WBSActivity activity;
	private EditText description;
	private ProjectTree tree;
	private LinearLayout customAttLayout;

	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		tree = (ProjectTree) getIntent().getSerializableExtra("com.example.pocket_wbs.model.PROJECT_TREE");
		String elementKey = getIntent().getStringExtra("com.example.pocket_wbs.ELEMENT_KEY");
		int position = getIntent().getIntExtra("com.example.pocket_wbs.model.ACTIVITY_INDEX", 0);
		
		WBSElement element = tree.getProjectElements().get(elementKey);
		activity = element.getActivityByIndex(position);		
		
		setContentView(R.layout.edit_wbsactivity_activity);
		this.getSupportActionBar().hide();
		description = (EditText) findViewById(R.id.activity_description_edit_text);
		//description.setText(activity.getDescription());
		updateTextViews();
		
		
		//Find layout for adding custom attributes
			customAttLayout = (LinearLayout) findViewById(R.id.attributesLayoutActivity);
			
			
		//GET custom attributes for Activity Level
			displayCustomAttributes();
    }
	
	public void cancelActivity(){
		moveToViewElementActivity();
	}
	
	@Override
	public void onBackPressed(){
		saveActivityFields();
		moveToViewElementActivity();
	}
	
	private void saveActivityFields(){
		
		setEmptyNumericFieldsToDefault();
		String newDescription = description.getText().toString();
		
		EditText budget = (EditText) findViewById(R.id.activityBudgetEdit);
		EditText actualCost = (EditText) findViewById(R.id.activityCostEdit);
		EditText sDay = (EditText) findViewById(R.id.editStartDay);
		String newStartDay = sDay.getText().toString();
		EditText sMonth = (EditText) findViewById(R.id.editStartMonth);
		String newStartMonth = sMonth.getText().toString();
		EditText sYear = (EditText) findViewById(R.id.editStartYear);
		String newStartYear = sYear.getText().toString();
		EditText fDay = (EditText) findViewById(R.id.editFinishDay);
		String newFinishDay = fDay.getText().toString();
		EditText fMonth = (EditText) findViewById(R.id.editFinishMonth);
		String newFinishMonth = fMonth.getText().toString();
		EditText fYear = (EditText) findViewById(R.id.editFinishYear);
		String newFinishYear = fYear.getText().toString();
		
		int sDayNum = Integer.parseInt(newStartDay);
		int fDayNum = Integer.parseInt(newFinishDay);
		int sMonthNum = Integer.parseInt(newStartMonth);
		int fMonthNum = Integer.parseInt(newFinishMonth);
		int sYearNum = Integer.parseInt(newStartYear);
		int fYearNum = Integer.parseInt(newFinishYear);
		
		
		double newBudget = Double.parseDouble(budget.getText().toString());
		double newCost = Double.parseDouble(actualCost.getText().toString());
		String startDate =  sDay + "/" + sMonth + "/" + sYear;
		String FinishDate = fDay + "/" + fMonth + "/" + fYear;
		
		String validatingString = activity.validateStartDate(sDayNum, sMonthNum, sYearNum);
		validatingString += activity.validateFinishDate(fDayNum, fMonthNum, fYearNum);
		validatingString += activity.validateTimeFrame(sDayNum, sMonthNum, sYearNum, fDayNum, fMonthNum, fYearNum);
		
		activity.setBudget(newBudget);
		activity.setActualCost(newCost);
		if(!newDescription.equals("")){
			activity.setDescription(newDescription);
		}
		if(validatingString.equals("")){
			activity.setStartDate(Integer.parseInt(newStartDay), Integer.parseInt(newStartMonth), Integer.parseInt(newStartYear));
			activity.setFinishDate(Integer.parseInt(newFinishDay), Integer.parseInt(newFinishMonth), Integer.parseInt(newFinishYear));
		}
		else{
			Toast saveMessage = new Toast(this);
			int displayTime = Toast.LENGTH_LONG*2;
			CharSequence message = "Date's not saved due to the following errors...\n" + validatingString;
			saveMessage.makeText(this, message, displayTime).show();
		}
	}
	
	private void moveToViewElementActivity(){
		Intent intent = new Intent(this, ViewElementActivity.class);
		WBSElement element = activity.getContainingElement();		
		intent.putExtra("com.example.pocket_wbs.ELEMENT_KEY", element.getElementKey());
		intent.putExtra("com.example.pocket_wbs.PROJECT_TREE", tree);
		startActivity(intent);
		finish();
	}
	
	public void updateTextViews(){
		DecimalFormat df = new DecimalFormat("0.00");
		
		EditText budget = (EditText) findViewById(R.id.activityBudgetEdit);
		EditText actualCost = (EditText) findViewById(R.id.activityCostEdit);
		EditText sDay = (EditText) findViewById(R.id.editStartDay);
		EditText sMonth = (EditText) findViewById(R.id.editStartMonth);
		EditText sYear = (EditText) findViewById(R.id.editStartYear);
		EditText fDay = (EditText) findViewById(R.id.editFinishDay);
		EditText fMonth = (EditText) findViewById(R.id.editFinishMonth);
		EditText fYear = (EditText) findViewById(R.id.editFinishYear);
		
		description.setText(activity.getDescription());
		budget.setText(df.format(activity.getBudget()));
		actualCost.setText(df.format((activity.getActualCost())));
		sDay.setText(Integer.toString(activity.getStartDays()));
		sMonth.setText(Integer.toString(activity.getStartMonths()));
		sYear.setText(Integer.toString(activity.getStartYears()));
		fDay.setText(Integer.toString(activity.getFinishDays()));
		fMonth.setText(Integer.toString(activity.getFinishMonths()));
		fYear.setText(Integer.toString(activity.getFinishYears()));
	}
	
	public void setEmptyNumericFieldsToDefault(){
		EditText budget = (EditText) findViewById(R.id.activityBudgetEdit);
		EditText actualCost = (EditText) findViewById(R.id.activityCostEdit);
		EditText sDay = (EditText) findViewById(R.id.editStartDay);
		EditText sMonth = (EditText) findViewById(R.id.editStartMonth);
		EditText sYear = (EditText) findViewById(R.id.editStartYear);
		EditText fDay = (EditText) findViewById(R.id.editFinishDay);
		EditText fMonth = (EditText) findViewById(R.id.editFinishMonth);
		EditText fYear = (EditText) findViewById(R.id.editFinishYear);
		EditText[] textFields = new EditText[]{budget, actualCost, sDay, sMonth, sYear, fDay, fMonth, fYear};
		for(EditText field : textFields){
			if(field.getText().toString().equals("")){
				field.setText("0");
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
                    	} else if (!activity.getAttributes().addName(customAttributeName)) {
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
		attributeValueField.setTextSize(20);
		attributeValueField.setTypeface(Typeface.DEFAULT);
		attributeValueField.setText(customAttributeValue);
		attributeValueField.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (event.getAction() == KeyEvent.ACTION_DOWN) {
					String attributeValue = attributeValueField.getText().toString();
					activity.getAttributes().put(customAttributeName, attributeValue);
				}
				return false;
			}
		});
		attributeValueField.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (!hasFocus) {
					String attributeValue = attributeValueField.getText().toString();
					activity.getAttributes().put(customAttributeName, attributeValue);
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
		Map<String, String> attributeMap = activity.getAttributes().getAttributesAndNames();
		
		if (attributeMap != null) {
			for (Map.Entry<String, String> attribute : attributeMap.entrySet()) {
				addCustomAttribute(attribute.getKey(), attribute.getValue());
			}
		}
	}
	
    public void toastMessage(String message){
    	Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
