import java.util.Map;
import java.util.Scanner;

public class RelationshipItem
{
    // ClassItem objects that represent the source and destination of the relationship
    private ClassItem source;
    private ClassItem destination;
    
    public RelationshipItem(final ClassItem source, final ClassItem destination){

        // checking for null values and throwing an exception if the source or destination are null
        // we can't have a relationship with only a source or only a destination
        if(source == null || destination == null){
            throw new IllegalArgumentException("source and destination must not be null");
        }

        // sets the source and destination ClassItem objects
        this.source = source;
        this.destination = destination;
    }

    /* addRelationship takes a Map of relationships we currently have created from the main.java, a ClassItem source and a ClassItem destination.
     * The goal is to create a relationship object between the source and destination and store it in the map.
     * If the relationship already exists, we return a message that the relationship already exists.
     */
    public String addRelationship(Map<String, RelationshipItem> map, final ClassItem source, final ClassItem destination){
        // checking for null values and throwing an exception if the source or destination are null
        // we can't have a relationship with only a source or only a destination
        if (source == null || destination == null) {
            throw new IllegalArgumentException("source and destination must not be null");
        }

        // creating the key for the relationship
        String key = source.getClassItemName() + "_" + destination.getClassItemName();

        // checking if the relationship is already created and stored in the map
        if (map.containsKey(key)) {
            return "Relationship already exists";
        }

        // creating the relationship
        RelationshipItem relationship = new RelationshipItem(source, destination);

        // storing the relationship in the map
        map.put(key, relationship);

        // returning a message that the relationship has been created
        return "A relationship has been created between" + source.getClassItemName() + " and " + destination.getClassItemName();
    }
    /*
     * removeRelationship takes a Map of relationships we currently have created from the main.java.
     * This method will list the relationships and prompt the user to remove one by typing the "key" or name of the relationship
     * If the relationship exists, it will be removed from the map and a message will be returned that the relationship has been removed.
     * If the relationship is typed incorrectly, a message will be returned that the relationship does not exist. The user will be prompted again.
     * If the user types "exit", the method will return a message that the user has exited the method, and the method ends.
     */
    public String removeRelationship(Map<String, RelationshipItem> relationshipMap){
        // can't have a null relationshipMap
        if(relationshipMap == null) {
            throw new IllegalArgumentException("relationshipMap must not be null");
        }
        // can't delete anything if the map is empty
        if(relationshipMap.isEmpty()){
            return "No relationships to remove";
        }

        // printing the relationships
        System.out.println("Relationships:");
        // for each loop with entry being the key and value of the map given by relationshipMap.entryset()
        for(Map.Entry<String, RelationshipItem> entry : relationshipMap.entrySet()){
            System.out.println("Key: " + entry.getKey() + " Value: " + entry.getValue().toString());
        }

        // prompt the user to type the key of the relationship they want to remove
        // scanner for scanning keyboard
        Scanner kb = new Scanner(System.in);

        // loop to keep prompting the user until they type "exit" or a valid key
        while(true){
            // getting input
            System.out.println("Enter the key of the relationship to remove (or type 'exit' to cancel):");
            String key = kb.nextLine();

            // if the user types exit we exit the method
            if(key.equalsIgnoreCase("exit")){
                kb.close();
                return "exiting delete menu...";
            }
            
            // if the relationship exists, we remove it and return a message that the relationship has been removed
            if(relationshipMap.containsKey(key)){
                relationshipMap.remove(key);
                kb.close();
                return "Relationship has been removed";
            }
            // if the relationship does not exist, we return a message that the relationship does not exist
            else{
                System.out.println("Please enter a valid option...");
            }
        }
    }

    //returns the source ClassItem object
    public ClassItem getSource() {
        return source;
    }

    //returns the destination ClassItem object
    public ClassItem getDestination() {
        return destination;
    }

    // overrides the toString method to return a string representation of the relationship
    @Override
    public String toString(){

        //the ClassItem objects have a getName() method that returns the name of the class
        return this.source.getClassItemName() + "has a relationship with " + this.destination.getClassItemName();
    }


};