import java.util.Map;

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