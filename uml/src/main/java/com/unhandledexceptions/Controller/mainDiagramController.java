package com.unhandledexceptions.Controller;

import com.unhandledexceptions.Model.Data;
import com.unhandledexceptions.View.ClassBox;
import com.unhandledexceptions.View.RelationLine;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Menu;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.Node;
import javafx.scene.transform.Scale;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;

public class mainDiagramController implements ClassBoxEventHandler {
    private mainDiagramController controller;

    private final double boxWidth = 200; // Width of the boxes
    private final double boxHeight = 300; // Height of the content box
    private RelationLine placingRelation;

    private Data data;
    private BaseController baseController;

    @FXML
    private StackPane bgpane;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private Menu addClassMenu;

    // Used for dragging Class boxes
    double offsetX;
    double offsetY;

    // Zoom in/out variables
    @FXML
    private final Scale scaleTransform = new Scale(1.0, 1.0);
    private double zoomFactor = 1.05;
    private final double minZoom = 0.4;
    private final double maxZoom = 1.5;

    public mainDiagramController() {
        controller = this;
        data = new Data();
        baseController = new BaseController(data);
    }

    @FXML
    private void initialize()
    {
        addClassMenu.setOnShowing(event -> {
            onAddClassClicked(); 
            });

        anchorPane.getTransforms().add(scaleTransform);

        scrollPane.addEventFilter(ScrollEvent.SCROLL, this::handleZoom);

        anchorPane.setOnMouseMoved(event -> mouseMove(event));

        anchorPane.setOnMouseClicked(event -> mouseClick(event));
    }

    @FXML
    public ClassBox addClass(String className)
    {
        ClassBox classBox = new ClassBox(className, boxWidth, boxHeight, controller);
        anchorPane.getChildren().add(classBox);

        // setup mouse drag
        classBox.getDragBox().setOnMousePressed(event -> {
                classBox.toFront();
                offsetX = (event.getSceneX() / scaleTransform.getX()) - classBox.getLayoutX();
                offsetY = (event.getSceneY() / scaleTransform.getY()) - classBox.getLayoutY();
            });
    
            classBox.getDragBox().setOnMouseDragged(event -> {
                double newX = (event.getSceneX() / scaleTransform.getX()) - offsetX;
                double newY = (event.getSceneY() / scaleTransform.getY()) - offsetY;
            
                classBox.setLayoutX(newX);
                classBox.setLayoutY(newY);

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
            ClassBox classBox = addClass("newClass");

            new Thread(() -> {
                try { Thread.sleep(50); 
                } catch (InterruptedException e) { e.printStackTrace(); }

                int i = (placingRelation.getI1() + 2) % 4;
                Rectangle r = classBox.getRanchor(i);
                double newX = event.getSceneX() - r.getBoundsInParent().getMinX();
                double newY = event.getSceneY() - r.getBoundsInParent().getMinY() - 35;
                classBox.setLayoutX(newX / scaleTransform.getX());
                classBox.setLayoutY(newY / scaleTransform.getY());

                placingRelation.setEnd(classBox, i);
                placingRelation.Update(scaleTransform);
                placingRelation = null;
            }).start();
        }
        else if (placingRelation != null && event.getButton() != MouseButton.PRIMARY)
        {
            anchorPane.getChildren().remove(placingRelation);
            placingRelation = null;
        }
    }

    //mouse click on relation anchor event
    private void ranchorClick(MouseEvent event, ClassBox classBox, int index)
    {
        if (placingRelation == null)
        {
            RelationLine line = new RelationLine();
            line.setStart(classBox, index);
            
            line.Update(scaleTransform, event);
            anchorPane.getChildren().add(line);
            placingRelation = line;
        }
        else
        {
            placingRelation.setEnd(classBox, index);
            placingRelation.Update(scaleTransform);
            placingRelation = null;
        }
    }

    @FXML
    public void resetZoom(ActionEvent event) {
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

    @FXML
    public void quit(ActionEvent event) {
        System.exit(0);
    }

    // ================================================================================================================================================================
    // Method to handle the class box click(Not implemented, might be delete?)
    @Override
    public void onClassBoxClicked(ClassBox classBox) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'onClassBoxClicked'");
    }

    // ================================================================================================================================================================
    // Method to handle the add class button
    @Override
    public void onAddClassClicked() {
        // displays dialog box prompting for class name
        String className = ClassBox.classNameDialog();
        // gets the result of adding the class
        String result = baseController.AddClassListener(className);
        // parse result for either successful rename or failure
        if (result == "good")
        {
            ClassBox classBox = addClass(className);
            classBox.Update();
        }
        else
        {
            ClassBox.showError(result);
        }
    }

    public void onClassNameClicked(String oldName, String newName, ClassBox classBox) {
        // trim and lower input for checking result (this might be redundant)
        oldName = oldName.trim().toLowerCase();
        newName = newName.trim().toLowerCase();

        String result = baseController.RenameClassListener(oldName, newName);
        // parse result for either successful rename or failure
        if (result == "good")
        {
            classBox.Update();
            ClassBox.renameClassLabel(newName, classBox);
        }
        else
        {
            ClassBox.showError(result);
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
