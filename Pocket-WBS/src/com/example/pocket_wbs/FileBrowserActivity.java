package com.example.pocket_wbs;

import java.io.File;

import com.example.pocket_wbs.model.FileBrowser;
import com.example.pocket_wbs.model.ProjectTree;
import com.example.pocket_wbs.model.WBSFileManager;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu, menu);
		updateActivity();
		return true;
	}
	
	public void updateActivity(){
		updateTextDisplay();
		updateListDisplay();
		updateBackButton();
		updateOpenButton();
	}
	
	public void updateTextDisplay(){
		TextView selectedFileText = (TextView) findViewById(R.id.selectedFileText);
		if(selectedFile.equals(browser.getCurrentFile())){
			selectedFileText.setText("No file selected");
		}
		else{
			selectedFileText.setText("Currently Selected: " + selectedFile.getName());
		}
	}
	
	public void updateListDisplay() {
		ListView list = (ListView) findViewById(R.id.fileBrowserList);
		String[] itemsList = browser.listFiles();
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, itemsList);
		list.setAdapter(adapter);
		setListEventHandlers();
	}
	
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
	
	public void updateOpenButton(){
		Button openButton = (Button) findViewById(R.id.selectButtonBrowser);
		if(selectedFile.equals(browser.getCurrentFile())){
			openButton.setVisibility(View.INVISIBLE);
		}
		else if(selectedFile.isDirectory()){
			openButton.setVisibility(View.INVISIBLE);
			browser.changeCurrentFile(selectedFile);
			updateActivity();
		}
		else if(selectedFile.isFile()){
			openButton.setVisibility(View.VISIBLE);
			openButton.setText("Select File");
		}
		setOpenButtonOnClickListener();
	}
	
	private void setOpenButtonOnClickListener(){
		Button openButton = (Button) findViewById(R.id.selectButtonBrowser);
		openButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(selectedFile.isDirectory() && !selectedFile.equals(browser.getCurrentFile())){
					browser.changeCurrentFile(selectedFile);
					updateActivity();
				}
				else if(selectedFile.isFile()){
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
	
	private void updateBackButton(){
		Button backButton = (Button) findViewById(R.id.backFileBrowser);
		if(browser.currentDirectoryIsRoot()){
			backButton.setVisibility(View.INVISIBLE);
		}
		else{
			backButton.setVisibility(View.VISIBLE);
		}
		setBackButtonOnClickListener();
	}
	
	private void setBackButtonOnClickListener(){
		Button backButton = (Button) findViewById(R.id.backFileBrowser);
		backButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				browser.moveUpFileHierarchy();
				selectedFile = browser.getCurrentFile();
				updateActivity();
			}
		});
	}
}
