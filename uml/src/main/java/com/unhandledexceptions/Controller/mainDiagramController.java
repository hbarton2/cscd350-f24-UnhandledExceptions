package com.unhandledexceptions.Controller;

import com.unhandledexceptions.View.ClassBox;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Menu;
import javafx.scene.layout.AnchorPane;
import javafx.scene.transform.Scale;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.ScrollEvent;

public class mainDiagramController 
{
    private final double boxWidth = 200; // Width of the boxes
    private final double boxHeight = 300; // Height of the content box

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private Menu addClassMenu;
    
    //Used for dragging Class boxes
    double offsetX;
    double offsetY;

    //Zoom in/out variables
    @FXML
    private final Scale scaleTransform = new Scale(1.0,1.0);
    private double zoomFactor = 1.05;
    private final double minZoom = 0.4;
    private final double maxZoom = 1.5;

    @FXML
    private void initialize()
    {
        addClassMenu.setOnShowing(event -> {
             addClass(); 
            });

        anchorPane.getTransforms().add(scaleTransform);

        scrollPane.addEventFilter(ScrollEvent.SCROLL, this::handleZoom);
    }

    @FXML
    public void addClass() 
    {
        ClassBox classBox = new ClassBox("newClass", boxWidth, boxHeight);
        anchorPane.getChildren().add(classBox);
        classBox.setOnMousePressed(event -> {
                classBox.toFront();
                offsetX = event.getSceneX() - classBox.getLayoutX();
                offsetY = event.getSceneY() - classBox.getLayoutY();
            });
    
            classBox.setOnMouseDragged(event -> {
                double newX = (event.getSceneX() - offsetX) * zoomFactor;
                double newY = (event.getSceneY() - offsetY) * zoomFactor;
            
            classBox.setLayoutX(newX);
            classBox.setLayoutY(newY);

            adjustAnchorPaneSize(newX, newY, classBox);
            });
    }

    @FXML
    public void resetZoom(ActionEvent event)
    {
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

    private void adjustAnchorPaneSize(double newX, double newY, ClassBox classBox){
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

    private void handleZoom(ScrollEvent event){
        if(event.isControlDown()){
            
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
    public void quit(ActionEvent event)
    {
        System.exit(0);
    }
    
}
