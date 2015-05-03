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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

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
		}
		catch(Exception e){
			System.out.println("Error: " + e.getMessage());
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
				System.out.println("Error: " + e.getMessage());
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
			System.out.println("Error: " + e.getMessage());
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
	
	public void showFileNameDialog(final Context context, final ProjectTree tree){
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
	
	public void showSaveMessage(Context context){
		Toast saveMessage = new Toast(context);
		int displayTime = Toast.LENGTH_SHORT;
		CharSequence message = "File Saved";
		saveMessage.makeText(context, message, displayTime).show();
	}
	
	public void showUnsuccessfulSaveMessage(Context context){
		Toast saveMessage = new Toast(context);
		int displayTime = Toast.LENGTH_LONG;
		CharSequence message = "You need to enter a name for your file.";
		saveMessage.makeText(context, message, displayTime).show();
	}
}
