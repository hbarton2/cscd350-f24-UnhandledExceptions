// Testing imports needed
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import com.unhandledexceptions.Model.ParameterItem;

public class ParameterItemTests {
  // Test blank constructor
  @Test
  public void blankConstructorTest() {
    ParameterItem item = new ParameterItem();
    assertTrue(item != null);
  }

  // Test constructor with null input
  @Test
  public void nullConstructorTest() {
    assertThrows(IllegalArgumentException.class, () -> {
            @SuppressWarnings("unused")
            ParameterItem item = new ParameterItem(null, null);
    });
  }

  // Test setName exception
  @Test
  public void setNameExceptionTest() {
    ParameterItem item = new ParameterItem();
    assertThrows(IllegalArgumentException.class, () -> {
            item.setName(null);
    });
  }
}
