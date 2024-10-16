import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {
	UI ui = new UI();
	Map<String, ClassItem> classItems = new HashMap<>();
	Map<String, RelationshipItem> relationshipItems = new HashMap<>();
	Scanner scanner = new Scanner(System.in);
	String input;

	public static void main(String[] args) {
		Main main = new Main();
		main.Menu();
		while (true)
			main.Run();
	}

	public void Run() {
		System.out.print("Type your choice (m for menu): ");
		input = scanner.nextLine();

		switch (input) {
			case "0": // list class
				System.out.print("Input name of class you would like details about:\n>");
				input = scanner.nextLine();
				ui.ListClass(classItems.get(input));
				break;
			case "1": // List Classes
				ui.ListClasses(classItems);

				// list class for testing
				// System.out.println(" Classes:");

				// for (Map.Entry<String, ClassItem> entry : classItems.entrySet()) {
				// String key = entry.getKey();
				// //ClassItem value = entry.getValue();
				// System.out.println(" -" + key);
				// }

				break;
			case "2": // List Relationships
				ui.ListRelationships(relationshipItems);
				break;
			case "3": // Add a Relationship
				System.out.println(RelationshipItem.addRelationship(relationshipItems, classItems));
				break;
			case "4": // Delete a Relationship
				System.out.println(RelationshipItem.removeRelationship(relationshipItems));
				break;
			case "5": // Add a Class
				System.out.print("Input name of class you would like to create:\n>");
				input = scanner.nextLine();
				System.out.println(ClassItem.addClassItem(classItems, input));
				break;
			case "6": // Delete a Class
				ui.ListClasses(classItems);
				System.out.print("Input name of class you would like to delete\n>");
				input = scanner.nextLine();
				System.out.println(ClassItem.removeClassItem(classItems, input, relationshipItems));
				break;
			case "7": // Rename a Class
				ui.ListClasses(classItems);
				System.out.print("Input name of class you want to rename\n>");
				String oldName = scanner.nextLine();
				System.out.println("Input new name for " + oldName);
				String newName = scanner.nextLine();
				System.out.println(ClassItem.renameClassItem(classItems, newName, oldName));
				break;
			case "8": // Add a Method
				System.out.print("Input name of class to add method to\n>");
				ClassItem tempClassItemAdd = classItems.get(scanner.nextLine());

				if (tempClassItemAdd == null) {
					System.out.println("Class does not exist.");
					return;
				}

				System.out.print("Input name of method you want to add\n>");
				System.out.println(tempClassItemAdd.addMethod(scanner.nextLine()));

				break;
			case "9": // Remove a Method
				System.out.print("Input name of class to remove method from\n>");
				ClassItem tempClassItemRemove = classItems.get(scanner.nextLine());

				if (tempClassItemRemove == null) {
					System.out.println("Class does not exist.");
					return;
				}

				System.out.println("Methods in selected class: " + tempClassItemRemove.methodItems.keySet());
				System.out.print("Input name of method you want to remove\n>");
				System.out.println(tempClassItemRemove.removeMethod(scanner.nextLine()));

				break;
			case "10": // Add a Parameter
				System.out.print("Input name of class method belongs to\n>");
				ClassItem tempClassItemAddParameter = classItems.get(scanner.nextLine());

				if (tempClassItemAddParameter == null) {
					System.out.println("Class does not exist.");
					return;
				}

				System.out.println("Methods in selected class: " + tempClassItemAddParameter.methodItems.keySet());
				System.out.print("Input name of method to add parameter to\n>");
				MethodItem tempMethodItemAddParameter = tempClassItemAddParameter.methodItems
						.get(scanner.nextLine());

				if (tempMethodItemAddParameter == null) {
					System.out.println("Method does not exist.");
					return;
				}
				System.out.print("Input name of parameter to add\n>");
				System.out.println(tempMethodItemAddParameter.addParameter(scanner.nextLine()));

				break;
			case "11": // Remove a Parameter
				System.out.print("Input name of class method belongs to\n>");
				ClassItem tempClassItemRemoveParameter = classItems.get(scanner.nextLine());

				if (tempClassItemRemoveParameter == null) {
					System.out.println("Class does not exist.");
					return;
				}

				System.out.println("Methods in selected class: " + tempClassItemRemoveParameter.methodItems.keySet());
				System.out.print("Input name of method to remove parameter from\n>");
				MethodItem tempMethodItemRemoveParameter = tempClassItemRemoveParameter.methodItems
						.get(scanner.nextLine());

				if (tempMethodItemRemoveParameter == null) {
					System.out.println("Method does not exist.");
					return;
				}

				System.out.print("Input name of parameter to remove\n>");
				System.out.println(tempMethodItemRemoveParameter.removeParameter(scanner.nextLine()));

				break;
			case "12": // Change a Parameter
				System.out.print("Input name of class method belongs to\n>");
				ClassItem tempClassItemChangeParameter = classItems.get(scanner.nextLine());
				
				if (tempClassItemChangeParameter == null) {
					System.out.println("Class does not exist.");
					return;
				}

				System.out.println("Methods in selected class: " + tempClassItemChangeParameter.methodItems.keySet());
				System.out.print("Input name of method parameter belongs to\n>");
				MethodItem tempMethodItemChangeParameter = tempClassItemChangeParameter.methodItems
						.get(scanner.nextLine());

				if (tempMethodItemChangeParameter == null) {
					System.out.println("Method does not exist.");
					return;
				}

				System.out.print("Input name of parameter to change\n>");
				String oldParameterNameChange = scanner.nextLine();
				System.out.println("Input new name for the parameter: " + oldParameterNameChange);
				String newParameterNameChange = scanner.nextLine();
				System.out.println(
						tempMethodItemChangeParameter.changeParameter(oldParameterNameChange, newParameterNameChange));

				break;
			case "13":	//Add a Field
				ui.ListClasses(classItems);
				if(!classItems.isEmpty()){
					System.out.print("Input the name of the class that you want to add a field to:\n>");
					ClassItem tempClassItem = classItems.get(scanner.nextLine());

					if(tempClassItem != null){
						System.out.print("Input name of field you want to add to " + tempClassItem.getClassItemName() + "\n> ");	
						System.out.println(tempClassItem.addField(scanner.nextLine()));
					} else {
						System.out.println("Class Item does not exist.");
					}
				} else {
					System.out.println("No classes to add field too.");
				}
				break;
			case "14":	//Remove a Field
				ui.ListClasses(classItems);
				if(!classItems.isEmpty()){
					System.out.print("Input the name of the class that you want to remove a field from:\n>");
					ClassItem tempClassItem = classItems.get(scanner.nextLine());

					if(tempClassItem != null){
						System.out.println("Fields in selected class: " + tempClassItem.fieldItems.keySet());
						System.out.print("Input name of field you want to remove from " + tempClassItem.getClassItemName() + "\n> ");	
						System.out.println(tempClassItem.removeField(scanner.nextLine()));
					} else {
						System.out.println("Class Item does not exist.");
					}
				} else {
					System.out.println("No classes to remove field from.");
				}
			break;
			case "15":	//Rename a Field
				ui.ListClasses(classItems);
				if(!classItems.isEmpty()){
					System.out.print("Input the name of the class that you want to rename a field in:\n>");
					ClassItem tempClassItem = classItems.get(scanner.nextLine());

					if(tempClassItem != null){
						System.out.println("Fields in selected class: " + tempClassItem.fieldItems.keySet());
						System.out.print("Input name of field you want to rename from " + tempClassItem.getClassItemName() + "\n> ");	
						String fieldNameOld = scanner.nextLine();
						System.out.println("Input name to rename " + fieldNameOld + " to: \n>");
						System.out.println(tempClassItem.renameField(fieldNameOld, scanner.nextLine()));
					} else {
						System.out.println("Class Item does not exist.");
					}
				} else {
					System.out.println("No classes to rename field of.");
				}
			break;
			case "h":
			case "help":
				ui.Help();
				break;
			case "e":	//exit
			case "exit": // Exit
				scanner.close();
				ui.Exit();
				break;
			case "m": //menu
				Menu();
				break;
			default:
				System.out.println("Unknown command.");
		}
	}
	
	private void Menu()
	{
		System.out.println("\n-=Unhandled Exceptions UML Editor=-");
		System.out.println("0. List Class Info");
		System.out.println("1. List Classes");
		System.out.println("2. List Relationships");
		System.out.println("3. Add Relationship");
		System.out.println("4. Delete Relationship");
		System.out.println("5. Add a Class");
		System.out.println("6. Delete a Class");
		System.out.println("7. Rename a Class");
		System.out.println("8. Add a Method");
		System.out.println("9. Remove a Method");
		System.out.println("10. Add a Parameter");
		System.out.println("11. Remove a Parameter");
		System.out.println("12. Change a Parameter");
		System.out.println("13. Add a Field");
		System.out.println("14. Remove a Field");
		System.out.println("15. Rename a Field");
		System.out.println("h or help for program assistance");
		System.out.println("e or exit. Exit the program.");
	}
};