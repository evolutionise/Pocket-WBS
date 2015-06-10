package com.project_pocket_wbs.pocket_wbs.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import android.util.Log;

public class WBSAttributes implements Serializable {
	
	final private static Map<String, Map<String, String>> allNames = new HashMap<String, Map<String, String>>();
	private WBSAttributeCollection collection;
	
	private String identifier;
	
	/**
	 * Create an attributes collection that belongs to a the parameters objects class
	 * 
	 * @param identifier to identify the attribute set by
	 */ 
	public WBSAttributes(Object identifier) {
		this.identifier = identifier.getClass().toString();
	}
	
	public WBSAttributeCollection getCollection() {
		if (collection == null) {
			collection = new WBSAttributeCollection();
		}
		return collection;
	}
	
	/**
	 * Add a name to attributes name collection
	 * 
	 * @param name
	 * @return
	 */
	public boolean addName(String name) {
		int sizeBefore = getAttributeNamesMap().size();
		getKey(name);
		int sizeAfter = getAttributeNamesMap().size();
		return sizeBefore != sizeAfter;
	}
	
	private String getKey(String name) {
		Map<String, String> names = getAttributeNamesMap();
		for (String key: names.keySet()) {
			if (names.get(key).equals(name)) {
				return key;
			}
		}
		//Create name if not found in names list
		String key = String.valueOf(names.keySet().size());
		names.put(key, name);
		return key;
	}
	
	/**
	 * Puts the attribute in the collection
	 * 
	 * @param name the name of the attribute
	 * @param value the value of the attribute
	 */
	public void put(String name, String value) {
		Log.d("WBSAttributes", "Set:" + name + " " + value);
		String key = getKey(name);
		getCollection().putAttribute(key, value);
	}
	
	/**
	 * Gets an attribute from the collection
	 * 
	 * @param name of the attribute
	 * @return the attribute requested with the given name
	 */
	private String getAttribute(String name) {
		String key = getKey(name);
		return getCollection().getAttribute(key);
	}
	
	/** 
	 * Get all the attribute names with any matching attributes
	 * 
	 * @return map where keys are attribute names and values are attributes 
	 */
	public Map<String, String> getAttributesAndNames() {
		Map<String, String> attributes = new LinkedHashMap<String, String>();
		if (allNames != null) {
			for (String name :  getAttributeNamesMap().values()) {
				String attribute = getAttribute(name);
				attributes.put(name, attribute);
			} 
		}
		return attributes;
	}
	
	/** Lazily get the names map
	 */
	private Map<String, String> getAttributeNamesMap() {
		Map<String, String> names = allNames.get(identifier);
		if (names == null) {
			names = new LinkedHashMap<String, String>();
			allNames.put(identifier, names);
		}
		return names;
	}
	
	private class WBSAttributeCollection implements Serializable {
		
		private Map<String, String> attributes;
		
		private Map<String, String> getAttributeMap() {
			if (attributes == null) {
				attributes = new HashMap<String, String>();
			}
			return attributes;
		}
		
		/** Update the attribute
		 */
		private void putAttribute(String key, String value) {
			getAttributeMap().put(key, value);
		}
		
		/** Get an attribute
		 */
		private String getAttribute(String key) {
			if (key == null) {
				return "";
			}
			String value = getAttributeMap().get(key);
			if (value == null) {
				return "";
			}
			return value;
		}
	}
}
