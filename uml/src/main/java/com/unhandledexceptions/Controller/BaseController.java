package com.unhandledexceptions.Controller;

import com.unhandledexceptions.Model.ClassItem;
import com.unhandledexceptions.Model.MethodItem;
import com.unhandledexceptions.Model.Data;
import com.unhandledexceptions.Model.RelationshipItem;
import com.unhandledexceptions.View.GUI;

import java.util.function.Supplier;

public class BaseController
{
    // our controller gets passed in a data object for storage from the CLI which gets passed a data object from main
    Data data;
    Caretaker careTaker;

    public BaseController(Data data)
    {
        this.data = data;
        this.careTaker = new Caretaker(data);
    }

    public Data getData()
    {
        return this.data;
    }

    public Caretaker getCareTaker()
    {
        return this.careTaker;
    }
   
    /*
     * Our controller is responsible for handling the commands passed in from the CLI and grabbing data from the model to manipulate.
     * The controller is also responsible for returning the results of the commands to the CLI to be displayed to the user.
     * The controller methods ONLY TAKE the input from the user and use the data object to manipulate the data via methods from the model.
     * All controller methods must follow suit and return a string to be displayed to the user via the CLI.
     */

    // Example: The addClass is only taking a string file name from the CLI input.
    public String withMemento(Supplier<String> action){
        careTaker.saveState();  // Save the current state
        String result = action.get();  // Perform the action

        // If the action is unsuccessful, undo the last saved state
        if (!result.equals("good")) {
            careTaker.removeLast();
        }

        return result;
    }

    public String AddClassListener(String newName)
    {
        // We take the input and use data to manipulate the class item hashmap in data, then pass in the user input, and this returns a message back to the CLI.
        /*
         * create memento
         * try to add class
         * if good 
         *  save memento in caretaker history
         *  return good message
         * if bad
         *  return bad message
         */
        return withMemento(() -> ClassItem.addClassItem(data.getClassItems(), newName));
    }

    public String RemoveClassListener(String name)
    {
      //  return ClassItem.removeClassItem(data.getClassItems(), data.getRelationshipItems(), name);
        return withMemento(() -> ClassItem.removeClassItem(data.getClassItems(), data.getRelationshipItems(), name));
    }

    public String RenameClassListener(String oldName, String newName)
    {
        return withMemento(() -> ClassItem.renameClassItem(data.getClassItems(), data.getRelationshipItems(), oldName, newName));
    }

    public String AddRelationshipListener(String source, String destination, String type)
    {
        // Need to ensure that the type is only one of four allowed types.
        if (!(type.toLowerCase().trim().equals("aggregation") || type.toLowerCase().trim().equals("composition") || type.toLowerCase().trim().equals("generalization") || type.toLowerCase().trim().equals("realization")))
        {
            // If it's not one of the four types, we return a message to the user.
            return "Invalid relationship type. Valid types: aggregation, composition, generalization, realization";
        }

        return withMemento(() -> RelationshipItem.addRelationship(data.getClassItems(), data.getRelationshipItems(), source, destination, type));
        
    }

    public String ChangeRelationshipTypeListener(String source, String destination, String type)
    {
        // Need to ensure that the type is only one of four allowed types.
        if (!(type.toLowerCase().trim().equals("aggregation") || type.toLowerCase().trim().equals("composition") || type.toLowerCase().trim().equals("generalization") || type.toLowerCase().trim().equals("realization")))
        {
            // If it's not one of the four types, we return a message to the user.
            return "Invalid relationship type. Valid types: aggregation, composition, generalization, realization";
        }

        return withMemento(() -> RelationshipItem.changeRelationType(data.getRelationshipItems(), source, destination, type));
        
    }

    public String RemoveRelationshipListener(String source, String destination)
    {
        return withMemento(() -> RelationshipItem.removeRelationship(data.getRelationshipItems(), source, destination));
    }

    public String AddFieldListener(String className, String type, String name)
    {
        return withMemento(() -> ClassItem.addField(data.getClassItems().get(className), type, name));
    }

    public String RemoveFieldListener(String className, String name)
    {
        return withMemento(() -> ClassItem.removeField(data.getClassItems().get(className), name));
    }

    public String RenameFieldListener(String className, String oldName, String newName)
    {
        return withMemento(() -> ClassItem.renameField(data.getClassItems().get(className), oldName, newName));
    }

    public String RetypeFieldListener(String className, String fieldName, String newType)
    {
        return withMemento(() -> ClassItem.retypeField(data.getClassItems().get(className), fieldName, newType));
    }

    public String AddMethodListener(String className, String name, String type)
    {
        return withMemento(() -> ClassItem.addMethod(data.getClassItems().get(className), name, type));
    }

    public String RemoveMethodListener(String className, String name)
    {
        return withMemento(() -> ClassItem.removeMethod(data.getClassItems().get(className), name));
    }

    public String RenameMethodListener(String className, String oldName, String newName)
    {
        return withMemento(() -> ClassItem.renameMethod(data.getClassItems().get(className), oldName, newName));
    }

    public String RetypeMethodListener(String className, String methodName, String newType){
        return withMemento(() -> ClassItem.retypeMethod(data.getClassItems().get(className), methodName, newType));
    }

    public String AddParameterListener(String className, String methodName, String type, String name)
    {
        // Here we need to get a methodItem object from data before passing it in to addParameter in the model.
        MethodItem methodItem = data.getClassItems().get(className).getMethodItems().get(methodName);
        return withMemento(() -> MethodItem.addParameter(methodItem, type, name));
    }

    public String RemoveParameterListener(String className, String methodName, String name, String type)
    {
        MethodItem methodItem = data.getClassItems().get(className).getMethodItems().get(methodName);
        return withMemento(() -> MethodItem.removeParameter(methodItem, type, name));
    }

    public String ChangeParameterListener(String className, String methodName, String oldType, String oldName, String newType, String newName)
    {
        MethodItem methodItem = data.getClassItems().get(className).getMethodItems().get(methodName);
        return withMemento(() -> MethodItem.changeParameter(methodItem, oldType, oldName, newType, newName));
    }

    public String RenameParameterListener(String className, String methodName, String newName, String oldName)
    {
        MethodItem methodItem = data.getClassItems().get(className).getMethodItems().get(methodName);
        return withMemento(() -> MethodItem.renameParameter(methodItem, oldName, newName));
    }

    public String RetypeParameterListener(String className, String methodName, String oldParamName, String newParamType)
    {
        MethodItem methodItem = data.getClassItems().get(className).getMethodItems().get(methodName);
        return withMemento(() -> MethodItem.retypeParameter(methodItem, oldParamName, newParamType));
    }

    public String undoListener() {
        return this.careTaker.undo();
    }

    public String redoListener() {
        return this.careTaker.redo();
    }

    /**
     * This method will take user input for screenshotting in the CLI with a file name.
     * The method sets modes and file name in the GUI to take a screenshot, because we are now launching a GUI thread when we call this method.
     * 
     * @param fileName the name the user wants the screenshot to be called
     * @return a string message indicating success or failure
     */
    public String screenshotListener(String fileName) {
        try 
        {
            GUI.setBaseController(this);
            GUI.setScreenshotMode(true);
            GUI.setScreenshotFile(fileName);
            GUI.main();

            return "good";
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return "Screenshot Unsuccessful";
        }
    }
}
