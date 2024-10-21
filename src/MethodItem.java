import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class MethodItem {
	private String methodName;
	// hash map to store ParamterItem's with their name as the key
	private HashMap<String, ParameterItem> parameters;

	public MethodItem() {} //blank constructor for IO serialization

	public MethodItem(String methodName) {
		// preconditions
		if (methodName == null || methodName.isBlank()) {
			throw new IllegalArgumentException("Method name cannot be null or blank - constructor");
		}

		// strip input to remove any leading or trailing whitespace
		methodName.trim();

		this.methodName = methodName;
		// initialize hash map
		this.parameters = new HashMap<>();
	}

	// getter to retrieve method name
	public String getMethodName() {
		return this.methodName;
	}

	// setter to set method name
	public void setMethodName(String newName) {
		if (newName == null || newName.isBlank()) {
			return;
		}

		this.methodName = newName;
	}

	//returns hash map of parameters for this method item
	public HashMap<String, ParameterItem> getParameters() {
		return this.parameters;
	}

	//sets hashmap of parameters for this method item
	public void setParameters(HashMap<String, ParameterItem> parameters) {
		this.parameters = parameters;
	}

	//overload addMethod function for continuous menu
	public static String addMethod(ClassItem classItem, MethodItem methodItem)
	{
		//check if there is an existing method with same name in the class item
		if(!classItem.methodItems.containsKey(methodItem.getMethodName()))
		{
			// create a list to store method item into map
			List<MethodItem> newList = new ArrayList<>();
			
			//add new method item to list
			newList.add(methodItem);
	
			// insert new method item into map
			classItem.methodItems.put(methodItem.getMethodName(), newList);
	
			// return successful add of method
			return "Method name: " + methodItem.getMethodName() + " successfully added.";
		}
		else //method already exists
		{
			//reference list of method items for the given method item
			List<MethodItem> methodList = classItem.methodItems.get(methodItem.getMethodName());

			//boolean to check if method item is unique
			boolean valid = true;

			//iterate through list of method items, checking each one against the new method item
			for(MethodItem aMethod : methodList)
			{
				//if new method item is not unique, set valid to false
				if(!aMethod.isValidOverload(methodItem))
				{
					valid = false;
				}
			}

			//return failure or success ? : valid
			if(valid){
				methodList.add(methodItem);
				return "Method name: " + methodItem.getMethodName() + " successfully added.";
			}else{
				return "Method: " + methodItem.getMethodName() + " already exists in class: " + classItem.getName(); 
			}
		}

	}

	public static String addMethod(ClassItem classItem, String methodName, Scanner kb){
		// preconditions
		if (methodName == null || methodName.isBlank()) {
			throw new IllegalArgumentException("Method name cannot be null or blank");
		}

		// trim any leading or trailing whitespace to ensure valid input
		methodName = methodName.trim();
		methodName = methodName.replace(" ", "");

		// check if the method name already exists in the class
		if (classItem.methodItems.containsKey(methodName)) {
			System.out.println("Method name: " + methodName + " already exists, would you like to overload it? (y/n)");

			//if user wants to overload, display current method items and parameters
			if(kb.nextLine().trim().toLowerCase().equals("y"))
			{
				//get list of current MethodItems
				List<MethodItem> currentMethodItems = classItem.methodItems.get(methodName);
				System.out.println("Current method parameters (Parameters must be different when overloading): ");
				
				//display current MethodItems and Parameters
				for(MethodItem aMethod : currentMethodItems)
				{
					System.out.println(aMethod.toString());
				}
				//create new methodItem object
				MethodItem overload = new MethodItem(methodName);
				String input = "";
				while (true) {
					//prompt user for parameter
					System.out.println("\nInput type and name pairs of parameters you would like to add (Example: 'type1 name1, type2 name2,...')\\n('exit' to quit)>");
					//read parameter from system.in
					input = kb.nextLine();
					
					if(input.trim().toLowerCase().equals("exit"))
					{
						break;
					}

					//split input into type and name
					String [] parts = input.trim().split(" ",2);
					//add new parameter object to method
					ParameterItem.addParameter(overload, parts[0], parts[1]);
				}

				boolean valid = true;

				//check against all other methods in list to ensure it is unique
				for(MethodItem testOverload : currentMethodItems)
				{
					if(!testOverload.isValidOverload(overload)){
						valid = false;
					}
				}
				if(valid)
				{
					//add overloaded method to method list
					currentMethodItems.add(overload);
					return "Method name: " + methodName + " successfully overloaded.";
				}else{
					//return failure to overload
					return "Failed to overload method name: " + methodName;
				}

			}
				// return failure message
				return "Method name: " + methodName + " already in use.";
		}
    	// create a new method object with the method name
        MethodItem newMethod = new MethodItem(methodName);

        // create a list to store method item into map
        List<MethodItem> newList = new ArrayList<>();
        
        //add new method item to list
        newList.add(newMethod);

        // insert new method item into map
        classItem.methodItems.put(methodName, newList);

        // return successful add of method
        return "Method name: " + methodName + " successfully added.";
	}

	//method to remove a methoditem from a class item (checks overload)
	public static String removeMethod(ClassItem classItem, String methodName, Scanner kb)
	{
		// preconditions
        if (methodName == null || methodName.isBlank()) {
            throw new IllegalArgumentException("Method name cannot be null or blank");
        }

        // trim any leading or trailing whitespace to ensure valid input
        methodName = methodName.trim();

        // check if the method name is a valid key
        if (!classItem.methodItems.containsKey(methodName)) {
            // return failure message
            return "Method name: " + methodName + " does not exist";
        }

        //reference list for given key to see if overloaded methods exist
        List<MethodItem> removeList = classItem.methodItems.get(methodName);

        //if size == 1 only 1 method stored in list and can be removed
        if(removeList.size() == 1)
        {
            // remove method item from hash map
            classItem.methodItems.remove(methodName);

            // return successful removal of method
            return "Method name: " + methodName + " successfully removed";
        }
        else
        {
            //overload method exists
            System.out.println("Multiple methods exist for name: " + methodName + ", select which to remove.");

            //iterate through each method present in list and prompt to keep or remove it
            for(MethodItem rMethod : removeList)
            {
                System.out.println(rMethod.toString());
                System.out.println("Do you want to remove this method? (y/n)");
                if(kb.nextLine().trim().toLowerCase().equals("y"))
                {
                    //remove specified methods from list
                    removeList.remove(rMethod);

                    return "Method removed successfully.";
                }
            }

            //return successful removal
            return "Methods removed successfully.";
        }
	}

	public static String renameMethod(ClassItem classItem, String oldName, String newName)
	{
		// preconditions
        if (oldName == null || oldName.isBlank() || newName == null || newName.isBlank()) {
            return "Method names cannot be null or blank.";
        }

        // trim any leading or trailing whitespace
        oldName = oldName.trim();
        newName = newName.trim();

        // check if the new name is already taken
        if (classItem.methodItems.containsKey(newName)) {
            return "Method name: " + newName + " already in use.";
        }

        // check if the old name is a valid key
        if (classItem.methodItems.containsKey(oldName)) {

            //get list of method items
            List<MethodItem> changeList = classItem.methodItems.get(oldName);
            
            //change all method items in list
            for(MethodItem cMethod : changeList)
            {
                cMethod.setMethodName(newName);
            }

            // remove old method from map
            classItem.methodItems.remove(oldName);

            // add new method item list into class under new name
            classItem.methodItems.put(newName, changeList);
            
            // return success
            return "Method name: " + oldName + " successfully changed to " + newName;
        } else {
            // invalid method name, return failure
            return "Method name: " + oldName + " does not exist.";
        }
	}

	// function to add a new parameter to a class item
	public static String addParameter(List<MethodItem> methodList, Scanner kb, String type, String name) {

		//checks if there are overloaded methods present in the list
		if(methodList.size() == 1)
		{
			//if only one exists, reference the method item object
			MethodItem tempMethodItemAddParameter = methodList.get(0);

			if (tempMethodItemAddParameter == null) {
				return "Method does not exist.";
			}
			
			//pass the method item object, parameter type, and name to addParameter function
			return ParameterItem.addParameter(tempMethodItemAddParameter, type, name);
			
		}
		else // overloaded method exists
		{
			System.out.println("Overloaded methods found, select which method to add parameter to: ");

			//iterate through list of method items and prompts user to select which of the existing overloaded methods to add to
			for(MethodItem addParamMethod : methodList)
			{

				System.out.println(addParamMethod.toString());

				System.out.println("Would you like to add the parameter to this method? (y/n)");

				if(kb.nextLine().trim().toLowerCase().equals("y"))
				{
					//if desired method is found, pass that method item, parameter type, and name to the addParameter function
					return ParameterItem.addParameter(addParamMethod, type, name);
				}
				
			}

			//if no methods are selected return failure
			return "Failed to add parameter.";
		}

	}

	//Overloaded addParameter method for continuous menu (accepts a method item that has not been added to a class item yet)
	public static String addParameter(MethodItem methodItem, String type, String name)
	{
		//passes a method item, parameter type, and name to addParameter function
		return ParameterItem.addParameter(methodItem, type, name);
	}

	public static String removeParameter(List<MethodItem> methodList, Scanner kb, String type, String name) {

		//check if the methodList exists
		if(methodList == null){
			return "Method does not exist";
		}

		//check if method is overloaded
		if(methodList.size() == 1)
		{
			//if method is not overloaded, reference only method item object
			MethodItem tempMethodItemRemoveParameter = methodList.get(0);

			//if method item is null return
			if (tempMethodItemRemoveParameter == null) {
				return "Method does not exist.";
			}

			//pass method item, parameter type, and name to removeParameter function
			return ParameterItem.removeParameter(tempMethodItemRemoveParameter, type, name);

		}
		else //overloaded methods exist
		{
			System.out.println("Overloaded methods found, select which method to remove parameter from: ");

			//iterate through all method items in list of method items, prompt user to select which one to remove parameter from
			for(MethodItem removeParamMethod : methodList)
			{
				System.out.println(removeParamMethod.toString());
				
				System.out.println("Would you like to remove a parameter to this method? (y/n)");
				if(kb.nextLine().trim().toLowerCase().equals("y"))
				{
					//pass correct method item, parameter type, and name to removeParameter function
					return ParameterItem.removeParameter(removeParamMethod, type, name);
				}
			}

			//if no method is selected return failure
			return "Failed to remove parameter";
		}

	}

	//method to change a parameter in a method
	public static String changeParameter(List<MethodItem> methodList, String oldType, String oldName, String newType, String newName, Scanner kb) {

		//check if method list contains method items
		if(methodList == null){
			return "Method does not exist";
		}

		//check if method is overloaded
		if(methodList.size() == 1)
		{
			//reference only method item object
			MethodItem methodChangeParam = methodList.get(0);

			//pass method item object, old parameter type, old parameter name, new parameter type, and new parameter name to changeParameter function
			return ParameterItem.changeParameter(methodChangeParam, oldType, oldName, newType, newName);

		}
		else //overloaded method exists
		{
			System.out.println("Overloaded methods found, select which method to change parameter from:");

			//iterate through list of method items and prompt user to select which method to change parameter in
			for(MethodItem aMethod : methodList)
			{
				System.out.println(aMethod.toString());
				System.out.println("Would you like to change a parameter in this method? (y/n)");

				if(kb.nextLine().trim().toLowerCase().equals("y"))
				{
					//pass method item object, old parameter type, old parameter name, new parameter type, and new parameter name to changeParameter function
					return ParameterItem.changeParameter(aMethod, oldType, oldName, newType, newName);

				}
			}

			//if no method is selected return failure
			return "error change param";
		}
		
	}

	// getter to retrieve a parameter from the map
	public ParameterItem getParameter(String parameterName) {
		return parameters.get(parameterName);
	}

	// function to remove a parameter from the map
	public String removeParameter(String type, String parameterName) {
		// preconditions
		if (parameterName == null || parameterName.isBlank()) {
			throw new IllegalArgumentException("Parameter name cannot be null or blank");
		}

		// strip input to remove any leading or trailing whitespace
		parameterName = parameterName.trim();

		// check if the parameter exists in the map
		if (!parameters.containsKey(parameterName)) {
			// return failure (parameter not in map)
			return "Parameter: " + type + " " + parameterName + " does not exist";
		}

		// remove parameter from map
		parameters.remove(parameterName);

		// return successful remove message
		return "Parameter: " + type + " " + parameterName + " removed successfully.";
	}

	// function to change a parameter name
	public String changeParameter(String oldType, String oldName, String newType, String newName) {
		// preconditions
		if (oldName == null || newName == null || oldName.isBlank() || newName.isBlank()) {
			throw new IllegalArgumentException("Parameter name cannot be null or blank");
		}

		// strip input to remove any leading or traililng whitespace
		oldName = oldName.trim();
		newName = newName.trim();

		// check if the new name is valid or is already attached to another
		// ParameterItem
		if (parameters.containsKey(newName)) {
			// return failure message for new name already in use
			return "New Parameter name: " + newName + " already in use.";
		}

		// check if the parameter exists in the map
		if (parameters.containsKey(oldName)) {

			// delete old entry in map
			parameters.remove(oldName);

			// create new parameter object
			ParameterItem newParam = new ParameterItem(newType, newName);

			// add new parameter back to map
			parameters.put(newName, newParam);

			// return successful rename message
			return "Parameter: " + oldType + " " + oldName + " successfully changed to: " + newType + " " + newName;
		} else {
			// old name key does not exist in map
			return "Parameter: " + oldType + " " + oldName + " does not exist.";
		}
	}

	public boolean isValidOverload(MethodItem other)
	{
		// check if size is different
		if(this.parameters.size() != other.parameters.size())
		{
			return true;
		}

		//counter map for the parameter types in the other method
		Map<String, Integer> thisParamTypeCounts = new HashMap<>();
    	for (ParameterItem param : this.parameters.values()) {
        	thisParamTypeCounts.merge(param.getType(), 1, Integer::sum);
    	}

    	//counter map for the parameter types in the other method
    	Map<String, Integer> otherParamTypeCounts = new HashMap<>();
    	for (ParameterItem param : other.parameters.values()) {
        	otherParamTypeCounts.merge(param.getType(), 1, Integer::sum);
    	}

    	// compare the two maps
    	return !thisParamTypeCounts.equals(otherParamTypeCounts);
	}

	// function to print the method and its parameters to a string
	@Override
	public String toString() {
		StringBuilder out = new StringBuilder();
		out.append("Method: ");
		out.append(methodName);
		out.append(" Parameters: ");
		// out.append(parameters.keySet());

		if(parameters.isEmpty())
		{
			out.append("None");
		}
		else
		{
			for (ParameterItem param : parameters.values()) {
				out.append("[" + param.toString() + "] ");
			}
		}

		return out.toString();
	}
}
