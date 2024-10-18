import java.util.Map;
import java.util.HashMap;
import java.util.Scanner;

public class ClassItem {
    Scanner kb = new Scanner(System.in);
    String name;

    // Names of FieldItem/MethodItem are keys to Map<k,v>
    Map<String, FieldItem> fieldItems;
    Map<String, MethodItem> methodItems;

    private ClassItem(final String classItemName) {
        this.name = classItemName;

        // initializes Maps
        this.fieldItems = new HashMap<String, FieldItem>();
        this.methodItems = new HashMap<String, MethodItem>();
    }

    // returns a class object, to be added to the map in Main.java
    // need to add precondition checking
    public static String addClassItem(final Map<String, ClassItem> classItems, final String classItemName) {
        String name = classItemName.toLowerCase().trim();// forces all classes to be in lower case and trims all leading
                                                         // and trailing "space" (refernce .trim() Java API for space
                                                         // definition).
        // if the classItemList does not already have a class named classItemName, we
        // create a new class
        if (!(classItems.containsKey(name))) {
            ClassItem createdClass = new ClassItem(name);
            classItems.put(createdClass.getClassItemName(), createdClass);
            return "Class \"" + createdClass.getClassItemName() + "\" created.";
        } else {
            // if classItemName is already in use in the classItemList that's passed in.
            return "Class name must be unique.";
        }
    }

    public String getClassItemName() {
        return this.name;
    }

    // private setter method to force condition checking through renameClassItem
    private void setClassItemName(String newClassItemName) {
        this.name = newClassItemName;
    }

    // check that the oldClassItemName exists in the classItemList
    // check that the newClassItemName is available to use

    public static String renameClassItem(final Map<String, ClassItem> classItemList, final String newClassItemName,
            final String oldClassItemName, Map<String, RelationshipItem> relationships) {
        if (classItemList == null) {
            throw new IllegalArgumentException("classItemList cannot be null");
        }

        if (newClassItemName.isBlank() || oldClassItemName.isBlank()) {
            throw new IllegalArgumentException("classItemNames cannot be blank");
        }

        if (newClassItemName == null || oldClassItemName == null) {
            throw new IllegalArgumentException("classItemNames cannot be null");
        }

        // checks that the old name to be changed exists, and that the name is not a
        // duplicate.
        if (classItemList.containsKey(oldClassItemName)) {

            if (!classItemList.containsKey(newClassItemName.toLowerCase().trim())) {
                // sets the classItem Object that is stored in the value associated with the Map
                // for the key oldClassItemName to be newClassItemName
                // gets the old class name from map
                String oldName = ((ClassItem) classItemList.get(oldClassItemName)).getClassItemName();
                // sets the new class name without updating the key in the map
                ((ClassItem) classItemList.get(oldClassItemName)).setClassItemName(newClassItemName);
                // ClassItem temp = new ClassItem(newClassItemName);
                classItemList.put(newClassItemName.toLowerCase().trim(), classItemList.remove(oldClassItemName));

                // need to update relationships to reflect the new class name in the keys
                // go through all relationships and update the keys that contain the old class name
                // the keys are source_destination
                for (Map.Entry<String, RelationshipItem> entry : relationships.entrySet()) {
                    String key = entry.getKey();
                    if (key.contains(oldClassItemName)) {
                        // split the key into source and destination
                        String[] split = key.split("_");
                        if (split[0].equals(oldClassItemName)){
                            // create a new key with the new class name
                            String newKey = newClassItemName + "_" + split[1];
                            // get the relationship object
                            RelationshipItem relationship = relationships.get(key);
                            // remove the old key
                            relationships.remove(key);
                            // add the relationship with the new key
                            relationships.put(newKey, relationship);
                        }
                        else {
                            // create a new key with the new class name
                            String newKey = split[0] + "_" + newClassItemName;
                            // get the relationship object
                            RelationshipItem relationship = relationships.get(key);
                            // remove the old key
                            relationships.remove(key);
                            // add the relationship with the new key
                            relationships.put(newKey, relationship);
                        }
                    }
                }

                return oldName + " class renamed to \"" + newClassItemName + "\"";
            } else {
                // displayed if newClassItemName is already a key in the HashMap
                return newClassItemName + " is already in use.";
            }
        } else {
            // displayed when oldClassItemName is not in the HashMap
            return oldClassItemName + " does not exist.";
        }
    }

    /*
     * takes Map to work with
     * and the name of the class to delete
     * and the map of relationships from main to remove the relationships
     * corresponding to the deleted class
     */
    public static String removeClassItem(final Map<String, ClassItem> classItems, final String classItemName,
            Map<String, RelationshipItem> relationships) {
        // precondition checking

        if (classItems == null) {
            throw new IllegalArgumentException("classItems cannot be null");
        }
        if (classItemName.isBlank()) {
            throw new IllegalArgumentException("classItemName cannot be blank");
        }

        // if the classItemName to delete is a key inside of the map given, we remove
        // the mapping for the key from the map.
        // .remove returns the previous value associated with the key, or null if it did
        // not exist.
        if (classItems.remove(classItemName) != null) {
            // need to delete relationships corresponding to ClassItem that got removed
            // this goes through all entries and if the key contains the class name, which
            // it should, it gets removed.
            relationships.entrySet().removeIf(entry -> entry.getKey().contains(classItemName));

            return classItemName + " and corresponding relationships have been removed.";
        } else {
            return "No class with name \"" + classItemName + "\" exists.";
        }

    }

