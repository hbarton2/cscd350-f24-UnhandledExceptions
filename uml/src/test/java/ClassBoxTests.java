// Testing imports
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.testfx.framework.junit5.Start;
import org.testfx.api.FxToolkit;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

//Project Imports
import com.unhandledexceptions.Controller.BaseController;
import com.unhandledexceptions.Model.ClassItem;
import com.unhandledexceptions.Model.Data;
import com.unhandledexceptions.View.ClassBox;
import com.unhandledexceptions.View.ClassBoxBasicBuilder;

import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;



//Project Imports


public class ClassBoxTests {

    private ClassBoxBasicBuilder classBoxBuilder;
    private AnchorPane anchorPane;
    private BaseController baseController;
    private String className = "testName";
    private double boxWidth = 200;
    private double boxHeight = 300;
    private ClassItem classItem = new ClassItem();

    @BeforeEach
    public void setUp() throws Exception {
        FxToolkit.registerPrimaryStage();
        anchorPane = new AnchorPane();
        baseController = new BaseController(new Data());
        baseController.AddClassListener(className);
        classItem = baseController.getData().getClassItems().get(className.toLowerCase().trim());
        classBoxBuilder = new ClassBoxBasicBuilder(anchorPane, baseController, className, boxWidth, boxHeight, classItem);
    }

    @Start
    public void start(Stage stage) {
        stage.setScene(new Scene(anchorPane, 600, 400));
        stage.show();
    }

    //ClassBoxBasicBuilder tests only.
    //test that we can create a classbox with no field or method pane
    @Test
    public void testEmptyClassBox() {
            ClassBox classBox = classBoxBuilder.build();
            //contentBox is the box inside of the classBox object that holds a list of panes we are adding things to display
            VBox contentBox = (VBox) classBox.lookup("#contentBox");
            assertEquals(1,contentBox.getChildren().size());
        }

    //test that we can create a classbox with just field pane
    @Test
    public void testClassBoxWithFieldPane() {
        classBoxBuilder.withFieldPane();
        ClassBox classBox = classBoxBuilder.build();
        
        VBox contentBox = (VBox) classBox.lookup("#contentBox");
        assertEquals(2, contentBox.getChildren().size()); // 1 for class name label + 1 for field pane

        //TODO: test that the panes that are inside of the contentBox are the correct panes.
    }

    //test that we can create a classbox with just method pane
    @Test
    public void testClassBoxWithMethodPane() {
        classBoxBuilder.withMethodPane();
        ClassBox classBox = classBoxBuilder.build();
        
        VBox contentBox = (VBox) classBox.lookup("#contentBox");
        assertEquals(2, contentBox.getChildren().size()); // 1 for class name label + 1 for method pane
    }

    //test that we can create a classbox with both field and method pane
    @Test
    public void testClassBoxWithBothFieldAndMethodPanes() {
        classBoxBuilder.withFieldPane();
        classBoxBuilder.withMethodPane();
        ClassBox classBox = classBoxBuilder.build();

        VBox contentBox = (VBox) classBox.lookup("#contentBox");
        assertEquals(3, contentBox.getChildren().size()); // 1 for class name label + 1 for field pane + 1 for method pane
    }

    //test that we can delete a field from a classbox
    // @Test
    // public void testClassBoxDeleteField() {
    //     classBoxBuilder.withFieldPane();
    //     ClassBox classBox = classBoxBuilder.build();
    //     //add field to classBox
    //    // baseController.AddFieldListener( className, "fieldType", "fieldName");
        
    //     //make sure that field was added to the class
    //     ClassItem classItem = baseController.getData().getClassItems().get(classBox.getClassName()); //getting error that classItem is null?
    //     assertTrue(classItem.getFieldItems().containsKey("fieldName"));

    //     //delete the field that was added via the gui 

    //     //assert that the field is no longer contained in the fieldItem hashMap
    // }

}