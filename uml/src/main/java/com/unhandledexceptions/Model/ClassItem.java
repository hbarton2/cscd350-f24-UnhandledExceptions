package com.unhandledexceptions.Model;

import java.util.HashMap;
import java.util.Scanner;

public class ClassItem {
    Scanner kb = new Scanner(System.in);
    String name;

    // Names of FieldItem/MethodItem are keys to Map<k,v>
    private HashMap<String, FieldItem> fieldItems;
    private HashMap<String, MethodItem> methodItems;

    public ClassItem() {} //blank constructor for IO serialization

    private ClassItem(final String classItemName) {
        this.name = classItemName;

        // initializes Maps
        this.fieldItems = new HashMap<String, FieldItem>();
        this.methodItems = new HashMap<String, MethodItem>();
    }


    //Used for tester methods and unit tests currently.
    public static String addClassItem(final HashMap<String, ClassItem> classItems, final String classItemName) {
        String name = classItemName.toLowerCase().trim();// forces all classes to be in lower case and trims all leading
                                                         // and trailing "space" (refernce .trim() Java API for space
                                                         // definition).
        /*if the classItemList does not already have a class named classItemName, we
         create a new ClassItem and add it to the HashMap*/
        if (!(classItems.containsKey(name))) {
            ClassItem createdClass = new ClassItem(name);
            classItems.put(createdClass.getName(), createdClass);
            return "Class \"" + createdClass.getName() + "\" created.";
        } else {
            // if classItemName is already in use in the classItemList that's passed in.
            return "Class name must be unique.";
        }
    }

    public String getName() {
        return this.name;
    }

    // private setter method to force condition checking through renameClassItem
    // made public for IO serialization
    public void setName(String name) {
        this.name = name;
    }

    public HashMap<String, FieldItem> getFieldItems() {
        return this.fieldItems;
    }

    public void setFieldItems(HashMap<String, FieldItem> fieldItems) {
        this.fieldItems = fieldItems;
    }

    public HashMap<String, MethodItem> getMethodItems() {
        return this.methodItems;
    }

    public void setMethodItems(HashMap<String, MethodItem> methodItems) {
        this.methodItems = methodItems;
    }

    // check that the oldClassItemName exists in the classItemList
    // check that the newClassItemName is available to use

