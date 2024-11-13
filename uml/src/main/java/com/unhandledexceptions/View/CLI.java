package com.unhandledexceptions.View;

// import java.util.Scanner;
import java.util.ArrayList;

import com.unhandledexceptions.Model.Data;
import com.unhandledexceptions.Controller.BaseController;
import jline.console.ConsoleReader;
import com.unhandledexceptions.Controller.CommandCompleter;
import java.io.IOException;

public final class CLI 
{
	BaseController controller;
	Data data;
  private static CLI instance;

	// private constructor for the CLI. takes in the data model and creates a new controller from the data
	private CLI(Data data)
	{
		this.data = data;
		this.controller = new BaseController(data);
	}

	/**
	 * This method is used to get the instance of the CLI class.
	 * If there isn't an instance created, it will create one and if there is already an instance, it returns it. 
	 * 
	 * @param Data the data object to store data into the model.
	 * @return The instance of the CLI. 
	 */
	public static CLI getInstance(Data data) {
		if (instance == null) {
			instance = new CLI(data);
		}
		return instance;
	}

	private void print(String line)
	{
		System.out.print(line);
	}

	private void println(String line)
	{
		System.out.println(line);
	}

	// main program loop. displays the prompt then starts checking for input
	public void Run()
	{
		try{
			print("\nueUML (m for menu): ");
			// create a new console reader, similar to Scanner
			ConsoleReader reader = new ConsoleReader();
			// add our completer to the console reader with our list of commands from UI.java
			// this will autocomplete only our commands and not any other input
			reader.addCompleter(new CommandCompleter(
				new ArrayList<>(UI.getCommands().keySet())));

			// read the input line with the reader and execute menu call
			println(CommandParsing(reader, reader.readLine().split(" ")));
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
	public String CommandParsing(ConsoleReader reader, String[] input)
	{
		String result = "";
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
				if (input.length != 2) return "Syntax: " + UI.CommandSyntax(input[0]);

				return (UI.ListClass(
					data.getClassItems().get(input[1]), data.getRelationshipItems()));
			case "h":
			case "help":
				UI.Help();
				break;
			case "save":
				if (input.length != 2) return "Syntax: " + UI.CommandSyntax(input[0]);

				return data.Save(input[1]);
			case "load":
				if (input.length != 2) return "Syntax: " + UI.CommandSyntax(input[0]);

				return data.Load(input[1]);
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
				if (input.length != 2) return "Syntax: " + UI.CommandSyntax(input[0]);
				// Then we call the controller method for adding a class and since the controller returns a String, we print the result.
				// input[1] = class name input
				result = controller.AddClassListener(input[1]);
				if (result != "good")
					return result;
				else
					println(input[1] + " added.");
				// Then we use UI which is another view module of the program to edit the class that was just created
				UI.editClass(input[1], reader, controller);
				// Then we use UI which is another view module of the program to list the classes.
				UI.ListClasses(data.getClassItems());
				break;
			case "removeclass":
				if (input.length != 2) return "Syntax: " + UI.CommandSyntax(input[0]);

				result = controller.RemoveClassListener(input[1]);
				if (result != "good")
					return result;
				else
					println(input[1] + " removed.");

				UI.ListClasses(data.getClassItems());
				break;
			case "renameclass":
				if (input.length != 3) return "Syntax: " + UI.CommandSyntax(input[0]);

				result = controller.RenameClassListener(input[1], input[2]);
				if (result != "good")
					return result;
				else
					println(input[1] + " renamed to " + input[2]);

				UI.ListClasses(data.getClassItems());
				break;
			case "addrelation":
				if (input.length != 4) return "Syntax: " + UI.CommandSyntax(input[0]);

				result = controller.AddRelationshipListener(input[1], input[2], input[3]);
				if (result != "good")
					return result;
				else
					println(input[3] + " relation added between " + input[1] + " and " + input[2]);

				UI.ListRelationships(data.getRelationshipItems());
				break;
			case "removerelation":
				if (input.length != 3) return "Syntax: " + UI.CommandSyntax(input[0]);

				result = controller.RemoveRelationshipListener(input[1], input[2]);
				if (result != "good")
					return result;
				else
					println("relation removed between " + input[1] + " and " + input[2]);

				UI.ListRelationships(data.getRelationshipItems());
				break;
			case "addfield":
				if (input.length < 4) return "Syntax: " + UI.CommandSyntax(input[0]);

				result = controller.AddFieldListener(input[1], input[2], input[3]);
				if (result != "good")
					return result;
				else
					println("Field " + input[3] + " added to " + input[1]);

				println(UI.ListClass(data.getClassItems().get(input[1]), data.getRelationshipItems()));
				break;
			case "removefield":
				if (input.length < 3) return "Syntax: " + UI.CommandSyntax(input[0]);

				result = controller.RemoveFieldListener(input[1], input[2]);
				if (result != "good")
					return result;
				else
					println("Field " + input[2] + " removed from " + input[1]);

				println(UI.ListClass(data.getClassItems().get(input[1]), data.getRelationshipItems()));
				break;
			case "renamefield":
				if (input.length < 4) return "Syntax: " + UI.CommandSyntax(input[0]);

				result = controller.RenameFieldListener(input[1], input[2], input[3]);
				if (result != "good")
					return result;
				else
					println("Field " + input[2] + " renamed to " + input[3]);

				println(UI.ListClass(data.getClassItems().get(input[1]), data.getRelationshipItems()));
				break;
				
			case "addmethod":
				if (input.length < 4) return "Syntax: " + UI.CommandSyntax(input[0]);

				result = controller.AddMethodListener(input[1], input[2], input[3]);
				if (result != "good")
					return result;
				else
					println("Method " + input[2] + " added to " + input[1]);

				println(UI.ListClass(data.getClassItems().get(input[1]), data.getRelationshipItems()));
				break;
			case "removemethod":
				if (input.length < 3) return "Syntax: " + UI.CommandSyntax(input[0]);

				result = controller.RemoveMethodListener(input[1], input[2]);
				if (result != "good")
					return result;
				else
					println("Method " + input[2] + " removed from " + input[1]);

				println(UI.ListClass(data.getClassItems().get(input[1]), data.getRelationshipItems()));
				break;
			case "renamemethod":
				if (input.length < 4) return "Syntax: " + UI.CommandSyntax(input[0]);

				result = controller.RenameMethodListener(input[1], input[2], input[3]);
				if (result != "good")
					return result;
				else
					println("Method " + input[2] + " renamed to " + input[3]);

				println(UI.ListClass(data.getClassItems().get(input[1]), data.getRelationshipItems()));
				break;

			case "addparameter":
				if (input.length < 5) return "Syntax: " + UI.CommandSyntax(input[0]);

				result = controller.AddParameterListener(input[1], input[2], input[3], input[4]);
				if (result != "good")
					return result;
				else
					println("Parameter " + input[4] + " added to " + input[2] + " in " + input[1]);

				println(UI.ListClass(data.getClassItems().get(input[1]), data.getRelationshipItems()));
				break;
			case "removeparameter":
				if (input.length < 5) return "Syntax: " + UI.CommandSyntax(input[0]);

				result = controller.RemoveParameterListener(input[1], input[2], input[3], input[4]);
				if (result != "good")
					return result;
				else
					println("Parameter " + input[4] + " removed from " + input[2] + " in " + input[1]);

				println(UI.ListClass(data.getClassItems().get(input[1]), data.getRelationshipItems()));
				break;
			case "changeparameter":
				if (input.length < 7) return "Syntax: " + UI.CommandSyntax(input[0]);

				result = controller.ChangeParameterListener(input[1], input[2], input[3], input[4], input[5], input[6]);
				if (result != "good")
					return result;
				else
					println("Parameter " + input[4] + " changed to " + input[6] + " in " + input[1]);

				println(UI.ListClass(data.getClassItems().get(input[1]), data.getRelationshipItems()));
				break;
			case "undo":
				result = controller.undoListener();
				if (result.equals("good"))
					return "Change Undone.";
				else
					return "Nothing to Undo.";
			case "redo":
				result = controller.redoListener();
				if (result.equals("good"))
					return "Change Redone.";
				else
					return "Nothing to Redo.";

			default:
				return "unknown command";

		}

		return "";
	}
}
