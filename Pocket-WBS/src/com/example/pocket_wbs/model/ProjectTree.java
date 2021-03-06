/**
 * Class for an object representing a project tree
 * @author Alix, Jamie
 */

package com.example.pocket_wbs.model;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Set;
import java.util.TreeMap;

public class ProjectTree implements Serializable{

	private String name;
	private String fileName;
	private WBSElement rootElement;
	
	public ProjectTree(String name){
		this.name = name;
		this.fileName = "";
		this.rootElement = new WBSElement(name);
	}

	/**
	 * This method is used to change the project's name after creation
	 * @param name - the new name for the project
	 * @throws IllegalArgumentException for null values
	 * @author Alix, Jamie
	 * TESTED
	 */
	public void setProjectName(String name){
		if(name.equals(null)){
			throw new IllegalArgumentException("The name cannot be null");
		}
		else{
			this.name = name;
			rootElement.updateNameOfRootElement(name);
		}
	}
	
	/**
	 * This method returns a string representation of the project's name
	 * @return String - the project's name
	 * @author Alix, Jamie
	 */
	public String getProjectName(){
		return this.name;
	}
	
	public WBSElement getRootElement(){
		return rootElement;
	}
	
	public String getFileName(){
		return this.fileName;
	}
	
	/**
	 * Method to add a child element to an existing tree
	 * @param parent
	 * @author Alix, Jamie
	 */
	// Legacy Method
	public WBSElement addChildElement(WBSElement parent, String name){
		WBSElement child = parent.addChild(name);
		return child;
	}
	
	/*
	 * Method to add child element to an existing tree - with additional parameters
	 * @author adrian
	 */
	public WBSElement addChildElement(WBSElement parent, String name, int startx, int starty){
		WBSElement child = parent.addChild(name, startx, starty);
		return child;
	}
	
	/**
	 * Method to add a sibling element to the beginning of it's sibling list
	 * @param name - the name for the new sibling
	 * @param sibling - the sibling element
	 */
	public WBSElement addNewFirstSibling(WBSElement sibling, String name, int startx, int starty){
		WBSElement child = sibling.getParent().addChildByIndex(name, 0, startx, starty);
		return child;
	}
	
	// Legacy Method
	public WBSElement addNewFirstSibling(WBSElement sibling, String name){
		WBSElement child = sibling.getParent().addChildByIndex(name, 0, "front");
		return child;
	}
	
	/**
	 * Method to add a sibling element to the end of it's sibling list
	 * @param name - the name for the new sibling
	 * @param sibling - the sibling element
	 */
	public WBSElement addNewLastSibling(WBSElement sibling, String name, int startx, int starty){
		WBSElement child = sibling.getParent().addChild(name, startx, starty);
		return child;
	}
	
	// Legacy Method
	public WBSElement addNewLastSibling(WBSElement sibling, String name){
		WBSElement child = sibling.getParent().addChild(name);
		return child;
	}
	
	/**
	 * Method to add a sibling element to the left of a selected element
	 * @param name of new element to be added
	 * @param rightElement - the element directly to the right of where the new element is going in
	 */
	public WBSElement addNewLeftSibling(WBSElement sibling, String name, int startx, int starty){
		int siblingIndex = sibling.getParent().getIndexOfChild(sibling);
		WBSElement child = sibling.getParent().addChildByIndex(name, siblingIndex, startx, starty);
		return child;
	}
	
	// Legacy Method
	public WBSElement addNewLeftSibling(WBSElement sibling, String name){
		int siblingIndex = sibling.getParent().getIndexOfChild(sibling);
		WBSElement child = sibling.getParent().addChildByIndex(name, siblingIndex, "left");
		return child;
	}
	
	/**
	 * Method to add a sibling element to the right of a selected element
	 * @param name - the name for the new sibling
	 * @param sibling - the sibling element
	 */
	public WBSElement addNewRightSibling(WBSElement sibling, String name, int startx, int starty){
		int siblingIndex = sibling.getParent().getIndexOfChild(sibling);
		WBSElement child = sibling.getParent().addChildByIndex(name, siblingIndex + 1, startx, starty);
		return child;
	}
	
	// Legacy Method
	public WBSElement addNewRightSibling(WBSElement sibling, String name){
		int siblingIndex = sibling.getParent().getIndexOfChild(sibling);
		WBSElement child = sibling.getParent().addChildByIndex(name, siblingIndex + 1, "right");
		return child;
	}
	
	/**
	 * Method to check if an element has children
	 * @author Alix & Jamie
	 * @param element (parent)
	 * @return boolean value of true if children exist
	 */
	public boolean hasChildren(WBSElement element){
		return element.hasChildren();
	}
	
	/**
	 * Method used to create a tree map of all elements so any element can
	 * be accessed by key.
	 * @return TreeMap - a map of all elements
	 */
	public TreeMap<String, WBSElement> getProjectElements(){
		TreeMap<String, WBSElement> elements = new TreeMap<String, WBSElement>();
		getProjectElements(rootElement, elements);
		return elements;
	}
	
	public void setFileName(String newName){
		if(newName.equals("") || newName.equals(null)){
			throw new IllegalArgumentException("You need to enter a name for the project.");
		}
		else{
			this.fileName = newName;
		}
	}

	/**
	 * Method used in TreeMap getProjectElements to place elements into the TreeMap
	 * itself to make it look simpler.
	 * @param element - the element to put into the tree
	 * @param elements - the map where the elements are being put into
	 */
	private void getProjectElements(WBSElement element, TreeMap<String, WBSElement> elements) {
		elements.put(element.getElementKey(), element);
		for(WBSElement child : element.getChildren()){
			getProjectElements(child, elements);
		}	
	}
	
	public void removeElement(String key){
		TreeMap<String, WBSElement> elements = this.getProjectElements();
		elements.remove(key);
	}
	
	/**
	 * Method used to get all elements as an ArrayList. It is simpler to iterate through
	 * all elements in an ArrayList than it is a TreeMap.
	 * @return ArrayList of all elements
	 */
	public ArrayList<WBSElement> getProjectElementsAsArray(){
		ArrayList<WBSElement> elements = new ArrayList<WBSElement>();
		TreeMap<String, WBSElement> elementMap = this.getProjectElements();
		for(TreeMap.Entry<String, WBSElement> entry : elementMap.entrySet()){
			WBSElement currentElement = entry.getValue(); 
			elements.add(currentElement);
		}
		return elements;
	}
	
	/**
	 * This method checks if the Project Tree has been saved to a file. The presence of
	 * a proper file name confirms that a project tree has been saved before.
	 * @return
	 */
	public boolean treeSavedToFile(){
		boolean savedToFile = true;
		if(this.fileName.equals("")){
			savedToFile = false;
		}
		return savedToFile;
	}
	
}