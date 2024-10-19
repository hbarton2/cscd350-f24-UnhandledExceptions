import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.util.HashMap;

public class unittests {
  
  // CLASSITEM TESTS
  // tests if renaming class updates relationship key
  @Test
  public void testRenameClassChangesRelationshipKey() {
    // map of class and relationship items
    HashMap<String, ClassItem> classes = new HashMap<>();
    HashMap<String, RelationshipItem> relationships = new HashMap<>();

    // create a class item
    ClassItem.addClassItem(classes, "class1");
    ClassItem.addClassItem(classes, "class2");

    // create a relationship between the two classes
    RelationshipItem relationship = new RelationshipItem(classes.get("class1"), classes.get("class2"));
    relationships.put("class1_class2", relationship);

    // rename class1 to class3
    ClassItem.renameClassItem(classes, "class3", "class1", relationships);

    // check that the key has been updated
    assertTrue(relationships.containsKey("class3_class2"));
  }
  
  // tests if renaming class updates relationship key
  @Test
  public void testRenameClassChangesRelationshipKeyFlipped() {
    // map of class and relationship items
    HashMap<String, ClassItem> classes = new HashMap<>();
    HashMap<String, RelationshipItem> relationships = new HashMap<>();

    // create a class item
    ClassItem.addClassItem(classes, "class1");
    ClassItem.addClassItem(classes, "class2");

    // create a relationship between the two classes
    RelationshipItem relationship = new RelationshipItem(classes.get("class2"), classes.get("class1"));
    relationships.put("class2_class1", relationship);

    // rename class1 to class3
    ClassItem.renameClassItem(classes, "class3", "class1", relationships);

    // check that the key has been updated
    assertTrue(relationships.containsKey("class2_class3"));
  }

  // UI TESTS
  // tests that listing class info gives the right output
  @Test
  public void testListClassInfo() {
    // map of class and relationship items
    HashMap<String, ClassItem> classes = new HashMap<>();
    HashMap<String, RelationshipItem> relationships = new HashMap<>();

    // create a class item
    ClassItem.addClassItem(classes, "class1");
    ClassItem.addClassItem(classes, "class2");

    // add a method to class1
    classes.get("class1").addMethod("method1");

    // create a relationship between the two classes
    RelationshipItem relationship = new RelationshipItem(classes.get("class2"), classes.get("class1"));
    relationships.put("class2_class1", relationship);

    // expected output
    String expected = "\nClass: class1\nFields: not yet\nMethods:\n\tMethod: method1 Parameters: \nRelationships: \n\t" + relationships.get("class2_class1").toString() + "\n";

    // I have to create a new UI using the default constructor because the ListClass method is not static, this is just to test.
    UI ui = new UI();
    assertEquals(expected, ui.ListClass(classes.get("class1"), relationships));
  }

}