    // Check if the new name to be used is available (not a duplicate name), if
    // duplicate, display message.
    // private static boolean checkValidNewName(final Map<String, ClassItem>
    // classItemList, final String newClassItemName){
    // if(!(classItemList.containsKey(newClassItemName))){ //if the name is not
    // return true;
    // }
    // System.out.println("\"" + newClassItemName + "\" is already in use." );
    // return false;
    // }

    // checks if the oldClassItem is a class already contained in the Map passed in.
    // displays error message when false.
    // private static boolean checkValidOldName(final Map<String, ClassItem>
    // classItemList, final String oldClassItemName){
    // if(!(classItemList.containsKey(oldClassItemName))){
    // return true;
    // }
    // System.out.println("\"" + oldClassItemName + "\" class does not exist");
    // return false;
    // }

    // method to add a new method to the map for this class item
    public String addMethod(String methodName) {
        // preconditions
        if (methodName == null || methodName.isBlank()) {
            throw new IllegalArgumentException("Method name cannot be null or blank");
        }

        // trim any leading or trailing whitespace to ensure valid input
        methodName = methodName.trim();

        // check if the method name already exists in the class
        if (methodItems.containsKey(methodName)) {
            // return failure message
            return "Method name: " + methodName + " already in use.";
        }

        // create a new method object with the method name
        MethodItem newMethod = new MethodItem(methodName);

        // insert new method item into map
        methodItems.put(methodName, newMethod);

        // return successful add of method
        return "Method name: " + methodName + " successfully added.";
    }

    // method to remove a method from class
    public String removeMethod(String methodName) {
        // preconditions
        if (methodName == null || methodName.isBlank()) {
            throw new IllegalArgumentException("Method name cannot be null or blank");
        }

        // trim any leading or trailing whitespace to ensure valid input
        methodName = methodName.trim();

        // check if the method name is a valid key
        if (!methodItems.containsKey(methodName)) {
            // return failure message
            return "Method name: " + methodName + " does not exist";
        }

        // remove method item from hash map
        methodItems.remove(methodName);

        // return successful removal of method
        return "Method name: " + methodName + " successfully removed";
    }

    public String renameMethod(String oldName, String newName) {
        // preconditions
        if (oldName == null || oldName.isBlank() || newName == null || newName.isBlank()) {
            return "Method names cannot be null or blank.";
        }

        // trim any leading or trailing whitespace
        oldName = oldName.trim();
        newName = newName.trim();

        // check if the new name is already taken
        if (methodItems.containsKey(newName)) {
            return "Method name: " + newName + " already in use.";
        }

        // check if the old name is a valid key
        if (methodItems.containsKey(oldName)) {
            // copy the old method
            MethodItem newMethod = methodItems.get(oldName);

            // set new method name
            newMethod.setMethodName(newName);

            // remove old method from map
            methodItems.remove(oldName);

            // add new method item to class
            methodItems.put(newName, newMethod);

            // return success
            return "Method name: " + oldName + " successfully changed to " + newName;
        } else {
            // invalid method name, return failure
            return "Method name: " + oldName + " does not exist.";
        }

    }

    public String addField(String fieldName) {
        // preconditions
        if (fieldName == null || fieldName.isBlank()) {
            return "Field name cannot be null or blank";
        }

        fieldName = fieldName.toLowerCase().trim();
        // check if field already exists
        if (fieldItems.containsKey(fieldName)) {
            return "Field name: " + fieldName + " already in use.";
        }

        // create new field item object
        FieldItem newField = new FieldItem(fieldName);

        // add new field item to map
        fieldItems.put(fieldName, newField);

        return fieldName + " was successfully added to " + this.name;
    }

    public String removeField(String fieldName) {
        // preconditions
        if (fieldName == null || fieldName.isBlank()) {
            return "Field name cannot be null or blank";
        }

        fieldName = fieldName.toLowerCase().trim();

        // check if field exists
        if (!fieldItems.containsKey(fieldName)) {
            return "Field name: " + fieldName + " does not exist.";
        }



        // remove field from map
        fieldItems.remove(fieldName);

        return "Field name: " + fieldName + " successfully removed.";
    }

    public String renameField(String oldName, String newName) {
        // preconditions
        if (oldName == null || oldName.isBlank() || newName == null || newName.isBlank()) {
            return "Field names cannot be null or blank";
        }

        // trim to remove any leading or trailing whitespace
        oldName = oldName.toLowerCase().trim();
        newName = newName.toLowerCase().trim();

        // check if new name is already in use
        if (fieldItems.containsKey(newName)) {
            return "Field name: " + newName + " already in use";
        }

        // if the old field exists change it
        if (fieldItems.containsKey(oldName)) {
            // copy old field object
            FieldItem newField = fieldItems.get(oldName);

            // remove old field from map
            fieldItems.remove(oldName);

            // set field objects name to new name
            newField.setFieldName(newName);

            // add new field item into map
            fieldItems.put(newName, newField);

            return "Field name: " + oldName + " successfully changed to " + newName;
        } else {
            return "Field name: " + oldName + " does not exist";
        }
    }

    public String toString() {
        return this.name;
    }

};
