package com.example.pocket_wbs.gui;




import java.util.List;

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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View.OnClickListener;

public class WBSActivityArrayAdapter extends ArrayAdapter<String>{
	  private final Context context;
	  private final WBSElement parent;
	  private final ProjectTree tree;
	  private List<String> activityList;
	  
	  public WBSActivityArrayAdapter(Context context,int layout, int layout2, WBSElement parent,ProjectTree tree) {
		super(context, layout, layout2, parent.getActivitiesAsStringArray());
	    this.context = context;
	    this.parent = parent;
	    this.tree = tree;
	    this.activityList = parent.getActivitiesAsStringArray();
	    add("Add new Activity");
	    
	  }
	  
	  private void addWBSActivity(int position){
		  if(!parent.hasChildren()){
			  WBSActivity activity = parent.addActivity("Activity");
			  insert(activity.getDescription(), getCount() - 1);
			  this.activityList = parent.getActivitiesAsStringArray();
			  
			  ViewElementActivity currentContext = (ViewElementActivity) context;
			  currentContext.saveFieldChanges();
			  Intent intent = new Intent(context, EditWBSActivityActivity.class);
			  intent.putExtra("com.example.pocket_wbs.model.PROJECT_TREE", tree);		  
			  intent.putExtra("com.example.pocket_wbs.ELEMENT_KEY", parent.getElementKey());
			  intent.putExtra("com.example.pocket_wbs.model.ACTIVITY_INDEX", position);
			  context.startActivity(intent);
		  }
	  }
	  
	  private void editWBSActivity(int position){
		  WBSActivity activity = parent.getActivityByIndex(position);
		  ViewElementActivity currentContext = (ViewElementActivity) context;
		  currentContext.saveFieldChanges();
		  Intent intent = new Intent(context, EditWBSActivityActivity.class);
		  intent.putExtra("com.example.pocket_wbs.model.PROJECT_TREE", tree);		  
		  intent.putExtra("com.example.pocket_wbs.ELEMENT_KEY", parent.getElementKey());
		  intent.putExtra("com.example.pocket_wbs.model.ACTIVITY_INDEX", position);
		  context.startActivity(intent);
	  }
	  
	  private void removeWBSActivity(int position){
		  super.remove(super.getItem(position));
		  parent.deleteActivityByIndex(position);
		  this.activityList = parent.getActivitiesAsStringArray();
		  notifyDataSetChanged();
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
	  public View getView (final int position, View convertView, ViewGroup parent){
		  convertView = super.getView(position, convertView, parent);
		  //If we are building the view for the last element in the list, we add an onclick listener so it can be used to add new WBSActivities
		  
		  
		    LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		    View rowView = inflater.inflate(R.layout.row_item_layout, parent, false);
	        TextView textView = (TextView) rowView.findViewById(R.id.activityName);
	        Button btnDelete = (Button) rowView.findViewById(R.id.deleteButton);
	        
	        
		  if(position + 1 == getCount()){	
			  
			  textView.setTextSize(20);
			  textView.setText("+ New Activity");
			  btnDelete.setEnabled(false);
			  btnDelete.setVisibility(View.GONE);
			  rowView.setOnClickListener(new NewWBSActivityOnClickListener(this, position));
		  } else {
			  textView.setText("" + activityList.get(position));
			  rowView.setOnClickListener(new EditWBSActivityOnClickListener(this, position));
			  btnDelete.setOnClickListener(new OnClickListener() 
		        {		
		            @Override
		            public void onClick(View v)
		            {
		            	confirmDeleteActivity(v.getContext(), position);
		            }
		        });
		  }
		  
		  return rowView;
	  }
	  
	  class NewWBSActivityOnClickListener implements OnClickListener{
		  
		  private WBSActivityArrayAdapter adapter;
		  int position;
		  
		  private NewWBSActivityOnClickListener(WBSActivityArrayAdapter adapter, int position){
			  this.adapter = adapter;
			  this.position = position;
		  }
				
		  @Override
		  public void onClick(View v) {
			  adapter.addWBSActivity(position);			
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
	  
	    public void confirmDeleteActivity(final Context context, final int position){
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
	    
	    public void toastMessage(String message){
	    	Toast.makeText(this.getContext(), message, Toast.LENGTH_LONG).show();
	    }
}



