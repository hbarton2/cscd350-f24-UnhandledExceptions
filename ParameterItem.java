public class ParameterItem
{
    private String parameterName;
    
    public ParameterItem(String parameterName)
    {
        this.parameterName = parameterName;
    }

    public String getParameterName()
    {
        return this.parameterName;
    }

    public void setParameterName(String parameterName)
    {
        this.parameterName = parameterName;
    }

    @Override
    public String toString()
    {
        return this.parameterName;
    }
}
