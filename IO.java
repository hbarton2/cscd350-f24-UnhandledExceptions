import java.util.HashMap;
import java.io.File;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import java.io.IOException;

public class IO
{
	public static String Save(
		HashMap<String, ClassItem> classItems,
		HashMap<String, RelationshipItem> relationshipItems)
	{		

		//combind both hashmaps into 1 hashmap
		HashMap<String, Object> items = new HashMap<>();
		items.put("classItems", classItems);
		items.put("relationshipItems", relationshipItems);

		try
		{
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.writeValue(new File("output.json"), items);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return "uh oh!";
		} catch (IOException e) {
			e.printStackTrace();
			return "uh oh!";
		}
		
		return "saved";
	}
	
	public static HashMap<String, Object> Load(
		HashMap<String, ClassItem> classItems,
		HashMap<String, RelationshipItem> relationshipItems)
	{		
		HashMap<String, Object> items =  new HashMap<>();

		try
		{
			ObjectMapper objectMapper = new ObjectMapper();
			items = objectMapper.readValue(new File("output.json"),
			 new TypeReference<HashMap<String, Object>>() {});
		} catch (JsonParseException e) {
        	e.printStackTrace();
      	} catch (JsonMappingException e) {
        	e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return items;
	}
};