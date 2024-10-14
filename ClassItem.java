import java.util.HashMap;
import java.util.List;
	
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
    public ClassItem createClassItem(final HashMap classItemList,final String classItemName){
        
            //if the classItemList does not already have a class named classItemName, we create a new class
        if(!(classItemList.containsKey(classItemName))){
            ClassItem createdClass = new ClassItem(classItemName);
            classItemList.put(createdClass.getClassItemName(),createdClass);
            return createdClass;
        }else{
            //if classItemName is already in use in the classItemList that's passed in.
            System.out.println("Class name must be unique.");
            return classItemList;
        }
    }

    public String getClassItemName(){
        return this.name;
    }

    //method to add a new method to the hashmap for this class item
    public void addMethod(String methodName){
        //check if the method name already exists in the class
        if(methodItems.containsKey(methodName))
        {
            System.out.println("Method name already in use.");
            return;
        }

        //create a new method object with the method name
        MethodItem newMethod = new MethodItem(methodName);

        //insert new method item into hashmap
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
        return "Class name: classFields: classMethods";
    }

};