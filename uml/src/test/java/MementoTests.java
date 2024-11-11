// testing imports
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.unhandledexceptions.Controller.Caretaker;
import com.unhandledexceptions.Model.ClassItem;
import com.unhandledexceptions.Model.RelationshipItem;
import com.unhandledexceptions.Model.Data;
import com.unhandledexceptions.Model.Data.Memento;

public class MementoTests {

  private Data data;

  // Setup the data object with some class items and a relationship item
  @BeforeEach
  public void setup() {
    this.data = new Data();
    ClassItem.addClassItem(data.getClassItems(), "class1");
    ClassItem.addClassItem(data.getClassItems(), "class2");
    RelationshipItem.addRelationship(data.getClassItems(), data.getRelationshipItems(), "class1", "class2", "compisition");
  }

  // Test that a memento was properly created
  @Test
  public void mementoConstructorTest() {
    // Test the constructor creates a Memento object
    // data.new because Memento is nested in Data.java
    Memento testMemento = data.new Memento(data.getClassItems(), data.getRelationshipItems());

    // Verify the memento stores the correct state
    assertEquals(data.getClassItems().keySet(), testMemento.getClassItems().keySet());
    assertEquals(data.getRelationshipItems().keySet(), testMemento.getRelationshipItems().keySet());

    // Verify that the objects are stored the same
    assertTrue(data.getClassItems().get("class1").getName().equals(testMemento.getClassItems().get("class1").getName()));
    assertTrue(data.getClassItems().get("class2").getName().equals(testMemento.getClassItems().get("class2").getName()));
    assertTrue(data.getRelationshipItems().get("class1_class2").getSource().equals(testMemento.getRelationshipItems().get("class1_class2").getSource()));
  }

  // Test that a memento was properly created in Data.java
  @Test
  public void createMementoTest() {
    // create memento object to test against
    Memento testMemento = data.new Memento(data.getClassItems(), data.getRelationshipItems());

    // create a memento object using createMemento()
    Memento memento = data.createMemento();

    // test the memento created from createMemento() is the same as the one created in the constructor
    assertEquals(testMemento.getClassItems().keySet(), memento.getClassItems().keySet());
    assertEquals(testMemento.getRelationshipItems().keySet(), memento.getRelationshipItems().keySet());
  }

  // Test that a memento was properly restored
  @Test
  public void restoreFromMementoTest() {
    // create memento object to test with
    Memento testMemento = data.new Memento(data.getClassItems(), data.getRelationshipItems());

    // create a new class to change the state of data
    ClassItem.addClassItem(data.getClassItems(), "class3");
    
    // Make sure data contains class3 now.
    assertTrue(data.getClassItems().containsKey("class3"));

    // restore the state of data from the memento
    data.restoreFromMemento(testMemento);
    
    // At this point class3 should NOT exist in data
    assertFalse(data.getClassItems().containsKey("class3"));
  }

  // test saveState() in caretaker
  @Test
  public void saveStateTest() {
    // create a caretaker object
    Caretaker caretaker = new Caretaker(data);

    // Test that the caretaker puts a memento in its undo stack
    caretaker.saveState();

    // Test that the undo stack has a memento in it
    assertFalse(caretaker.getUndoStack().isEmpty());
    // peek() returns the top of the stack without removing it
    assertTrue(caretaker.getUndoStack().peek() instanceof Memento);
  }

  // test undo()
  @Test
  public void undoTest() {
    // create a caretaker object
    Caretaker caretaker = new Caretaker(data);

    // caretaker saves the state
    caretaker.saveState();

    // create a new class to change the state of data
    ClassItem.addClassItem(data.getClassItems(), "class3");

    // make sure the class got added in data for the new state
    assertTrue(data.getClassItems().containsKey("class3"));

    // need to save the state because we added something
    caretaker.saveState();

    // undo the change
    caretaker.undo();

    // At this point class3 should NOT exist in data
    assertFalse(data.getClassItems().containsKey("class3"));
    // check the redo stack in caretaker to make sure we have a memento in it
    assertFalse(caretaker.getRedoStack().isEmpty());
    assertTrue(caretaker.getRedoStack().peek() instanceof Memento);
  }

