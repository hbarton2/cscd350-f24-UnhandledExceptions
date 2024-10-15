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
		System.out.println("4. Add a Class");
		System.out.println("5. Delete a Class");
		System.out.println("6. Exit");
		input = scanner.nextLine();
		String response;

		switch (input)
		{
			case "1":
				ui.ListClasses(classItems);

				//list class for testing
				// System.out.println("	Classes:");

				
				// for (Map.Entry<String, ClassItem> entry : classItems.entrySet()) {
				// 	String key = entry.getKey();
				// 	//ClassItem value = entry.getValue();
				// 	System.out.println("		-" + key);
				// }
				
				break;
			case "2":
				ui.ListRelationships(relationshipItems);
				break;
			case "3":
				response = RelationshipItem.addRelationship(relationshipItems, classItems);
				System.out.println(response);
				break;
			case "4":
				System.out.println("Input name of class you would like to create");
				input = scanner.nextLine();
				response = ClassItem.addClassItem(classItems, input);
				System.out.println(response);
				break;
			case "5":
				System.out.println("Input name of class you would like to delete");
				input = scanner.nextLine();
				response = ClassItem.removeClassItem(classItems, input);
				System.out.println(response);
				break;
			case "6":
				ui.Exit();
				//add scanner.close() inside of ui.Exit() method
				break;
			default:
					System.out.println("Default: ?");
		}
	}
};