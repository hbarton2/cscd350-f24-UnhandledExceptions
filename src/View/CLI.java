package View;
import java.util.Scanner;

import Model.Data;
import Controller.BaseController;

public class CLI 
{
	BaseController controller;
	Data data;
  Scanner scanner = new Scanner(System.in);

	// constructor for the CLI. takes in the data model and creates a new controller from the data
	public CLI(Data data)
	{
		this.data = data;
		this.controller = new BaseController(data);
	}

	// main program loop. displays the prompt then starts checking for input
	public void Run()
	{
		System.out.print("\nueUML (m for menu): ");
		CommandParsing(scanner.nextLine().split(" "));
	}

	/*	takes the input split by space.
		switch catches the first word and proceeds from there
		each command first checks if the proper number of parameters are present in the input line
		if not, it prints the proper syntax for that command from UI.CommandSyntax.
		if so, it runs the command.
	*/
	public void CommandParsing(String[] input)
	{
		switch (input[0].toLowerCase())
		{
			case "c":
			case "classes":
				UI.ListClasses(data.getClassItems());
				break;
			case "r":
			case "relationships":
				UI.ListRelationships(data.getRelationshipItems());
				break;
			case "v":
			case "view":
				if (input.length != 2)
					{ System.out.println("Syntax: " + UI.CommandSyntax(input[0]));
					return; }
				System.out.println(UI.ListClass(
					data.getClassItems().get(input[1]), data.getRelationshipItems()));
				break;
			case "h":
			case "help":
				UI.Help();
				break;
			case "save":
				if (input.length != 2)
					{ System.out.println("Syntax: " + UI.CommandSyntax(input[0]));
					return; }
				System.out.println(data.Save(input[1]));
				break;
			case "load":
				if (input.length != 2)
					{ System.out.println("Syntax: " + UI.CommandSyntax(input[0]));
					return; }
				System.out.println(data.Load(input[1]));
				break;
			case "e":
			case "exit":
				UI.Exit();
				break;
			case "m":
				UI.Menu(false);
				break;
			case "69": // shhh
				//tester();
				break;
			/*
			case "edit":
				if (input.length != 2)
				{ System.out.println("Syntax: " + UI.CommandSyntax(input[0]));
					return; }
				if(data.getClassItems().containsKey(input[1])){
					ClassItem.addToClassMenu(data.getClassItems().get(input[1]),scanner);
				}
				break;
			*/
			case "addclass":
			// Our switch statements typically take this form.
			// First we check the syntax of the command to make sure it's correct.
				if (input.length != 2)
					{ System.out.println("Syntax: " + UI.CommandSyntax(input[0]));
					return; }
				// Then we call the controller method for adding a class and since the controller returns a String, we print the result.
				// input[1] = class name input
				System.out.println(controller.AddClassListener(input[1]));
				UI.editClass(input[1], scanner, controller);
				// Then we use UI which is another view module of the program to list the classes.
				UI.ListClasses(data.getClassItems());
				break;
			case "removeclass":
				if (input.length != 2)
					{ System.out.println("Syntax: " + UI.CommandSyntax(input[0]));
					return; }
				System.out.println(controller.RemoveClassListener(input[1]));
				UI.ListClasses(data.getClassItems());
				break;
			case "renameclass":
				if (input.length != 3)
					{ System.out.println("Syntax: " + UI.CommandSyntax(input[0]));
					return; }
				System.out.println(controller.RenameClassListener(input[1], input[2]));
				UI.ListClasses(data.getClassItems());
				break;

			case "addrelation":
				if (input.length != 3)
					{ System.out.println("Syntax: " + UI.CommandSyntax(input[0]));
					return; }
				System.out.println(controller.AddRelationshipListener(input[1], input[2]));
				UI.ListRelationships(data.getRelationshipItems());
				break;
			case "removerelation":
				if (input.length != 3)
					{ System.out.println("Syntax: " + UI.CommandSyntax(input[0]));
					return; }
				System.out.println(controller.RemoveRelationshipListener(input[1], input[2]));
				UI.ListRelationships(data.getRelationshipItems());
				break;

			case "addfield":
				if (input.length < 4)
					{ System.out.println("Syntax: " + UI.CommandSyntax(input[0]));
					return; }
				System.out.println(controller.AddFieldListener(input[1], input[2], input[3]));
				System.out.println(UI.ListClass(data.getClassItems().get(input[1]), data.getRelationshipItems()));
				break;
			case "removefield":
				if (input.length < 3)
					{ System.out.println("Syntax: " + UI.CommandSyntax(input[0]));
					return; }
				System.out.println(controller.RemoveFieldListener(input[1], input[2]));
				System.out.println(UI.ListClass(data.getClassItems().get(input[1]), data.getRelationshipItems()));
				break;
			case "renamefield":
				if (input.length < 4)
					{ System.out.println("Syntax: " + UI.CommandSyntax(input[0]));
					return; }
				System.out.println(controller.RenameFieldListener(input[1], input[2], input[3]));
				System.out.println(UI.ListClass(data.getClassItems().get(input[1]), data.getRelationshipItems()));
				break;
				
			case "addmethod":
				if (input.length < 3)
					{ System.out.println("Syntax: " + UI.CommandSyntax(input[0]));
					return; }
				System.out.println(controller.AddMethodListener(input[1], input[2]));
				System.out.println(UI.ListClass(data.getClassItems().get(input[1]), data.getRelationshipItems()));
				break;
			case "removemethod":
				if (input.length < 3)
					{ System.out.println("Syntax: " + UI.CommandSyntax(input[0]));
					return; }
					System.out.println(controller.RemoveMethodListener(input[1], input[2]));
				System.out.println(UI.ListClass(data.getClassItems().get(input[1]), data.getRelationshipItems()));
				break;
			case "renamemethod":
				if (input.length < 4)
					{ System.out.println("Syntax: " + UI.CommandSyntax(input[0]));
					return; }
				System.out.println(controller.RenameMethodListener(input[1], input[2], input[3]));
				System.out.println(UI.ListClass(data.getClassItems().get(input[1]), data.getRelationshipItems()));
				break;

			case "addparameter":
				if (input.length < 5)
					{ System.out.println("Syntax: " + UI.CommandSyntax(input[0]));
					return; }
				System.out.println(controller.AddParameterListener(input[1], input[2], input[3], input[4]));
				System.out.println(UI.ListClass(data.getClassItems().get(input[1]), data.getRelationshipItems()));
				break;
			case "removeparameter":
				if (input.length < 5)
					{ System.out.println("Syntax: " + UI.CommandSyntax(input[0]));
					return; }
				System.out.println(controller.RemoveParameterListener(input[1], input[2], input[3], input[4]));
				System.out.println(UI.ListClass(data.getClassItems().get(input[1]), data.getRelationshipItems()));
				break;
			case "changeparameter":
				if (input.length < 7)
					{ System.out.println("Syntax: " + UI.CommandSyntax(input[0]));
					return; }
				System.out.println(controller.ChangeParameterListener(input[1], input[2], input[3], input[4], input[5], input[6]));
				System.out.println(UI.ListClass(data.getClassItems().get(input[1]), data.getRelationshipItems()));
				break;

			default:
				System.out.println("unknown command");
		}
	}

