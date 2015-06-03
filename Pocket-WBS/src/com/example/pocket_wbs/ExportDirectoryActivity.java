package com.example.pocket_wbs;

import java.io.File;

import com.example.pocket_wbs.model.FileBrowser;
import com.example.pocket_wbs.model.ProjectTree;
import com.example.pocket_wbs.model.WBSFileManager;

import android.support.v7.app.ActionBarActivity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

public class ExportDirectoryActivity extends ActionBarActivity {

	public ArrayAdapter adapter;
	public ProjectTree tree;
	public FileBrowser browser = new FileBrowser();
	public WBSFileManager fileManager = new WBSFileManager();
	public File selectedFile = browser.getCurrentFile();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_export_directory);
		Intent intent = getIntent();
		if(intent.hasExtra("com.example.pocket_wbs.PROJECT_TREE")){
			this.tree = (ProjectTree) intent.getSerializableExtra("com.example.pocket_wbs.PROJECT_TREE");
		}
		updateActivity();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.export_directory, menu);
		updateActivity();
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
	
	/**
	 * The main GUI update method of the activity
	 */
	public void updateActivity(){
		updateTextDisplay();
		updateListDisplay();
		updateBackButton();
		updateSaveButton();
		updateNewFolderButton();
	}
	
	/**
	 * This method handles the updates of the text elements in the
	 * activity.
	 */
	private void updateTextDisplay(){
		TextView selectedFileText = (TextView) findViewById(R.id.directoryText);
		selectedFileText.setText("Currently Selected: " + selectedFile.getName());
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
				if(clickedFile.isDirectory()){
					selectedFile = clickedFile;
					browser.changeCurrentFile(selectedFile);
				}
				updateActivity();
			}
		});
	}
	
	/**
	 * This method updates the open buttons display and event handler
	 */
	public void updateSaveButton(){
		setSaveButtonEventHandlers();
	}
	
	/**
	 * This method sets the event handler for a click on
	 * the open button
	 */
	private void setSaveButtonEventHandlers(){
		Button openButton = (Button) findViewById(R.id.selectButtonBrowser);
		openButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(!selectedFile.equals(null)){
					if(!tree.equals(null)){
						fileManager.exportFile(v.getContext(), selectedFile, tree);
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
		Button backButton = (Button) findViewById(R.id.backFileBrowser);
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
		Button backButton = (Button) findViewById(R.id.backFileBrowser);
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
	
	private void updateNewFolderButton(){
		Button newFolderButton = (Button) findViewById(R.id.newFolderButton);
		newFolderButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(selectedFile.isDirectory()){
					createFileDialog(selectedFile);
				}
				else{
					if(selectedFile.isFile()){
						createFileDialog(selectedFile.getParentFile());
					}
				}
			}
		});
	}
	
	public void createFileDialog(final File location){
		AlertDialog.Builder newFolderDialog = new AlertDialog.Builder(this);
		final EditText input = new EditText(this);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
		input.setLayoutParams(lp);
		newFolderDialog.setView(input);
        newFolderDialog.setTitle("");
        newFolderDialog.setMessage("Enter a name for the folder.");
        newFolderDialog.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int which) {
                    	String folderName = input.getText().toString();
                    	boolean folderCreated = fileManager.createNewFolder(folderName, location);
                    	if(!folderCreated){
                    		toastMessage("The folder already exists");
                    	}
                    	else{
                    		toastMessage("Folder " + "'" + folderName + "'" + " created.");
                    	}
                    	dialog.cancel();
                    	updateActivity();
                    }
                });
        newFolderDialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        newFolderDialog.show();
	}
	
	@Override
	public void onBackPressed(){
		Intent intent = new Intent(this, GUImain.class);
		intent.putExtra("com.example.pocket_wbs.NEW_TREE", tree);
		startActivity(intent);
		finish();
	}
	
	public void toastMessage(String message){
		Toast.makeText(this, message, Toast.LENGTH_LONG).show();
	}
}
