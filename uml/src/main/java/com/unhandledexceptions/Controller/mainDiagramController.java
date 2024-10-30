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
    }

    @FXML
    public void quit(ActionEvent event)
    {
        System.exit(0);
    }
    
}
