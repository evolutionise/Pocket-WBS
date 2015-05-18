package com.example.pocket_wbs.model;

import java.io.File;
import java.util.ArrayList;
import java.util.regex.Pattern;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

/**
 * Class for managing the display of the devices external file structure (SD Card).
 * The purpose of this class is to allow the user to view the files on the SD card and
 * navigate to any exported ProjectTree files. 
 * @author Jamie Seymour
 */
public class FileBrowser {

	private File currentFile;
	private File rootDirectory;
	
	/**
	 * Default constructor for a FileBrowser object
	 */
	public FileBrowser(){
			this.currentFile = Environment.getExternalStorageDirectory();
			this.rootDirectory = Environment.getExternalStorageDirectory();
	}
	
	/**
	 * This methods checks to see whether the users device currently has
	 * an SD card inserted into their device.
	 * @return true if an SD card is inserted into the device
	 */
	public boolean externalMemoryMounted(){
		boolean result = false;
		if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
			if(!Environment.isExternalStorageEmulated()){
				result = true;
			}
		}
		return result;
	}
	
	/**
	 * This method presents a string list of the file structure of the 
	 * currently selected directory. Used to provide information for the 
	 * adapter that creates the list in FileBrowserActivity.
	 * @return String[] - a list of all files and directories in the current directory
	 */
	public String[] listFiles(){
		ArrayList<String> filesList = new ArrayList<String>();
		File[] files = this.currentFile.listFiles();
		for(File file : files){
			if(file.getName().endsWith(".PTWBS") || file.isDirectory()){
				filesList.add(file.getName());
			}
		}
		String[] output = stringArrayListToArray(filesList);
		return output;
	}
	
	/**
	 * A method used purely in the listFiles() method to convert the ArrayList
	 * to a regular Array.
	 * @param arrayList - the ArrayList being converted to a string array
	 * @return The new string array created
	 */
	private String[] stringArrayListToArray(ArrayList<String> arrayList){
		int itemNum = arrayList.size();
		int currentItem = 0;
		String[] items = new String[itemNum];
		for(String item : arrayList){
			items[currentItem] = item;
			currentItem++;
		}
		return items;
	}
	
	/**
	 * This method changes the current working directory of the file browser
	 * @param file - the new working directory of the file browser
	 */
	public void changeCurrentFile(File file){
		if(file.exists()){
			this.currentFile = file;
		}
	}
	
	/**
	 * This method changes the current working directory to the on above it
	 * (the directory in which the current file/directory you are using is 
	 * stored). This method does not allow you to move past the root directory
	 * of the external storage medium.
	 */
	public void moveUpFileHierarchy(){
		if(!currentFile.equals(rootDirectory)){
			currentFile = currentFile.getParentFile();
		}
	}
	
	/**
	 * This method is used to get the current working directory of the file browser
	 * @return File - the current working directory or selected file
	 */
	public File getCurrentFile(){
		return this.currentFile;
	}
	
	/**
	 * This method gets the initial directory of the SD card
	 * @return File - the file representing the root directory
	 */
	public File getRootDirectory(){
		return this.rootDirectory;
	}
	
	/**
	 * Method to check whether the explorer is currently in the root directory
	 * @return true when the explorer is in the root directory
	 */
	public boolean currentDirectoryIsRoot(){
		boolean result = false;
		if(currentFile.equals(rootDirectory)){
			result = true;
		}
		return result;
	}
	
	/**
	 * This method displays an error message stating no SD Card is inserted into the phone.
	 * Used in conjunction with the externalMemoryMounted method to alert the user to a
	 * missing SD card.
	 * @param context
	 */
	public void displayCardMissingMessage(Context context){
		Toast errorMessage = new Toast(context);
		String message = "You need to insert an SD card to load exported files.";
		int displayTime = Toast.LENGTH_LONG;
		errorMessage.makeText(context, message, displayTime).show();
	}
	
	/**
	 * This method displays an error message when a file that is being imported
	 * into the application is not that of a project tree file.
	 * @param context - the context in which to display the message
	 */
	public void displayInvalidFileMessage(Context context){
		Toast errorMessage = new Toast(context);
		String message = "You need to select a Pocket-WBS file.";
		int displayTime = Toast.LENGTH_LONG;
		errorMessage.makeText(context, message, displayTime).show();
	}
	
	public void displayMessage(String message, Context context){
		Toast errorMessage = new Toast(context);
		int displayTime = Toast.LENGTH_LONG;
		errorMessage.makeText(context, message, displayTime).show();
	}
	
	/**
	 * This method checks whether the device has a removable SD card inserted.
	 * @return true if a removable SD card is inserted.
	 */
	public boolean removableMemoryInserted(){
		boolean result = false;
		File[] extStorageDirs = Environment.getRootDirectory().listFiles();
		for(File directory : extStorageDirs){
			if(new File(directory + "/LOST.DIR").exists()){
				result = true;
				break;
			}
		}
		return result;
	}
	
	/**
	 * This method checks to see if the devices primary storage is
	 * emulated.
	 * @return true if the primary storage is emulated.
	 */
	public boolean extMemoryEmulated(){
		return Environment.isExternalStorageEmulated();
	}
}
