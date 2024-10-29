package com.unhandledexceptions.View;

import com.fasterxml.jackson.databind.ext.OptionalHandlerFactory;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputDialog;
import java.util.Optional;
import javafx.scene.control.TitledPane;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class ClassBox extends StackPane
{
    public ClassBox(String classNameIn, double boxWidth, double boxHeight)
    {
        getStyleClass().add("class-box"); // Add CSS style class for the box

        // Create a VBox to hold the class name label and the TitledPanes
        VBox vbox = new VBox();
        vbox.setSpacing(10); // Set spacing between components
        vbox.setAlignment(Pos.CENTER); // Center align the contents of the VBox

        // Create and style the class name label
        Label className = new Label(classNameIn);
        className.getStyleClass().add("class-name-label"); // Add CSS class for the class name label
        className.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.SECONDARY) {
                NameClicked(className);
            }
        });

        // Create separator line (optional, uncomment if needed)
        // Separator separatorLine = new Separator();

        // Create TitledPane for fields
        TitledPane fieldsPane = new TitledPane();
        fieldsPane.setExpanded(false);
        fieldsPane.setText("Fields");
        ListView<String> fieldsList = new ListView<>();
        fieldsList.getStyleClass().add("list-view"); // Apply CSS class for the fields ListView
        fieldsPane.setContent(fieldsList);

        // Create TitledPane for methods
        TitledPane methodsPane = new TitledPane();
        methodsPane.setExpanded(false);
        methodsPane.setText("Methods");
        ListView<String> methodsList = new ListView<>();
        methodsList.getStyleClass().add("list-view"); // Apply CSS class for the methods ListView
        methodsPane.setContent(methodsList);

        // Add the components to the VBox
        vbox.getChildren().addAll(className, fieldsPane, methodsPane);

        // Add the VBox to the classBox
        getChildren().add(vbox);
    }

    private void NameClicked(Label className)
    {
        TextInputDialog input = new TextInputDialog();
        input.setTitle("Add Class");
        input.setHeaderText("Enter the class name");
        input.setContentText("Class name: ");
        
        Optional<String> result = input.showAndWait();

        if(result.isPresent())
        {
            className.setText(result.get());
        }
    }
}
