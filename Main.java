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
		while (true)
			main.Run();
	}

	public void Run() {
		System.out.println("-=Unhandled Exceptions UML Editor=-");
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
		System.out.println("13. Exit");
		input = scanner.nextLine();

		switch (input) {
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
				System.out.println("Input name of class you would like to create");
				input = scanner.nextLine();
				System.out.println(ClassItem.addClassItem(classItems, input));
				break;
			case "6": // Delete a Class
				System.out.println("Input name of class you would like to delete");
				input = scanner.nextLine();
				System.out.println(ClassItem.removeClassItem(classItems, input, relationshipItems));
				break;
			case "7": // Rename a Class
				System.out.println("Input name of class you want to rename");
				String oldName = scanner.nextLine();
				System.out.println("Input new name for " + oldName);
				String newName = scanner.nextLine();
				ClassItem.renameClassItem(classItems, newName, oldName);
				break;
			case "8": // Add a Method
				System.out.println("Input name of class to add method to");
				ClassItem tempClassItemAdd = classItems.get(scanner.nextLine());

				if (tempClassItemAdd == null) {
					System.out.println("Class does not exist.");
					return;
				}

				System.out.println("Input name of method you want to add");
				System.out.println(tempClassItemAdd.addMethod(scanner.nextLine()));

				break;
			case "9": // Remove a Method
				System.out.println("Input name of class to remove method from");
				ClassItem tempClassItemRemove = classItems.get(scanner.nextLine());

				if (tempClassItemRemove == null) {
					System.out.println("Class does not exist.");
					return;
				}

				System.out.println("Methods in selected class: " + tempClassItemRemove.methodItems.keySet());
				System.out.println("Input name of method you want to remove");
				System.out.println(tempClassItemRemove.removeMethod(scanner.nextLine()));

				break;
			case "10": // Add a Parameter
				System.out.println("Input name of class method belongs to");
				ClassItem tempClassItemAddParameter = classItems.get(scanner.nextLine());

				if (tempClassItemAddParameter == null) {
					System.out.println("Class does not exist.");
					return;
				}

				System.out.println("Methods in selected class: " + tempClassItemAddParameter.methodItems.keySet());
				System.out.println("Input name of method to add parameter to");
				MethodItem tempMethodItemAddParameter = tempClassItemAddParameter.methodItems
						.get(scanner.nextLine());

				if (tempMethodItemAddParameter == null) {
					System.out.println("Method does not exist.");
					return;
				}
				System.out.println("Input name of parameter to add");
				System.out.println(tempMethodItemAddParameter.addParameter(scanner.nextLine()));

				break;
			case "11": // Remove a Parameter
				System.out.println("Input name of class method belongs to");
				ClassItem tempClassItemRemoveParameter = classItems.get(scanner.nextLine());

				if (tempClassItemRemoveParameter == null) {
					System.out.println("Class does not exist.");
					return;
				}

				System.out.println("Methods in selected class: " + tempClassItemRemoveParameter.methodItems.keySet());
				System.out.println("Input name of method to remove parameter from");
				MethodItem tempMethodItemRemoveParameter = tempClassItemRemoveParameter.methodItems
						.get(scanner.nextLine());

				if (tempMethodItemRemoveParameter == null) {
					System.out.println("Method does not exist.");
					return;
				}

				System.out.println("Input name of parameter to remove");
				System.out.println(tempMethodItemRemoveParameter.removeParameter(scanner.nextLine()));

				break;
			case "12": // Change a Parameter
				System.out.println("Input name of class method belongs to");
				ClassItem tempClassItemChangeParameter = classItems.get(scanner.nextLine());

				if (tempClassItemChangeParameter == null) {
					System.out.println("Class does not exist.");
					return;
				}

				System.out.println("Methods in selected class: " + tempClassItemChangeParameter.methodItems.keySet());
				System.out.println("Input name of method parameter belongs to");
				MethodItem tempMethodItemChangeParameter = tempClassItemChangeParameter.methodItems
						.get(scanner.nextLine());

				if (tempMethodItemChangeParameter == null) {
					System.out.println("Method does not exist.");
					return;
				}

				System.out.println("Input name of parameter to change");
				String oldParameterNameChange = scanner.nextLine();
				System.out.println("Input new name for the parameter: " + oldParameterNameChange);
				String newParameterNameChange = scanner.nextLine();
				System.out.println(
						tempMethodItemChangeParameter.changeParameter(oldParameterNameChange, newParameterNameChange));

				break;
			case "13": // Exit
				ui.Exit();
				// add scanner.close() inside of ui.Exit() method
				break;
			default:
				System.out.println("Default: ?");
		}
	}
};