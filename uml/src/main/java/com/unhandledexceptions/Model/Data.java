package com.unhandledexceptions.Model;

import java.util.HashMap;
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

		return "uh oh";
	}

	/*
	 * uses jackson api for serialization
	 * combinds both classitems and relationshipitems into 1 hashmap,
	 * maps it to a json string, then saves that to the specified file.
	 */
	public String Save(String filepath)
	{		
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
		HashMap<String, Object> items =  new HashMap<>();

		try
		{//try to read the combined hashmap from the file
			ObjectMapper objectMapper = new ObjectMapper();
			items = objectMapper.readValue(new File(filepath),
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

		return "successfully loaded " + filepath;
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

};
