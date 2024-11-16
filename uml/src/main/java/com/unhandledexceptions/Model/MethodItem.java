package com.unhandledexceptions.Model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.HashMap;

public class MethodItem implements UMLObject {
	private String methodName;
	// return type for the method
	private String type;
	// hash map to store ParamterItem's with their name as the key
	private HashMap<String, ParameterItem> parameters;
	// property change support for updating parameter items
	private final PropertyChangeSupport support = new PropertyChangeSupport(this);

	public MethodItem() {
	} // blank constructor for IO serialization

	public MethodItem(String methodName, String type) {
		// preconditions
		if (methodName == null || methodName.isBlank() || type == null || type.isBlank()) {
			throw new IllegalArgumentException("Method name or type cannot be null or blank - constructor");
		}

		// strip input to remove any leading or trailing whitespace
		methodName.trim();
		type.trim();

		this.methodName = methodName;
		this.type = type;
		// initialize hash map
		this.parameters = new HashMap<>();
	}

	// getter to retrieve method name
	public String getName() {
		return this.methodName;
	}

	// setter to set method name
	public void setName(String newName) {
		if (newName == null || newName.isBlank()) {
			return;
		}

		this.methodName = newName;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String newType) {
		this.type = newType;
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
			// ParameterItem parameter = new ParameterItem(type, parameterName);
			ParameterItem parameter = UMLObjectFactory.createUMLObject(UMLObjectFactory.ObjectType.PARAMETERITEM,
					parameterName, type);
			// insert new parameter item into map
			methodItem.getParameters().put(parameterName, parameter);
			// fire property change event
			methodItem.support.firePropertyChange("parameterChange", null, parameter);

		} catch (IllegalArgumentException e) {
			return e.getMessage();
		}

		// return successful add of parameter
		return "good";
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
		// fire property change event
		methodItem.support.firePropertyChange("parameterChange", null, parameterName);
		// return successful remove message
		return "good";
	}

	// function to change a parameter name
	public static String changeParameter(MethodItem methodItem, String oldType, String oldName, String newType,
			String newName) {
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
			// fire property change event
			methodItem.support.firePropertyChange("parameterChange", oldName, newName);
			// return successful rename message
			return "good";
		} else {
			// old name key does not exist in map
			return "Parameter: " + oldType + " " + oldName + " does not exist.";
		}
	}

	public static String renameParameter(MethodItem methodItem, String oldParamName, String newParamName) {
		// preconditions
		if (oldParamName == null || newParamName == null || oldParamName.isBlank() || newParamName.isBlank()) {
			throw new IllegalArgumentException("Parameter name cannot be null or blank");
		}

		newParamName = newParamName.trim();

		if (methodItem.getParameters().containsKey(oldParamName)) {

			ParameterItem newParam = methodItem.getParameter(oldParamName);

			newParam.setName(newParamName);

			methodItem.getParameters().remove(oldParamName);

			methodItem.getParameters().put(newParamName, newParam);
			// fire property change event
			methodItem.support.firePropertyChange("parameterChange", oldParamName, newParamName);
			// return successfull rename message
			return "good";
		} else {
			// methodItem does not contain a method with that name.
			return "RENAMEParameter: " + oldParamName + " does not exist.";
		}
	}

	public static String retypeParameter(MethodItem methodItem, String paramName, String newParamType) {
		// preconditions
		if (paramName == null || newParamType == null || paramName.isBlank() || newParamType.isBlank()) {
			throw new IllegalArgumentException("Parameter name cannot be null or blank");
		}
		newParamType = newParamType.trim();

		System.out.println(methodItem.getParameters().keySet());

		if (methodItem.getParameters().containsKey(paramName)) {

			methodItem.getParameter(paramName).setType(newParamType);
			// fire property change event
			methodItem.support.firePropertyChange("parameterChange", null, paramName);
			return "good";
		} else {
			return "RETYPEParameter: " + paramName + " does not exist.";
		}
	}

	// function to print the method and its parameters to a string
	@Override
	public String toString() {
		StringBuilder out = new StringBuilder();
		out.append("Method: ");
		out.append(type + " ");
		out.append(methodName);
		out.append(" Parameters: ");
		// out.append(parameters.keySet());

		for (ParameterItem param : parameters.values()) {
			out.append("[" + param.toString() + "] ");
		}

		return out.toString();
	}

	public void addPropertyChangeListener(PropertyChangeListener listener) {
		support.addPropertyChangeListener(listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		support.removePropertyChangeListener(listener);
	}
}
