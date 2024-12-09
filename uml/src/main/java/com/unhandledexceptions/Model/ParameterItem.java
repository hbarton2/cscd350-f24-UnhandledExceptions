package com.unhandledexceptions.Model;

public class ParameterItem implements UMLObject {


    // String representing the name
    private String parameterName;
    private String type;

    public ParameterItem() {
    } // blank constructor for IO serialization

    // constructor
    public ParameterItem(String parameterName, String type) {
        if (parameterName == null || parameterName.isBlank()) {
            throw new IllegalArgumentException("Type and Parameter name cannot be null or blank - constructor");
        }

        // if (DataType.isValidType(type)) {
        this.parameterName = parameterName;
        this.type = type;
        // } else {
        // throw new IllegalArgumentException("Invalid type in parameter constructor");
        // }

    }

    // getter to retrieve the parameter name
    public String getName() {
        return this.parameterName;
    }

    // setter to set the parameter name
    public void setName(String parameterName) {
        if (parameterName == null || parameterName.isBlank()) {
            throw new IllegalArgumentException("Parameter name cannot be null or blank");
        }

        this.parameterName = parameterName;
    }

    /*
     * public String getParameterType(ParameterItem parameter) {
     * if (parameter == null) {
     * throw new IllegalAccessError("Parameter Item is null");
     * }
     * 
     * return parameter.type;
     * }
     * 
     * replacing this with standardized getter and setter for IO serialization.
     * saving incase it was necessary.
     */

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return this.type + " " + this.parameterName;
    }
}
