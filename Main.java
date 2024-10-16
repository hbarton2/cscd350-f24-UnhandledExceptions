import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main
{	
	UI ui = new UI();
	Map<String, ClassItem> classItems = new HashMap<>();
	Map<String, RelationshipItem> relationshipItems = new HashMap<>();
	Scanner scanner = new Scanner(System.in);
	String input;
	
	public static void main(String []args)
	{
		Main main  = new Main();
		while(true) main.Run();
	}
		
	public void Run()
	{		
		System.out.println("-=Unhandled Exceptions UML Editor=-");
		System.out.println("1. List Classes");
		System.out.println("2. List Relationships");
		System.out.println("3. Add Relationship");
		System.out.println("4. Delete Relationship");
		System.out.println("5. Add a Class");
		System.out.println("6. Delete a Class");
		System.out.println("7. Rename a Class");
		System.out.println("8. Exit");
		input = scanner.nextLine();

		switch (input)
		{
			case "1":	//List Classes
				ui.ListClasses(classItems);

				//list class for testing
				// System.out.println("	Classes:");

				
				// for (Map.Entry<String, ClassItem> entry : classItems.entrySet()) {
				// 	String key = entry.getKey();
				// 	//ClassItem value = entry.getValue();
				// 	System.out.println("		-" + key);
				// }
				
				break;
			case "2":	//List Relationships
				ui.ListRelationships(relationshipItems);
				break;
			case "3":	//Add a Relationship
				System.out.println(RelationshipItem.addRelationship(relationshipItems, classItems));
				break;
			case "4":	//Delete a Relationship
				System.out.println(RelationshipItem.removeRelationship(relationshipItems));
				break;
			case "5":	//Add a Class
				System.out.println("Input name of class you would like to create");
				input = scanner.nextLine();
				System.out.println(ClassItem.addClassItem(classItems, input));
				break;	
			case "6":	//Delete a Class
				System.out.println("Input name of class you would like to delete");
				input = scanner.nextLine();
				System.out.println(ClassItem.removeClassItem(classItems, input));
				break;
			case "7":	//Rename a Class
				System.out.println("Input name of class you want to rename");
				String oldName = scanner.nextLine();
				System.out.println("Input new name for " + oldName);
				String newName = scanner.nextLine();
				System.out.println(ClassItem.renameClassItem(classItems, newName, oldName));
				break;
			case "8":	//Exit
				ui.Exit();
				//add scanner.close() inside of ui.Exit() method
				break;
			default:
					System.out.println("Default: ?");
		}
	}
};