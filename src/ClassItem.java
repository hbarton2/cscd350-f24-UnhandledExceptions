import java.util.HashMap;
import java.util.Scanner;

public class ClassItem {
    Scanner kb = new Scanner(System.in);
    String name;

    // Names of FieldItem/MethodItem are keys to Map<k,v>
    HashMap<String, FieldItem> fieldItems;
    HashMap<String, MethodItem> methodItems;

    public ClassItem() {} //blank constructor for IO serialization

    private ClassItem(final String classItemName) {
        this.name = classItemName;

        // initializes Maps
        this.fieldItems = new HashMap<String, FieldItem>();
        this.methodItems = new HashMap<String, MethodItem>();
    }

    public static void addClassItem(final HashMap<String, ClassItem> classItems, final String classItemName) {
        String name = classItemName.toLowerCase().trim();// forces all classes to be in lower case and trims all leading
                                                         // and trailing "space" (refernce .trim() Java API for space
                                                         // definition).
        // if the classItemList does not already have a class named classItemName, we
        // create a new class
        if (!(classItems.containsKey(name))) {
            ClassItem createdClass = new ClassItem(name);
            classItems.put(createdClass.getName(), createdClass);
            System.out.println("Class \"" + createdClass.getName() + "\" created.");
        } else {
            // if classItemName is already in use in the classItemList that's passed in.
            System.out.println("Class name must be unique.");;
        }
    }
    public static void addClassItem(final HashMap<String, ClassItem> classItems, Scanner scanner, String inputName){
        /*when add class option is selected, we call addClassItem function*/
        boolean validName = false;
        String userInput = inputName.toLowerCase().trim();    //initialized to null
        while(!validName) {//gets user input for name until valid input, checks for null and blank input. returns when input is a name not in classItems already.
            //System.out.print("Enter class name you would like to add: \n>");
            //userInput = scanner.nextLine().toLowerCase().trim();
            validName = checkValidNewName(classItems, userInput);
        }
        String className = userInput;   //sets className variable to userInput that was valid.
        ClassItem newClass = new ClassItem(className);  //creates a new class.

        boolean addClassFin = false;
        while(!addClassFin){
            System.out.print("Add Field/Methods? (Y/N) \n>");
            userInput = scanner.nextLine().toLowerCase().trim();

            //switch case for adding more info
            switch (userInput) {
                case "n": case "no": //Creating class with no fields or methods
                    classItems.put(newClass.getName(), newClass);   //adds class with no fields/methods to classItems map
                    break;
                case "y": case "yes": //Adding fields and/or methods to class
                    addToClassMenu(newClass, scanner);
                    break;
                default:
                    System.out.println("Input valid option.");
            }
            addClassFin = true;
        }
        //finished adding class
        classItems.put(newClass.getName(), newClass);

        /*
        Created testClass
        Fields:
         */
        System.out.println("Created " + newClass.getName());

        if(!newClass.fieldItems.isEmpty()) {
            StringBuilder fields = new StringBuilder();
            for (HashMap.Entry<String, FieldItem> entry : newClass.fieldItems.entrySet())
                fields.append("\nField: " + entry.getValue().toString());
            System.out.print(fields.toString());
        }

        if(!newClass.fieldItems.isEmpty()) {
            StringBuilder methods = new StringBuilder();
            for (HashMap.Entry<String, MethodItem> entry : newClass.methodItems.entrySet())
                methods.append("\nField(s): " + entry.getValue().toString());
            System.out.println(methods.toString());
        }

    }

    public static void addToClassMenu(ClassItem classItem, Scanner scanner){

        boolean adding = true;

        while(adding) {
            System.out.println("+==================================+");
            System.out.println("Class: " + classItem.getName());
            //prints fields
            if(!(classItem.fieldItems.size() == 0)) {
                StringBuilder field_sb = new StringBuilder();
                for (HashMap.Entry<String, FieldItem> entry : classItem.fieldItems.entrySet())
                    field_sb.append("\nField(s): " + entry.getValue().toString());
                System.out.println(field_sb.toString());
            }else{
                System.out.println("Field(s): No Fields added yet.");
            }
            //prints methods
            if(!(classItem.methodItems.isEmpty())){
                StringBuilder method_sb = new StringBuilder();
                for (HashMap.Entry<String, MethodItem> entry : classItem.methodItems.entrySet())
                    method_sb.append("\nMethod(s): " + entry.getValue().toString());
                System.out.println(method_sb.toString());
            }else{
                System.out.println("Method(s): No Methods added yet.");
            }
            System.out.println("+==================================+");
            System.out.println("\"Add Fields\"");
            //add delete field/method functionality here?
            System.out.print("\"Add Methods\"\n('exit' to quit)>");
            String userInput = scanner.nextLine().toLowerCase().trim();
            switch (userInput) {
                case "add fields":
                case "add field":
                    addFieldMenu(scanner, classItem.fieldItems);
                    break;
                case "add methods":
                case "add method":
                    break;
                case "exit":    //done adding to class, return to main menu
                    adding = false;
                    break;
                default:
                    System.out.println("Input valid option (or type 'exit' to quit) \n>");
            }
        }
        //return to main menu
    }

