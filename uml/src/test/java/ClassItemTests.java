// Testing imports needed
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.beans.PropertyChangeListener;
import java.util.HashMap;
import com.unhandledexceptions.Model.ClassItem; 
import com.unhandledexceptions.Model.RelationshipItem;

import javafx.beans.property.Property;



public class ClassItemTests {

  // classMap to store the class objects
  private HashMap<String, ClassItem> classMap;
  private HashMap<String, RelationshipItem> relationshipMap;
  private PropertyChangeListener mockListener;

  // test setup before each class runs
  @BeforeEach
  public void setUp() {
    classMap = new HashMap<String, ClassItem>();
    relationshipMap = new HashMap<String, RelationshipItem>();
    ClassItem.addClassItem(classMap, "testerclass");
  }

  // tests to see if add class works properly for a unique class
  @Test
  public void testAddClassItem() {
    // add the class testclass
    ClassItem.addClassItem(classMap, "testclass");

    // make sure the key and class object is correct in the classMap
    assertTrue(classMap.containsKey("testclass"));
    assertTrue(classMap.get("testclass").getName().equals("testclass"));
  }

  // tests to see if adding a class duplicate of a class already created shows expected output
  @Test
  public void testAddClassItemDuplicateClass() {
    // add the class testclass
    ClassItem.addClassItem(classMap, "testclass");

    // add the duplicate and see if it gives proper output
    assertEquals("Class name must be unique.", ClassItem.addClassItem(classMap, "testclass"));
  }

  // tests removing class works
  @Test
  public void testRemoveClassItem() {
    // remove the class testerclass
    ClassItem.removeClassItem(classMap, relationshipMap, "testerclass");

    // see if the class is removed from the classMap
    assertFalse(classMap.containsKey("testerclass"));
  }

  // tests removing classes if the class does not exist
  @Test
  public void testRemoveClassItemNoClassExists() {
    // remove a class that does not exist
    assertEquals("No class with name \"blahblahblah\" exists.", ClassItem.removeClassItem(classMap, relationshipMap, "blahblahblah"));
  }

  // tests if removing a class removes the relationships
  @Test
  public void testRemovingClassRemovesRelationships() {
    // add a class and a relationship between the two classes
    ClassItem.addClassItem(classMap, "testclass");
    RelationshipItem.addRelationship(classMap, relationshipMap, "testclass", "testerclass", "composition");

    // remove the class
    ClassItem.removeClassItem(classMap, relationshipMap, "testclass");

    // see if the relationship is removed, there should be no relationships in the map
    assertTrue(relationshipMap.isEmpty());
  }

  // tests renaming a class works
  @Test
  public void testRenameClassItem() {
    // rename the class testerclass
    ClassItem.renameClassItem(classMap, relationshipMap, "testerclass", "newclass");

    // make sure testerclass isn't in classMap anymore
    assertFalse(classMap.containsKey("testerclass"));

    // make sure newClass is in classMap
    assertTrue(classMap.containsKey("newclass"));
  }

  // tests to see if renaming changes the relationship as well
  @Test
  public void testRenameClassItemChangeRelationship() {
    // add a class and a relationship between the two classes
    ClassItem.addClassItem(classMap, "testclass");
    RelationshipItem.addRelationship(classMap, relationshipMap, "testclass", "testerclass", "composition");

    // rename the testclass
    ClassItem.renameClassItem(classMap, relationshipMap, "testclass", "newclass");

    // make sure the relationship is updated
    assertTrue(relationshipMap.containsKey("newclass_testerclass"));
  }

  // tests to see what happens when you rename a class that does not exist
  @Test
  public void testRenameClassItemNoClassExists() {
    // rename a class that does not exist
    assertEquals("blahblahblah does not exist.", ClassItem.renameClassItem(classMap, relationshipMap, "blahblahblah", "newclass"));
  }

  // tests to see what happens when you rename a class to a name that already exists
  @Test
  public void testRenameClassItemDuplicateClass() {
    // add a class to the classMap
    ClassItem.addClassItem(classMap, "testclass");

    // rename the class to a name that already exists
    assertEquals("testerclass is already in use.", ClassItem.renameClassItem(classMap, relationshipMap, "testclass", "testerclass"));
  }
  @Test
  public void testRenameClassItemListNull() {
    assertThrows(IllegalArgumentException.class, () -> {
      ClassItem.renameClassItem(null, relationshipMap, "testclass", "testerclass");
    });
  }
  @Test
  public void testRenameClassItemNewNameIsBlank() {
    assertThrows(IllegalArgumentException.class, () -> {
      ClassItem.renameClassItem(classMap, relationshipMap, "testclass", "");
    });
  }
  //Test is expecting a NullPointerException... not an IllegalArgumentException
  @Test
  public void testRenameClassItemNewNameIsNull() {
    assertThrows(NullPointerException.class, () -> {
      ClassItem.renameClassItem(classMap, relationshipMap, "testclass", null);
    });
  }
  @Test
  public void testRenameClassItemOldNameIsBlank() {
    assertThrows(IllegalArgumentException.class, () -> {
      ClassItem.renameClassItem(classMap, relationshipMap, "", "testclass1");
    });
  }
  @Test
  public void testRenameClassItemOldNameIsNull() {
    assertThrows(NullPointerException.class, () -> {
      ClassItem.renameClassItem(classMap, relationshipMap, null, "testclass1");
    });
  }


