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
		System.out.println("4. Exit");
		input = scanner.nextLine();

		switch (input)
		{
			case "1":
				ui.ListClasses(classItems);
				break;
			case "2":
				ui.ListRelationships(relationshipItems);
				break;
			case "3":
				String response = RelationshipItem.addRelationship(relationshipItems, classItems);
				System.out.println(response);
				break;
			case "4":
				ui.Exit();
				break;
			default:
					System.out.println("Default: ?");
		}
	}
};