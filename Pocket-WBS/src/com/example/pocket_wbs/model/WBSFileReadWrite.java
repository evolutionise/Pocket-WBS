package com.example.pocket_wbs.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import android.content.Context;

public class WBSFileReadWrite {

	
	private ProjectTree tree;
	
	public WBSFileReadWrite(ProjectTree tree){
		this.tree = tree;
	}
	
	public void saveTreeToFile(){
		try {
			FileOutputStream fileOutput = new FileOutputStream("/pocket_wbs/save_data/project_tree.ser");
			ObjectOutputStream objectOutput = new ObjectOutputStream(fileOutput);
			objectOutput.writeObject(this);
			objectOutput.close();
			fileOutput.close();
		}
		catch(IOException e){
			//TODO
		}
	}
	
	public ProjectTree loadTreeFromFile(){
		ProjectTree tree = new ProjectTree("Project");
		try{
			FileInputStream fileInput = new FileInputStream("/pocket_wbs/save_data/project_tree.ser");
			ObjectInputStream objectInput = new ObjectInputStream(fileInput);
			tree = (ProjectTree) objectInput.readObject();
		}
		catch(Exception e){
			//TODO
		}
		return tree;
	}
	
}
