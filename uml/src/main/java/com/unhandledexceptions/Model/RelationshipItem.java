package com.unhandledexceptions.Model;

import java.util.HashMap;
import javafx.geometry.Point2D;

public class RelationshipItem
{
    // ClassItem objects that represent the source and destination of the relationship
    private ClassItem source;
    private ClassItem destination;
    private String type;
    private Point2D sourceLoc, destLoc;
    
    public RelationshipItem() {} //blank constructor for IO serialization

    public RelationshipItem(final ClassItem source, final ClassItem destination, final String type)
    {
        sourceLoc = Point2D.ZERO;
        destLoc = Point2D.ZERO;
        // checking for null values and throwing an exception if the source or destination are null
        // we can't have a relationship with only a source or only a destination
        if(source == null || destination == null){
            throw new IllegalArgumentException("source and destination must not be null");
        }

        // sets the source and destination ClassItem objects as well as the String type
        this.source = source;
        this.destination = destination;
        this.type = type.substring(0, 1).toUpperCase() + type.substring(1).toLowerCase();
    }

    /**
     * The point of this constructor is to make a deep copy object of a RelationshipItem.
     * 
     * @param relationship the relationship object to make a copy of
     */

    private RelationshipItem(RelationshipItem relationship)
    {
        this.source = relationship.source;
        this.destination = relationship.destination;
        this.type = relationship.type;
        this.sourceLoc = relationship.sourceLoc;
        this.destLoc = relationship.destLoc;
    }

    /**
     * The method used to copy a relationshipItem
     * 
     * @param other the relationship item to be copied
     * @return a relationshipItem object that is a copy of the parameter object passed in 
     */
    public static RelationshipItem copyRelationshipItem(RelationshipItem other) {
        return new RelationshipItem(other);
    }

    /* addRelationship takes a Map of relationships we currently have created from the main.java, String source and String destination.
     * The goal is to create a relationship object between the source and destination and store it in the map.
     * If the relationship already exists, we return a message that the relationship already exists.
    */

    public static String addRelationship(
        HashMap<String, ClassItem> classes, 
        HashMap<String, RelationshipItem> relationships, String source, String destination, String type)
    {
        // at this point the key is created with everything being lowercase
        String key = source + "_" + destination;

        // at this point we need to create a relationship to add to the map
        // we need to check if the classes exist
        if (!classes.containsKey(source) || !classes.containsKey(destination)){
            return "One or both of the classes do not exist";
        }

        //proper case for type
        type = type.substring(0, 1).toUpperCase() + type.substring(1).toLowerCase();

        // if the relationship already exists, we return a message that the relationship already exists
        if(relationships.containsKey(key)){
            relationships.get(key).setType(type);
        }

        // creating the relationship object
        RelationshipItem relationship = new RelationshipItem(classes.get(source), classes.get(destination), type);

        // adding the relationship to the map
        relationships.put(key, relationship);

        return "good";
    }


    /*
     * removeRelationship takes a Map of relationships we currently have created from the main.java, a String source and a String destination for the class names.
     * It removes a relationship from the passed in relationship map if it exists based on the key. \
     * If the relationship does not exist, we return a message that the relationship was not found.
    */
    public static String removeRelationship(
        HashMap<String, RelationshipItem> relationshipMap, String source, String destination)
    {
        // key is sourceclass_destinationclass
        String key = source + "_" + destination;
        // if the key is in the relationship map, we remove it along with the value which is a relationship object
        if(relationshipMap.containsKey(key)){
            relationshipMap.remove(key);
            return "good";
        }
        else{
            return "Relationship not found.";
        }
    }

    public static String changeRelationType(
        HashMap<String, RelationshipItem> relationshipMap,
        String source, String dest, String type)
    {
        // key is sourceclass_destinationclass
        String key = source + "_" + dest;

        //proper case for type
        type = type.substring(0, 1).toUpperCase() + type.substring(1).toLowerCase();

        // if the key is in the relationship map, we remove it along with the value which is a relationship object
        if(relationshipMap.containsKey(key)){
            relationshipMap.get(key).setType(type);
            return "good";
        }
        else{
            return "Relationship not found.";
        }
    }

    //returns the source ClassItem object
    public ClassItem getSource() {
        return this.source;
    }

    //sets the destination
    public void setSource(ClassItem source) {
        this.source = source;
    }

    //returns the destination ClassItem object
    public ClassItem getDestination() {
        return this.destination;
    }

    //sets the destination
    public void setDestination(ClassItem destination) {
        this.destination = destination;
    }

    public Point2D getSourceLoc()
    {
        return this.sourceLoc;
    }

    public void setSourceLoc(Point2D sourceLoc)
    {
        this.sourceLoc = sourceLoc;
    }

    public Point2D getDestLoc()
    {
        return this.destLoc;
    }

    public void setDestLoc(Point2D destLoc)
    {
        this.destLoc = destLoc;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        //proper case for type
        type = type.substring(0, 1).toUpperCase() + type.substring(1).toLowerCase();
        this.type = type;
    }

    // overrides the toString method to return a string representation of the relationship
    @Override
    public String toString(){

        //the ClassItem objects have a getName() method that returns the name of the class
        return this.source.getName() + " ---- " + this.type + " ----> " + this.destination.getName();
    }


};
