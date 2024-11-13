import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.concurrent.TimeoutException;

import javafx.scene.control.Label;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.api.FxToolkit;

import com.unhandledexceptions.Controller.BaseController;
import com.unhandledexceptions.Model.ClassItem;
import com.unhandledexceptions.Model.Data;
import com.unhandledexceptions.View.ClassBox;
import com.unhandledexceptions.View.ClassBoxBasicBuilder;

import javafx.scene.layout.AnchorPane;

public class ObserverTests {
    Data data;
    BaseController baseController;
    ClassBoxBasicBuilder classBoxBuilder;
    AnchorPane anchorPane;
    ClassItem classItem;
    ClassBox classBox;

    @BeforeEach
    public void setUp() throws TimeoutException {
        FxToolkit.registerPrimaryStage();
        data = new Data();
        baseController = new BaseController(data);
        baseController.AddClassListener("testClass");
        anchorPane = new AnchorPane();
        classItem = data.getClassItems().get("testclass");
        classBoxBuilder = new ClassBoxBasicBuilder(anchorPane, baseController, "testClass", 200, 300, classItem);
        classBoxBuilder.withFieldPane();
        classBoxBuilder.withMethodPane();
        classBox = classBoxBuilder.build();
    }

    @Test
    public void testListenerExists() {  
        PropertyChangeSupport pcs = classItem.getSupport(); // get the property change support object from the classItem
        assertTrue(pcs.hasListeners(null)); // null means all properties (in this case, only one)
    }

    @Test
    public void testListenerFires() { // test that the listener fires when the class name is changed
        baseController.RenameClassListener("testclass", "newName");
        assertEquals("newName", classBox.getClassName());
    }

    // testing all components of the class box
    @Test
    public void testClassBoxComponents() {
        baseController.AddFieldListener("testclass", "int", "num");
        baseController.AddMethodListener("testclass", "testMethod", "void");
        assertEquals(1, classBox.getFieldsList().getItems().size());
        assertEquals(1, classBox.getMethodsList().getItems().size());
    }

    // @Test
    // public void testListenerFires() {
    //     baseController.RenameClassListener("testClass", "newName");
    //     Label label = (Label) classBox.lookup("#className");
    //     assertEquals("newName", label.getText());
    // }
}
