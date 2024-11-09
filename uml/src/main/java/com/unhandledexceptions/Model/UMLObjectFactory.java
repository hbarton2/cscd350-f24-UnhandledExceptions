package com.unhandledexceptions.Model;

public class UMLObjectFactory {

    public enum ObjectType {
        METHODITEM,
        PARAMETERITEM,
        FIELDITEM
    }

    @SuppressWarnings("unchecked")
    public static <T extends UMLObject> T createUMLObject(ObjectType objectType, String name, String type) {
        if (objectType == null) {
            throw new IllegalArgumentException("Object type cannot be null");
        }

        switch (objectType) {
            case METHODITEM:
                return (T) new MethodItem(name, type);

            case PARAMETERITEM:
                return (T) new ParameterItem(name, type);

            case FIELDITEM:
                return (T) new FieldItem(name, type);

            default:
                break;
        }
        return null;
    }
}
