package com.example.pocket_wbs;

import com.example.pocket_wbs.model.ProjectTree;
import com.example.pocket_wbs.model.WBSActivity;
import com.example.pocket_wbs.model.WBSElement;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

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
		//description.setText(activity.getDescription());
		updateTextViews();
    }
	
	public void saveActivity(View view){
		
		setEmptyNumericFieldsToDefault();
		String newDescription = description.getText().toString();
		
		Context context = getApplicationContext();
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
		
		double newBudget = Double.parseDouble(budget.getText().toString());
		double newCost = Double.parseDouble(actualCost.getText().toString());
		String startDate =  sDay + "/" + sMonth + "/" + sYear;
		String FinishDate = fDay + "/" + fMonth + "/" + fYear;
		
		String validatingString = activity.validateFormInputs(newDescription, newBudget, newCost);
		validatingString += activity.validateStartDate(Integer.parseInt(newStartDay), Integer.parseInt(newStartMonth), Integer.parseInt(newStartYear));
		validatingString += activity.validateFinishDate(Integer.parseInt(newFinishDay), Integer.parseInt(newFinishMonth), Integer.parseInt(newFinishYear));
		
		if(validatingString == ""){
			activity.setDescription(newDescription);
			activity.setBudget(newBudget);
			activity.setActualCost(newCost);
			activity.setStartDate(Integer.parseInt(newStartDay), Integer.parseInt(newStartMonth), Integer.parseInt(newStartYear));
			activity.setFinishDate(Integer.parseInt(newFinishDay), Integer.parseInt(newFinishMonth), Integer.parseInt(newFinishYear));
			Toast saveMessage = new Toast(context);
			int displayTime = Toast.LENGTH_SHORT;
			CharSequence message = "Changes Saved";
			saveMessage.makeText(context, message, displayTime).show();
			//updateTextViews();
			//onBackPressed();
		}
		else{
			Toast saveMessage = new Toast(context);
			int displayTime = Toast.LENGTH_SHORT;
			CharSequence message = "You need to fix the following errors...\n" + validatingString;
			for (int i=0; i < 2; i++)
			{
			    saveMessage.makeText(view.getContext(), message, Toast.LENGTH_LONG).show();
			}
		}
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
	
	public void updateTextViews(){
		EditText budget = (EditText) findViewById(R.id.activityBudgetEdit);
		EditText actualCost = (EditText) findViewById(R.id.activityCostEdit);
		EditText sDay = (EditText) findViewById(R.id.editStartDay);
		EditText sMonth = (EditText) findViewById(R.id.editStartMonth);
		EditText sYear = (EditText) findViewById(R.id.editStartYear);
		EditText fDay = (EditText) findViewById(R.id.editFinishDay);
		EditText fMonth = (EditText) findViewById(R.id.editFinishMonth);
		EditText fYear = (EditText) findViewById(R.id.editFinishYear);
		
		description.setText(activity.getDescription());
		budget.setText(Double.toString(activity.getBudget()));
		actualCost.setText(Double.toString(activity.getActualCost()));
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
}
