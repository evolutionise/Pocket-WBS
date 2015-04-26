package com.example.pocket_wbs.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;

public class WBSFileManager {
	
	/**
	 * Default constructor for a WBSFileManager.
	 */
	public WBSFileManager(){

	}
	
	/**
	 * This method saves the current project tree being worked on to the applications
	 * internal memory.
	 * @param context - the applications context for finding the applications internal storage directory.
	 * @param tree - the tree being saved to the internal storage directory.
	 */
	public void saveTreeToFile(Context context, ProjectTree tree){
		try {
			FileOutputStream fileOutput = context.openFileOutput(tree.getProjectName(), Context.MODE_PRIVATE);
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
	
}
