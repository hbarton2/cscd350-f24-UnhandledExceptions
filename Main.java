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
				System.out.println(ClassItem.removeClassItem(classItems, input));
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
				String classNameAddMethod = scanner.nextLine();
				ClassItem tempClassItemAdd = classItems.get(classNameAddMethod);
				System.out.println("Input name of method you want to add");
				String methodNameAdd = scanner.nextLine();
				System.out.println(tempClassItemAdd.addMethod(methodNameAdd));
				break;
			case "9": // Remove a Method
				System.out.println("Input name of class to remove method from");
				String classNameRemoveMethod = scanner.nextLine();
				ClassItem tempClassItemRemove = classItems.get(classNameRemoveMethod);
				System.out.println("Input name of method you want to remove");
				String methodNameRemove = scanner.nextLine();
				System.out.println(tempClassItemRemove.removeMethod(methodNameRemove));
				break;
			case "10": // Add a Parameter
				System.out.println("Input name of class method belongs to");
				String classNameAddParameter = scanner.nextLine();
				ClassItem tempClassItemAddParameter = classItems.get(classNameAddParameter);
				System.out.println("Methods in selected class: " + tempClassItemAddParameter.methodItems.keySet());// debugging
				System.out.println("Input name of method to add parameter to");
				String methodNameAddParameter = scanner.nextLine();
				MethodItem tempMethodItemAddParameter = tempClassItemAddParameter.methodItems
						.get(methodNameAddParameter);
				System.out.println("Input name of parameter to add");
				String parameterNameAdd = scanner.nextLine();
				System.out.println(tempMethodItemAddParameter.addParameter(parameterNameAdd));
				break;
			case "11": // Remove a Parameter
				System.out.println("Input name of class method belongs to");
				String classNameRemoveParameter = scanner.nextLine();
				ClassItem tempClassItemRemoveParameter = classItems.get(classNameRemoveParameter);
				System.out.println("Input name of method to remove parameter from");
				String methodNameRemoveParameter = scanner.nextLine();
				MethodItem tempMethodItemRemoveParameter = tempClassItemRemoveParameter.methodItems
						.get(methodNameRemoveParameter);
				System.out.println("Input name of parameter to remove");
				String parameterNameRemove = scanner.nextLine();
				System.out.println(tempMethodItemRemoveParameter.removeParameter(parameterNameRemove));
				break;
			case "12": // Change a Parameter
				System.out.println("Input name of class method belongs to");
				String classNameChangeParameter = scanner.nextLine();
				ClassItem tempClassItemChangeParameter = classItems.get(classNameChangeParameter);
				System.out.println("Input name of method parameter belongs to");
				String methodNameChangeParameter = scanner.nextLine();
				MethodItem tempMethodItemChangeParameter = tempClassItemChangeParameter.methodItems
						.get(methodNameChangeParameter);
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