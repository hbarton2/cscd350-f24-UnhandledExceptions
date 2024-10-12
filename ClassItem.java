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

    //private setter method to force condition checking through renameClassItem
    private void setClassItemName(String newClassItemName){
        this.name = newClassItemName;
    }
    
    //Check if the new name to be used is available (not a duplicate name)
    private boolean checkValidNewName(final HashMap classItemList, final String newClassItemName){
        if(!(classItemList.containsKey(newClassItemName))){    //if the name is not 
            return true;
        }
        return false;
    }
    

    //not sure what format we want to return yet.
    public String toString(){
        return "Class name: classFields: classMethods";
    }

};