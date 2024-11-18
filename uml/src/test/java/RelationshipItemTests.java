// Testing imports needed
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.util.HashMap;
import com.unhandledexceptions.Model.ClassItem; 
import com.unhandledexceptions.Model.RelationshipItem;

public class RelationshipItemTests {
  // classMap to store the class objects
  private HashMap<String, ClassItem> classMap;
  private HashMap<String, RelationshipItem> relationshipMap;

  // test setup before each test runs
  @BeforeEach
  public void setUp() {
    classMap = new HashMap<String, ClassItem>();
    relationshipMap = new HashMap<String, RelationshipItem>();
    ClassItem.addClassItem(classMap, "class1");
    ClassItem.addClassItem(classMap, "class2");
    //relationshipMap.put("class1_class2", new RelationshipItem(classMap.get("class1"), classMap.get("class2"), "composition"));
  }

  // test that a relationship is created successfully
  @Test
  public void testAddRelationship() {
    // proper return stating the relationship was created succesfully 
    assertEquals("good", RelationshipItem.addRelationship(classMap, relationshipMap, "class1", "class2", "composition"));
    // ensures that the map has the correct key
    assertTrue(relationshipMap.containsKey("class1_class2"));
    // ensures that the map has the correct relationship object
    assertEquals("class1 ---- Composition ----> class2", relationshipMap.get("class1_class2").toString());
  }

  // test that a relationship is not created if the source class does not exist
  @Test
  public void testAddRelationshipSourceDoesNotExist() {
    // proper return stating that one or both of the classes do not exist
    assertEquals("One or both of the classes do not exist", RelationshipItem.addRelationship(classMap, relationshipMap, "class3", "class2", "composition"));
    // ensures that the map does not have the key
    assertFalse(relationshipMap.containsKey("class3_class2"));
  }

  // test that a relationship is not created if the destination class does not exist
  @Test
  public void testAddRelationshipDestinationDoesNotExist() {
    // proper return stating that one or both of the classes do not exist
    assertEquals("One or both of the classes do not exist", RelationshipItem.addRelationship(classMap, relationshipMap, "class1", "class3", "composition"));
    // ensures that the map does not have the key
    assertFalse(relationshipMap.containsKey("class1_class3"));
  }

  // test that a relationship is not created if the relationship already exists
  @Test
  public void testAddRelationshipAlreadyExists() {
    // create relationship
    RelationshipItem.addRelationship(classMap, relationshipMap, "class1", "class2", "composition");
    // see if we can create the same relationship twice
    assertEquals("good", RelationshipItem.addRelationship(classMap, relationshipMap, "class1", "class2", "composition"));
  }

  // test that a class can have multiple relationships
  @Test
  public void testClassMultipleRelationships() {
    // make relationship with class1 and class2
    RelationshipItem.addRelationship(classMap, relationshipMap, "class1", "class2", "realization");
    // create class3
    ClassItem.addClassItem(classMap, "class3");
    // make relationship with class1 and class3
    assertEquals("good", RelationshipItem.addRelationship(classMap, relationshipMap, "class1", "class3", "aggregation"));
    assertTrue(relationshipMap.containsKey("class1_class3"));
  }

  // test that a relationship is removed successfully
  @Test
  public void testRemoveRelationship() {
    // create relationship
    RelationshipItem.addRelationship(classMap, relationshipMap, "class1", "class2", "composition");
    // remove relationship
    assertEquals("good", RelationshipItem.removeRelationship(relationshipMap, "class1", "class2"));
    // ensure that the map does not have the key
    assertFalse(relationshipMap.containsKey("class1_class2"));
  }

  // test that a relationship is not removed if it does not exist
  @Test
  public void testRemoveRelationshipNotFound() {
    // remove relationship that does not exist
    assertEquals("Relationship not found.", RelationshipItem.removeRelationship(relationshipMap, "class1", "class2"));
  }

  // test that the relationship doesn't get removed if source and destination are mixed
  @Test
  public void testRemoveWithScrambledClasses() {
    // create relationship
    RelationshipItem.addRelationship(classMap, relationshipMap, "class1", "class2", "composition");
    // remove relationship with source and destination mixed
    assertEquals("Relationship not found.", RelationshipItem.removeRelationship(relationshipMap, "class2", "class1"));
    // ensure that the map still has the key
    assertTrue(relationshipMap.containsKey("class1_class2"));
  }

}
