// Testing imports needed
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import com.unhandledexceptions.Model.FieldItem;

public class FieldItemTests {
  // Test blank constructor
  @Test 
  public void serialConstructorTest() {
    FieldItem fieldItem = new FieldItem();
    assertTrue(fieldItem != null);
  }

  // Test setName
  @Test
  public void setNameTest() {
    FieldItem fieldItem = new FieldItem();
    fieldItem.setName("name");
    assertTrue(fieldItem.getName().equals("name"));
  }

  // Test setType
  @Test
  public void setTypeTest() {
    FieldItem fieldItem = new FieldItem();
    fieldItem.setType("type");
    assertTrue(fieldItem.getType().equals("type"));
  }

  // Test toString
  @Test
  public void toStringTest() {
    FieldItem fieldItem = new FieldItem("name", "int");
    assertEquals("int name", fieldItem.toString());
  }
}
