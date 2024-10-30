package com.unhandledexceptions.Controller;

import com.unhandledexceptions.View.ClassBox;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Menu;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.ScrollPane;

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

    @FXML
    private void initialize()
    {
        addClassMenu.setOnShowing(event -> {
             addClass(); 
            });
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
                double newX = (event.getSceneX() - offsetX);
                double newY = (event.getSceneY() - offsetY);
            
            classBox.setLayoutX(newX);
            classBox.setLayoutY(newY);

            adjustAnchorPaneSize(newX, newY, classBox);
            });
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
    @FXML
    public void quit(ActionEvent event)
    {
        System.exit(0);
    }
    
}
