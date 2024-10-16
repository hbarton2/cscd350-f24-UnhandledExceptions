import java.util.HashMap;
import java.io.File;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.IOException;
import java.util.ArrayList;

public class IO
{
	public static String Save(
		HashMap<String, ClassItem> classItems,
		HashMap<String, RelationshipItem> relationshipItems)
	{

		ArrayList<ClassIO> classIOs = new ArrayList<>();
		for (String key : classItems.keySet())
		{
			ClassItem classItem = classItems.get(key);
			ClassIO classIO = new ClassIO();
			classIO.name = classItem.getClassItemName();
			classIOs.add(classIO);
		}
		
		try
		{
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.writeValue(new File("output.json"), classIOs);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return "uh oh!";
		} catch (IOException e) {
			e.printStackTrace();
			return "uh oh!";
		}
		
		return "save";
	}
	
	public static String Load(
		HashMap<String, ClassItem> classItems,
		HashMap<String, RelationshipItem> relationshipItems)
	{
		classItems.clear();
		
		ArrayList<ClassIO> classIOs = new ArrayList();
		
		try
		{
			ObjectMapper objectMapper = new ObjectMapper();
			classIOs = objectMapper.readValue(new File("output.json"),
				objectMapper.getTypeFactory().constructCollectionType(ArrayList.class, ClassIO.class));
		} catch (IOException e) {
			e.printStackTrace();
			return "uh oh!";
		}
			
		for (ClassIO classIO : classIOs)
		{
				ClassItem.addClassItem(classItems, classIO.name);
		}
		
		return "load";
	}
};

class ClassIO
{
	public String name;
	public ClassIO() {}
}