import java.util.Map;
import java.util.HashMap;
	
public class ClassItem
{	
    String name;


    //Names of FieldItem/MethodItem are keys to Map<k,v>
    Map<String,FieldItem> fieldItems;
    Map<String,MethodItem> methodItems;  

    private ClassItem(final String classItemName){
        this.name = classItemName;

        //initializes Maps
        this.fieldItems = new HashMap<String, FieldItem>();
        this.methodItems = new HashMap<String, MethodItem>();
    }

    //returns a class object, to be added to the map in Main.java
    //need to add precondition checking
    public static String addClassItem(final Map<String, ClassItem> classItems,final String classItemName){
        String name = classItemName.toLowerCase().trim();//forces all classes to be in lower case and trims all leading and trailing "space" (refernce .trim() Java API for space definition).
            //if the classItemList does not already have a class named classItemName, we create a new class
        if(!(classItems.containsKey(name))){
            ClassItem createdClass = new ClassItem(name);
            classItems.put(createdClass.getClassItemName(),createdClass);
            return "Class \"" + createdClass.getClassItemName() + "\" created.";
        }else{
            //if classItemName is already in use in the classItemList that's passed in.
            return "Class name must be unique.";
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

    public void renameClassItem(final Map<String, ClassItem> classItemList, final String newClassItemName, final String oldClassItemName){
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
            //sets the classItem Object that is stored in the value associated with the Map for the key oldClassItemName to be newClassItemName
            String oldName = ((ClassItem) classItemList.get(oldClassItemName)).getClassItemName();
            ((ClassItem) classItemList.get(oldClassItemName)).setClassItemName(newClassItemName);
            System.out.println(oldName + " class renamed to \"" + newClassItemName + "\"" );
        }
        //if either of the checks fail, an error message is displayed from their respective method.
    }

    /*
     * takes Map to work with
     * and the name of the class to delete
     */
    public static String removeClassItem(final Map<String, ClassItem> classItems,final String classItemName){
        //precondition checking

        if(classItems == null){
            throw new IllegalArgumentException("classItems cannot be null");
        }
        if(classItemName.isBlank()){
            throw new IllegalArgumentException("classItemName cannot be blank");
        }

        //if the classItemName to delete is a key inside of the map given, we remove the mapping for the key from the map.    
        //.remove returns a boolean if removed or not.
        if(classItems.remove(classItemName) != null){
            return classItemName + " has been removed.";
        }else{
            return "No class with name \"" + classItemName + "\" exists.";
        }

        }
        


    //Check if the new name to be used is available (not a duplicate name), if duplicate, display message.
    private boolean checkValidNewName(final Map<String, ClassItem> classItemList, final String newClassItemName){
        if(!(classItemList.containsKey(newClassItemName))){    //if the name is not 
            return true;
        }
        System.out.println("\"" + newClassItemName + "\" is already in use." );
        return false;
    }

    //checks if the oldClassItem is a class already contained in the Map passed in.
        //displays error message when false.
    private boolean checkValidOldName(final Map<String, ClassItem> classItemList, final String oldClassItemName){
        if(!(classItemList.containsKey(oldClassItemName))){
            return true;
        }
        System.out.println("\"" + oldClassItemName + "\" class does not exist");
        return false;
    }

    //method to add a new method to the map for this class item
    public void addMethod(String methodName){
        //check if the method name already exists in the class
        if(methodItems.containsKey(methodName))
        {
            System.out.println("Method name already in use.");
            return;
        }

        //create a new method object with the method name
        MethodItem newMethod = new MethodItem(methodName);

        //insert new method item into map
        methodItems.put(methodName, newMethod);
    }

    // method to remove a method from class
    public void removeMethod(String methodName)
    {
        //check if the method name is a valid key
        if(!methodItems.containsKey(methodName))
        {
            System.out.println("Method name: " + methodName + " does not exist");
            return;
        }

        //remove method item from hash map
        methodItems.remove(methodName);
    }
    

    //not sure what format we want to return yet.
    public String toString(){
        return this.name;
    }

};
