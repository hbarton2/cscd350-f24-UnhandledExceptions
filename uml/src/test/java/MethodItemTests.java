// Testing imports
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import com.unhandledexceptions.Model.MethodItem;
import com.unhandledexceptions.Model.ParameterItem;
import java.util.HashMap;


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

    // Test removing a param that doesn't exist
    @Test
    public void testRemoveParameterDoesNotExist() {
        // Create the MethodItem
        MethodItem methodItem = new MethodItem("methodName", "type");
        // remove the parameter
        String result = MethodItem.removeParameter(methodItem, "type", "parameterName");
        // Assert we have the right result
        assertEquals("Parameter: type parameterName does not exist", result);
    }

    // Test removing a param with a null name
    @Test
    public void testRemoveNullParameter() {
        // Create the MethodItem
        MethodItem methodItem = new MethodItem("methodName", "type");
        // Ensure that an IllegalArgumentException is thrown when the parameter name is null
        assertThrows(IllegalArgumentException.class, () -> {
            MethodItem.removeParameter(methodItem, "type", null);
        });
    }

    // Test changing a parameter
    @Test
    public void testChangeParameter() {
        // Create the MethodItem
        MethodItem methodItem = new MethodItem("methodName", "type");
        // add a parameter
        MethodItem.addParameter(methodItem, "type", "parameterName");
        // Assert we have the parameter
        assertTrue(methodItem.getParameters().containsKey("parameterName"));
        // change the parameter
        MethodItem.changeParameter(methodItem, "type", "parameterName", "newType", "newName");
        // Assert we don't have the parameter anymore
        assertFalse(methodItem.getParameters().containsKey("parameterName"));
        // Assert we have the right parameter
        assertEquals("newName", methodItem.getParameters().get("newName").getName());
    }

    // Test if renaming parameter is already in use
    @Test
    public void testChangeParameterInUse() {
        // Create the MethodItem
        MethodItem methodItem = new MethodItem("methodName", "type");
        // add a parameter
        MethodItem.addParameter(methodItem, "type", "parameterName");
        // Assert we have the parameter
        assertTrue(methodItem.getParameters().containsKey("parameterName"));
        // change the parameter
        String result = MethodItem.changeParameter(methodItem, "type", "parameterName", "newType", "parameterName");
        // Assert we have the right result
        assertEquals("New Parameter name: parameterName already in use.", result);
        // Assert we still have the same parameter
        assertTrue(methodItem.getParameters().containsKey("parameterName"));
    }
    
    // Test changing nonexistent parameter
    @Test
    public void testChangeParameterDoesntExist() {
        // Create the MethodItem
        MethodItem methodItem = new MethodItem("methodName", "type");
        // change the parameter
        String result = MethodItem.changeParameter(methodItem, "type", "parameterName", "newType", "newName");
        // Assert we have the right result
        assertEquals("Parameter: type parameterName does not exist.", result);
    }

    // Test changing a parameter with a null name
    @Test
    public void testChangeNullParameter() {
        // Create the MethodItem
        MethodItem methodItem = new MethodItem("methodName", "type");
        // Ensure that an IllegalArgumentException is thrown when the parameter name is null
        assertThrows(IllegalArgumentException.class, () -> {
            MethodItem.changeParameter(methodItem, "type", null, "newType", "newName");
        });
    }

    // Test renaming parameter
    @Test
    public void testRenameParameter() {
        // Create the MethodItem
        MethodItem methodItem = new MethodItem("methodName", "type");
        // add a parameter
        MethodItem.addParameter(methodItem, "type", "parameterName");
        // rename the parameter
        MethodItem.renameParameter(methodItem, "parameterName", "newName");
        // assert we have the new name and not the old name
        assertFalse(methodItem.getParameters().containsKey("parameterName"));
        assertTrue(methodItem.getParameters().containsKey("newName"));
    }

    // Test renaming parameter that doesn't exist
    @Test
    public void testRenameParameterNotExist() {
        // Create the MethodItem
        MethodItem methodItem = new MethodItem("methodName", "type");
        // rename parameter
        String result = MethodItem.renameParameter(methodItem, "blaj", "slkja;dls");
        // make sure we have correct output
        assertEquals("Parameter: blaj does not exist.", result);
    }

    // Test null input for name of parameter
    @Test
    public void testRenameNullParameter() {
        // Create the MethodItem
        MethodItem methodItem = new MethodItem("methodName", "type");
        // Ensure that an IllegalArgumentException is thrown when the parameter name is null
        assertThrows(IllegalArgumentException.class, () -> {
            MethodItem.renameParameter(methodItem, null, "newName");
        });
    }

    // Test retype parameter
    @Test
    public void testRetypeParameter() {
        // Create the MethodItem
        MethodItem methodItem = new MethodItem("methodName", "type");
        // Add parameter
        MethodItem.addParameter(methodItem, "type", "parameterName");
        // change the type
        MethodItem.retypeParameter(methodItem, "parameterName", "int");
        // assert we have the correct type
        assertEquals("int", methodItem.getParameter("parameterName").getType());
        assertNotEquals("type", methodItem.getParameter("parameterName").getType());
    }

    // Test retype parameter that doesn't exist
    @Test
    public void testRetypeParameterNotExist() {
        // Create the MethodItem
        MethodItem methodItem = new MethodItem("methodName", "type");
        // retype parameter
        String result = MethodItem.retypeParameter(methodItem, "parameterName", "int");
        // make sure we have correct output
        assertEquals("Parameter: parameterName does not exist.", result);
    }
    // Test null input for retype parameter
    @Test
    public void testRetypeNullParameter() {
        // Create the MethodItem
        MethodItem methodItem = new MethodItem("methodName", "type");
        // Ensure that an IllegalArgumentException is thrown when the parameter name is null
        assertThrows(IllegalArgumentException.class, () -> {
            MethodItem.retypeParameter(methodItem, null, "int");
        });
    }

    // Test toString
    @Test
    public void toStringTest() {
        // Create the MethodItem
        MethodItem methodItem = new MethodItem("methodName", "type");
        // Add parameter
        MethodItem.addParameter(methodItem, "type", "parameterName");
        // Expected result
        String expected = "Method: type methodName Parameters: [type parameterName] ";
        // Assert we have the right string
        assertEquals(expected, methodItem.toString());
    }

    // Test setParameters 
    @Test
    public void testSetParameters() {
        // Create the MethodItem
        MethodItem methodItem = new MethodItem("methodName", "type");
        // Create a new HashMap
        HashMap<String, ParameterItem> parameters = new HashMap<>();
        // Add a parameter
        parameters.put("parameterName", new ParameterItem("parameterName", "type"));
        // Set the parameters
        methodItem.setParameters(parameters);
        // Assert we have the right parameters
        assertEquals("parameterName", methodItem.getParameters().get("parameterName").getName());
    }
}
