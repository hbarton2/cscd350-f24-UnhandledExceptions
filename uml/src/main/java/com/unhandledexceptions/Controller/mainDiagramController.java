package com.unhandledexceptions.Controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.unhandledexceptions.Model.ClassItem;
import com.unhandledexceptions.Model.Data;
import com.unhandledexceptions.Model.RelationshipItem;
import com.unhandledexceptions.View.ClassBox;
import com.unhandledexceptions.View.ClassBoxBasicBuilder;
import com.unhandledexceptions.View.RelationLine;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.Node;
import javafx.scene.transform.Scale;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;

public class mainDiagramController
{
    //private mainDiagramController controller;
    
    private Data data;
    private BaseController baseController;
    private final double boxWidth = 200; // Width of the boxes
    private final double boxHeight = 300; // Height of the content box
    private RelationLine placingRelation;
    private double offsetX; // Used for dragging Class boxes
    private double offsetY;

    public mainDiagramController() {
        //controller = this;
        data = new Data();
        baseController = new BaseController(data);
    }

    @FXML private StackPane bgpane;
    @FXML private AnchorPane anchorPane;
    @FXML private ScrollPane scrollPane;
    @FXML private Menu addClassMenu;
    @FXML private Menu openRecentMenu;

    // Zoom in/out variables
    @FXML private final Scale scaleTransform = new Scale(1.0, 1.0);
    private double zoomFactor = 1.05;
    private final double minZoom = 0.4;
    private final double maxZoom = 1.5;

    @FXML private void initialize()
    {
        //controller = this;
        data = new Data();
        baseController = new BaseController(data);
        anchorPane.getTransforms().add(scaleTransform);
        scrollPane.addEventFilter(ScrollEvent.SCROLL, this::handleZoom);
        anchorPane.setOnMouseMoved(event -> mouseMove(event));
        anchorPane.setOnScroll(event -> mouseScroll(event));
        anchorPane.setOnMouseClicked(event -> mouseClick(event));
    }

    public void newMenuClick()
    {
        data.Clear();

        ClearAll();
    }

    public void openRecentMenuShowing()
    {
        //clear items
        openRecentMenu.getItems().clear();

        // Get the current working directory
        File dir = new File(System.getProperty("user.dir"));

        // Filter for .json files
        File[] jsonFiles = dir.listFiles((directory, name) -> name.endsWith(".json"));
        if (jsonFiles != null) {
            Arrays.stream(jsonFiles).forEach(file -> {
                // Create a menu item for each .json file
                MenuItem fileItem = new MenuItem(file.getName());
                
                // Set an action for each menu item (e.g., to open the file)
                fileItem.setOnAction(event -> {
                    newMenuClick();
                    data.Load(file.getAbsolutePath());
                    LoadAll();
                });
                
                // Add the menu item to the "Open Recent" submenu
                openRecentMenu.getItems().add(fileItem);
            });
        }
    }

    public void saveMenuClick()
    {
        data.Save(anchorPane);
    }

    public void saveAsMenuClick()
    {
        data.SaveAs(anchorPane);
    }

    public void openMenuClick()
    {
        String result = data.Load(anchorPane);

        if (result.equals("good"))
        {
            //clear all
            newMenuClick();

            LoadAll();   
        }
    }

    private void ClearAll()
    {
        ArrayList<Node> children = new ArrayList<>();
        
        for (Node child : anchorPane.getChildren())
                children.add(child);
        
        anchorPane.getChildren().removeAll(children);
    }

