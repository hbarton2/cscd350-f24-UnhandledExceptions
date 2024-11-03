package com.unhandledexceptions.Controller;

import com.unhandledexceptions.Model.ClassItem;
import com.unhandledexceptions.Model.MethodItem;
import com.unhandledexceptions.Model.Data;
import com.unhandledexceptions.Model.RelationshipItem;

public class BaseController
{
    // our controller gets passed in a data object for storage from the CLI which gets passed a data object from main
    Data data;

    public BaseController(Data data)
    {
        this.data = data;
    }

    /*
     * Our controller is responsible for handling the commands passed in from the CLI and grabbing data from the model to manipulate.
     * The controller is also responsible for returning the results of the commands to the CLI to be displayed to the user.
     * The controller methods ONLY TAKE the input from the user and use the data object to manipulate the data via methods from the model.
     * All controller methods must follow suit and return a string to be displayed to the user via the CLI.
     */

    // Example: The addClass is only taking a string file name from the CLI input.
    public String AddClassListener(String newName)
    {
        // We take the input and use data to manipulate the class item hashmap in data, then pass in the user input, and this returns a message back to the CLI.
        return ClassItem.addClassItem(data.getClassItems(), newName);
    }

    public String RemoveClassListener(String name)
    {
        return ClassItem.removeClassItem(data.getClassItems(), data.getRelationshipItems(), name);
    }

    public String RenameClassListener(String oldName, String newName)
    {
        return ClassItem.renameClassItem(data.getClassItems(), data.getRelationshipItems(), oldName, newName);
    }

    public String AddRelationshipListener(String source, String destination, String type)
    {
        // Need to ensure that the type is only one of four allowed types.
        if (!(type.toLowerCase().trim().equals("aggregation") || type.toLowerCase().trim().equals("composition") || type.toLowerCase().trim().equals("generalization") || type.toLowerCase().trim().equals("realization")))
        {
            // If it's not one of the four types, we return a message to the user.
            return "Invalid relationship type. Valid types: aggregation, composition, generalization, realization";
        }

        return RelationshipItem.addRelationship(data.getClassItems(), data.getRelationshipItems(), source, destination, type);
    }

    public String RemoveRelationshipListener(String source, String destination)
    {
        return RelationshipItem.removeRelationship(data.getRelationshipItems(), source, destination);
    }

    public String PlaceRelationshipListener(String source, String dest, int sourceInt, int destInt)
    {
        return RelationshipItem.placeRelation(data.getRelationshipItems(), source, dest, sourceInt, destInt);
    }

    public String AddFieldListener(String className, String type, String name)
    {
        return ClassItem.addField(data.getClassItems().get(className), type, name);
    }

    public String RemoveFieldListener(String className, String name)
    {
        return ClassItem.removeField(data.getClassItems().get(className), name);
    }

    public String RenameFieldListener(String className, String oldName, String newName)
    {
        return ClassItem.renameField(data.getClassItems().get(className), oldName, newName);
    }

    public String AddMethodListener(String className, String name, String type)
    {
        return ClassItem.addMethod(data.getClassItems().get(className), name, type);
    }

    public String RemoveMethodListener(String className, String name)
    {
        return ClassItem.removeMethod(data.getClassItems().get(className), name);
    }

    public String RenameMethodListener(String className, String oldName, String newName)
    {
        return ClassItem.renameMethod(data.getClassItems().get(className), oldName, newName);
    }

    public String AddParameterListener(String className, String methodName, String type, String name)
    {
        // Here we need to get a methodItem object from data before passing it in to addParameter in the model.
        MethodItem methodItem = data.getClassItems().get(className).getMethodItems().get(methodName);
        return MethodItem.addParameter(methodItem, type, name);
    }

    public String RemoveParameterListener(String className, String methodName, String name, String type)
    {
        MethodItem methodItem = data.getClassItems().get(className).getMethodItems().get(methodName);
        return MethodItem.removeParameter(methodItem, type, name);
    }

    public String ChangeParameterListener(String className, String methodName, String oldType, String oldName, String newType, String newName)
    {
        MethodItem methodItem = data.getClassItems().get(className).getMethodItems().get(methodName);
        return MethodItem.changeParameter(methodItem, oldType, oldName, newType, newName);
    }
}
