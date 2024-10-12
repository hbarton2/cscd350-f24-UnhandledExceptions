import java.util.HashMap;

public class MethodItem
{
	private String methodName;
	//hash map to store ParamterItem's with their name as the key
	private HashMap<String, ParameterItem> parameters; 

	public MethodItem(String methodName)
	{
		this.methodName = methodName;
		//initialize hash map
		this.parameters = new HashMap<>();
	}

	//function to add a new parameter to the hash map
	public void addParameter(String parameterName, ParameterItem parameter)
	{
		//check if the parameter already exists
		if(parameters.containsKey(parameterName))
		{
			return;
		}
		
		parameters.put(parameterName, parameter);
	}

	//getter to retrieve a parameter from the map
	public ParameterItem getParameter(String parameterName)
	{
		return parameters.get(parameterName);
	}

	//function to remove a parameter from the map
	public void removeParameter(String parameterName)
	{
		//check if the parameter exists in the map
		if(!parameters.containsKey(parameterName))
		{
			return;
		}

		parameters.remove(parameterName);
		
	}

	//function to change a parameter name
	public void changeParameter(String oldName, String newName)
	{
		//check if the new name is valid or is already attached to another ParameterItem
		if(parameters.containsKey(newName))
		{
			return;
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
