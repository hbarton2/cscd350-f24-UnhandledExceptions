package com.unhandledexceptions.Controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.unhandledexceptions.Model.ClassItem;
import com.unhandledexceptions.Model.Data;
import com.unhandledexceptions.Model.RelationshipItem;
import com.unhandledexceptions.View.ClassBox;
import com.unhandledexceptions.View.RelationLine;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Menu;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.Node;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Scale;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;

public class mainDiagramController
{
    private mainDiagramController controller;
    
    private Data data;
    private BaseController baseController;
    private final double boxWidth = 200; // Width of the boxes
    private final double boxHeight = 300; // Height of the content box
    private RelationLine placingRelation;
    private double offsetX; // Used for dragging Class boxes
    private double offsetY;

    public mainDiagramController() {
        controller = this;
        data = new Data();
        baseController = new BaseController(data);
    }

    @FXML private StackPane bgpane;
    @FXML private AnchorPane anchorPane;
    @FXML private ScrollPane scrollPane;
    @FXML private Menu addClassMenu;

    // Zoom in/out variables
    @FXML private final Scale scaleTransform = new Scale(1.0, 1.0);
    private double zoomFactor = 1.05;
    private final double minZoom = 0.4;
    private final double maxZoom = 1.5;

    @FXML private void initialize()
    {
        controller = this;
        data = new Data();
        baseController = new BaseController(data);

        addClassMenu.setOnShowing(event -> {
            onAddClassClicked(); 
            });

        anchorPane.getTransforms().add(scaleTransform);

        scrollPane.addEventFilter(ScrollEvent.SCROLL, this::handleZoom);

        anchorPane.setOnMouseMoved(event -> mouseMove(event));

        anchorPane.setOnMouseClicked(event -> mouseClick(event));
    }

    @FXML public void newMenuClick()
    {
        data.Clear();

        ArrayList<Node> children = new ArrayList<>();
        
        for (Node child : anchorPane.getChildren())
                children.add(child);
        
        anchorPane.getChildren().removeAll(children);
    }

    @FXML public void openRecentMenuClick()
    {
        //open recent
    }

    @FXML public void saveMenuClick()
    {
        //save without asking for file name
    }

    @FXML public void saveAsMenuClick()
    {
        TextInputDialog input = new TextInputDialog();
        input.setTitle("Save Project");
        input.setHeaderText("Enter the file name");
        input.setContentText("File name: ");

        Optional<String> result = input.showAndWait();

        if (result.isPresent()) {
            data.Save(result.get());
        }
    }

    @FXML public void openMenuClick()
    {
        TextInputDialog input = new TextInputDialog();
        input.setTitle("Open Project");
        input.setHeaderText("Enter the file name");
        input.setContentText("File name: ");

        Optional<String> result = input.showAndWait();

        //clear all
        newMenuClick();

        if (result.isPresent()) {
            data.Load(result.get());
        }

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
            int sourceLoc = entry.getValue().getSourceLoc();
            ClassBox dest = getClassBoxByName(entry.getValue().getDestination().getName());
            int destLoc = entry.getValue().getDestLoc();

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

    @FXML public ClassBox addClass(String className)
    {
        ClassBox classBox = new ClassBox(anchorPane, baseController, className, boxWidth, boxHeight);
        //new ClassBox(baseController, className, boxWidth, boxHeight, controller);
        anchorPane.getChildren().add(classBox);

        classBox.setOnMouseClicked(event -> {
                event.consume();
            });
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
                data.getClassItems().get(classBox.getClassName()).setX(newX);
                data.getClassItems().get(classBox.getClassName()).setY(newY);

                adjustAnchorPaneSize(newX, newY, classBox);
                updateRelationLines();
            });

        //setup ranchor events
        for (int i = 0; i < 4; i++)
        {
            int index = i;
            classBox.getRanchor(i).setOnMouseClicked(event -> {
                ranchorClick(event, classBox, index);
                event.consume();
            });
        }

        return classBox;
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

    private void mouseMove(MouseEvent event)
    {
        if (placingRelation != null)
            placingRelation.Update(scaleTransform, event);
    }

    // mouse click on background event
    private void mouseClick(MouseEvent event)
    {
        if (placingRelation != null && event.getButton() == MouseButton.PRIMARY)
        {
            //user is dragging around a relation line and has clicked the background
            //add a new class to hook the relation line to.
            ClassBox classBox = onAddClassClicked();
            if (classBox == null) return;

            new Thread(() -> {
                try { Thread.sleep(60);
                } catch (InterruptedException e) { e.printStackTrace(); }

                int i = (placingRelation.getI1() + 2) % 4;
                Rectangle r = classBox.getRanchor(i);
                double newX = event.getSceneX() - r.getBoundsInParent().getMinX();
                double newY = event.getSceneY() - r.getBoundsInParent().getMinY() - 35;
                classBox.setLayoutX(newX / scaleTransform.getX());
                classBox.setLayoutY(newY / scaleTransform.getY());

                placingRelation.setEnd(classBox, i);
                placingRelation.Update(scaleTransform);
                placingRelation.Save(baseController); //update model
                placingRelation = null;
            }).start();
        }
        else if (placingRelation != null && event.getButton() != MouseButton.PRIMARY)
        {
            //user is dragging around a relation line and has rightclicked the background
            //stop placing the relation line. just delete it
            //need to find an alternative for our right mouse button challenged friends
            anchorPane.getChildren().remove(placingRelation);
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
    private void ranchorClick(MouseEvent event, ClassBox classBox, int index)
    {
        if (placingRelation == null)
        {
            RelationLine line = new RelationLine(baseController, anchorPane);
            line.setStart(classBox, index);
            
            line.Update(scaleTransform, event);
            anchorPane.getChildren().add(line);
            placingRelation = line;
        }
        else
        {
            placingRelation.setEnd(classBox, index);
            placingRelation.Update(scaleTransform);
            placingRelation.Save(baseController); //update model
            placingRelation = null;
        }
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
        // gets the result of adding the class
        String result = baseController.AddClassListener(className);
        // parse result for either successful rename or failure
        if (result == "good")
        {
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

}
