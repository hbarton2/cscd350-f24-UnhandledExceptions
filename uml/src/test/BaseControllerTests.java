// Testing imports
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

// Project imports
import com.unhandledexceptions.Controller.BaseController;
import com.unhandledexceptions.Model.ClassItem;
import com.unhandledexceptions.Model.Data;

public class BaseControllerTests {
  // Runs before every test
  @beforeEach
  public void setUp() {
    Data data = new Data();
    BaseController baseController = new BaseController(data);
  }

  // Test proper construction of BaseController
  @Test
  public void testBaseController() {
    assertNotNull(baseController);
  }

}