	/*
	// simple test method for us (shhh!)
	private void tester() 
	{
		HashMap<String, ClassItem> classItems = data.getClassItems();
		HashMap<String, RelationshipItem> relationshipItems = data.getRelationshipItems();

		ClassItem.addClassItem(classItems, "mcdonalds");
		classItems.get("mcdonalds").addField("location", "String");
		classItems.get("mcdonalds").addField("owner", "person");
		classItems.get("mcdonalds").addMethod("cook_fries");
		classItems.get("mcdonalds").addMethod("cook_burger");
		classItems.get("mcdonalds").getMethodItems().get("cook_fries").addParameter("int", "time");
		classItems.get("mcdonalds").getMethodItems().get("cook_fries").addParameter("String", "potatoes");
		classItems.get("mcdonalds").getMethodItems().get("cook_burger").addParameter("int", "time");
		classItems.get("mcdonalds").getMethodItems().get("cook_burger").addParameter("String", "patty");

		ClassItem.addClassItem(classItems, "tacobell");
		classItems.get("tacobell").addField("location", "String");
		classItems.get("tacobell").addField)("owner", "Person");
		classItems.get("tacobell").addMethod("cook_taco");
		classItems.get("tacobell").addMethod("cook_casadilla");
		classItems.get("tacobell").getMethodItems().get("cook_taco").addParameter("int", "time");
		classItems.get("tacobell").getMethodItems().get("cook_taco").addParameter("String", "meat");
		classItems.get("tacobell").getMethodItems().get("cook_casadilla").addParameter("int", "time");
		classItems.get("tacobell").getMethodItems().get("cook_casadilla").addParameter("String", "chicken");

		RelationshipItem relationship = new RelationshipItem(
				classItems.get("mcdonalds"), classItems.get("tacobell"));

		// adding the relationship to the map
		String key = "mcdonalds_tacobell";
		relationshipItems.put(key, relationship);
	}
	*/
}
