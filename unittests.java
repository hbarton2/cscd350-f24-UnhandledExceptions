import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.Map;

public class unittests {
  
  @Test
  public void testRenameClassChangesRelationshipKey() {
    // map of class and relationship items
    Map<String, ClassItem> classes = new HashMap<>();
    Map<String, RelationshipItem> relationships = new HashMap<>();

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

}
