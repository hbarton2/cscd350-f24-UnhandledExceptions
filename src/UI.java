import java.util.HashMap;
public class UI
{
	private static final HashMap<String, String> commandSyntax = new HashMap<>();

	static {
		commandSyntax.put("addclass", "addclass classname");
		commandSyntax.put("removeclass", "removeclass classname");
		commandSyntax.put("renameclass", "renameclass classname newclassname");
		commandSyntax.put("addrelation", "addrelation (currently relationship handles the input with the user.)");
		commandSyntax.put("removerelation", "removerelation (currently relationship handles the input with the user.)");
		commandSyntax.put("addfield", "addfield classname type fieldname");
		commandSyntax.put("removefield", "removefield classname fieldname");
		commandSyntax.put("renamefield", "renamefield classname fieldname newfieldname");
		commandSyntax.put("addmethod", "addmethod classname methodname");
		commandSyntax.put("removemethod", "removemethod classname methodname");
		commandSyntax.put("renamemethod", "renamemethod classname methodname newmethodname");
		commandSyntax.put("addparameter", "addparameter classname methodname parametertype parametername");
		commandSyntax.put("removeparameter", "removeparameter classname methodname parametertype parametername");
		commandSyntax.put("changeparameter", "changeparameter classname methodname parametertype parametername "
						+ "newparametertype newparametername");
	}

	// nicer way to get syntax strings
	public static String CommandSyntax(String command)
	{
		return commandSyntax.get(command);
	}

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
	public static String ListClass(ClassItem classItem, HashMap<String, RelationshipItem> relationships)
	{
		if (classItem == null)
		{
			return "Invalid class name.";
		}
		
		StringBuilder result = new StringBuilder();
		result.append("\nClass: ").append(classItem.toString()).append("\nFields: ");
		
		//fields
		result.append("not yet\n");

		result.append("Methods:\n");
		
		//methods
		StringBuilder sb = new StringBuilder();
		for (HashMap.Entry<String, MethodItem> entry : classItem.methodItems.entrySet()) {
			//adding a tab to the beginning of each method
			sb.append("\t").append(entry.getValue().toString()).append("\n"); 
		}
		
		result.append(sb.toString());

		//relationships
		result.append("Relationships: \n");
		for (HashMap.Entry<String, RelationshipItem> entry : relationships.entrySet()) 
		{
			if (entry.getKey().contains(classItem.getName()))
				result.append("\t" + entry.getValue().toString()).append("\n");
		}

		return result.toString();
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
		System.out.println("\n" + UI.CommandSyntax("addclass"));
		System.out.println(UI.CommandSyntax("removeclass"));
		System.out.println(UI.CommandSyntax("renameclass"));
		System.out.println(UI.CommandSyntax("addrelation"));
		System.out.println(UI.CommandSyntax("removerelation"));
		System.out.println(UI.CommandSyntax("addfield"));
		System.out.println(UI.CommandSyntax("removefield"));
		System.out.println(UI.CommandSyntax("renamefield"));
		System.out.println(UI.CommandSyntax("addmethod"));
		System.out.println(UI.CommandSyntax("removemethod"));
		System.out.println(UI.CommandSyntax("renamemethod"));
		System.out.println(UI.CommandSyntax("addparameter"));
		System.out.println(UI.CommandSyntax("removeparameter"));
		System.out.println(UI.CommandSyntax("changeparameter"));

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