    private void LoadAll()
    {
        //load classes
        HashMap<String, ClassItem> classItems = data.getClassItems();
        for (Map.Entry<String, ClassItem> entry : classItems.entrySet()) 
        {
            ClassBox classBox = addClass(entry.getKey());
            classBox.setLayoutX(entry.getValue().getX());
            classBox.setLayoutY(entry.getValue().getY());
            classBox.Update();
        }

        //load relationships
        HashMap<String, RelationshipItem> relationItems = data.getRelationshipItems();
        for (Map.Entry<String, RelationshipItem> entry : relationItems.entrySet()) 
        {
            ClassBox source = getClassBoxByName(entry.getValue().getSource().getName());
            Point2D sourceLoc = entry.getValue().getSourceLoc();
            ClassBox dest = getClassBoxByName(entry.getValue().getDestination().getName());
            Point2D destLoc = entry.getValue().getDestLoc();

            if (source != null && dest != null)
            {
                RelationLine rLine = new RelationLine(baseController, anchorPane);
                rLine.setStart(source, sourceLoc);
                rLine.setEnd(dest, destLoc);
                rLine.setType(entry.getValue().getType());
                anchorPane.getChildren().add(rLine);
                
                Platform.runLater(new Runnable() {
                    @Override public void run() {
                        rLine.Update(scaleTransform);
                    }
                });
            }
        }
    }

    @FXML public void resetZoom(ActionEvent event) {
        event.consume();
        scaleTransform.setX(1.0);
        scaleTransform.setY(1.0);

        anchorPane.setScaleX(1.0);
        anchorPane.setScaleY(1.0);
        anchorPane.layout();

        // reset the scroll pane to the top-left corner
        // scrollPane.setVvalue(0); // Scroll to the top
        // scrollPane.setHvalue(0); // Scroll to the left
    }

    @FXML public void quitMenuClick(ActionEvent event) {
        System.exit(0);
    }

    private void updateRelationLines() {
        for (Node node : anchorPane.getChildren()) {
            if (node instanceof RelationLine) {
                RelationLine line = (RelationLine) node;
                line.Update(scaleTransform);
            }
        }
    }

    private void mouseScroll(ScrollEvent event)
    {
        if (placingRelation != null)
        {
            if (event.getDeltaY() > 0) // Scrolling up
                placingRelation.setType(getNextRelationType(placingRelation.getType(), false));
            else if (event.getDeltaY() < 0) // Scrolling down
                placingRelation.setType(getNextRelationType(placingRelation.getType(), true));
            
            placingRelation.Update(scaleTransform, getFakeMouseEvent(event.getX(), event.getY()));
        }
    }

    private void mouseMove(MouseEvent event)
    {
        if (placingRelation != null)
        {
            placingRelation.Update(scaleTransform, event);
        }

        for (Node node : anchorPane.getChildren()) {
            if (node instanceof RelationLine) {
                RelationLine line = (RelationLine) node;
                //line.mouseMoved(event);
            }
            if (node instanceof ClassBox) {
                ClassBox classBox = (ClassBox) node;
                if (placingRelation == null || placingRelation.getSource() != classBox)
                    classBox.mouseMoved(event);
            }
        }
    }

    // mouse click on background event
    private void mouseClick(MouseEvent event)
    {
        if (placingRelation == null && event.isShiftDown())
        {
            String name = "newClass" + new Random().nextInt(100000);
            baseController.AddClassListener(name);
            ClassBox classBox = addClass(name);
            classBox.Update();
            return;
        }

        if (placingRelation != null && event.getButton() == MouseButton.PRIMARY)
        {
            //user is dragging around a relation line and has clicked the background
            //add a new class to hook the relation line to.
            baseController.getCareTaker().saveState();
            baseController.getCareTaker().Lock();
            ClassBox classBox = onAddClassClicked();
            baseController.getCareTaker().Unlock();
            if (classBox == null) return;

            new Thread(() -> {
                try { Thread.sleep(60);
                } catch (InterruptedException e) { e.printStackTrace(); }

                double newX = event.getSceneX();
                double newY = event.getSceneY() - 35;
                classBox.setLayoutX(newX / scaleTransform.getX());
                classBox.setLayoutY(newY / scaleTransform.getY());

                placingRelation.setEnd(classBox, Point2D.ZERO);
                placingRelation.Update(scaleTransform);
                baseController.getCareTaker().Lock();
                placingRelation.Save(); //update model
                baseController.getCareTaker().Unlock();
                placingRelation = null;
            }).start();
        }
        else if (placingRelation != null && event.getButton() != MouseButton.PRIMARY)
        {
            //user is dragging around a relation line and has rightclicked the background
            //stop placing the relation line. just delete it
            //need to find an alternative for our right mouse button challenged friends
            placingRelation.Remove(true);
            placingRelation = null;
        }
        else if (placingRelation == null && event.getButton() == MouseButton.PRIMARY)
        {
            //user is not dragging around a relation line and has clicked the background
            //addclass
            //ClassBox classBox = onAddClassClicked();
            //if (classBox == null) return;
            //classBox.setLayoutX(event.getSceneX() / scaleTransform.getX());
            //classBox.setLayoutY(event.getSceneY() / scaleTransform.getY());
        }

    }

