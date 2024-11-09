// Testing imports
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

//Project Imports
import com.unhandledexceptions.Controller.BaseController;
import com.unhandledexceptions.View.ClassBox;
import com.unhandledexceptions.View.ClassBoxBasicBuilder;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.Node;

import org.junit.jupiter.api.BeforeEach;

//Project Imports


public class ClassBoxTests {

private ClassBoxBasicBuilder classBoxBuilder;
private AnchorPane anchorPane;
private BaseController baseController;
private String className = "testName";
private double boxWidth = 200;
private double boxHeight = 300;

@BeforeEach
public void setUp() {
    this.classBoxBuilder = new ClassBoxBasicBuilder(anchorPane, baseController, className, boxWidth, boxHeight);
}

//ClassBoxBasicBuilder tests only.
//test that we can create a classbox with no field or method pane
@Test
public void testEmptyClassBox() {
    ClassBox classBox = classBoxBuilder.build();
    VBox contentBox = (VBox) classBox.lookup("#contentBox");
    assertEquals(1,contentBox.getChildren().size());
}



//test that we can create a classbox with just field pane

//test that we can create a classbox with just method pane

//test that we can create a classbox with both field and method pane
}