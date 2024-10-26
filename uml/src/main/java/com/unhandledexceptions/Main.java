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
		System.out.print("1. CLI\n2. GUI\n>");

		Scanner scanner = new Scanner(System.in);
		if (scanner.nextLine().equals("1"))
		{
			CLI cli = new CLI(new Data());
			while (true)
				cli.Run();
		}
		else
		{
			GUI.main();
		}
		scanner.close();
	}
};
