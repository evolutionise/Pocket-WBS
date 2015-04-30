package com.example.pocket_wbs;

import java.util.ArrayList;
import java.util.List;

import com.example.pocket_wbs.model.ProjectTree;
import com.example.pocket_wbs.model.WBSFileManager;

import android.support.v7.app.ActionBarActivity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ListView;
import android.widget.Toast;

public class LoadWBSActivity extends ActionBarActivity {

	public ArrayAdapter adapter;
	public boolean dialogOpen = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_load_wbs);
		updateTextDisplays();
		updateListDisplay();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.load_wb, menu);
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
	
	public void updateTextDisplays() {
		Context context = getApplicationContext();
		TextView loadText = (TextView) findViewById(R.id.loadText);
		WBSFileManager fileManager = new WBSFileManager();
		if(fileManager.listFiles(context).length == 0) {
			loadText.setText("No files to be displayed");
		}
		else {
			loadText.setText("Please Select A File");
		}
	}
	
	public void updateListDisplay() {
		ListView list = (ListView) findViewById(R.id.fileSelectList);
		WBSFileManager fileManager = new WBSFileManager();
		String[] itemsList = fileManager.listFiles(this.getApplicationContext());
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, itemsList);
		list.setAdapter(adapter);
		setListEventHandlers();
	}
	
	public void setListEventHandlers(){
		final ListView list = (ListView) findViewById(R.id.fileSelectList);
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				if(!dialogOpen){
					int itemIndex = position;
					String fileName = (String) list.getItemAtPosition(itemIndex);
					loadExistingWBS(fileName);
				}
			}
		});
		list.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				int itemIndex = position;
				String fileName = (String) list.getItemAtPosition(itemIndex);
				showDeleteConfirmation(fileName);
				return false;
			}
			
		});
	}
	
	public void loadExistingWBS(String projectName){ 
		Context context = getApplicationContext();
		WBSFileManager fileManager = new WBSFileManager();
		ProjectTree tree = fileManager.loadTreeFromFile(context, projectName);
		Intent intent = new Intent(this, GUImain.class);
		intent.putExtra("com.example.pocket_wbs.NEW_TREE", tree);
		startActivity(intent);
	}
	
	public void deleteExistingWBS(String projectName){
		Toast deleteMessage = new Toast(this);
		int displayTime = Toast.LENGTH_SHORT;
		String message = "File Deleted";
		Context context = getApplicationContext();
		WBSFileManager fileManager = new WBSFileManager();
		fileManager.deleteFile(context, projectName);
		updateListDisplay();
		updateTextDisplays();
		deleteMessage.makeText(context, message, displayTime);
	}
	
	public void showDeleteConfirmation(String fileName){
		final String file = fileName;
		AlertDialog.Builder deleteConfirmation = new AlertDialog.Builder(LoadWBSActivity.this);
        deleteConfirmation.setTitle("");
        deleteConfirmation.setMessage("Are you sure you want to delete " + file);
        deleteConfirmation.setIcon(R.drawable.pocketwbsicon2);
        deleteConfirmation.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int which) {
                    	deleteExistingWBS(file);
                    	dialog.cancel();
                    	dialogOpen = false;
                    }
                });
        deleteConfirmation.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        dialogOpen = false;
                    }
                });
        dialogOpen = true;
        deleteConfirmation.show();
	}
}