  @Test
  public void testAddMethod() {
    // add a method to the class
    classMap.get("testerclass").addMethod(classMap.get("testerclass"), "testmethod", "testType");

    // make sure the method is in the class
    assertTrue(classMap.get("testerclass").getMethodItems().containsKey("testmethod"));
  }

  @Test
  public void testAddMethodNullName() {
    // add a method with a null name
    IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> {
      classMap.get("testerclass").addMethod(classMap.get("testerclass"), null, "testType");
    });

    // make sure the exception is thrown
    assertEquals("Method name cannot be null or blank", e.getMessage());
  }

  @Test
  public void testAddMethodAlreadyExists() {
    // add a method to the class
    classMap.get("testerclass").addMethod(classMap.get("testerclass"), "testmethod", "testType");

    // add the same method again
    assertEquals("Method name: testmethod already in use.", classMap.get("testerclass").addMethod(classMap.get("testerclass"), "testmethod", "testType"));
  }

  @Test
  public void testRemoveMethod() {
    // add a method to the class
    classMap.get("testerclass").addMethod(classMap.get("testerclass"), "testmethod", "testType");

    // remove the method
    classMap.get("testerclass").removeMethod(classMap.get("testerclass"), "testmethod");

    // make sure the method is removed
    assertFalse(classMap.get("testerclass").getMethodItems().containsKey("testmethod"));
  }
  
  @Test
  public void testRemoveMethodNullName () {
    // remove a method with a null name
    IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> {
      classMap.get("testerclass").removeMethod(classMap.get("testerclass"), null);
    });

    // make sure the exception is thrown
    assertEquals("Method name cannot be null or blank", e.getMessage());
  }

  @Test
  public void testRemoveMethodDoesNotExist() {
    // remove a method that does not exist
    assertEquals("Method name: testmethod does not exist", classMap.get("testerclass").removeMethod(classMap.get("testerclass"), "testmethod"));
  }

  @Test
  public void testRenameMethod() {
    // add a method to the class
    classMap.get("testerclass").addMethod(classMap.get("testerclass"), "testmethod", "testType");

    // rename the method
    classMap.get("testerclass").renameMethod(classMap.get("testerclass"), "testmethod", "newmethod");

    // make sure the method is renamed
    assertTrue(classMap.get("testerclass").getMethodItems().containsKey("newmethod"));
    
  }

  @Test
  public void testRenameMethodNullName() {
    // rename a method with a null name
    assertEquals("Method names cannot be null or blank.", classMap.get(classMap.get("testerclass")).renameMethod(classMap.get("testerclass"), null, "newmethod"));
  }

  @Test
  public void testRenameMethodAlreadyExists() {
    // add a method to the class
    classMap.get("testerclass").addMethod(classMap.get("testerclass"), "testmethod", "testType");

    // attempt rename with existing method name
    assertEquals("Method name: testmethod already in use.", classMap.get("testerclass").renameMethod(classMap.get("testerclass"), "testmethod", "testmethod"));
  }
  @Test
  public void testRenameField() {
    // add a field to the class
    classMap.get("testerclass").addField(classMap.get("testerclass"), "testtype", "testfield");

    // rename the field
    classMap.get("testerclass").renameField(classMap.get("testerclass"), "testfield", "newfield");

    // make sure the field is renamed
    assertTrue(classMap.get("testerclass").getFieldItems().containsKey("newfield"));
  }

  @Test
  public void testRenameFieldNullName() {
    // rename a field with a null name
    classMap.get("testerclass").addField(classMap.get("testerclass"), "testtype", "testfield");

    assertEquals("Field names cannot be null or blank", classMap.get(classMap.get("testerclass")).renameField(classMap.get("testerclass"), null, "newfield"));
  }

  @Test
  public void testRetypeField() {
    // add a field to the class
    classMap.get("testerclass").addField(classMap.get("testerclass"), "testtype", "testfield");

    // retype the field
    classMap.get("testerclass").retypeField(classMap.get("testerclass"), "testfield", "newtype");

    // make sure the field is retyped
    assertEquals("newtype", classMap.get("testerclass").getFieldItems().get("testfield").getType());
  }

  @Test
  public void testRetypeMethod() {
    // add a method to the class
    classMap.get("testerclass").addMethod(classMap.get("testerclass"), "testmethod", "testType");

    // retype the method
    classMap.get("testerclass").retypeMethod(classMap.get("testerclass"), "testmethod", "newType");

    // make sure the method is retyped
    assertEquals("newType", classMap.get("testerclass").getMethodItems().get("testmethod").getType());
  }

  @Test
  public void testRemoveField() {
    // add a field to the class
    classMap.get("testerclass").addField(classMap.get("testerclass"), "testtype", "testfield");

    // remove the field
    classMap.get("testerclass").removeField(classMap.get("testerclass"), "testfield");

    // make sure the field is removed
    assertFalse(classMap.get("testerclass").getFieldItems().containsKey("testfield"));
  }

  @Test
  public void testRemoveFieldNullName() {
    // make sure the exception is thrown
    assertEquals("Field name cannot be null or blank", classMap.get("testerclass").removeField(classMap.get("testerclass"), null));
  }
}
