/*
    - Class to create a ParameterItem object
    - ParameterItems are constructed with a String representing the name
    - ParameterItems will be stored into a map in the MethodItem class
*/

public class ParameterItem
{
    //String representing the name
    private String parameterName;

    //constructor
    public ParameterItem(String parameterName)
    {
        this.parameterName = parameterName;
    }

    //getter to retrieve the parameter name
    public String getParameterName()
    {
        return this.parameterName;
    }

    //setter to set the parameter name
    public void setParameterName(String parameterName)
    {
        this.parameterName = parameterName;
    }

    //placeholder toString
    @Override
    public String toString()
    {
        return this.parameterName;
    }
}