    //mouse click on relation anchor event
    private void ranchorClick(MouseEvent event, ClassBox classBox)
    {
        //get offset
        double offsetX = classBox.getRanchor().getCenterX() - classBox.getLayoutX();
        double offsetY = classBox.getRanchor().getCenterY() - classBox.getLayoutY();
        Point2D offset = new Point2D(offsetX, offsetY);
        //System.out.println(offset.toString());

        if (placingRelation == null)
        {
            RelationLine line = new RelationLine(baseController, anchorPane);
            line.setStart(classBox, offset);
            
            line.Update(scaleTransform, event);
            anchorPane.getChildren().add(line);
            placingRelation = line;
        }
        else
        {
            placingRelation.setEnd(classBox, offset);
            placingRelation.Update(scaleTransform);
            placingRelation.Save(); //update model
            placingRelation = null;
        }
    }

    private void handleZoom(ScrollEvent event) {
        if (event.isControlDown()) {

            double deltaY = event.getDeltaY();

            if (deltaY < 0) {
                scaleTransform.setX(Math.max(minZoom, scaleTransform.getX() / zoomFactor));
                scaleTransform.setY(Math.max(minZoom, scaleTransform.getY() / zoomFactor));
            } else {
                scaleTransform.setX(Math.min(maxZoom, scaleTransform.getX() * zoomFactor));
                scaleTransform.setY(Math.min(maxZoom, scaleTransform.getY() * zoomFactor));
            }

            event.consume();
        }
    }
    // ================================================================================================================================================================
    // Method to handle the add class button
    
    public ClassBox onAddClassClicked() {
        // displays dialog box prompting for class name
        String className = ClassBox.classNameDialog();

        // Check if className is null or blank
        if (className == null || className.trim().isEmpty()) {
            System.out.println("Class name cannot be null or blank.");
            return null;
        }

        // gets the result of adding the class
        String result = baseController.AddClassListener(className);
        // parse result for either successful rename or failure
        if (result == "good")
        {
            //adds classbox to the view
            ClassBox classBox = addClass(className);
            classBox.Update();
            return classBox;
        }
        else
        {
            ClassBox.showError(result);
            return null;
        }
    }

    public void onDeleteButtonClicked(ClassBox classBox, String className) {
        // Pass className to method and attempt to delete
        String result = baseController.RemoveClassListener(className);
        // parse result for either successful rename or failure
        if (result == "good")
        {
            anchorPane.getChildren().remove(classBox);
        }
        else
        {
            ClassBox.showError(result);
        }
    }

    // when the undo button is clicked
    public void onUndoClicked() {
        String result = baseController.undoListener();
        if (result.equals("good"))
        {
            ClearAll();
            LoadAll();
        }
    }

    // when the redo button is clicked
    public void onRedoClicked() {
        String result = baseController.redoListener();
        if (result.equals("good"))
        {
            ClearAll();
            LoadAll();
        }
    }

