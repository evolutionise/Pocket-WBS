package com.example.pocket_wbs.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import com.example.pocket_wbs.LoadWBSActivity;
import com.example.pocket_wbs.MenuActivity;
import com.example.pocket_wbs.R;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * Class for managing the persistence of ProjectTree objects.
 * Handles input and output of files to internal and external storage areas.
 * @author Jamie Seymour
 */
public class WBSFileManager {
	
	/**
	 * Default constructor for a WBSFileManager.
	 */
	public WBSFileManager(){

	}
	
	/**
	 * This method saves the current project tree being worked on to the applications
	 * internal memory. Used if a previous save of the project tree exists and overwrites
	 * the previous save.
	 * @param context - the applications context for finding the applications internal storage directory.
	 * @param tree - the tree being saved to the internal storage directory.
	 */
	public void saveTreeToFile(Context context, ProjectTree tree){
		try {
			FileOutputStream fileOutput = context.openFileOutput(tree.getFileName(), Context.MODE_PRIVATE);
			ObjectOutputStream objectOutput = new ObjectOutputStream(fileOutput);
			objectOutput.writeObject(tree);
			objectOutput.close();
			fileOutput.close();
			showSaveMessage(context);
		}
		catch(Exception e){
			showErrorMessage(context, "Error: " + e.getMessage());
		}
	}
	
	/**
	 * This method saves the current project tree being worked on to the applications
	 * internal memory. Used when the user wants to save a project tree to a new file
	 * of a different name.
	 * @param context - the applications context for finding the applications internal storage directory.
	 * @param tree - the tree being saved to the internal storage directory.
	 * @param fileName - the new file name for the project.
	 */
	public boolean saveTreeToFileAs(Context context, ProjectTree tree, String fileName){
		boolean fileNameValid = true;
		if(fileName.equals("") || fileName.equals(null)){
			fileNameValid = false;
		}
		if(fileNameValid){
			try {
				tree.setFileName(fileName);
				FileOutputStream fileOutput = context.openFileOutput(fileName, Context.MODE_PRIVATE);
				ObjectOutputStream objectOutput = new ObjectOutputStream(fileOutput);
				objectOutput.writeObject(tree);
				objectOutput.close();
				fileOutput.close();
			}
			catch(Exception e){
				showErrorMessage(context, "Error: " + e.getMessage());
			}	
		}
		return fileNameValid;
	}
	
	/**
	 * This method retrieves a specified project from the applications internal
	 * memory.
	 * @param context - the applications context for finding the applications internal storage directory.
	 * @param projectName - the name of the project to retrieve.
	 * @return ProjectTree - the wbs saved to the chosen file.
	 */
	public ProjectTree loadTreeFromFile(Context context, String projectName){
		ProjectTree tree = new ProjectTree("Loaded Project");
		try{
			FileInputStream fileInput = context.openFileInput(projectName);
			ObjectInputStream objectInput = new ObjectInputStream(fileInput);
			tree = (ProjectTree) objectInput.readObject();
		}
		catch(Exception e){
			showErrorMessage(context, "Error: " + e.getMessage());
		}
		return tree;
	}
	
