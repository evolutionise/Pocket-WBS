package com.example.pocket_wbs.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class WBSAttributes {
	
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
	
	private WBSAttributeCollection getCollection() {
		if (collection == null) {
			collection = new WBSAttributeCollection(getAttributeNamesMap());
		}
		return collection;
	}
	
	/**
	 * Set a new attribute
	 * 
	 * @param name the name of the attribute
	 * @param value the value of the attribute
	 * @return true if the attribute was created successfully
	 */
	public boolean set(String name, String value) {
		return getCollection().setAttribute(name, value);
	}
	
	/**
	 * Updates a current attribute
	 * 
	 * @param name the name of the attribute
	 * @param value the value of the attribute
	 */
	public void update(String name, String value) {
		getCollection().updateAttribute(name, value);
	}
	
	/**
	 * Gets an attribute from the collection
	 * 
	 * @param name of the attribute
	 * @return the attribute requested with the given name
	 */
	public WBSAttribute getAttribute(String name) {
		return getCollection().getAttribute(name);
	}
	
	/**
	 * Get all attributes from the collection
	 * @return all attributes
	 */
	public WBSAttribute[] getAttributes() {
		return getCollection().getAttributes();
	}
	
	/** 
	 * Get all the attribute names with any matching attributes
	 * 
	 * @return map where keys are attribute names and values are attributes 
	 */
	public Map<String, WBSAttribute> getAttributesAndNames() {
		Map<String, WBSAttribute> attributes = new HashMap<String, WBSAttribute>();
		for (String name: allNames.get(identifier).keySet()) {
			attributes.put(name, getAttribute(name));
		}
		return attributes;
	}
	
	/** Lazily get the names map
	 */
	private Map<String, String> getAttributeNamesMap() {
		Map<String, String> names = allNames.get(identifier);
		if (names == null) {
			names = new HashMap<String, String>();
			allNames.put(identifier, names);
		}
		return names;
	}
	
	private class WBSAttributeCollection {
		
		private Map<String, String> names;
		private Map<String, WBSAttribute> attributes;
		
		private WBSAttributeCollection(Map<String, String> names) {
			this.names = names;
		}
		
		private Map<String, WBSAttribute> getAttributeMap() {
			if (attributes == null) {
				attributes = new HashMap<String, WBSAttribute>();
			}
			return attributes;
		}

		/** Set the attribute, return false if already exists
		 */
		private boolean setAttribute(String name, String value) {
			String key = getKey(name);
			if (getAttributeMap().containsKey(key)) {
				return false; // value already exists
			}
			getAttributeMap().put(key, new WBSAttribute(this, key, value));
			return true;
		}
		
		/** Update the attribute
		 */
		private void updateAttribute(String name, String value) {
			String key = getKey(name);
			if (getAttributeMap().containsKey(key)) {
				getAttributeMap().get(key).value = value;
			} else {
				getAttributeMap().put(key, new WBSAttribute(this, key, value));
			}
		}
		
		/** Get an attribute
		 */
		private WBSAttribute getAttribute(String name) {
			String key = getKey(name);
			if (key == null) {
				return null;
			}
			return getAttributeMap().get(key);
		}
		
		/** Get all attributes
		 */
		private WBSAttribute[] getAttributes() {
			Collection<WBSAttribute> attributes = getAttributeMap().values();
			return attributes.toArray(new WBSAttribute[0]);
		}

		private String getKey(String name) {
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

		public boolean setName(String key, String name) {
			if (names.containsValue(name)) {
				return false; //Name already exists
			}
			names.put(key, name);
			return true;
		}

		public String getName(String key) {
			return names.get(key);
		}
		
	}
	
	/**
	 * The storage object of an attribute
	 */
	public class WBSAttribute {
		
		private WBSAttributeCollection collection;
		private String value;
		private String key;
		
		private WBSAttribute(WBSAttributeCollection collection, String key, String value) {
			this.collection = collection;
			this.value = value;
			this.key = key;
		}
		
		public void setValue(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}
		
		/**
		 * Updates the name for all attributes of this type
		 * 
		 * @param name
		 * @return
		 */
		public boolean setName(String name) {
			return collection.setName(key, name);
		}
		
		public String getName(String name) {
			return collection.getName(key);
		}
	}
}
