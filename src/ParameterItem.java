/*
    - Class to create a ParameterItem object
    - ParameterItems are constructed with a String representing the name
    - ParameterItems will be stored into a map in the MethodItem class
*/

import java.util.Scanner;

public class ParameterItem {
    // enum to check if the data type is valid
    public enum DataType {
        INT,
        FLOAT,
        DOUBLE,
        STRING,
        BOOLEAN,
        CHAR,
        LONG,
        BYTE,
        SHORT;

        // method to check if a string is a valid DataType
        public static boolean isValidType(String type) {
            try {
                DataType.valueOf(type.toUpperCase());
                return true;
            } catch (IllegalArgumentException e) {
                return false;
            }
        }
    }

    // String representing the name
    private String parameterName;
    private String type;

    public ParameterItem() {} //blank constructor for IO serialization

    // constructor
    public ParameterItem(String type, String parameterName) {
        if (parameterName == null || parameterName.isBlank()) {
            throw new IllegalArgumentException("Type and Parameter name cannot be null or blank - constructor");
        }

        if (DataType.isValidType(type)) {
            this.parameterName = parameterName;
            this.type = type;
        } else {
            throw new IllegalArgumentException("Invalid type in parameter constructor");
        }

    }

    // getter to retrieve the parameter name
    public String getParameterName() {
        return this.parameterName;
    }

    // setter to set the parameter name
    public void setParameterName(String parameterName) {
        if (parameterName == null || parameterName.isBlank()) {
            throw new IllegalArgumentException("Parameter name cannot be null or blank");
        }

        this.parameterName = parameterName;
    }

    public static String addParameter(MethodItem methodItem, String type, String parameterName, Scanner kb)
    {
       // preconditions
		if (parameterName == null || parameterName.isBlank()) {
			throw new IllegalArgumentException("Parameter name cannot be null or blank");
		}

		// strip input to remove any leading or trailing whitespace
		parameterName = parameterName.trim();

		// check if the parameter already exists
		if (methodItem.getParameters().containsKey(parameterName)) {
			// return failure message
			return "Parameter name: " + parameterName + " already in use.";
		}

		try {

			// create parameter object
			ParameterItem parameter = new ParameterItem(type, parameterName);
			// insert new parameter item into map
			methodItem.getParameters().put(parameterName, parameter);

		} catch (IllegalArgumentException e) {
			return e.getMessage();
		}

		// return successful add of parameter
		return "Parameter: " + type + " " + parameterName + " successfully added.";
    }

    public static String removeParameter(MethodItem methodItem, String type, String parameterName)
    {
        // preconditions
		if (parameterName == null || parameterName.isBlank()) {
			throw new IllegalArgumentException("Parameter name cannot be null or blank");
		}

		// strip input to remove any leading or trailing whitespace
		parameterName = parameterName.trim();

		// check if the parameter exists in the map
		if (!methodItem.getParameters().containsKey(parameterName)) {
			// return failure (parameter not in map)
			return "Parameter: " + type + " " + parameterName + " does not exist";
		}

		// remove parameter from map
		methodItem.getParameters().remove(parameterName);

		// return successful remove message
		return "Parameter: " + type + " " + parameterName + " removed successfully.";
    }

    public static String changeParameter(MethodItem methodItem, String oldType, String oldName, String newType, String newName){
        // preconditions
		if (oldName == null || newName == null || oldName.isBlank() || newName.isBlank()) {
			throw new IllegalArgumentException("Parameter name cannot be null or blank");
		}

		// strip input to remove any leading or traililng whitespace
		oldName = oldName.trim();
		newName = newName.trim();

		// check if the new name is valid or is already attached to another
		// ParameterItem
		if (methodItem.getParameters().containsKey(newName)) {
			// return failure message for new name already in use
			return "New Parameter name: " + newName + " already in use.";
		}

		// check if the parameter exists in the map
		if (methodItem.getParameters().containsKey(oldName)) {

			// delete old entry in map
			methodItem.getParameters().remove(oldName);

			// create new parameter object
			ParameterItem newParam = new ParameterItem(newType, newName);

			// add new parameter back to map
			methodItem.getParameters().put(newName, newParam);

			// return successful rename message
			return "Parameter: " + oldType + " " + oldName + " successfully changed to: " + newType + " " + newName;
		} else {
			// old name key does not exist in map
			return "Parameter: " + oldType + " " + oldName + " does not exist.";
		}
    }

    /*
        public String getParameterType(ParameterItem parameter) {
            if (parameter == null) {
                throw new IllegalAccessError("Parameter Item is null");
            }

            return parameter.type;
        }
        
        replacing this with standardized getter and setter for IO serialization.
        saving incase it was necessary.
    */

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    // placeholder toString
    @Override
    public String toString() {
        return this.type + " " + this.parameterName;
    }
}
