package com.unhandledexceptions.View;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputDialog;
import java.util.Optional;
import javafx.scene.control.TitledPane;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;

public class ClassBox extends StackPane
{
    final double RANCHOR_VIEW_DISTANCE = 50;  // Distance threshold for visibility

    private VBox dragBox;
    private Rectangle[] ranchors = new Rectangle[4];
    //private double offsetX;
    //private double offsetY;

    public ClassBox(String classNameIn, double boxWidth, double boxHeight)
    {
        //getStyleClass().add("class-box"); // Add CSS style class for the box

        //create structure with boxes
        VBox vbase = new VBox();
        vbase.setSpacing(10);
        vbase.setAlignment(Pos.CENTER);
        //vbase.setStyle("-fx-background-color: lightblue;"); < debugging
        HBox hbase = new HBox();
        hbase.setSpacing(10);
        hbase.setAlignment(Pos.CENTER);
        //hbase.setStyle("-fx-background-color: red;"); < debugging

        // Create a VBox to hold the class name label and the TitledPanes
        VBox vbox = new VBox();
        dragBox = vbox;
        vbox.setSpacing(10); // Set spacing between components
        vbox.setAlignment(Pos.CENTER); // Center align the contents of the VBox
        vbox.getStyleClass().add("class-box");

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

        fieldsPane.setMaxHeight(150);
        ListView<String> fieldsList = new ListView<>();
        fieldsList.getStyleClass().add("list-view"); // Apply CSS class for the fields ListView

        ObservableList<String> dummyFields = FXCollections.observableArrayList(
         "Field1 : int",
            "Field2 : String",
            "Field3 : boolean",
            "Field4 : int",
            "Field5 : String",
            "Field6 : boolean",
            "Field7 : int",
            "Field8 : String",
            "Field9 : boolean",
            "Field10 : int",
            "Field11 : String",
            "Field12 : boolean",
            "Field13 : int",
            "Field14 : String",
            "Field15 : boolean",
            "Field16 : int",
            "Field17 : String",
            "Field18 : boolean"
        );
        fieldsList.setItems(dummyFields);

        fieldsPane.setContent(fieldsList);

        // Create TitledPane for methods
        TitledPane methodsPane = new TitledPane();
        methodsPane.setExpanded(false);
        methodsPane.setText("Methods");
        methodsPane.setMaxHeight(200);
        
        //List that will hold the individual method panes
        ListView<TitledPane> methodsList = new ListView<>();
        methodsList.getStyleClass().add("list-view"); // Apply CSS class for the methods ListView
                
        //method panes to go in methodsList
        TitledPane method1 = new TitledPane();
        method1.setExpanded(false);
        method1.setText("method1");
        method1.setMaxHeight(125);
        method1.setMaxWidth(220);
        
        ListView<String> method1List = new ListView<>();
        ObservableList<String> method1Params = FXCollections.observableArrayList(
            "Type : Param1",
            "Type : Param2",
            "Type : Param3"
        );

        ObservableList<TitledPane> titlePanes = FXCollections.observableArrayList(
            method1
        );

        //adds list of params to method1's list
        method1List.setItems(method1Params);

        //adds method1List to method1 TitlePane
        method1.setContent(method1List);

        //adds list of titlePanes to the overall methodsList
        methodsList.setItems(titlePanes);

        //Adds methods TitlePane's List to the methodPane
        methodsPane.setContent(methodsList);
/*                                                      _
         * MethodsPane                  _                        |
         *       method1                 |__method1 TitlePane    |
         *          method1Params       _|                       |
         *                              _                        |___ methodsPane TitlePane
         *       method2                 |__method2 TitlePane    |
         *          method2Params       _|                       |                                               
         *                                                      _|
         */ 

        
        //ranchors (relationship anchors)
        for (int i = 0; i < 4; i++)
        {
            ranchors[i] = new Rectangle(10, 10);
            ranchors[i].setFill(Color.BLACK);
            //int index = i;
            //ranchor[i].setOnMouseClicked(event -> AddRelation(index));
        }

        //structure
        vbase.getChildren().addAll(ranchors[0], hbase, ranchors[2]);
        hbase.getChildren().addAll(ranchors[3], vbox, ranchors[1]);

        // Add the components to the VBox
        vbox.getChildren().addAll(className, fieldsPane, methodsPane);

        // Add the VBox to the classBox
        getChildren().add(vbase);

        // ranchor visibility
        vbase.setOnMouseEntered(event -> {
            for (Rectangle r : ranchors)
                r.setVisible(true);
        });
        vbase.setOnMouseExited(event -> {
            for (Rectangle r : ranchors)
                r.setVisible(false);
        });

        vbox.setOnMouseEntered(event -> {
            for (Rectangle r : ranchors)
                r.setVisible(false);
        });
        vbox.setOnMouseExited(event -> {
            for (Rectangle r : ranchors)
                r.setVisible(true);
        });
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

    public VBox getDragBox()
    {
        return this.dragBox;
    }

    public Rectangle[] getRanchors()
    {
        return this.ranchors;
    }
}
