package com.unhandledexceptions.Model;

import java.util.HashMap;
import java.util.Map;
import java.io.File;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import java.io.IOException;

public class Data
{
	private HashMap<String, ClassItem> classItems = new HashMap<>();
	private HashMap<String, RelationshipItem> relationshipItems = new HashMap<>();
	private FileChooser fileChooser = new FileChooser();
	private String currentPath = "";

	public Data()
	{
		//filers for file types
		fileChooser.getExtensionFilters().add(
			new FileChooser.ExtensionFilter("JSON Files", "*.json")
		);

		//initial directory
		fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
	}

	//clear method to reset this data object to a blank state
	public void Clear()
	{
		classItems.clear();
		relationshipItems.clear();
	}

	//basic getters and setters
	public String getCurrentPath()
	{
		return this.currentPath;
	}
	
	public void setCurrentPath(String currentPath)
	{
		this.currentPath = currentPath;
	}

	public HashMap<String, ClassItem> getClassItems()
	{
        return this.classItems;
    }

    public void setClassItems(HashMap<String, ClassItem> classItems)
	{
        this.classItems = classItems;
    }

	public HashMap<String, RelationshipItem> getRelationshipItems()
	{
        return this.relationshipItems;
    }

    public void setRelationshipItems(HashMap<String, RelationshipItem> relationshipItems)
	{
        this.relationshipItems = relationshipItems;
    }

	//save called from GUI. Presents a file chooser dialog box if the current project
	//hasn't been saved yet. otherwise just saves to the currently open project's file.
	public String Save(AnchorPane anchorPane)
	{
		if (currentPath.equals(""))
			return SaveAs(anchorPane);
		else
			return Save(currentPath);
	}


	//saveAs called from GUI. Presents a file chooser dialog box to the user then saves.
	public String SaveAs(AnchorPane anchorPane)
	{
		//get stage for filechooser dialog
		Stage stage = (Stage) anchorPane.getScene().getWindow();

		//display filechooser and get back filepath
		File selectedFile = fileChooser.showSaveDialog(stage);

		//check that a filepath was chosen, if so, save to it
		if (selectedFile != null)
		{
			currentPath = selectedFile.getAbsolutePath();
			return Save(currentPath);
		}

		return "uh oh";
	}

	public String Load(AnchorPane anchorPane)
	{
		//get stage for filechooser dialog
		Stage stage = (Stage) anchorPane.getScene().getWindow();

		//display filechooser and get back filepath
		File selectedFile = fileChooser.showOpenDialog(stage);

		//check that a filepath was chosen, if so, load from it
		if (selectedFile != null)
		{
			currentPath = selectedFile.getAbsolutePath();
			return Load(currentPath);
		}
		else
		{
			return "user canceled load";
		}
	}

	/*
	 * uses jackson api for serialization
	 * combinds both classitems and relationshipitems into 1 hashmap,
	 * maps it to a json string, then saves that to the specified file.
	 */
	public String Save(String filepath)
	{
		//jsonify
		filepath = filepath.endsWith(".json") ? filepath : filepath + ".json";

		//combind both hashmaps into 1 hashmap
		HashMap<String, Object> items = new HashMap<>();
		items.put("classItems", classItems);
		items.put("relationshipItems", relationshipItems);

		try
		{//try to write the combined hashmap to the file
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.writeValue(new File(filepath), items);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return "uh oh!";
		} catch (IOException e) {
			e.printStackTrace();
			return "uh oh!";
		}
		
		return "saved to " + filepath;
	}
	
	/*
	* uses jackson api for serialization
	* makes a new hashmap to store both classitems and relationshipitems
	* maps the json string from the specified file to the items hashmap and sends
	* it back to main to be split up and deployed.
	* with more time, will figure out how to split it up and deploy it from here.
	*/
	public String Load(String filepath)
	{
		if (filepath == null || filepath.isEmpty()) {
			return "invalid filepath";
		}
		File file = new File(filepath);
		if (!file.exists()) {
			return "file does not exist";
		}
		HashMap<String, Object> items = new HashMap<>();
		try
		{//try to read the combined hashmap from the file
			ObjectMapper objectMapper = new ObjectMapper();
			items = objectMapper.readValue(file,
			 new TypeReference<HashMap<String, Object>>() {});
		} catch (JsonParseException e) {
        	e.printStackTrace();
      	} catch (JsonMappingException e) {
        	e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		//clear both hashmaps
		classItems.clear();
		relationshipItems.clear();

		//split up items into classItems and relationshipItems
		ObjectMapper objectMapper = new ObjectMapper();
		classItems.putAll(objectMapper.convertValue(items.get("classItems"),
			new TypeReference<HashMap<String, ClassItem>>() {}));
		relationshipItems.putAll(objectMapper.convertValue(items.get("relationshipItems"),
			new TypeReference<HashMap<String, RelationshipItem>>() {}));

		return "good";
	}

	//equals override to assist in comparing objects for unit testing
	@Override
	public boolean equals(Object o) 
	{
		//make sure the incoming object is valid
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Data data = (Data) o;

		//convert this object's equals method to return it's important field's toString methods.
		return this.classItems.toString().equals(data.classItems.toString()) &&
			this.relationshipItems.toString().equals(data.relationshipItems.toString());
	}

	/*
	 * This object is used to store the state of our Data class.
	 * These objects will be stored in a stack in our caretaker class.
	 * Data is the originator because we are storing snapshots of Data object.
	 * Memento is nested in Data because it is only used by Data.
	 * There are no setters because we can't change the state of a memento.
	 */
	public class Memento {
		// These fields are final because they should NOT be changed, we are saving "snapshots" as objects.
		private final HashMap<String, ClassItem> classItemsMap;
		private final HashMap<String, RelationshipItem> relationshipItems;

		public Memento(HashMap<String, ClassItem> classItems, HashMap<String, RelationshipItem> relationshipItems) {
		// we need to create a deep copy of the classItems and relationshipItems so that the memento object is independent of the Data object
		this.classItemsMap = new HashMap<>();
		// create deep copies of everything in the classItem hashmap
		for (Map.Entry<String, ClassItem> entry : classItems.entrySet()) {
			this.classItemsMap.put(entry.getKey(), ClassItem.copyClassItem(entry.getValue()));
		}

		this.relationshipItems = new HashMap<>();
		for (Map.Entry<String, RelationshipItem> entry : relationshipItems.entrySet()) {
				this.relationshipItems.put(entry.getKey(), RelationshipItem.copyRelationshipItem(entry.getValue()));
			}
		}

		public HashMap<String, ClassItem> getClassItems() {
			return classItemsMap;
		}

		public HashMap<String, RelationshipItem> getRelationshipItems() {
			return relationshipItems;
		}
	}

	/**
	 * This method creates a new memento object.
	 * It is used by the caretaker class to save the state of the Data object.
	 * 
	 * @return the memento object
	 */
	public Memento createMemento() {
		return new Memento(classItems, relationshipItems);
	}

	/**
	 * This method restores the state of the Data object from a memento object.
	 * It is used by the caretaker class to restore the state of the Data object.
	 * 
	 * @param memento the memento object to restore the data from
	 */

	public void restoreFromMemento(Memento memento) {
		this.setClassItems(memento.getClassItems());
		this.setRelationshipItems(memento.getRelationshipItems());
	}


};
