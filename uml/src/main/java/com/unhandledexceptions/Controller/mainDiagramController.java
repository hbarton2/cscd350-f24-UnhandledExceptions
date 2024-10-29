package com.unhandledexceptions.Controller;

import com.unhandledexceptions.View.ClassBox;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Menu;
import javafx.scene.layout.AnchorPane;

public class mainDiagramController 
{
    private final double boxWidth = 200; // Width of the boxes
    private final double boxHeight = 300; // Height of the content box
    private double offsetX;
    private double offsetY;
    
    @FXML
    private AnchorPane anchorPane;

    @FXML
    private Menu addClassMenu;

    @FXML
    private void initialize()
    {
        addClassMenu.setOnShowing(event -> {
             addClass("newClass"); 
            });
    }

    @FXML
    public void addClass(String classNameIn)
    {
        ClassBox classBox = new ClassBox(classNameIn, boxWidth, boxHeight);
        anchorPane.getChildren().add(classBox);

        // Set up mouse drag functionality
        classBox.setOnMousePressed(event -> {
            classBox.toFront();
            offsetX = event.getSceneX() - classBox.getLayoutX();
            offsetY = event.getSceneY() - classBox.getLayoutY();
        });

        classBox.setOnMouseDragged(event -> {
            classBox.setLayoutX(event.getSceneX() - offsetX);
            classBox.setLayoutY(event.getSceneY() - offsetY);
        });
    }

    @FXML
    public void addClassButton(ActionEvent event)
    {
        addClass("newClass");
    }

    @FXML
    public void quit(ActionEvent event)
    {
        System.exit(0);
    }
    
}
