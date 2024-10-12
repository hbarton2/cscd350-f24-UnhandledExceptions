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
    
    //check that the oldClassItemName exists in the classItemList
    //check that the newClassItemName is available to use

    private void renameClassItem(final HashMap classItemList, final String newClassItemName, final String oldClassItemName){
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
            String oldName = classItemList.get(oldClassItemName).getClassItemName;
            classItemList.get(oldClassItemName).setClassItemName(newClassItemName);
            System.out.println(oldName + " class renamed to \"" + newClassItemName + "\"" );
        }
        //if either of the checks fail, an error message is displayed from their respective method.
    }

    //Check if the new name to be used is available (not a duplicate name)
    private boolean checkValidNewName(final HashMap classItemList, final String newClassItemName){
        if(!(classItemList.containsKey(newClassItemName))){    //if the name is not 
            return true;
        }
        System.out.println("\"" + newClassItemName + "\" is already in use." );
        return false;
    }

    //checks if the oldClassItem is a class already contained in the HashMap passed in.
        //displays error message when false.
    private boolean checkValidOldName(final HashMap classItemList, final String oldClassItemName){
        if(!(classItemList.contains(oldClassItemName))){
            return true;
        }
        System.out.println("\"" + oldClassItemName + "\" class does not exist");
        return false;
    }
    

    //not sure what format we want to return yet.
    public String toString(){
        return "Class name: classFields: classMethods";
    }

};