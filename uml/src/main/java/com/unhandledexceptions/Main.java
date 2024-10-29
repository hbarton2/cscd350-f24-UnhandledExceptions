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
		System.out.print("1. CLI (type cli or c)\n2. GUI (type gui or g)\n>");

		Scanner scanner = new Scanner(System.in);
		String input = scanner.nextLine().toLowerCase();
		if (input.equals("cli") || input.equals("c"))
		{
			CLI cli = new CLI(new Data());
			while (true)
				cli.Run();
		}
		else if(input.equals("gui") || input.equals("g"))
		{
			GUI.main();
		}
		else
		{
			System.err.println("Invalid Input. please re-run the program and try again.");
		}
		scanner.close();
	}
};
