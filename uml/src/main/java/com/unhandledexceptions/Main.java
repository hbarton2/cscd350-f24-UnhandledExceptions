package com.unhandledexceptions;

import java.util.Scanner;

import com.unhandledexceptions.Model.Data;
import com.unhandledexceptions.View.CLI;
import com.unhandledexceptions.View.GUI;

public class Main
{
	// prints the program name and then starts looping run.
	public static void main(String[] args)
	{
		System.out.println("\n-=Unhandled Exceptions UML Editor=-");

        for (String arg : args) 
		{
            if (arg.equals("--cli")) 
			{
				// this is how to construct the CLI in the singleton pattern, ensures there is only one instance.
				CLI cli = CLI.getInstance(new Data());
				while (true)
					cli.Run();
			}
			else if (arg.equals("--gui")) 
			{
				GUI.main();
				return;
			}
        }
		
		System.out.print("Command line mode: (type cli or c)\nGraphical mode: (type gui or g)\nExit: (anything else)\n>");
		Scanner scanner = new Scanner(System.in);
		String input = scanner.nextLine().toLowerCase();
		if (input.equals("cli") || input.equals("c"))
		{
			// this is how to construct the CLI in the singleton pattern, ensures there is only one instance.
			CLI cli = CLI.getInstance(new Data());
			while (true)
				cli.Run();
		}
		else if(input.equals("gui") || input.equals("g"))
		{
			GUI.main();
		}
		else
		{
			System.out.println("Program terminated. Goodbye!");
			scanner.close();
			System.exit(0);
		}		
		scanner.close();
	}
};
