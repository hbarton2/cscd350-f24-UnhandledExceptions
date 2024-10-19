import java.util.HashMap;
public class UI
{
	/*
		ListClasses takes a list of ClassItem, and displays them
	*/
	public void ListClasses(HashMap<String, ClassItem> classItems)
	{
		if (classItems.isEmpty())
		{
			System.out.println("\nNo available classes. Add some with option 5!\n");	
			return;
		}
		
		System.out.println("\nCurrent available classes...");
		
		StringBuilder sb = new StringBuilder();
		for (HashMap.Entry<String, ClassItem> entry : classItems.entrySet()) 
			sb.append(entry.getValue().toString() + ", ");
		sb.delete(sb.length() - 2, sb.length());
		
		System.out.println(sb.toString() + "\n");
	}
		
	/*
		ListClass takes a ClassItem reference, and returns its info as a String
	*/
	public String ListClass(ClassItem classItem)
	{
		if (classItem == null)
		{
			return "Invalid class name.";
		}
		
		StringBuilder result = new StringBuilder();
		result.append("\nClass: ").append(classItem.toString()).append("\nFields: ");
		
		//fields
		result.append("not yet");
		
		//methods
		StringBuilder sb = new StringBuilder();
		for (HashMap.Entry<String, MethodItem> entry : classItem.methodItems.entrySet()) 
			sb.append("\nMethod: ").append(entry.getValue().toString());
		
		result.append(sb.toString());
		return result.toString();
	}
	
	/*
		ListRelationships takes a list of RelationshipItems, and displays them
	*/
	public void ListRelationships(HashMap<String, RelationshipItem> relationshipItems)
	{
		if (relationshipItems.isEmpty())
		{
			System.out.println("\nNo available relationships. Add some with option 3!\n");	
			return;
		}
		
		System.out.println("\nCurrent class relationships...");
		
		for (HashMap.Entry<String, RelationshipItem> entry : relationshipItems.entrySet()) 
		{
			System.out.println(entry.getValue().toString());
		}
		
		System.out.println();
	}
	
	/*
		Help displays our help menu and or list of commands
	*/
	public void Help()
	{
		System.out.println("UML Editor basics: \n");
		System.out.println("Main UI: Type in the corresponding number to each feature\n");
		System.out.println("Add: Type in the name of the relationship/class/method of your liking\n");
		System.out.println("Delete: Type in the name of the relationship/class/method you would like to remove from the UML\n");
		System.out.println("Rename: Type in the name a specific relationship/class/method you would like to select,");
		System.out.println("then include the name you would like to change the relationship/class/method to\n");
		System.out.println("Parameter: Firstly, type in the name of the class the parameter belongs to");
		System.out.println("then, type in the name of the paramater you want to add/delete/rename\n");
	}
	
	/*
		Exit says bye and quits
	*/
	public void Exit()
	{
		System.out.println("This concludes this program's execution. Goodbye!");
		System.exit(0);
	}
};