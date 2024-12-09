// Testing imports needed
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import com.unhandledexceptions.Model.UMLObjectFactory;

public class UMLObjectFactoryTests {
  // test the factory if object type is null
  @Test
  public void nullObjectTest() {
    assertThrows(IllegalArgumentException.class, () -> {
      UMLObjectFactory.createUMLObject(null, "name", "type");
    });
  }

}