    private static void addFieldMenu(Scanner scanner, HashMap<String, FieldItem> fieldItems){
        boolean addingFields = true;

        while(addingFields){
            System.out.println("Current Field(s):");
            if(!fieldItems.isEmpty()) { //fieldItems can't be null, constructor initializes them to empty HashMaps
                for (HashMap.Entry<String, FieldItem> entry : fieldItems.entrySet()) {
                    //String key = entry.getKey();
                    FieldItem value = entry.getValue();
                    System.out.println(" - " + value);
                }
                //add function to delete in this menu
            } else {
                System.out.println("No Fields added yet.");
            }

            boolean validInput = false;
            while(!validInput) {
                System.out.print("\nInput type and name pairs of Fields you would ike to add (Example: 'type1 name1, type2 name2,...')\n('exit' to quit)>");
                String userInput = scanner.nextLine();
                if(userInput.equals("exit")){
                    addingFields = false;
                    validInput = true;
                    continue;
                }
                //splits user input by ","
                String[] fieldPairs = userInput.split(",");

                for (String pair : fieldPairs){
                    String[] parts = pair.trim().split(" ");    //triming leading and trailing spaces

                    //checks that type and name are in the fieldPair, if not, it prompts, and skips to next pair the user gave.
                    if(parts.length != 2) {
                        System.out.println("Invalid input " + pair + " requires a type and name.");
                        continue;
                    }

                    String type = parts[0];
                    String name =   parts[1];

                    if(fieldItems.containsKey(name)){   //if the pair's name is already a field, skip it.
                        System.out.println("Duplicate field name: " + name + " is already defined.");
                        continue;
                    }

                    //adds field to field, didn't call add field method because it returns string
                    FieldItem newField = new FieldItem(name, type);
                    fieldItems.put(name, newField);
                    System.out.println("Added " + type + " " + name);
                }

            }
            //exited loop, returning to add field/methods menu
        }
    }
    private static void addMethodMenu(Scanner scanner, HashMap<String, MethodItem> methodItems){
        boolean addingMethods = true;

        while(addingMethods){
            System.out.println("Current Field(s):");
            if(!methodItems.isEmpty()) { //fieldItems can't be null, constructor initializes them to empty HashMaps
                for (HashMap.Entry<String, MethodItem> entry : methodItems.entrySet()) {
                    //String key = entry.getKey();
                    MethodItem value = entry.getValue();
                    System.out.println(" - " + value);
                }
                //add function to delete in this menu
            } else {
                System.out.println("No Methods added yet.");
            }

            boolean validInput = false;
            while(!validInput) {
                System.out.println("\nInput Name of Method, followed my parameter type and name you would like to add (Example: 'Method1: type1 name1, type2 name2')\n('exit' to quit)>");
                String userInput = scanner.nextLine();
                if(userInput.equals("exit")){
                    addingMethods = false;
                    validInput = true;
                    continue;
                }
                //
                String[] nameAndParams = userInput.split(":");
                MethodItem tempMethodItem = new MethodItem(nameAndParams[0]);   //creates MethodItem with name as name user wanted.

                //splits parameters given into pairs by ","
                String[] paramPairs = nameAndParams[1].split(",");

                for (String pair : paramPairs){
                    String[] parts = pair.trim().split(" ");    //triming leading and trailing spaces

                    //checks that type and name are in the fieldPair, if not, it prompts, and skips to next pair the user gave.
                    if(parts.length != 2) {
                        System.out.println("Invalid input " + pair + " requires a type and name.");
                        continue;
                    }

                    String type = parts[0];
                    String name =   parts[1];

                    //add type and name to methodItems parameter
                }

            }
            //exited loop, returning to add field/methods menu
        }
    }
    // public getters and setters for fields required by IO serialization
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

    public static String renameClassItem(final HashMap<String, ClassItem> classItemList, final String newClassItemName,
            final String oldClassItemName, HashMap<String, RelationshipItem> relationships) {
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
    public static String removeClassItem(final HashMap<String, ClassItem> classItems, final String classItemName,
        HashMap<String, RelationshipItem> relationships) {
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

    public String addField(String fieldName, String type) {
        // preconditions
        if (fieldName == null || fieldName.isBlank() || type == null || type.isBlank()) {
            return "Field name or type cannot be null or blank";
        }

        fieldName = fieldName.toLowerCase().trim();
        // types can be uppercase for example: String name
        //type = type.toLowerCase().trim();
        type = type.trim();

        // check if field already exists
        if (fieldItems.containsKey(fieldName)) {
            return "Field name: " + fieldName + " already in use.";
        }

        // create new field item object
        FieldItem newField = new FieldItem(fieldName, type);

        // add new field item to map
        fieldItems.put(fieldName, newField);

        return fieldName + " was successfully added to " + this.name;
    }

    public String removeField(String fieldName) {
        // preconditions
        if (fieldName == null || fieldName.isBlank()) {
            return "Field name cannot be null or blank";
        }   

        // trim leading and trailing whitespace
        fieldName = fieldName.trim();

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
        oldName = oldName.trim();
        newName = newName.trim();

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