// Testing imports needed
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.nio.file.Path;

import com.unhandledexceptions.Model.Data;
import com.unhandledexceptions.Model.RelationshipItem;

import javafx.scene.layout.AnchorPane;

import com.unhandledexceptions.Model.ClassItem;

public class DataTests 
{
  @Test
  public void testGuiSpecificSaveLoad(@TempDir Path tempDir) throws Exception
  {
    String result = "";

    //arrange original data
    Data origData = new Data();
    File file = tempDir.resolve("testfile1.json").toFile();
    origData.setCurrentPath(file.getAbsolutePath());

    //add stuff to original data
    ClassItem.addClassItem(origData.getClassItems(), "class1");
    ClassItem.addClassItem(origData.getClassItems(), "class2");
    RelationshipItem.addRelationship(origData.getClassItems(), origData.getRelationshipItems(), "class1", "class2", "composition");

    //save original data
    result = origData.Save(new AnchorPane());

    //test if save returned a positive string
    assertEquals(result, "saved to " + origData.getCurrentPath(), "result should equal saved to " + origData.getCurrentPath());

    //test the file was created
    assertTrue(file.exists(), "File should be created by save method.");

    //load file into new data
    Data newData = new Data();
    newData.setCurrentPath(file.getAbsolutePath());
    result = newData.Load(newData.getCurrentPath());

    //test if load returned a positive string
    assertEquals(result, "successfully loaded " + newData.getCurrentPath(), "result should equal successfully loaded " + newData.getCurrentPath());

    //test the new data is equal to the original data
    assertEquals(origData, newData, "Loaded data should match original data.");
  }


  @Test
  public void testSaveLoad() {
    // create data object
    Data data1 = new Data();
    // create class item
    ClassItem.addClassItem(data1.getClassItems(), "class1");
    // make sure saved properly in data
    assertEquals(data1.getClassItems().size(), 1);
    //save the file
    String res = data1.Save("test123.json");
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
