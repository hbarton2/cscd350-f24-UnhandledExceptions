import java.util.Scanner;

public class Main 
{
	// prints the program name and then starts looping run.
	public static void main(String[] args)
	{
		System.out.println("\n-=Unhandled Exceptions UML Editor=-");
		System.out.print("1. CLI\n2. GUI");

		Scanner scanner = new Scanner(System.in);
		if (scanner.nextLine() == "1")
		{
			scanner.close();
			CLI cli = new CLI(new Data());
			while (true)
				cli.Run();
		}
		else
		{
			scanner.close();
			GUI gui = new GUI(new Data());
			while (true)
				gui.Run();
		}
	}

};