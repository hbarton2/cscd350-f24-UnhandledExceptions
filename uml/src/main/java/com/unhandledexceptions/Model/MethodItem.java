package com.unhandledexceptions.Model;

import java.util.HashMap;

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

	public HashMap<String, ParameterItem> getParameters() {
		return this.parameters;
	}

	public void setParameters(HashMap<String, ParameterItem> parameters) {
		this.parameters = parameters;
	}

	// function to add a new parameter to the hash map
	public static String addParameter(MethodItem methodItem, String type, String parameterName) {
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

	// getter to retrieve a parameter from the map
	public ParameterItem getParameter(String parameterName) {
		return parameters.get(parameterName);
	}

	// function to remove a parameter from the map
	public static String removeParameter(MethodItem methodItem, String type, String parameterName) {
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

	// function to change a parameter name
	public static String changeParameter(MethodItem methodItem, String oldType, String oldName, String newType, String newName) {
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

	// function to print the method and its parameters to a string
	@Override
	public String toString() {
		StringBuilder out = new StringBuilder();
		out.append("Method: ");
		out.append(methodName);
		out.append(" Parameters: ");
		// out.append(parameters.keySet());

		for (ParameterItem param : parameters.values()) {
			out.append("[" + param.toString() + "] ");
		}

		return out.toString();
	}
}
