package com.example.pocket_wbs.gui;



import com.example.pocket_wbs.model.WBSActivity;
import com.example.pocket_wbs.model.WBSElement;

import android.content.Context;
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

	  public WBSActivityArrayAdapter(Context context, WBSElement parent) {
		super(context, android.R.layout.simple_list_item_1, parent.getActivitiesAsStringArray());
	    this.context = context;
	    this.parent = parent;
	    add("Tap to add new Activity");
	  }
	  
	  private void addWBSActivity(){
		  WBSActivity activity = parent.addActivity("Activity " + getCount());
		  insert(activity.getDescription(), getCount() - 1);
	  }
	  
	  @Override
	  public View getView (int position, View convertView, ViewGroup parent){
		  convertView = super.getView(position, convertView, parent);
		  //If we are building the view for the last element in the list, we add an onclick listener so it can be used to add new WBSActivities
		  if(position + 1 == getCount()){	
			  convertView.setOnClickListener(new NewWBSActivityOnClickListener(this));
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
}
