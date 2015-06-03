package com.example.pocket_wbs;

import java.io.File;

import com.example.pocket_wbs.model.FileBrowser;
import com.example.pocket_wbs.model.ProjectTree;
import com.example.pocket_wbs.model.WBSFileManager;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.TextView;

public class FileBrowserActivity extends ActionBarActivity {

	public ArrayAdapter adapter;
	public FileBrowser browser = new FileBrowser();
	public WBSFileManager fileManager = new WBSFileManager();
	public File selectedFile = browser.getCurrentFile();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_file_browser);
		updateActivity();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu, menu);
		updateActivity();
		return true;
	}
	
	/**
	 * The main GUI update method of the activity
	 */
	public void updateActivity(){
		updateTextDisplay();
		updateListDisplay();
		updateBackButton();
		updateOpenButton();
	}
	
	/**
	 * This method handles the updates of the text elements in the
	 * activity.
	 */
	private void updateTextDisplay(){
		TextView selectedFileText = (TextView) findViewById(R.id.selectedFileText);
		if(selectedFile.equals(browser.getCurrentFile())){
			selectedFileText.setText("No file selected");
		}
		else{
			selectedFileText.setText("Currently Selected: " + selectedFile.getName());
		}
	}
	
	/**
	 * This method handles the update of the file browser list in the
	 * activity
	 */
	private void updateListDisplay() {
		ListView list = (ListView) findViewById(R.id.fileBrowserList);
		String[] itemsList = browser.listFiles();
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_activated_1, itemsList);
		list.setAdapter(adapter);
		setListEventHandlers();
	}
	
	/**
	 * This method sets the event handler for the file browser
	 * list.
	 */
	private void setListEventHandlers(){
		final ListView list = (ListView) findViewById(R.id.fileBrowserList);
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				File clickedFile = new File(browser.getCurrentFile(), (String) list.getItemAtPosition(position));
				selectedFile = clickedFile;
				updateActivity();
			}
		});
	}
	
	/**
	 * This method updates the open buttons display and event handler
	 */
	public void updateOpenButton(){
		Button openButton = (Button) findViewById(R.id.selectFileButton);
		if(selectedFile.equals(browser.getCurrentFile())){
			openButton.setTextColor(Color.parseColor("#777777"));
			openButton.setClickable(false);
		}
		else if(selectedFile.isDirectory()){
			openButton.setTextColor(Color.parseColor("#777777"));
			openButton.setClickable(false);
			browser.changeCurrentFile(selectedFile);
			updateActivity();
		}
		else if(selectedFile.isFile()){
			openButton.setTextColor(Color.parseColor("#000000"));
			openButton.setClickable(true);
			openButton.setText("Select File");
		}
		setOpenButtonOnClickListener();
	}
	
	/**
	 * This method sets the event handler for a click on
	 * the open button
	 */
	private void setOpenButtonOnClickListener(){
		Button openButton = (Button) findViewById(R.id.selectFileButton);
		openButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(selectedFile.isFile()){
					ProjectTree tree = fileManager.importFile(v.getContext(), selectedFile);
					if(!tree.equals(null)){
						Intent intent = new Intent(v.getContext(), GUImain.class);
						intent.putExtra("com.example.pocket_wbs.NEW_TREE", tree);
						startActivity(intent);
					}
					else{
						browser.displayInvalidFileMessage(v.getContext());
					}
				}
			}
		});
	}
	
	/**
	 * This method updates the back buttons display and event handler
	 */
	private void updateBackButton(){
		Button backButton = (Button) findViewById(R.id.previousButton);
		if(browser.currentDirectoryIsRoot()){
			backButton.setTextColor(Color.parseColor("#777777"));
			backButton.setClickable(false);
		}
		else{
			backButton.setTextColor(Color.parseColor("#000000"));
			backButton.setClickable(true);
		}
		setBackButtonOnClickListener();
	}
	
	/**
	 * This method sets the event handler for the back button
	 */
	private void setBackButtonOnClickListener(){
		Button backButton = (Button) findViewById(R.id.previousButton);
		backButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(!browser.currentDirectoryIsRoot()){
					browser.moveUpFileHierarchy();
					selectedFile = browser.getCurrentFile();
					updateActivity();
				}
			}
		});
	}
}