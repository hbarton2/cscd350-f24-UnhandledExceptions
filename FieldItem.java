public class FieldItem
{
	private String fieldName;
	
	public FieldItem(String fieldName)
    	{
        	this.fieldName = fieldName;
    	}

	public String getFieldName()
	{
		return this.fieldName;
	}

	public void setFieldName(String fieldName)
	{
		this.fieldName = fieldName;
	}

	@Override
	public String toString()
	{
		return this.fieldName;
	}
}