  // test if there is nothing in the undo stack when we undo.
  @Test
  public void undoNothingTest() {
    // create a caretaker object
    Caretaker caretaker = new Caretaker(data);

    // undo the change but there isn't anything to undo
    caretaker.undo();

    // check the redo stack in caretaker to make sure we have nothing in it
    assertTrue(caretaker.getRedoStack().isEmpty());

    // make sure the state has NOT changed
    assertTrue(data.getClassItems().containsKey("class1"));
    assertTrue(data.getClassItems().containsKey("class2"));
    assertTrue(data.getRelationshipItems().containsKey("class1_class2"));
  }

  // test redo()
  @Test
  public void redoTest() {
    // create a caretaker object
    Caretaker caretaker = new Caretaker(data);

    // caretaker saves the state
    caretaker.saveState();

    // create a new class to change the state of data
    ClassItem.addClassItem(data.getClassItems(), "class3");

    // make sure the class got added in data for the new state
    assertTrue(data.getClassItems().containsKey("class3"));

    // need to save the state because we added something
    caretaker.saveState();

    // undo the change
    caretaker.undo();

    // At this point class3 should NOT exist in data
    assertFalse(data.getClassItems().containsKey("class3"));
    // check the redo stack in caretaker to make sure we have a memento in it
    assertFalse(caretaker.getRedoStack().isEmpty());
    assertTrue(caretaker.getRedoStack().peek() instanceof Memento);

    // redo the change
    caretaker.redo();

    // At this point class3 should exist in data
    assertTrue(data.getClassItems().containsKey("class3"));
  }
  // test if there is nothing in the redo stack when we redo.
  @Test
  public void redoNothingTest() {
    // create a caretaker object
    Caretaker caretaker = new Caretaker(data);

    // redo the change but there isn't anything to redo
    caretaker.redo();

    // make sure the state has NOT changed
    assertTrue(data.getClassItems().containsKey("class1"));
    assertTrue(data.getClassItems().containsKey("class2"));
    assertTrue(data.getRelationshipItems().containsKey("class1_class2"));
  }

  //test that removeLast() method only removes the last object pushed to the history.
  @Test
  public void removeLastTest(){
    Caretaker careTaker = new Caretaker(data);
    
    //current stack is empty
    assertTrue(careTaker.getUndoStack().isEmpty());

    //pushes memento onto the stack
    careTaker.saveState(); 
    Memento mementoState1 = careTaker.getUndoStack().peek();
    assertTrue(!careTaker.getUndoStack().isEmpty());  //assert that the state was saved in the stack

    //pushes memento onto the stack
    careTaker.saveState(); 
    Memento mementoState2 = careTaker.getUndoStack().peek(); //saves the object that is pushed onto the stack

    // assert that the removeLast only removes the latest state pushed to it.
    assertEquals(mementoState2, careTaker.removeLast()); 
    // assert that the stack now has the original state on top.
    assertEquals(mementoState1, careTaker.getUndoStack().peek()); 
    
  }

  // Testing the state of adding a field to a class, then undoing it
  @Test
  public void undoFieldTest() {
    Caretaker caretaker = new Caretaker(data);

    // save the state
    caretaker.saveState();

    // add a field to class1
    ClassItem.addField(data.getClassItems().get("class1"), "String", "name");
    // make sure a field was added
    assertTrue(data.getClassItems().get("class1").getFieldItems().containsKey("name"));

    // save the state
    caretaker.saveState();

    // undo the change
    caretaker.undo();

    // make sure the field was removed
    assertFalse(data.getClassItems().get("class1").getFieldItems().containsKey("name"));
  }
  
  

}
