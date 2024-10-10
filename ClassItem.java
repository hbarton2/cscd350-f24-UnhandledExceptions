import java.util.List;
	
public class ClassItem
{	
    String name;

    List<FieldItem> fieldItems;

    List<MethodItem> methodItems;
	//List<FieldItem> fieldItem
	//List<MethodItem> methodItem
    
	
    public static void ClassItem()
    {
        //nothing
    }
    

    /* Add class method
     * Checks name is valid
     * 
     * if valid
     *  creates classItem object with field list and method list null.
     *  prints "class created successfully"
     *  returns classItem object
     * 
     * if not valid
     *  does not create class object
     *  
     */
    private ClassItem(String classItemName){
        this.name = classItemName;
        this.fieldItems = new ArrayList<>();
        this.methodItems = new ArrayList<>();
    }

    //returns a class object, to be added to the map in Main.java
    public ClassItem createClassItem(String classItemName){
        ClassItem createdClass = new ClassItem(classItemName);
        return createdClass;
    }
    
};