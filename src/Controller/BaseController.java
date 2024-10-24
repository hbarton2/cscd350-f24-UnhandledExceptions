package Controller;

import Model.ClassItem;
import Model.MethodItem;
import Model.Data;
import Model.RelationshipItem;

public class BaseController
{
    Data data = new Data();
    
    public BaseController(Data data)
    {
        this.data = data;
    }

    public String AddClass(String newName)
    {
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