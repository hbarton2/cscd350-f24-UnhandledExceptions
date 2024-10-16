import java.util.Map;

public class UI
{
	/*
		ListClasses takes a list of ClassItem, and displays them
	*/
	public void ListClasses(Map<String, ClassItem> classItems)
	{
		if (classItems.isEmpty())
		{
			System.out.println("\nNo available classes. Add some with option 5!\n");	
			return;
		}
		
		System.out.println("\nCurrent available classes...");
		
		StringBuilder sb = new StringBuilder();
		for (Map.Entry<String, ClassItem> entry : classItems.entrySet()) 
			sb.append(entry.getValue().toString() + ", ");
		sb.delete(sb.length() - 2, sb.length());
		
		System.out.println(sb.toString() + "\n");
	}
		
	/*
		ListClass takes a ClassItem reference, and displays its info
	*/
	public void ListClass(ClassItem classItem)
	{
		if (classItem == null)
		{
			System.out.println("Invalid class name.");
			return;
		}
		
		System.out.println();
		System.out.print("Class: " + classItem.toString() + "\nFields: ");
		
		//fields
		System.out.print("not yet");
		
		//methods
		StringBuilder sb = new StringBuilder();
		for (Map.Entry<String, MethodItem> entry : classItem.methodItems.entrySet()) 
			sb.append("\nMethod: " + entry.getValue().toString());
		
		System.out.println(sb.toString());
	}
	
	/*
		ListRelationships takes a list of RelationshipItems, and displays them
	*/
	public void ListRelationships(Map<String, RelationshipItem> relationshipItems)
	{
		if (relationshipItems.isEmpty())
		{
			System.out.println("\nNo available relationships. Add some with option 3!\n");	
			return;
		}
		
		System.out.println("\nCurrent class relationships...");
		
		for (Map.Entry<String, RelationshipItem> entry : relationshipItems.entrySet()) 
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
		System.out.println("Git Gud");
	}
	
	/*
		Exit says bye and quits
	*/
	public void Exit()
	{
		System.out.println("Bye then.");
		System.exit(0);
	}
};