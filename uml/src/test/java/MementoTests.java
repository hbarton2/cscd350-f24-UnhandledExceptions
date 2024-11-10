// testing imports
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import com.unhandledexceptions.Model.ClassItem;
import com.unhandledexceptions.Model.RelationshipItem;
import com.unhandledexceptions.Model.Data;
import com.unhandledexceptions.Model.Data.Memento;

public class MementoTests {

  // Test that a memento was properly created
  @Test
  public void mementoConstructorTest() {
    // create hashmaps for relationships and classes and Data 
    Data data = new Data();
    HashMap<String, ClassItem> classes = new HashMap<>();
    HashMap<String, RelationshipItem> relationships = new HashMap<>();
    
    // create classes and relationships for memento
    ClassItem.addClassItem(classes, "class1");
    ClassItem.addClassItem(classes, "class2");
    RelationshipItem.addRelationship(classes, relationships, "class1", "class2", "compisition");

    // set the maps in data
    data.setClassItems(classes);
    data.setRelationshipItems(relationships);

    // Test the constructor creates a Memento object
    // data.new because Memento is nested in Data.java
    Memento testMemento = data.new Memento(data.getClassItems(), data.getRelationshipItems());

    // Verify the memento stores the correct state
    assertEquals(data.getClassItems(), testMemento.getClassItems());
    assertEquals(data.getRelationshipItems(), testMemento.getRelationshipItems());

    // Verify that the objects are stored the same
    assertTrue(data.getClassItems().get("class1").equals(testMemento.getClassItems().get("class1")));
    assertTrue(data.getClassItems().get("class2").equals(testMemento.getClassItems().get("class2")));
    assertTrue(data.getRelationshipItems().get("class1_class2").equals(testMemento.getRelationshipItems().get("class1_class2")));
  }

  // Test that a memento was properly created in Data.java
  @Test
  public void createMementoTest() {
    Data data = new Data();
    HashMap<String, ClassItem> classes = new HashMap<>();
    HashMap<String, RelationshipItem> relationships = new HashMap<>();
    
    // create classes and relationships for memento
    ClassItem.addClassItem(classes, "class1");
    ClassItem.addClassItem(classes, "class2");
    RelationshipItem.addRelationship(classes, relationships, "class1", "class2", "compisition");

    // set the maps in data
    data.setClassItems(classes);
    data.setRelationshipItems(relationships);

    // Test the constructor creates a Memento object
    // data.new because Memento is nested in Data.java
    Memento testMemento = data.new Memento(data.getClassItems(), data.getRelationshipItems());

    Memento memento = data.createMemento();

    // test the memento created from createMemento() is the same as the one created in the constructor
    assertEquals(testMemento.getClassItems(), memento.getClassItems());
    assertEquals(testMemento.getRelationshipItems(), memento.getRelationshipItems());
  }

}
