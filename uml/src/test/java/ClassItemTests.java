// Testing imports needed
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.util.HashMap;
import com.unhandledexceptions.Model.ClassItem; 



public class ClassItemTests {

  // classMap to store the class objects
  private HashMap<String, ClassItem> classMap;

  // test setup before each class runs
  @BeforeEach
  public void setUp() {
    classMap = new HashMap<String, ClassItem>();
  }

  @Test
  public void testAddClassItem() {
    // add the class testclass
    ClassItem.addClassItem(classMap, "testclass");

    // make sure the key and class object is correct in the classMap
    assertTrue(classMap.containsKey("testclass"));
    assertTrue(classMap.get("testclass").getName().equals("testclass"));
  }

  

}
