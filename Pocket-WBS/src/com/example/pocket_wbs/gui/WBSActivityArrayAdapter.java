package com.example.pocket_wbs.gui;




import com.example.pocket_wbs.EditWBSActivityActivity;
import com.example.pocket_wbs.R;
import com.example.pocket_wbs.ViewElementActivity;
import com.example.pocket_wbs.model.ProjectTree;
import com.example.pocket_wbs.model.WBSActivity;
import com.example.pocket_wbs.model.WBSElement;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.widget.ArrayAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.View.OnClickListener;

public class WBSActivityArrayAdapter extends ArrayAdapter<String>{
	  private final Context context;
	  private final WBSElement parent;
	  private final ProjectTree tree;

	  public WBSActivityArrayAdapter(Context context, WBSElement parent, ProjectTree tree) {
		super(context, android.R.layout.simple_list_item_1, parent.getActivitiesAsStringArray());
	    this.context = context;
	    this.parent = parent;
	    this.tree = tree;
	    add("Tap to add new Activity");
	  }
	  
	  private void addWBSActivity(){
		  if(!parent.hasChildren()){
			  WBSActivity activity = parent.addActivity("Activity " + getCount());
			  insert(activity.getDescription(), getCount() - 1);
		  }
	  }
	  
	  private void editWBSActivity(int position){
		  WBSActivity activity = parent.getActivityByIndex(position);
		  Intent intent = new Intent(context, EditWBSActivityActivity.class);
		  intent.putExtra("com.example.pocket_wbs.model.PROJECT_TREE", tree);		  
		  intent.putExtra("com.example.pocket_wbs.ELEMENT_KEY", parent.getElementKey());
		  intent.putExtra("com.example.pocket_wbs.model.ACTIVITY_INDEX", position);
		  context.startActivity(intent);
	  }
	  
	  private void removeWBSActivity(int position){
		  parent.deleteActivityByIndex(position);
	  }
	  
	  public void confirmDeleteActivity(final int position){
		  AlertDialog.Builder exitDialog = new AlertDialog.Builder(context);
		  exitDialog.setTitle("Delete Activity");
		  exitDialog.setMessage("Are you sure you want to delete this activity?");
		  exitDialog.setIcon(R.drawable.pocketwbsicon2);
	      exitDialog.setPositiveButton("Yes",
	              new DialogInterface.OnClickListener() {
	                  public void onClick(DialogInterface dialog,int which) {   
	                	removeWBSActivity(position);
	                   	dialog.cancel();
	                  }
	              });
	        exitDialog.setNegativeButton("No",
	                new DialogInterface.OnClickListener() {
	                    public void onClick(DialogInterface dialog, int which) {
	                        dialog.cancel();
	                    }
	                });
	        exitDialog.show();
    }
	  
	  @Override
	  public View getView (int position, View convertView, ViewGroup parent){
		  convertView = super.getView(position, convertView, parent);
		  //If we are building the view for the last element in the list, we add an onclick listener so it can be used to add new WBSActivities
		  if(position + 1 == getCount()){	
			  convertView.setOnClickListener(new NewWBSActivityOnClickListener(this));
		  } else {
			  convertView.setOnClickListener(new EditWBSActivityOnClickListener(this, position));
		  }
		  
		  return convertView;
	  }
	  
	  class NewWBSActivityOnClickListener implements OnClickListener{
		  
		  private WBSActivityArrayAdapter adapter;
		  
		  private NewWBSActivityOnClickListener(WBSActivityArrayAdapter adapter){
			  this.adapter = adapter;
		  }
				
		  @Override
		  public void onClick(View v) {
			  adapter.addWBSActivity();			
		  }
		  
	  }
	  
	  class EditWBSActivityOnClickListener implements OnClickListener{
		  private WBSActivityArrayAdapter adapter;
		  int position;
		  
		  private EditWBSActivityOnClickListener(WBSActivityArrayAdapter adapter, int position){
			  this.adapter = adapter;
			  this.position = position;
		  }
		  
		  @Override
		  public void onClick(View v){
			  adapter.editWBSActivity(position);
		  }
	  }
}
