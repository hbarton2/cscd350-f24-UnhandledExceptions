package com.unhandledexceptions.View;

import java.util.Scanner;
import java.util.ArrayList;

import com.unhandledexceptions.Model.Data;
import com.unhandledexceptions.Controller.BaseController;
import jline.console.ConsoleReader;
import com.unhandledexceptions.Controller.CommandCompleter;
import java.io.IOException;

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
		try{
			System.out.print("\nueUML (m for menu): ");
			// create a new console reader, similar to Scanner
			ConsoleReader reader = new ConsoleReader();
			// add our completer to the console reader with our list of commands from UI.java
			// this will autocomplete only our commands and not any other input
			reader.addCompleter(new CommandCompleter(
				new ArrayList<>(UI.getCommands().keySet())));

			// read the input line with the reader and execute menu call
			CommandParsing(reader, reader.readLine().split(" "));
		} catch(IOException e){
			e.printStackTrace();
		}
		
	}

	/*	takes the input split by space.
		switch catches the first word and proceeds from there
		each command first checks if the proper number of parameters are present in the input line
		if not, it prints the proper syntax for that command from UI.CommandSyntax.
		if so, it runs the command.
	*/
	public void CommandParsing(ConsoleReader reader, String[] input)
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
			case "addclass":
			// Our switch statements typically take this form.
			// First we check the syntax of the command to make sure it's correct.
				if (input.length != 2)
					{ System.out.println("Syntax: " + UI.CommandSyntax(input[0]));
					return; }
				// Then we call the controller method for adding a class and since the controller returns a String, we print the result.
				// input[1] = class name input
				System.out.println(controller.AddClassListener(input[1]));
				// Then we use UI which is another view module of the program to edit the class that was just created
				UI.editClass(input[1], reader, controller);
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
				if (input.length != 4)
					{ System.out.println("Syntax: " + UI.CommandSyntax(input[0]));
					return; }
				System.out.println(controller.AddRelationshipListener(input[1], input[2], input[3]));
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

}
