// testing imports
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

// modules being tested for singleton
import com.unhandledexceptions.View.CLI;
import com.unhandledexceptions.View.GUI;
import com.unhandledexceptions.Model.Data;

public class SingletonTests {
  // Test that only one instance is created
  @Test
  public void testCLISingleton() {
    // Get the first instance of CLI
    CLI instance1 = CLI.getInstance(new Data());
    // Get the second instance of CLI
    CLI instance2 = CLI.getInstance(new Data());

    // Assert that both instances are the same
    assertSame(instance1, instance2, "CLI instances are not the same");
  }

  // Test that the instance is not null
  @Test
  public void testCLINotNull() {
    // Get the instance of CLI
    CLI instance = CLI.getInstance(new Data());

    // Assert that the instance is not null
    assertNotNull(instance, "CLI instance is null");
  }

}
