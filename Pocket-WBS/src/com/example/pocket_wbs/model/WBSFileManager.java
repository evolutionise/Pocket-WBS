package com.example.pocket_wbs.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import android.content.Context;

public class WBSFileManager {
	
	private static String fileName = "project_file.ser";
	
	public WBSFileManager(){

	}
	
	public void saveTreeToFile(Context context, ProjectTree tree){
		try {
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
	
	public ProjectTree loadTreeFromFile(Context context){
		ProjectTree tree = new ProjectTree("Project");
		try{
			FileInputStream fileInput = context.openFileInput(fileName);
			ObjectInputStream objectInput = new ObjectInputStream(fileInput);
			tree = (ProjectTree) objectInput.readObject();
		}
		catch(Exception e){
			System.out.println("Error: " + e.getMessage());
		}
		return tree;
	}
	
}
