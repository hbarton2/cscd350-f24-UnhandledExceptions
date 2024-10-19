import java.util.HashMap;
public class UI
{
	/*
		ListClasses takes a list of ClassItem, and displays them
	*/
	public static void ListClasses(HashMap<String, ClassItem> classItems)
	{
		System.out.println("\nCurrent classes...");

		if (classItems.isEmpty())
		{
			System.out.println("None.");
			return;
		}
		
		StringBuilder sb = new StringBuilder();
		for (HashMap.Entry<String, ClassItem> entry : classItems.entrySet()) 
			sb.append(entry.getValue().toString() + ", ");
		sb.delete(sb.length() - 2, sb.length());
		
		System.out.println(sb.toString());
	}
		
	/*
		ListClass takes a ClassItem reference, and displays its info
	*/
	public static void ListClass(ClassItem classItem)
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
		for (HashMap.Entry<String, MethodItem> entry : classItem.methodItems.entrySet()) 
			sb.append("\nMethod: " + entry.getValue().toString());
		
		System.out.println(sb.toString());
	}
	
	/*
		ListRelationships takes a list of RelationshipItems, and displays them
	*/
	public static void ListRelationships(HashMap<String, RelationshipItem> relationshipItems)
	{
		System.out.println("\nCurrent class relationships...");

		if (relationshipItems.isEmpty())
		{
			System.out.println("None.");	
			return;
		}
		
		for (HashMap.Entry<String, RelationshipItem> entry : relationshipItems.entrySet()) 
		{
			System.out.println(entry.getValue().toString());
		}
	}
	
	/*
		Help displays our help menu and or list of commands
	*/
	public static void Help()
	{
		System.out.println("\naddclass classname");
		System.out.println("removeclass classname");
		System.out.println("renameclass classname newclassname");
		System.out.println("addrelation (currently relationship handles the input with the user.)");
		System.out.println("removerelation (currently relationship handles the input with the user.)");
		System.out.println("addfield classname fieldname");
		System.out.println("removefield classname fieldname");
		System.out.println("renamefield classname fieldname newfieldname");
		System.out.println("addmethod classname methodname");
		System.out.println("removemethod classname methodname");
		System.out.println("renamemethod classname methodname newmethodname");
		System.out.println("addparameter classname methodname parametertype parametername");
		System.out.println("removeparameter classname methodname parametertype parametername");
		System.out.println("changeparameter classname methodname parametertype parametername " 
			+ "newparametertype newparametername");
		/*
		System.out.println("UML Editor basics: \n");
		System.out.println("Main UI: Type in the corresponding number to each feature\n");
		System.out.println("Add: Type in the name of the relationship/class/method of your liking\n");
		System.out.println("Delete: Type in the name of the relationship/class/method you would like to remove from the UML\n");
		System.out.println("Rename: Type in the name a specific relationship/class/method you would like to select,");
		System.out.println("then include the name you would like to change the relationship/class/method to\n");
		System.out.println("Parameter: Firstly, type in the name of the class the parameter belongs to");
		System.out.println("then, type in the name of the paramater you want to add/delete/rename\n");
		*/
	}
	
	/*
		Exit says bye and quits
	*/
	public static void Exit()
	{
		System.out.println("This concludes this program's execution. Goodbye!");
		System.exit(0);
	}
};