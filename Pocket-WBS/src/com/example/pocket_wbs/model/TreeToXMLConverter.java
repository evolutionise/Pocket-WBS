package com.example.pocket_wbs.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;

import android.os.Environment;

/**
 * Class for creating an XML file out of a project tree that can be
 * imported and used by Microsoft project.
 * @author Jamie Seymour
 */
public class TreeToXMLConverter {
	
	private ProjectTree tree;
	private File xmlFile;
	private File xmlCalendar;
	private String fileName;
	
	private PrintWriter xmlWriter;
	private BufferedReader bFileReader;
	private URL calendarUrl = getClass().getResource("xml_calendar.txt");
	private static String EXPORT_DIRECTORY = Environment.getExternalStorageDirectory() + "/Pocket-WBS/";
	
	/**
	 * The default constructor for a TreeToXMLConverter object
	 * @param tree - the tree to convert to XML
	 */
	public TreeToXMLConverter(ProjectTree tree){
		this.tree = tree;
		this.setFileName();
		this.createBaseFile();
		this.instantiateFileWriter();
		this.instantiateFileReader();
		this.xmlCalendar = new File(this.calendarUrl.getPath());
	}
	
//============Constructor Methods============
	
	/**
	 * This method is used in the constructor to set the file name
	 * specified by the project tree itself.
	 */
	private void setFileName(){
		if(tree.treeSavedToFile()){
			this.fileName = tree.getFileName();
		}
		else{
			this.fileName = tree.getProjectName();
		}
	}
	
	/**
	 * This method creates the base xml file where all the project tree
	 * information will be appended to. Used in the constructor.
	 */
	private void createBaseFile(){
		this.xmlFile = new File(EXPORT_DIRECTORY, this.fileName + ".xml");
		if(!this.xmlFile.exists()){
			try {
				this.xmlFile.createNewFile();
			} 
			catch (Exception e) {
				//TODO
			}
		}
	}
	
	/**
	 * This method is used in the constructor to create the file writer,
	 * which is used to append text to the xml file.
	 */
	private void instantiateFileWriter(){
		try{
			FileWriter fWriter = new FileWriter(this.xmlFile, true); 
			BufferedWriter bWriter = new BufferedWriter(fWriter);
			this.xmlWriter = new PrintWriter(bWriter);
		}
		catch(Exception e){
			//TODO
		}
	}
	
	/**
	 * This method is used by the constructor to create the file reader,
	 * which is used to get all of the MS Project work calendar information
	 * stored in the local file xml_calendar.txt
	 */
	private void instantiateFileReader(){
		try{
			FileReader fReader = new FileReader(this.xmlCalendar);
			this.bFileReader = new BufferedReader(fReader);
		}
		catch(Exception e){
			//TODO
		}
	}

//============Tree Conversion Methods============
	
	/**
	 * This method creates the XML object
	 * @returns File - the new XML file
	 */
	public File convertTreeToXML(){
		addXMLOpenTags();
		addFileDetails();
		addDefaultWorkingCalendar();
		addTasks();
		addCloseTag();
		this.xmlWriter.close();
		return this.xmlFile;
	}
	
	/**
	 * This method adds the initial xml tags required for import
	 * into MS Project.
	 */
	private void addXMLOpenTags(){
		String openTags = "";
		openTags += "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n";
		openTags += "<Project xmlns=\"http://schemas.microsoft.com/project\">";
		this.xmlWriter.println(openTags);
	}
	
	/**
	 * This method adds the base details for the file being created, such as
	 * the name for the file itself and the save version (the save version
	 * represents the version of project that this xml file is for, in this 
	 * case MS Project 2010).
	 */
	private void addFileDetails(){
		String fileDetails = "";
		fileDetails += "<SaveVersion>14</SaveVersion>\n";
		fileDetails += "<Name>" + this.xmlFile.getName() + "</Name>\n";
		fileDetails += "<Company></Company>\n";
		fileDetails += "<Author></Author>\n";
		fileDetails += "<FYStartDate>1</FYStartDate>\n";
		fileDetails += "<CurrentSymbol>$</CurrentSymbol>";
		this.xmlWriter.println(fileDetails);
	}
	
	/**
	 * This method adds the working calendar to the xml file. This is so
	 * MS Project can define the default working calendar for the project.
	 */
	private void addDefaultWorkingCalendar(){
		String calendar = "";
		try{
			String line = bFileReader.readLine();
			while(line != null){
				calendar += line + "\n";
				line = bFileReader.readLine();
			}
		}
		catch(Exception e){
			//TODO
		}
		try {
			this.bFileReader.close();
		} 
		catch (Exception e) {
			//TODO
		}
		this.xmlWriter.println(calendar);
	}
	
	/**
	 * This method converts each of the project tree's elements into readable
	 * WBS elements in Microsoft Project.
	 */
	private void addTasks(){
		this.xmlWriter.println("<Tasks>");
		int id = 0;
		ArrayList<WBSElement> elements = tree.getProjectElementsAsArray();
		for(WBSElement element : elements){
			String task = "";
			task += "<Task>\n";
			task += "<UID>" + id + "</UID>\n";
			task += "<ID>" + id + "</ID>\n";
			task += "<Name>" + element.getName() + "</Name>\n";
			task += "<Active>1</Active>\n";
			task += "<Manual>0</Manual>\n";
			task += "<Type>1</Type>\n";
			task += "<IsNull>0</IsNull>\n";
			task += "<CreateDate></CreateDate>\n";
			task += "<WBS>" + element.getElementKey() + "</WBS>\n";
			task += "<OutlineNumber>" + element.getElementKey() + "</OutlineNumber>\n";
			task += "<OutlineLevel>" + element.getElementLevel() + "</OutlineLevel>\n";
			task += "</Task>";
			this.xmlWriter.println(task);
			id++;
		}
		this.xmlWriter.println("</Tasks>");
	}
	
	/**
	 * This methods adds the final close tag to the xml file,
	 * completing the file.
	 */
	private void addCloseTag(){
		this.xmlWriter.println("</Project>");
	}
}
