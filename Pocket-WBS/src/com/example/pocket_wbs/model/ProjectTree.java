/**
 * Class for an object representing a project tree
 * @author Alix, Jamie
 */

package com.example.pocket_wbs.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Set;
import java.util.TreeMap;

public class ProjectTree {

	private String name;
	private WBSElement rootElement;
	
	public ProjectTree(String name){
		this.name = name;
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
			rootElement.setName(name);
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
	
	/**
	 * Method to add a child element to an existing tree
	 * @param parent
	 * @author Alix, Jamie
	 */
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
	public WBSElement addNewFirstSibling(WBSElement sibling, String name){
		WBSElement child = sibling.getParent().addChildByIndex(name, 0);
		return child;
	}
	
	/**
	 * Method to add a sibling element to the end of it's sibling list
	 * @param name - the name for the new sibling
	 * @param sibling - the sibling element
	 */
	public WBSElement addNewLastSibling(WBSElement sibling, String name){
		WBSElement child = sibling.getParent().addChild(name);
		return child;
	}
	
	/**
	 * Method to add a sibling element to the left of a selected element
	 * @param name of new element to be added
	 * @param rightElement - the element directly to the right of where the new element is going in
	 */
	public WBSElement addNewLeftSibling(WBSElement sibling, String name){
		int siblingIndex = sibling.getParent().getIndexOfChild(sibling);
		WBSElement child = sibling.getParent().addChildByIndex(name, siblingIndex);
		return child;
	}
	
	/**
	 * Method to add a sibling element to the right of a selected element
	 * @param name - the name for the new sibling
	 * @param sibling - the sibling element
	 */
	public WBSElement addNewRightSibling(WBSElement sibling, String name){
		int siblingIndex = sibling.getParent().getIndexOfChild(sibling);
		WBSElement child = sibling.getParent().addChildByIndex(name, siblingIndex + 1);
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
	
	public HashMap<String, WBSElement> getProjectElements(){
		HashMap<String, WBSElement> elements = new HashMap<String, WBSElement>();
		getProjectElements(rootElement, elements);
		return elements;
	}

	private void getProjectElements(WBSElement element, HashMap<String, WBSElement> elements) {
		elements.put(element.getElementKey(), element);
		for(WBSElement child : element.getChildren()){
			getProjectElements(child, elements);
		}	
	}
	
	public static String Test(){
		
		ProjectTree project = new ProjectTree("[Name of Project]");
		project.addChildElement(project.rootElement, "1");
		project.addChildElement(project.getProjectElements().get("1"), "1.1");
		project.addChildElement(project.getProjectElements().get("1"), "1.3");
		project.addNewLeftSibling(project.getProjectElements().get("1.2"), "1.2");
		project.addChildElement(project.rootElement, "2");
		project.addChildElement(project.rootElement, "3");
				
		HashMap<String, WBSElement> map = project.getProjectElements();
		String[] keys = map.keySet().toArray(new String[0]);
		String output = "";
		for(int i = 0; i < keys.length; i++){
			output += "Key: " + keys[i];
			output += " Value: " + map.get(keys[i]).getName() + "\n";
		}
		
		return output;		
	}
	
}