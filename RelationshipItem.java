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
        return this.source.getName() + "has a relationship with " + this.destination.getName();
    }


};