	/**
	 * This method saves the project tree to a file that is stored on the devices
	 * external memory (an SD card).
	 * @param context - the applications context for displaying messages
	 * @param tree - the tree to be saved to the file.
	 */
	public void exportFile(Context context, ProjectTree tree){
		String fileName = "";
		if(tree.treeSavedToFile()){
			fileName = tree.getFileName() + ".PTWBS";
		}
		else{
			fileName = tree.getProjectName() + ".PTWBS";
		}
		File exportDir = new File(Environment.getExternalStorageDirectory() + "/Pocket-WBS/");
		if(!exportDir.exists()){
			exportDir.mkdir();
		}
		if(fileName.equals("") || fileName.equals(null)){
			fileName = tree.getProjectName();
		}
		if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
			try{
				File exportFile = new File(exportDir, fileName);
				FileOutputStream fileOutput = new FileOutputStream(exportFile);
				ObjectOutputStream objectOutput = new ObjectOutputStream(fileOutput);
				objectOutput.writeObject(tree);
				objectOutput.close();
				fileOutput.flush();
				fileOutput.close();
				Uri uri = Uri.fromFile(exportFile);
				context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
				showExportMessage(context, exportFile.getAbsolutePath());
			}
			catch(Exception e){
				showErrorMessage(context, "Error: " + e.getMessage());
			}
		}
		else{
			showExportErrorMessage(context);
		}
	}
	
	/**
	 * This method imports a project tree file back into the application from
	 * an SD card. An error message is displayed if the file is not a project
	 * tree file.
	 * @param context - used to display the error message when necessary in the correct activity
	 * @param file - the file being imported back into the application
	 * @return tree - the ProjectTree object stored in the file to be loaded by the application
	 */
	public ProjectTree importFile(Context context, File file){
		ProjectTree tree = new ProjectTree("Loaded Project");
		try{
			FileInputStream fileInput = new FileInputStream(file);
			ObjectInputStream objectInput = new ObjectInputStream(fileInput);
			tree = (ProjectTree) objectInput.readObject();
		}
		catch(Exception e){
			showErrorMessage(context, "Error: " + e.getMessage());
		}
		return tree;
	}
	
	/**
	 * This method gets a list of the names of the currently saved project files 
	 * in the internal directory.
	 * @param context - the applications context for finding the applications internal storage directory.
	 * @return String[] - an array of all the project wbs names.
	 */
	public String[] listFiles(Context context){
		File wbsFileDir = context.getFilesDir();
		String[] files = wbsFileDir.list();
		return files;
	}
	
	/**
	 * This method returns a string output of the files list. Used for early testing
	 * purposes, marked for deletion.
	 * @param context - the applications context for finding the applications internal storage directory.
	 * @return String - A readable list of all project file names.
	 */
	public String getFileListAsString(Context context){
		String[] list = this.listFiles(context);
		String listDisplay = "";
		if(list.length == 0){
			listDisplay = "No files to display.";
		}
		for(String entry : list){
			listDisplay += entry + "\n";
		}
		return listDisplay;
	}
	
	/**
	 * This method deletes the file specified by the name
	 * @param context - the applications context for getting the save directory
	 * @param fileName - the name of the file to delete
	 * @return true if the file was successfully deleted, false if not
	 */
	public boolean deleteFile(Context context, String fileName){
		boolean fileDeleted = false;
		File file = context.getFileStreamPath(fileName);
		if(file.exists()){
			file.delete();
			fileDeleted = true;
		}
		return fileDeleted;
	}
	
	/**
	 * This method presents a dialog box where the user can name and save there
	 * file to the application.
	 * @param context - the context where the dialog box is displayed
	 * @param tree - the tree being saved to file
	 */
	public void showSaveAsDialog(final Context context, final ProjectTree tree){
		AlertDialog.Builder saveAsDialog = new AlertDialog.Builder(context);
		final EditText input = new EditText(context);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
		input.setLayoutParams(lp);
		saveAsDialog.setView(input);
        saveAsDialog.setTitle("");
        saveAsDialog.setMessage("Please type a name for your file. This will override files of" +
        		" the same name.");
        saveAsDialog.setIcon(R.drawable.pocketwbsicon2);
        saveAsDialog.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int which) {
                    	String fileName = input.getText().toString();
                    	boolean saveSuccessful = saveTreeToFileAs(context, tree, fileName);
                    	if(saveSuccessful){
                    		showSaveMessage(context);
                    		dialog.cancel();
                    	}
                    	else{
                    		showUnsuccessfulSaveMessage(context);
                    	}
                    	dialog.cancel();
                    }
                });
        saveAsDialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        saveAsDialog.show();
	}
	
	/**
	 * This method displays a confirmation message to the user to confirm a successful
	 * file save to the applications internal memory.
	 * @param context - the context where the message is displayed
	 */
	private void showSaveMessage(Context context){
		Toast saveMessage = new Toast(context);
		int displayTime = Toast.LENGTH_SHORT;
		CharSequence message = "File Saved";
		saveMessage.makeText(context, message, displayTime).show();
	}
	
	/**
	 * 
	 * @param context
	 * @param filePath
	 */
	private void showExportMessage(Context context, String filePath){
		Toast exportMessage = new Toast(context);
		int displayTime = Toast.LENGTH_LONG;
		CharSequence message = "The file has been exported to " + filePath + ".";
		exportMessage.makeText(context, message, displayTime).show();
	}
	
	private void showUnsuccessfulSaveMessage(Context context){
		Toast saveMessage = new Toast(context);
		int displayTime = Toast.LENGTH_LONG;
		CharSequence message = "You need to enter a name for your file.";
		saveMessage.makeText(context, message, displayTime).show();
	}
	
	private void showErrorMessage(Context context, String message){
		Toast errorMessage = new Toast(context);
		int displayTime = Toast.LENGTH_LONG;
		errorMessage.makeText(context, message, displayTime).show();
	}
	
	private void showExportErrorMessage(Context context){
		Toast exportMessage = new Toast(context);
		int displayTime = Toast.LENGTH_LONG;
		CharSequence message = "You need to insert an SD Card.";
		exportMessage.makeText(context, message, displayTime).show();
	}
}
