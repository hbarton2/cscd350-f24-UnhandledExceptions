// Testing imports
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

// Project imports
import com.unhandledexceptions.Controller.BaseController;
import com.unhandledexceptions.Model.Data;

public class BaseControllerTests {
  // private fields for setUp()
  private Data data;
  private BaseController baseController;
  // Runs before every test
  @BeforeEach
  public void setUp() {
    data = new Data();
    baseController = new BaseController(data);
  }

  // Test proper construction of BaseController
  @Test
  public void testBaseController() {
    // makes sure that the BaseController is not null, meaning it was properly constructed
    assertNotNull(baseController);
  }

  // Test adding a class with the controller
  @Test 
  public void testAddClassListener() {
    // Test adding a class
    String result = baseController.AddClassListener("TestClass");
    assertEquals("good", result);
    System.out.println(data.getClassItems().keySet());
    // ensure that the class was added to the data object
    assertTrue(data.getClassItems().containsKey("testclass"));
    // ensure the class name wasn't changed
    assertEquals("TestClass", data.getClassItems().get("testclass").getName());
  }

  // Test renaming a class with the controller
  @Test
  public void testRemoveClassListener() {
    // Test removing a class
    baseController.AddClassListener("testclass");
    String result = baseController.RemoveClassListener("testclass");
    assertEquals("good", result);
    // ensure that the class was removed from the data object
    assertFalse(data.getClassItems().containsKey("testclass"));
  }

  // Test renaming a class with the controller
  // TODO: We need class names to be case sensitive, once they are, change this test to reflect that
  @Test
  public void testRenameClassListener() {
    // Test renaming a class
    baseController.AddClassListener("testclass");
    String result = baseController.RenameClassListener("testclass", "newTestClass");
    assertEquals("good", result);
    // ensure that the class was renamed in the data object
    assertTrue(data.getClassItems().containsKey("newtestclass"));
    assertFalse(data.getClassItems().containsKey("testclass"));
  }

  // Test adding a relationship with the controller
  @Test
  public void testAddRelationshipListener() {
    // Test adding a relationship
    baseController.AddClassListener("testclass1");
    baseController.AddClassListener("testclass2");
    String result = baseController.AddRelationshipListener("testclass1", "testclass2", "aggregation");
    assertEquals("good", result);
    // ensure that the relationship was added to the data object
    assertTrue(data.getRelationshipItems().containsKey("testclass1_testclass2"));
    // ensure the relationship type is correct
    assertEquals("aggregation", data.getRelationshipItems().get("testclass1_testclass2").getType());
  }

  // Test removing a relationship
  @Test
  public void testRemoveRelationshipListener() {
    // Test removing a relationship
    baseController.AddClassListener("testclass1");
    baseController.AddClassListener("testclass2");
    baseController.AddRelationshipListener("testclass1", "testclass2", "aggregation");
    String result = baseController.RemoveRelationshipListener("testclass1", "testclass2");
    assertEquals("good", result);
    // ensure that the relationship was removed from the data object
    assertFalse(data.getRelationshipItems().containsKey("testclass1_testclass2"));
  }

  // Test type of relationship
  @Test
  public void testAddRelationshipListenerInvalidType() {
    // Test adding a relationship with an invalid type
    baseController.AddClassListener("testclass1");
    baseController.AddClassListener("testclass2");
    String result = baseController.AddRelationshipListener("testclass1", "testclass2", "someType");
    assertEquals("Invalid relationship type. Valid types: aggregation, composition, generalization, realization", result);
    // ensure that the relationship was not added to the data object
    assertFalse(data.getRelationshipItems().containsKey("testclass1_testclass2"));
  }

  /* 
   * We don't need to test placing a relationship with an invalid type because the dropdown when creating a relationship
   * only allows for valid types, so it is impossible to place a relationship with an invalid type.
   */

  // Test adding a field
  @Test
  public void testAddFieldListener() {
    // Test adding a field
    baseController.AddClassListener("testclass");
    String result = baseController.AddFieldListener("testclass", "int", "testField");
    assertEquals("good", result);
    // ensure that the field was added to the class in the data object and the name wasn't changed
    assertTrue(data.getClassItems().get("testclass").getFieldItems().get("testfield").getName().equals("testfield"));
    // ensure the field type is correct
    assertEquals("int", data.getClassItems().get("testclass").getFieldItems().get("testfield").getType());
  }

  

}
