package com.unhandledexceptions.Model;

import java.util.HashMap;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class ClassItem implements PropertyChangeListener{
    String name;

    @JsonIgnore // ignore support for serialization, not a POJO
    private final PropertyChangeSupport support = new PropertyChangeSupport(this);

    // Names of FieldItem/MethodItem are keys to Map<k,v>
    private HashMap<String, FieldItem> fieldItems;
    private HashMap<String, MethodItem> methodItems;
    private double x, y;

    public ClassItem() {
    } // blank constructor for IO serialization

    private ClassItem(final String classItemName) {
        this.name = classItemName.trim();
        this.x = 0;
        this.y = 0;

        // initializes Maps
        this.fieldItems = new HashMap<String, FieldItem>();
        this.methodItems = new HashMap<String, MethodItem>();
    }

    /**
     * The point of this constructor is to make a deep copy of a classItem, that way
     * when we create a copy it is a completely new object, not a memory address reference
     * 
     * @param other a ClassItem object that you wish to copy
     */
    private ClassItem(ClassItem other){
        this.name = other.name;
        this.fieldItems = new HashMap<String, FieldItem>(other.fieldItems);
        this.methodItems = new HashMap<String, MethodItem>(other.methodItems);
        this.x = other.x;
        this.y = other.y;
    }

    /**
     * The copy method to make a new object that is a copy of a classItem.
     * 
     * @param other the ClassItem to be copied
     * @return the copy object of the parameter other
     */
    public static ClassItem copyClassItem(ClassItem other) {
        return new ClassItem(other);
    }

    // Used for tester methods and unit tests currently.
    public static String addClassItem(final HashMap<String, ClassItem> classItems, final String classItemName) {
        String name = classItemName.toLowerCase().trim();// forces all classes to be in lower case and trims all leading
                                                         // and trailing "space" (refernce .trim() Java API for space
                                                         // definition).
        
        /*
         * if the classItemList does not already have a class named classItemName, we
         * create a new ClassItem and add it to the HashMap
         */
        if (!(classItems.containsKey(name))) {
            ClassItem createdClass = new ClassItem(classItemName.trim());
            classItems.put(name, createdClass);
            // fire support for added class item
            createdClass.support.firePropertyChange("classItem", null, createdClass);
            return "good";
        } else {
            // if classItemName is already in use in the classItemList that's passed in.
            return "Class name must be unique.";
        }
    }
    //randomized locations for the class items will only work up to 23-24 classes in GUI
    public static void initialPosition(HashMap<String, ClassItem> classItems, ClassItem classItem) {
        // take in and loop each class item in the map until it's finished
        boolean finished = false;//not finished going though the list of classes
        while(!finished){
            finished = true;//ready to end the loop unless a box is too close to this one
            double x1 = Math.random() * 900;//boundaries of the GUI box creation
            double y1 = Math.random() * 565;
            if(classItems.size() < 18){//hard cap on how many classes can be displayed. can be reoved once scrolling thorugh is working
                for (HashMap.Entry<String, ClassItem> entry : classItems.entrySet()) {//for loop iterates through list of classes to check distance
                    double x2 = entry.getValue().getX();
                    double y2 = entry.getValue().getY();
                    double distance = Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
                    if(distance < 150){//if the distance is less than 150 pixels, the loop will start over and try again
                        finished = false;
                        break;
                    }
                }
            }
            classItem.setX(x1);//got throught the loop and found not too close to other classes, set the x and y values
            classItem.setY(y1);
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

    public double getX() {
        return this.x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return this.y;
    }

    public void setY(double y) {
        this.y = y;
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
            HashMap<String, RelationshipItem> relationships, final String oldClassItemName,
            final String newClassItemName) {
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
        if (classItemList.containsKey(oldClassItemName.toLowerCase().trim())) {

            if (!classItemList.containsKey(newClassItemName.toLowerCase().trim())) {
                // sets the classItem Object that is stored in the value associated with the Map
                // for the key oldClassItemName to be newClassItemName
                // sets the new class name without updating the key in the map
                ((ClassItem) classItemList.get(oldClassItemName.toLowerCase().trim())).setName(newClassItemName.trim());
                // ClassItem temp = new ClassItem(newClassItemName);
                classItemList.put(newClassItemName.toLowerCase().trim(), classItemList.remove(oldClassItemName.toLowerCase().trim()));

                // need to update relationships to reflect the new class name in the keys
                // go through all relationships and update the keys that contain the old class
                // name
                // the keys are source_destination
                for (HashMap.Entry<String, RelationshipItem> entry : relationships.entrySet()) {
                    String key = entry.getKey();
                    if (key.contains(oldClassItemName)) {
                        // split the key into source and destination
                        String[] split = key.split("_");
                        if (split[0].equals(oldClassItemName)) {
                            // create a new key with the new class name
                            String newKey = newClassItemName + "_" + split[1];
                            // get the relationship object
                            RelationshipItem relationship = relationships.get(key);
                            // remove the old key
                            relationships.remove(key);
                            // add the relationship with the new key
                            relationships.put(newKey, relationship);
                        } else {
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

                // fire support for renamed class item
                classItemList.get(newClassItemName.toLowerCase().trim()).support.firePropertyChange("name", oldClassItemName, newClassItemName);
                return "good";
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

        String key = classItemName.toLowerCase().trim();

        // if the classItemName to delete is a key inside of the map given, we remove
        // the mapping for the key from the map.
        // .remove returns the previous value associated with the key, or null if it did
        // not exist.
        if (classItems.containsKey(key)) {
            //retrive the classItem object to be removed
            ClassItem classItem = classItems.get(key);
            //remove the item from the list
            classItems.remove(key);
            // fire support for deleted class item
            classItem.support.firePropertyChange("removeBox", classItemName, null);
            //classItems.remove(classItemName) != null
            // need to delete relationships corresponding to ClassItem that got removed
            // this goes through all entries and if the key contains the class name, which
            // it should, it gets removed.
            relationships.entrySet().removeIf(entry -> entry.getKey().contains(key));


            return "good";
        } else {
            return "No class with name \"" + classItemName + "\" exists.";
        }

    }

    /*
     * // Check if the new name to be used is available (not a duplicate name), if
     * // duplicate, display message.
     * // private static boolean checkValidNewName(final Map<String, ClassItem>
     * private static boolean checkValidNewName(final HashMap<String, ClassItem>
     * classItemList, final String newClassItemName) {
     * if (newClassItemName == null || newClassItemName.isBlank()){
     * System.out.println("Name cannot be blank.");
     * return false;
     * }
     * 
     * if(!(classItemList.containsKey(newClassItemName))) //if the name is not
     * return true;
     * System.out.println("\"" + newClassItemName + "\" is already in use." );
     * return false;
     * }
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
    public static String addMethod(ClassItem classItem, String methodName, String returnType) {
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
        // MethodItem newMethod = new MethodItem(methodName, returnType);
        MethodItem newMethod = UMLObjectFactory.createUMLObject(UMLObjectFactory.ObjectType.METHODITEM, methodName,
                returnType);

        // insert new method item into map
        classItem.getMethodItems().put(methodName, newMethod);

        // add classItem as a listener to the new method for parameter changes
        newMethod.addPropertyChangeListener(classItem);

        // fire support for added method item
        classItem.support.firePropertyChange("method", returnType, newMethod);


        // return successful add of method
        return "good";
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
        classItem.support.firePropertyChange("method", classItem, methodName);

        // return successful removal of method
        return "good";
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
            newMethod.setName(newName);

            // remove old method from map
            classItem.getMethodItems().remove(oldName);

            // add new method item to class
            classItem.getMethodItems().put(newName, newMethod);
            // fire support for renamed method item
            classItem.support.firePropertyChange("method", oldName, newName);

            // return success
            return "good";
        } else {
            // invalid method name, return failure
            return "Method name: " + oldName + " does not exist.";
        }

    }

    public static String retypeMethod(ClassItem classItem, String methodName, String newType) {
        // preconditions
        if (newType == null || newType.isBlank()) {
            return "Method names cannot be null or blank.";
        }
        newType = newType.trim();

        // check if the method name is a valid key
        if (classItem.getMethodItems().containsKey(methodName)) {

            MethodItem newMethod = classItem.getMethodItems().get(methodName);

            // changes the type of the method
            newMethod.setType(newType);
            // fire support for retype method
            classItem.support.firePropertyChange("method", newType, newMethod); // fire property change

            // return success
            return "good";
        } else {
            // invalid method name, return failure
            return "Method name: " + methodName + " does not exist.";
        }
    }

    public static String addField(ClassItem classItem, String type, String fieldName) {
        // preconditions
        if (fieldName == null || fieldName.isBlank() || type == null || type.isBlank()) {
            return "Field name or type cannot be null or blank";
        }

        fieldName = fieldName.toLowerCase().trim();
        // types can be uppercase for example: String name
        // type = type.toLowerCase().trim();
        type = type.trim();

        // check if field already exists
        if (classItem.getFieldItems().containsKey(fieldName)) {
            return "Field name: " + fieldName + " already in use.";
        }

        // create new field item object
        // FieldItem newField = new FieldItem(fieldName, type);
        FieldItem newField = UMLObjectFactory.createUMLObject(UMLObjectFactory.ObjectType.FIELDITEM, fieldName, type);

        // add new field item to map
        classItem.getFieldItems().put(fieldName, newField);
        // fire support for added field item
        classItem.support.firePropertyChange("field", type, newField);

        return "good";
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
            return "good";
        }

        // remove field from map
        classItem.getFieldItems().remove(fieldName);
        // fire support for removed field item
        classItem.support.firePropertyChange("field", classItem, fieldName);

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
            newField.setName(newName);

            // add new field item into map
            classItem.getFieldItems().put(newName, newField);
            // fire support for renamed field item
            classItem.support.firePropertyChange("field", newName, newField);

            return "good";
        } else {
            return "Field name: " + oldName + " does not exist";
        }
    }

    public static String retypeField(ClassItem classItem, String fieldName, String newType) {
        // preconditions
        if (fieldName == null || fieldName.isBlank() || newType == null || newType.isBlank()) {
            return "Field names cannot be null or blank";
        }

        // trim to remove any leading or trailing whitespace
        newType = newType.trim();

        // if the old field exists change it
        if (classItem.getFieldItems().containsKey(fieldName)) {
            // copy old field object
            FieldItem Field = classItem.getFieldItems().get(fieldName);

            // set field objects type to new type
            Field.setType(newType);
            // fire support for retype field item
            classItem.support.firePropertyChange("field", newType, Field);

            return "good";
        } else {
            return "Field name: " + fieldName + " does not exist";
        }
    }

    public String toString() {
        return this.name;
    }

    // Property Change Listener methods
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        support.removePropertyChangeListener(listener);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        support.firePropertyChange(evt.getPropertyName(), evt.getOldValue(), evt.getNewValue());
    }

    public PropertyChangeSupport getSupport() {
        return support;
    }
};