    public ClassBox addClass(String className)
    {
        //creates a classBoxBuilder calls adds the panes we need, then builds it.
        ClassBoxBasicBuilder classBoxBuilder = new ClassBoxBasicBuilder(anchorPane, baseController, className, boxWidth, boxHeight, data.getClassItems().get(className.toLowerCase().trim()));
        classBoxBuilder.withFieldPane();
        classBoxBuilder.withMethodPane();
        ClassBox classBox = classBoxBuilder.build();
       
        anchorPane.getChildren().add(classBox);

        classBox.setOnMouseMoved(event -> {
            classBox.getRanchor().setVisible(false);
            event.consume();
        });
        classBox.setOnMouseClicked(event -> event.consume());

        // setup mouse drag
        classBox.getDragBox().setOnMousePressed(event -> {
                classBox.toFront();
                offsetX = (event.getSceneX() / scaleTransform.getX()) - classBox.getLayoutX();
                offsetY = (event.getSceneY() / scaleTransform.getY()) - classBox.getLayoutY();
                event.consume();
        });
    
        classBox.getDragBox().setOnMouseDragged(event -> {
            double newX = (event.getSceneX() / scaleTransform.getX()) - offsetX;
            double newY = (event.getSceneY() / scaleTransform.getY()) - offsetY;
        
            classBox.setLayoutX(newX);
            classBox.setLayoutY(newY);
            data.getClassItems().get(classBox.getClassName().toLowerCase().trim()).setX(newX);
            data.getClassItems().get(classBox.getClassName().toLowerCase().trim()).setY(newY);

            adjustAnchorPaneSize(newX, newY, classBox);
            updateRelationLines();
        });

        classBox.getRanchor().setOnMouseClicked(event -> {
            ranchorClick(event, classBox);
            event.consume();
        });

        return classBox;
    }

    private ClassBox getClassBoxByName(String className)
    {
        for (Node node : anchorPane.getChildren())
        {
            if (node instanceof ClassBox)
            {
                ClassBox classBox = (ClassBox) node;
                if (classBox.getClassName().equals(className))
                    return classBox;
            }
        }
        return null;
    }

    private void adjustAnchorPaneSize(double newX, double newY, ClassBox classBox) {
        // Expand AnchorPane if object goes beyond current bounds
        if (newX < 0) {
            anchorPane.setPrefWidth(anchorPane.getPrefWidth() - newX);
            classBox.setLayoutX(0);
        }
        if (newY < 0) {
            anchorPane.setPrefHeight(anchorPane.getPrefHeight() - newY);
            classBox.setLayoutY(0);
        }

        if (newX + boxWidth > anchorPane.getPrefWidth()) {
            anchorPane.setPrefWidth(newX + boxWidth);
        }

        if (newY + boxHeight > anchorPane.getPrefHeight()) {
            anchorPane.setPrefHeight(newY + boxHeight);
        }
    }

    private MouseEvent getFakeMouseEvent(double mouseX, double mouseY)
    {
        // Create a minimal MouseEvent for just the required X and Y
        MouseEvent fakeMouseEvent = new MouseEvent(
            MouseEvent.MOUSE_MOVED,  // Event type
            mouseX,            // X coordinate
            mouseY,            // Y coordinate
            0,                       // Screen X (unused, can be 0)
            0,                       // Screen Y (unused, can be 0)
            MouseButton.NONE,        // No button pressed
            0,                       // Click count
            false,                   // Shift down
            false,                   // Control down
            false,                   // Alt down
            false,                   // Meta down
            false,                   // Primary button down
            false,                   // Middle button down
            false,                   // Secondary button down
            false,                   // Synthesized
            false,                   // Popup trigger
            false,                   // Still since press
            null                     // Pick result
        );

        return fakeMouseEvent;
    }

    private String getNextRelationType(String currentType, boolean next)
    {
        String[] types = { "Aggregation", "Composition", "Generalization", "Realization" };
        int currentIndex = java.util.Arrays.asList(types).indexOf(currentType);

        if (next)
            currentIndex = (currentIndex - 1 + types.length) % types.length;
        else
            currentIndex = (currentIndex + 1) % types.length;
        
        return types[currentIndex];
    }
}
