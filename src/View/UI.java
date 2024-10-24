package View;
import java.util.HashMap;

import Model.ClassItem;
import Model.FieldItem;
import Model.MethodItem;
import Model.RelationshipItem;
public class UI
{
	// hashmap of commands and usages for the help and for displaying usage to user if input is wrong
	private static final HashMap<String, String> commandSyntax = new HashMap<>();

	static {
		commandSyntax.put("v", "v [classname]");
		commandSyntax.put("view", "view [classname]");
		commandSyntax.put("save", "save [filename]");
		commandSyntax.put("load", "load [filename]");
		commandSyntax.put("addclass", "addclass [classname]");
		commandSyntax.put("removeclass", "removeclass [classname]");
		commandSyntax.put("renameclass", "renameclass [classname] [newclassname]");
		commandSyntax.put("addrelation", "addrelation [sourceclassname] [destinationclassname]");
		commandSyntax.put("removerelation", "removerelation [sourceclassname] [destinationclassname]");
		commandSyntax.put("addfield", "addfield [classname] [type] [fieldname]");
		commandSyntax.put("removefield", "removefield [classname] [fieldname]");
		commandSyntax.put("renamefield", "renamefield [classname] [fieldname] [newfieldname]");
		commandSyntax.put("addmethod", "addmethod [classname] [methodname]");
		commandSyntax.put("removemethod", "removemethod [classname] [methodname]");
		commandSyntax.put("renamemethod", "renamemethod [classname] [methodname] [newmethodname]");
		commandSyntax.put("addparameter", "addparameter [classname] [methodname] [parametertype] [parametername]");
		commandSyntax.put("removeparameter", "removeparameter [classname] [methodname] [parametertype] [parametername]");
		commandSyntax.put("changeparameter", "changeparameter [classname] [methodname] [parametertype] [parametername] "
						+ "[newparametertype] [newparametername]");
		commandSyntax.put("edit", "edit [classname]");
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
		ListClass takes a ClassItem reference, and displays its info such as fields, methods, and parameters for its methods
	*/
	public static String ListClass(ClassItem classItem, HashMap<String, RelationshipItem> relationships)
	{
		if (classItem == null)
		{
			return "Invalid class name.";
		}
		
		StringBuilder result = new StringBuilder();
		result.append("\nClass: ").append(classItem.toString()).append("\nFields:\n");
		
		//prints list of fields indented to read easier
		for (HashMap.Entry<String, FieldItem> entry : classItem.getFieldItems().entrySet()) 
		{
			result.append("\t" + entry.getValue().toString()).append("\n");
		}

		result.append("Methods:\n");
		
		//methods
		StringBuilder sb = new StringBuilder();
		for (HashMap.Entry<String, MethodItem> entry : classItem.getMethodItems().entrySet()) {
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
	 * Menu displays basic navigational commands and file IO
	 * boolean verbose changes its appearance for normal use vs help display.
	 * if verbose is false, prints normal menu
	 * if verbose is true, prints help menu
	*/
	public static void Menu(boolean verbose)
	{
		if (!verbose) System.out.print("List Current Classes: ");
		System.out.print("c or classes\n");
		if (verbose) System.out.println("\t- List currently available classes.");
		if (!verbose) System.out.print("List Current Relationships: ");
		System.out.print("r or relationships\n");
		if (verbose) System.out.println("\t- Lists current relationships between classes.");
		if (!verbose) System.out.print("View Class Details: ");
		System.out.print("v [classname] or view [classname]\n");
		if (verbose) System.out.println("\t- View details about a specific class.");
		if (!verbose) System.out.print("Display help/command list: ");
		System.out.print("h or help\n");
		if (verbose) System.out.println("\t- Get help with the program.");
		if (!verbose) System.out.print("Save: ");
		System.out.print("save [filename]\n");
		if (verbose) System.out.println("\t- Saves current project to a json file.");
		if (!verbose) System.out.print("Load: ");
		System.out.print("load [filename]\n");
		if (verbose) System.out.println("\t- Loads a previously saved project.");
		if (!verbose) System.out.print("Exit: ");
		System.out.print("e or exit\n");
		if (verbose) System.out.println("\t- Enough work for now.");
	}

	/*
		Help displays our list of commands and how they work and usage
	*/
	public static void Help()
	{
		System.out.println("\nHelp Menu:\n");
		UI.Menu(true);
		// UI.CommandSyntax(command) lists value from hashmap at top of the file
		System.out.println("\n" + UI.CommandSyntax("addclass"));
		System.out.println("\t- Adds a class to the UML.");
		System.out.println(UI.CommandSyntax("removeclass"));
		System.out.println("\t- Removes a class from the UML.");
		System.out.println(UI.CommandSyntax("renameclass"));
		System.out.println("\t- Renames a class in the UML.\n");

		System.out.println(UI.CommandSyntax("addrelation"));
		System.out.println("\t- Adds a relationship between two classes.");
		System.out.println(UI.CommandSyntax("removerelation"));
		System.out.println("\t- Removes a relationship between two classes.\n");

		System.out.println(UI.CommandSyntax("addfield"));
		System.out.println("\t- Adds a field to a class.");
		System.out.println(UI.CommandSyntax("removefield"));
		System.out.println("\t- Removes a field from a class.");
		System.out.println(UI.CommandSyntax("renamefield"));
		System.out.println("\t- Renames a field.\n");

		System.out.println(UI.CommandSyntax("addmethod"));
		System.out.println("\t- Adds a method to a class.");
		System.out.println(UI.CommandSyntax("removemethod"));
		System.out.println("\t- Removes a method from a class.");
		System.out.println(UI.CommandSyntax("renamemethod"));
		System.out.println("\t- Renames a method.\n");

		System.out.println(UI.CommandSyntax("addparameter"));
		System.out.println("\t- Adds a parameter to a method.");
		System.out.println(UI.CommandSyntax("removeparameter"));
		System.out.println("\t- Removes a parameter from a method.");
		System.out.println(UI.CommandSyntax("changeparameter"));
		System.out.println("\t- Renames a parameter.\n");

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