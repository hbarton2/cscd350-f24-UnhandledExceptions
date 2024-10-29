package com.unhandledexceptions.Controller;

import java.io.IOException;
import java.util.Optional;

import com.unhandledexceptions.View.GUI;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.TitledPane;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;

public class newDiagramController {

    private double offsetX;
    private double offsetY;
    
    @FXML
    private VBox classBox;
    
    //referenced in .fxml "fx:id='anchorPane'"
    @FXML
    private AnchorPane anchorPane;
    
    private final double boxWidth = 200; // Width of the boxes
    private final double boxHeight = 300; // Height of the content box
    private final double padding = 20; // Space between boxes
    private final double startX = 30; // Starting X position
    private final double startY = 30; // Starting Y position

    @FXML
    public void addClass(String classNameIn){
        // Creates frame for ClassItem box
        StackPane classBox = new StackPane();
        classBox.getStyleClass().add("class-box"); // Add CSS style class for the box
        //classBox.setStyle("-fx-background-color: #b91010; -fx-border-color: gray; -fx-border-width: 1px;");

        anchorPane.getChildren().add(classBox);

        // Create a VBox to hold the class name label and the TitledPanes
        VBox vbox = new VBox();
        vbox.setSpacing(10); // Set spacing between components
        vbox.setAlignment(Pos.CENTER); // Center align the contents of the VBox

        // Create and style the class name label
        Label className = new Label(classNameIn);
        className.getStyleClass().add("class-name-label"); // Add CSS class for the class name label

        // Create separator line (optional, uncomment if needed)
        // Separator separatorLine = new Separator();

        // Create TitledPane for fields
        TitledPane fieldsPane = new TitledPane();
        fieldsPane.setText("Fields");
        ListView<String> fieldsList = new ListView<>();
        fieldsList.getStyleClass().add("list-view"); // Apply CSS class for the fields ListView
        fieldsPane.setContent(fieldsList);

        // Create TitledPane for methods
        TitledPane methodsPane = new TitledPane();
        methodsPane.setText("Methods");
        ListView<String> methodsList = new ListView<>();
        methodsList.getStyleClass().add("list-view"); // Apply CSS class for the methods ListView
        methodsPane.setContent(methodsList);

        // Add the components to the VBox
        vbox.getChildren().addAll(className, fieldsPane, methodsPane);

        // Set up mouse drag functionality
        classBox.setOnMousePressed(event -> {
            classBox.toFront();
            offsetX = event.getX();
            offsetY = event.getY();
        });

        classBox.setOnMouseDragged(event -> {
            classBox.setLayoutX(event.getSceneX() - offsetX);
            classBox.setLayoutY(event.getSceneY() - offsetY);
        });

        // Add the VBox to the classBox
        classBox.getChildren().add(vbox);
         
    }


    @FXML
    public void addClassButton(ActionEvent event)
    {
        TextInputDialog className = new TextInputDialog();
        className.setTitle("Add Class");
        className.setHeaderText("Enter the class name");
        className.setContentText("Class name: ");


        Optional<String> result = className.showAndWait();

        if(result.isPresent())
        {
            String classNameIn = result.get();
            addClass(classNameIn);
        }

        
    }


    private void adjustBoxPositions() {
        double anchorPaneWidth = anchorPane.getWidth();
    
        // Reset count for rows
        int count = 0; // Count of boxes in the current row
        double currentY = startY; // Start from the top for Y
    
        // Loop through all children of the AnchorPane to adjust positions
        for (var node : anchorPane.getChildren()) {
            if (node instanceof StackPane) {
                double newX = startX + count * (boxWidth + padding);
    
                // Check if the box fits in the current row
                if (newX + boxWidth > anchorPaneWidth) {
                    // Move to the next row if it exceeds the width
                    count = 0; // Reset count for new row
                    newX = startX; // Reset X position to start
                    currentY += boxHeight + padding; // Move down for new row
                }
    
                // Set new positions
                node.setTranslateX(newX);
                node.setTranslateY(currentY); // Use the updated Y position
                count++; // Increment count for next box
            }
        }
    }

    
    


    @FXML
    public void quit(ActionEvent event){
        System.exit(0);
    }
    
}
