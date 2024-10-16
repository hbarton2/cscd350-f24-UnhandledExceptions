import java.util.HashMap;

public class MethodItem
{
	private String methodName;
	//hash map to store ParamterItem's with their name as the key
	private HashMap<String, ParameterItem> parameters; 

	public MethodItem(String methodName)
	{
		//preconditions
		if(methodName == null || methodName.isBlank())
		{
			throw new IllegalArgumentException("Method name cannot be null or blank - constructor");
		}

		//strip input to remove any leading or trailing whitespace
		methodName.trim();

		this.methodName = methodName;
		//initialize hash map
		this.parameters = new HashMap<>();
	}

	//getter to retrieve method name
	public String getMethodName()
	{
		return this.methodName;
	}

	//setter to set method name
	public void setMethodName(String newName)
	{
		if(newName == null || newName.isBlank())
		{
			return;
		}
		
		this.methodName = newName;
	}

	//function to add a new parameter to the hash map
	public String addParameter(String parameterName)
	{
		//preconditions
		if(parameterName == null || parameterName.isBlank())
		{
			throw new IllegalArgumentException("Parameter name cannot be null or blank");
		}

		//strip input to remove any leading or trailing whitespace
		parameterName = parameterName.trim();

		//check if the parameter already exists
		if(parameters.containsKey(parameterName))
		{
			//return failure message
			return "Parameter name: " + parameterName + " already in use.";
		}

		//create parameter object
		ParameterItem parameter = new ParameterItem(parameterName);
		
		//insert new parameter item into map
		parameters.put(parameterName, parameter);

		//return successful add of parameter
		return "Parameter name: " + parameterName + " successfully added.";
	}

	//getter to retrieve a parameter from the map
	public ParameterItem getParameter(String parameterName)
	{
		return parameters.get(parameterName);
	}

	//function to remove a parameter from the map
	public String removeParameter(String parameterName)
	{
		//preconditions
		if(parameterName == null || parameterName.isBlank())
		{
			throw new IllegalArgumentException("Parameter name cannot be null or blank");
		}

		//strip input to remove any leading or trailing whitespace
		parameterName = parameterName.trim();

		//check if the parameter exists in the map
		if(!parameters.containsKey(parameterName))
		{
			//return failure (parameter not in map)
			return "Parameter name: " + parameterName + " does not exist";
		}

		//remove parameter from map
		parameters.remove(parameterName);
		
		//return successful remove message
		return "Parameter name: " + parameterName + " removed successfully.";
	}

	//function to change a parameter name
	public String changeParameter(String oldName, String newName)
	{
		//preconditions
		if(oldName == null || newName == null || oldName.isBlank() || newName.isBlank())
		{
			throw new IllegalArgumentException("Parameter name cannot be null or blank");
		}

		//strip input to remove any leading or traililng whitespace
		oldName = oldName.trim();
		newName = newName.trim();

		//check if the new name is valid or is already attached to another ParameterItem
		if(parameters.containsKey(newName))
		{
			//return failure message for new name already in use
			return "New Parameter name: " + newName + " already in use.";
		}

		//check if the parameter exists in the map
		if(parameters.containsKey(oldName))
		{
			//store the old parameter
			ParameterItem parameter = parameters.get(oldName);

			//delete old entry in map
			parameters.remove(oldName);

			//add parameter back to map under new name
			parameters.put(newName, parameter);

			//return successful rename message
			return "Parameter name: " + oldName + " successfully changed to: " + newName;
		}else{
			//old name key does not exist in map
			return "Parameter name: " + oldName + " does not exist.";
		}
	}

	//function to print the method and its parameters to a string
	@Override
	public String toString() 
	{
    		StringBuilder out = new StringBuilder();
    		out.append("Method Name: ").append(methodName).append("\n");
    		out.append("Parameters:");

		//iterates through the parameters in the map, space delimiter
    		for (String parameterName : parameters.keySet())
		{
        		out.append(parameterName).append(" ");
    		}

    		return out.toString();
	}
}
