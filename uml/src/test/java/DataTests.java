// Testing imports needed
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.io.File;

import com.unhandledexceptions.Model.Data;
import com.unhandledexceptions.Model.ClassItem;

public class DataTests {

  @Test
  public void testSaveLoad() {
    // create data object
    Data data = new Data();
    // create class item
    ClassItem.addClassItem(data.getClassItems(), "class1");
    // make sure saved properly in data
    assertEquals(data.getClassItems().size(), 1);
    //save the file
    String res = data.Save("test123.json");
    // make sure file got saved
    assertEquals("saved to test123.json", res);
    // create new data object
    Data data2 = new Data();
    // load data from file
    res = data2.Load("test123.json");
    // make sure loaded successfully 
    assertEquals("successfully loaded test123.json", res);
    // make sure class item loaded properly
    assertEquals(data2.getClassItems().size(), 1);
    // delete the test file
    new File("test123.json").delete();
  }
}
