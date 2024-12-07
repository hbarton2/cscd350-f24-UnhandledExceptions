package com.unhandledexceptions.Model;

import java.util.HashMap;

public class RelationshipItem
{
    // ClassItem objects that represent the source and destination of the relationship
    private ClassItem source;
    private ClassItem destination;
    private String type;
    //private Point2D sourceLoc, destLoc;
    private double sourceX, sourceY, destX, destY;
    
    public RelationshipItem() {} //blank constructor for IO serialization

    public RelationshipItem(final ClassItem source, final ClassItem destination, final String type)
    {
        sourceX = 0; sourceY = 0; destX = 0; destY = 0;
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
        setSourceX(relationship.getSourceX());
        setSourceY(relationship.getSourceY());
        setDestX(relationship.getDestX());
        setDestY(relationship.getDestY());
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

    public double getSourceX() { return this.sourceX; }
    public double getSourceY() { return this.sourceY; }
    public double getDestX() { return this.destX; }
    public double getDestY() { return this.destY; }
    public void setSourceX(double sourceX) { this.sourceX = sourceX; }
    public void setSourceY(double sourceY) { this.sourceY = sourceY; }
    public void setDestX(double destX) { this.destX = destX; }
    public void setDestY(double destY) { this.destY = destY; }

    // public Point2D getSourceOffset()
    // {
    //     Point2D sourceOffset = new Point2D(this.sourceX, this.sourceY);
    //     return sourceOffset;
    // }

    // public Point2D getDestOffset()
    // {
    //     Point2D destOffset = new Point2D(this.destX, this.destY);
    //     return destOffset;
    // }

    // public void setSourceOffset(Point2D sourceOffset)
    // {
    //     this.sourceX = sourceOffset.getX();
    //     this.sourceY = sourceOffset.getY();
    // }

    // public void setDestOffset(Point2D destOffset)
    // {
    //     this.destX = destOffset.getX();
    //     this.destY = destOffset.getY();
    // }

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
