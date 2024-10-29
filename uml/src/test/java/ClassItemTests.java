// Testing imports needed
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.util.HashMap;
import com.unhandledexceptions.Model.ClassItem; 
import com.unhandledexceptions.Model.RelationshipItem;



public class ClassItemTests {

  // classMap to store the class objects
  private HashMap<String, ClassItem> classMap;
  private HashMap<String, RelationshipItem> relationshipMap;

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

  @Test
  public void testRemoveClassItem() {
    // remove the class testerclass
    ClassItem.removeClassItem(classMap, relationshipMap, "testerclass");

    // see if the class is removed from the classMap
    assertFalse(classMap.containsKey("testerclass"));
  }

  @Test
  public void testRemoveClassItemNoClassExists() {
    // remove a class that does not exist
    assertEquals("No class with name \"blahblahblah\" exists.", ClassItem.removeClassItem(classMap, relationshipMap, "blahblahblah"));
  }

  

   

}