    public static String renameClassItem(final HashMap<String, ClassItem> classItemList,
         HashMap<String, RelationshipItem> relationships, final String newClassItemName,
            final String oldClassItemName) {
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
                String oldName = ((ClassItem) classItemList.get(oldClassItemName)).getName();
                // sets the new class name without updating the key in the map
                ((ClassItem) classItemList.get(oldClassItemName)).setName(newClassItemName);
                // ClassItem temp = new ClassItem(newClassItemName);
                classItemList.put(newClassItemName.toLowerCase().trim(), classItemList.remove(oldClassItemName));

                // need to update relationships to reflect the new class name in the keys
                // go through all relationships and update the keys that contain the old class name
                // the keys are source_destination
                for (HashMap.Entry<String, RelationshipItem> entry : relationships.entrySet()) {
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
    public static String removeClassItem(final HashMap<String, ClassItem> classItems,
        HashMap<String, RelationshipItem> relationships, final String classItemName) {
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

    /*
    // Check if the new name to be used is available (not a duplicate name), if
    // duplicate, display message.
    // private static boolean checkValidNewName(final Map<String, ClassItem>
    private static boolean checkValidNewName(final HashMap<String, ClassItem> classItemList, final String newClassItemName) {
        if (newClassItemName == null || newClassItemName.isBlank()){
            System.out.println("Name cannot be blank.");
           return false;
       }

        if(!(classItemList.containsKey(newClassItemName))) //if the name is not
        return true;
        System.out.println("\"" + newClassItemName + "\" is already in use." );
        return false;
    }
    */
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
    public static String addMethod(ClassItem classItem, String methodName) {
        // preconditions
        if (methodName == null || methodName.isBlank()) {
            throw new IllegalArgumentException("Method name cannot be null or blank");
        }

        // trim any leading or trailing whitespace to ensure valid input
        methodName = methodName.trim();

        // check if the method name already exists in the class
        if (classItem.getMethodItems().containsKey(methodName)) {
            // return failure message
            return "Method name: " + methodName + " already in use.";
        }

        // create a new method object with the method name
        MethodItem newMethod = new MethodItem(methodName);

        // insert new method item into map
        classItem.getMethodItems().put(methodName, newMethod);

        // return successful add of method
        return "Method name: " + methodName + " successfully added.";
    }

    // method to remove a method from class
    public static String removeMethod(ClassItem classItem, String methodName) {
        // preconditions
        if (methodName == null || methodName.isBlank()) {
            throw new IllegalArgumentException("Method name cannot be null or blank");
        }

        // trim any leading or trailing whitespace to ensure valid input
        methodName = methodName.trim();

        // check if the method name is a valid key
        if (!classItem.getMethodItems().containsKey(methodName)) {
            // return failure message
            return "Method name: " + methodName + " does not exist";
        }

        // remove method item from hash map
        classItem.getMethodItems().remove(methodName);

        // return successful removal of method
        return "Method name: " + methodName + " successfully removed";
    }

    public static String renameMethod(ClassItem classItem, String oldName, String newName) {
        // preconditions
        if (oldName == null || oldName.isBlank() || newName == null || newName.isBlank()) {
            return "Method names cannot be null or blank.";
        }

        // trim any leading or trailing whitespace
        oldName = oldName.trim();
        newName = newName.trim();

        // check if the new name is already taken
        if (classItem.getMethodItems().containsKey(newName)) {
            return "Method name: " + newName + " already in use.";
        }

        // check if the old name is a valid key
        if (classItem.getMethodItems().containsKey(oldName)) {
            // copy the old method
            MethodItem newMethod = classItem.getMethodItems().get(oldName);

            // set new method name
            newMethod.setMethodName(newName);

            // remove old method from map
            classItem.getMethodItems().remove(oldName);

            // add new method item to class
            classItem.getMethodItems().put(newName, newMethod);

            // return success
            return "Method name: " + oldName + " successfully changed to " + newName;
        } else {
            // invalid method name, return failure
            return "Method name: " + oldName + " does not exist.";
        }

    }

    public static String addField(ClassItem classItem, String type, String fieldName) {
        // preconditions
        if (fieldName == null || fieldName.isBlank() || type == null || type.isBlank()) {
            return "Field name or type cannot be null or blank";
        }

        fieldName = fieldName.toLowerCase().trim();
        // types can be uppercase for example: String name
        //type = type.toLowerCase().trim();
        type = type.trim();

        // check if field already exists
        if (classItem.getFieldItems().containsKey(fieldName)) {
            return "Field name: " + fieldName + " already in use.";
        }

        // create new field item object
        FieldItem newField = new FieldItem(fieldName, type);

        // add new field item to map
        classItem.getFieldItems().put(fieldName, newField);

        return fieldName + " was successfully added to " + classItem.getName();
    }

    public static String removeField(ClassItem classItem, String fieldName) {
        // preconditions
        if (fieldName == null || fieldName.isBlank()) {
            return "Field name cannot be null or blank";
        }   

        // trim leading and trailing whitespace
        fieldName = fieldName.trim();

        // check if field exists
        if (!classItem.getFieldItems().containsKey(fieldName)) {
            return "Field name: " + fieldName + " does not exist.";
        }



        // remove field from map
        classItem.getFieldItems().remove(fieldName);

        return "Field name: " + fieldName + " successfully removed.";
    }

    public static String renameField(ClassItem classItem, String oldName, String newName) {
        // preconditions
        if (oldName == null || oldName.isBlank() || newName == null || newName.isBlank()) {
            return "Field names cannot be null or blank";
        }

        // trim to remove any leading or trailing whitespace
        oldName = oldName.trim();
        newName = newName.trim();

        // check if new name is already in use
        if (classItem.getFieldItems().containsKey(newName)) {
            return "Field name: " + newName + " already in use";
        }

        // if the old field exists change it
        if (classItem.getFieldItems().containsKey(oldName)) {
            // copy old field object
            FieldItem newField = classItem.getFieldItems().get(oldName);

            // remove old field from map
            classItem.getFieldItems().remove(oldName);

            // set field objects name to new name
            newField.setFieldName(newName);

            // add new field item into map
            classItem.getFieldItems().put(newName, newField);

            return "Field name: " + oldName + " successfully changed to " + newName;
        } else {
            return "Field name: " + oldName + " does not exist";
        }
    }

    public String toString() {
        return this.name;
    }

};
