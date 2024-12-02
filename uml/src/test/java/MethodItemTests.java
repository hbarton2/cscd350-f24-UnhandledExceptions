// Testing imports
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.BeforeEach;

import com.unhandledexceptions.Model.MethodItem;
import com.unhandledexceptions.Model.ParameterItem;


public class MethodItemTests {
    // Test MethodItem constructor
    @Test
    public void testMethodItemConstructor() {
        // Create the MethodItem
        MethodItem methodItem = new MethodItem("methodName", "type");
        // Assert we have the right types, these also test getters for name and types
        assertEquals("methodName", methodItem.getName());
        assertEquals("type", methodItem.getType());
    }

    // Test MethodItem constructor with null method name
    @Test
    public void testIncorrectMethodItemConstructor() {
        // Ensure that an IllegalArgumentException is thrown when the method name is null
        assertThrows(IllegalArgumentException.class, () -> {
            @SuppressWarnings("unused")
            MethodItem methodItem = new MethodItem(null, "type");
        });
    }

    // Test setName
    @Test
    public void testSetName() {
        // Create the MethodItem
        MethodItem methodItem = new MethodItem("methodName", "type");
        // Set the name
        methodItem.setName("newName");
        // Assert we have the right name
        assertEquals("newName", methodItem.getName());
    }

    // Test setName with null input
    @Test
    public void testSetNullName() {
        // Create the MethodItem
        MethodItem methodItem = new MethodItem("methodName", "type");
        // Set the name
        methodItem.setName(null);
        // Assert we have the right name
        assertEquals("methodName", methodItem.getName());
    }

    // Test setType
    @Test
    public void testSetType() {
        // Create the MethodItem
        MethodItem methodItem = new MethodItem("methodName", "type");
        // Set the type
        methodItem.setType("newType");
        // Assert we have the right type
        assertEquals("newType", methodItem.getType());
    }

    // Test adding a parameter
    @Test
    public void testAddParameter() {
        // Create the MethodItem
        MethodItem methodItem = new MethodItem("methodName", "type");
        // add a parameter
        MethodItem.addParameter(methodItem, "type", "parameterName");
        // Assert we have the right parameter
        assertEquals("parameterName", methodItem.getParameters().get("parameterName").getName());
    }

    // Test adding a parameter with a duplicate name
    @Test
    public void testAddParameterDuplicate() {
        // Create the MethodItem
        MethodItem methodItem = new MethodItem("methodName", "type");
        // Add a parameter
        MethodItem.addParameter(methodItem, "type", "name");
        String result = MethodItem.addParameter(methodItem, "type", "name");

        // Assert that the parameter was not added
        assertEquals("Parameter name: name already in use.", result);

    }

    // Test adding a parameter with a null name
    @Test
    public void testAddBadParameter() {
        // Create the MethodItem
        MethodItem methodItem = new MethodItem("methodName", "type");
        // Ensure that an IllegalArgumentException is thrown when the parameter name is null
        assertThrows(IllegalArgumentException.class, () -> {
            MethodItem.addParameter(methodItem, "type", null);
        });
    }

    // Test getParameter
    @Test
    public void testGetParameter() {
        // Create the MethodItem
        MethodItem methodItem = new MethodItem("methodName", "type");
        // add a parameter
        MethodItem.addParameter(methodItem, "type", "parameterName");
        // Get the parameter
        ParameterItem parameter = methodItem.getParameters().get("parameterName");
        // Assert we have the right parameter
        assertEquals("parameterName", parameter.getName());
    }

    // Test removing a parameter
    @Test
    public void testRemoveParameter() {
        // Create the MethodItem
        MethodItem methodItem = new MethodItem("methodName", "type");
        // add a parameter
        MethodItem.addParameter(methodItem, "type", "parameterName");
        // Assert we have the parameter
        assertTrue(methodItem.getParameters().containsKey("parameterName"));
        // remove the parameter
        MethodItem.removeParameter(methodItem, "type", "parameterName");
        // Assert we don't have the parameter anymore
        assertFalse(methodItem.getParameters().containsKey("parameterName"));
    }

}
