import java.util.Scanner;

public class Main 
{
	// prints the program name and then starts looping run.
	public static void main(String[] args)
	{
		System.out.println("\n-=Unhandled Exceptions UML Editor=-");
		System.out.print("1. CLI\n2. GUI\n>");

		Scanner scanner = new Scanner(System.in);
		String input = scanner.nextLine();
		if (input.equals("1"))
		{
			CLI cli = new CLI(new Data());
			while (true)
				cli.Run();
		}
		else if (input.equals("2"))
		{
			GUI gui = new GUI(new Data());
			while(true)
				gui.Run();
		}
		else
		{
			System.err.println("Invalid input. Try again.");
		}
	}

};