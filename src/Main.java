import java.util.HashMap;
import java.util.Scanner;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Main {
	HashMap<String, ClassItem> classItems = new HashMap<>();
	HashMap<String, RelationshipItem> relationshipItems = new HashMap<>();
	Scanner scanner = new Scanner(System.in);
	String input;

	public static void main(String[] args) {
		Main main = new Main();
		System.out.println("\n-=Unhandled Exceptions UML Editor=-");
		while (true)
			main.Run();
	}

	public void Run()
	{
		System.out.print("\nueUML (m for menu): ");
		CommandParsing(scanner.nextLine().split(" "));
	}

	public void CommandParsing(String[] input)
	{
		switch (input[0].toLowerCase())
		{
			case "c":
			case "classes":
				UI.ListClasses(classItems);
				break;
			case "r":
			case "relationships":
				UI.ListRelationships(relationshipItems);
				break;
			case "v":
			case "view":
				if (input.length != 2)
					{ System.out.println("Syntax: " + UI.CommandSyntax(input[0]));
					return; }
				System.out.println(UI.ListClass(classItems.get(input[1]), relationshipItems));
				break;
			case "h":
			case "help":
				UI.Help();
				break;
			case "save":
				if (input.length != 2)
					{ System.out.println("Syntax: " + UI.CommandSyntax(input[0]));
					return; }
				System.out.println(IO.Save(classItems, relationshipItems, input[1]));
				break;
			case "load":
				if (input.length != 2)
					{ System.out.println("Syntax: " + UI.CommandSyntax(input[0]));
					return; }
				//load from file into items
				HashMap<String, Object> items = IO.Load(input[1]);

				//clear both hashmaps
				classItems.clear();
				relationshipItems.clear();

				//split up items into classItems and relationshipItems
				ObjectMapper objectMapper = new ObjectMapper();
				classItems.putAll(objectMapper.convertValue(items.get("classItems"),
					new TypeReference<HashMap<String, ClassItem>>() {}));
				relationshipItems.putAll(objectMapper.convertValue(items.get("relationshipItems"),
					new TypeReference<HashMap<String, RelationshipItem>>() {}));
				
				System.out.println("file loaded");
				break;
			case "e":
			case "exit":
				UI.Exit();
				break;
			case "m":
				UI.Menu(false);
				break;
			case "69": // shhh
				tester();
				break;
			case "edit":
				if (input.length != 2)
				{ System.out.println("Syntax: " + UI.CommandSyntax(input[0]));
					return; }
				if(classItems.containsKey(input[1])){
					ClassItem.addToClassMenu(classItems.get(input[1]),scanner);
				}
				break;
			case "addclass":
				if (input.length != 2)
					{ System.out.println("Syntax: " + UI.CommandSyntax(input[0]));
					return; }
				ClassItem.addClassItem(classItems, scanner, input[1]);
				UI.ListClasses(classItems);
				break;
			case "removeclass":
				if (input.length != 2)
					{ System.out.println("Syntax: " + UI.CommandSyntax(input[0]));
					return; }
				System.out.println(ClassItem.removeClassItem(
					classItems, input[1], relationshipItems));
				UI.ListClasses(classItems);
				break;
			case "renameclass":
				if (input.length != 3)
					{ System.out.println("Syntax: " + UI.CommandSyntax(input[0]));
					return; }
				System.out.println(ClassItem.renameClassItem(
					classItems, input[1], input[2], relationshipItems));
				UI.ListClasses(classItems);
				break;

			case "addrelation":
				if (input.length != 3)
					{ System.out.println("Syntax: " + UI.CommandSyntax(input[0]));
					return; }
				System.out.println(RelationshipItem.addRelationship(
					relationshipItems, classItems, input[1], input[2]));
				UI.ListRelationships(relationshipItems);
				break;
			case "removerelation":
				if (input.length != 3)
					{ System.out.println("Syntax: " + UI.CommandSyntax(input[0]));
					return; }
				System.out.println(RelationshipItem.removeRelationship(
					relationshipItems, input[1], input[2]));
				UI.ListRelationships(relationshipItems);
				break;

			case "addfield":
				if (input.length < 4)
					{ System.out.println("Syntax: " + UI.CommandSyntax(input[0]));
					return; }
				System.out.println(classItems.get(input[1]).addField(input[3], input[2]));
				System.out.println(UI.ListClass(classItems.get(input[1]), relationshipItems));
				break;
			case "removefield":
				if (input.length < 3)
					{ System.out.println("Syntax: " + UI.CommandSyntax(input[0]));
					return; }
				System.out.println(classItems.get(input[1]).removeField(input[2]));
				System.out.println(UI.ListClass(classItems.get(input[1]), relationshipItems));
				break;
			case "renamefield":
				if (input.length < 4)
					{ System.out.println("Syntax: " + UI.CommandSyntax(input[0]));
					return; }
				System.out.println(classItems.get(input[1]).renameField(input[2], input[3]));
				System.out.println(UI.ListClass(classItems.get(input[1]), relationshipItems));
				break;
				
			case "addmethod":
				if (input.length < 3)
					{ System.out.println("Syntax: " + UI.CommandSyntax(input[0]));
					return; }
				System.out.println(classItems.get(input[1]).addMethod(input[2]));
				System.out.println(UI.ListClass(classItems.get(input[1]), relationshipItems));
				break;
			case "removemethod":
				if (input.length < 3)
					{ System.out.println("Syntax: " + UI.CommandSyntax(input[0]));
					return; }
				System.out.println(classItems.get(input[1]).removeMethod(input[2]));
				System.out.println(UI.ListClass(classItems.get(input[1]), relationshipItems));
				break;
			case "renamemethod":
				if (input.length < 4)
					{ System.out.println("Syntax: " + UI.CommandSyntax(input[0]));
					return; }
				System.out.println(classItems.get(input[1]).renameMethod(input[2], input[3]));
				System.out.println(UI.ListClass(classItems.get(input[1]), relationshipItems));
				break;

			case "addparameter":
				if (input.length < 5)
					{ System.out.println("Syntax: " + UI.CommandSyntax(input[0]));
					return; }
				System.out.println(classItems.get(input[1]).methodItems.get(input[2])
					.addParameter(input[3], input[4]));
				System.out.println(UI.ListClass(classItems.get(input[1]), relationshipItems));
				break;
			case "removeparameter":
				if (input.length < 5)
					{ System.out.println("Syntax: " + UI.CommandSyntax(input[0]));
					return; }
				System.out.println(classItems.get(input[1]).methodItems.get(input[2])
					.removeParameter(input[3], input[4]));
				System.out.println(UI.ListClass(classItems.get(input[1]), relationshipItems));
				break;
			case "changeparameter":
				if (input.length < 7)
					{ System.out.println("Syntax: " + UI.CommandSyntax(input[0]));
					return; }
				System.out.println(classItems.get(input[1]).methodItems.get(input[2])
					.changeParameter(input[3], input[4], input[5], input[6]));
				System.out.println(UI.ListClass(classItems.get(input[1]), relationshipItems));
				break;

			default:
				System.out.println("unknown command");
		}
	}

	private void tester() 
	{
		ClassItem.addClassItem(classItems, "mcdonalds");
		classItems.get("mcdonalds").addField("location", "String");
		classItems.get("mcdonalds").addField("owner", "person");
		classItems.get("mcdonalds").addMethod("cook_fries");
		classItems.get("mcdonalds").addMethod("cook_burger");
		classItems.get("mcdonalds").methodItems.get("cook_fries").addParameter("int", "time");
		classItems.get("mcdonalds").methodItems.get("cook_fries").addParameter("String", "potatoes");
		classItems.get("mcdonalds").methodItems.get("cook_burger").addParameter("int", "time");
		classItems.get("mcdonalds").methodItems.get("cook_burger").addParameter("String", "patty");

		ClassItem.addClassItem(classItems, "tacobell");
		classItems.get("tacobell").addField("location", "String");
		classItems.get("tacobell").addField("owner", "Person");
		classItems.get("tacobell").addMethod("cook_taco");
		classItems.get("tacobell").addMethod("cook_casadilla");
		classItems.get("tacobell").methodItems.get("cook_taco").addParameter("int", "time");
		classItems.get("tacobell").methodItems.get("cook_taco").addParameter("String", "meat");
		classItems.get("tacobell").methodItems.get("cook_casadilla").addParameter("int", "time");
		classItems.get("tacobell").methodItems.get("cook_casadilla").addParameter("String", "chicken");

		RelationshipItem relationship = new RelationshipItem(
				classItems.get("mcdonalds"), classItems.get("tacobell"));

		// adding the relationship to the map
		String key = "mcdonalds_tacobell";
		relationshipItems.put(key, relationship);
	}
};