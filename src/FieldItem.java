public class FieldItem
{
	private String fieldName;
	private String type;
	
	public FieldItem() {} //blank constructor for IO serialization

	public FieldItem(String fieldName, String type)
  {
    this.fieldName = fieldName;
		this.type = type;
  }

	public String getFieldName()
	{
		return this.fieldName;
	}

	public String getType()
	{
		return this.type;
	}

	public void setFieldName(String fieldName)
	{
		this.fieldName = fieldName;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	@Override
	public String toString()
	{
		return this.type + " " + this.fieldName;
	}
}
