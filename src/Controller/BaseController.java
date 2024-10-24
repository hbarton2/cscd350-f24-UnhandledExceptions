package Controller;

import Model.ClassItem;
import Model.MethodItem;
import Model.Data;
import Model.RelationshipItem;

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
    public String AddClass(String newName)
    {
        // We take the input and use data to manipulate the class item hashmap in data, then pass in the user input, and this returns a message back to the CLI.
        return ClassItem.addClassItem(data.getClassItems(), newName);
    }

    public String RemoveClass(String name)
    {
        return ClassItem.removeClassItem(data.getClassItems(), data.getRelationshipItems(), name);
    }

    public String RenameClass(String oldName, String newName)
    {
        return ClassItem.renameClassItem(data.getClassItems(), data.getRelationshipItems(), oldName, newName);
    }

    public String AddRelationship(String source, String destination)
    {
        return RelationshipItem.addRelationship(data.getClassItems(), data.getRelationshipItems(), source, destination);
    }

    public String RemoveRelationship(String source, String destination)
    {
        return RelationshipItem.removeRelationship(data.getRelationshipItems(), source, destination);
    }

    public String AddField(String className, String type, String name)
    {
        return ClassItem.addField(data.getClassItems().get(className), type, name);
    }

    public String RemoveField(String className, String name)
    {
        return ClassItem.removeField(data.getClassItems().get(className), name);
    }

    public String RenameField(String className, String oldName, String newName)
    {
        return ClassItem.renameField(data.getClassItems().get(className), oldName, newName);
    }

    public String AddMethod(String className, String name)
    {
        return ClassItem.addMethod(data.getClassItems().get(className), name);
    }

    public String RemoveMethod(String className, String name)
    {
        return ClassItem.removeMethod(data.getClassItems().get(className), name);
    }

    public String RenameMethod(String className, String oldName, String newName)
    {
        return ClassItem.renameMethod(data.getClassItems().get(className), oldName, newName);
    }

    public String AddParameter(String className, String methodName, String type, String name)
    {
        // Here we need to get a methodItem object from data before passing it in to addParameter in the model.
        MethodItem methodItem = data.getClassItems().get(className).getMethodItems().get(methodName);
        return MethodItem.addParameter(methodItem, type, name);
    }

    public String RemoveParameter(String className, String methodName, String name, String type)
    {
        MethodItem methodItem = data.getClassItems().get(className).getMethodItems().get(methodName);
        return MethodItem.removeParameter(methodItem, type, name);
    }

    public String ChangeParameter(String className, String methodName, String oldType, String oldName, String newType, String newName)
    {
        MethodItem methodItem = data.getClassItems().get(className).getMethodItems().get(methodName);
        return MethodItem.changeParameter(methodItem, oldType, oldName, newType, newName);
    }
}