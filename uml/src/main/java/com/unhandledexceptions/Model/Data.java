package com.unhandledexceptions.Model;

import java.util.HashMap;
import java.io.File;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import java.io.IOException;

public class Data
{
	private HashMap<String, ClassItem> classItems = new HashMap<>();
	private HashMap<String, RelationshipItem> relationshipItems = new HashMap<>();

	public Data()
	{
		//constructor
	}

	public void Clear()
	{
		classItems.clear();
		relationshipItems.clear();
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
		{
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
		{
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

	/*
	 * This object is used to store the state of our Data class.
	 * These objects will be stored in a stack in our caretaker class.
	 * Data is the originator because we are storing snapshots of Data object.
	 */
	public class Memento {
		// These fields are final because they should NOT be changed, we are saving "snapshots" as objects.
		private final Data data;
		private final HashMap<String, ClassItem> classItemsMap;
		private final HashMap<String, RelationshipItem> relationshipItems;

		public Memento(Data data, HashMap<String, ClassItem> classItems, HashMap<String, RelationshipItem> relationshipItems) {
			this.data = data;
			this.classItemsMap = classItems;
			this.relationshipItems = relationshipItems;
		}

		public HashMap<String, ClassItem> getClassItems() {
			return classItemsMap;
		}

		public HashMap<String, RelationshipItem> getRelationshipItems() {
			return relationshipItems;
		}

	}
};
