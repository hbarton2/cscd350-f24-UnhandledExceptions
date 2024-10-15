import java.util.HashMap;
import java.util.Map;
	
public class ClassItem
{	
    String name;


    //Names of FieldItem/MethodItem are keys to HashMap<k,v>
    HashMap<String,FieldItem> fieldItems;
    HashMap<String,MethodItem> methodItems;  

    private ClassItem(final String classItemName){
        this.name = classItemName;

        //initializes HashMaps
        this.fieldItems = new HashMap<>();
        this.methodItems = new HashMap<>();
    }

    //returns a class object, to be added to the map in Main.java
    //need to add precondition checking
    public static ClassItem createClassItem(final Map<String, ClassItem> classItems,final String classItemName){
        
            //if the classItemList does not already have a class named classItemName, we create a new class
        if(!(classItems.containsKey(classItemName))){
            ClassItem createdClass = new ClassItem(classItemName);
            classItems.put(createdClass.getClassItemName(),createdClass);
            System.out.println("Class " + createdClass.getClassItemName() + " created.");
            return createdClass;
        }else{
            //if classItemName is already in use in the classItemList that's passed in.
            System.out.println("Class name must be unique.");
            return null;
        }
    }

    public String getClassItemName(){
        return this.name;
    }

    //private setter method to force condition checking through renameClassItem
    private void setClassItemName(String newClassItemName){
        this.name = newClassItemName;
    }
    
    //check that the oldClassItemName exists in the classItemList
    //check that the newClassItemName is available to use

    public void renameClassItem(final HashMap<String, ClassItem> classItemList, final String newClassItemName, final String oldClassItemName){
        if(classItemList == null){
            throw new IllegalArgumentException("classItemList cannot be null");
        }

        if(newClassItemName.isBlank() ||oldClassItemName.isBlank()){
            throw new IllegalArgumentException("classItemNames cannot be blank");
        }

        if(newClassItemName == null || oldClassItemName == null){
            throw new IllegalArgumentException("classItemNames cannot be null");
        }

        //checks that the name is not a duplicate, and that the old name to be changed, exists.
        if(checkValidOldName(classItemList, oldClassItemName) && checkValidNewName(classItemList, newClassItemName)){
            //sets the classItem Object that is stored in the value associated with the HashMap for the key oldClassItemName to be newClassItemName
            String oldName = ((ClassItem) classItemList.get(oldClassItemName)).getClassItemName();
            ((ClassItem) classItemList.get(oldClassItemName)).setClassItemName(newClassItemName);
            System.out.println(oldName + " class renamed to \"" + newClassItemName + "\"" );
        }
        //if either of the checks fail, an error message is displayed from their respective method.
    }

    //Check if the new name to be used is available (not a duplicate name)
    private boolean checkValidNewName(final HashMap<String, ClassItem> classItemList, final String newClassItemName){
        if(!(classItemList.containsKey(newClassItemName))){    //if the name is not 
            return true;
        }
        System.out.println("\"" + newClassItemName + "\" is already in use." );
        return false;
    }

    //checks if the oldClassItem is a class already contained in the HashMap passed in.
        //displays error message when false.
    private boolean checkValidOldName(final HashMap<String, ClassItem> classItemList, final String oldClassItemName){
        if(!(classItemList.containsKey(oldClassItemName))){
            return true;
        }
        System.out.println("\"" + oldClassItemName + "\" class does not exist");
        return false;
    }

    //method to add a new method to the hashmap for this class item
    public String addMethod(String methodName)
    {
	//preconditions
	if(methodName == null || methodName.isBlank())
	{
	    throw new IllegalArgumentException("Method name cannot be null or blank");
	}

	//trim any leading or trailing whitespace to ensure valid input
	methodName = methodName.strip();
	    
        //check if the method name already exists in the class
        if(methodItems.containsKey(methodName))
        {
	    //return failure message
            return "Method name: " + methodName + " already in use.";
        }

        //create a new method object with the method name
        MethodItem newMethod = new MethodItem(methodName);

        //insert new method item into hashmap
        methodItems.put(methodName, newMethod);

	//return successful add of method
	return "Method name: " + methodName + " successfully added.";
    }

    // method to remove a method from class
    public String removeMethod(String methodName)
    {
	//preconditions
	if(methodName == null || methodName.isBlank())
	{
	    throw new IllegalArgumentException("Method name cannot be null or blank");
	}
	    
	//trim any leading or trailing whitespace to ensure valid input
	methodName = methodName.strip();
	    
        //check if the method name is a valid key
        if(!methodItems.containsKey(methodName))
        {
	    //return failure message
            return "Method name: " + methodName + " does not exist";
        }

        //remove method item from hash map
        methodItems.remove(methodName);

	//return successful removal of method
	return "Method name: " + methodName + " successfully removed";
    }
    

    //not sure what format we want to return yet.
    public String toString(){
        return "Class name: classFields: classMethods";
    }

};
