package com.unhandledexceptions.View;

import com.unhandledexceptions.Controller.BaseController;
import javafx.geometry.Pos;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class ClassBoxBuilder {
    private AnchorPane anchorPane;
    private BaseController baseController;
    private String className;
    private double boxWidth;
    private double boxHeight;
    private boolean fields = false;
    private boolean methods = false;
    private Rectangle[] ranchors = new Rectangle[4];

    public ClassBoxBuilder(AnchorPane anchorPane, BaseController baseController, String className, double boxWidth, double boxHeight) {
        this.anchorPane = anchorPane;
        this.baseController = baseController;
        this.className = className;
        this.boxWidth = boxWidth;
        this.boxHeight = boxHeight;
       
        //setup();
    }

    public VBox setup(){
        VBox vbase = new VBox();
        vbase.setSpacing(10);
        vbase.setAlignment(Pos.CENTER);

        HBox hbase = new HBox();
        hbase.setSpacing(10);
        hbase.setAlignment(Pos.CENTER);

        VBox vbox = new VBox();
        vbox.setId("contentBox");
        //this.dragBox = dragBox;
        vbox.setSpacing(10); // Set spacing between components
        vbox.setAlignment(Pos.CENTER); // Center align the contents of the VBox
        vbox.getStyleClass().add("class-box");

        // ranchors (relationship anchors)
        for (int i = 0; i < 4; i++) {
            ranchors[i] = new Rectangle(15, 15);
            ranchors[i].setFill(Color.BLACK);
        }

        // structure
        vbase.getChildren().addAll(ranchors[0], hbase, ranchors[2]);
        hbase.getChildren().addAll(ranchors[3], vbox, ranchors[1]);


        // Add the VBox to the classBox
        //getChildren().add(vbase);

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
        return vbase;   //returns vbase to be added as a child to a classBox, setting the contents of the classbox object
    }

    public ClassBoxBuilder withFieldPane() {
        this.fields = true;
        return this;
    }

    public ClassBoxBuilder withMethodPane() {
        this.methods = true;
        return this;
    }

    public ClassBox build() {
        ClassBox classBox = new ClassBox(anchorPane, baseController, className, boxWidth, boxHeight, ranchors);
        classBox.getChildren().add(setup());    //adds VBox that is holding all of the content to the classBox
        HBox nameAndDelete = classBox.nameAndDelete(this.className);    //creates nameAndDelete part
        VBox box = (VBox) classBox.lookup("#contentBox");   //grabs container VBox that holds the panes
        classBox.setDragBox(box);   //sets dragBox in the classBox that is used for dragging.

        box.getChildren().add(nameAndDelete);   //adds the nameAndDelete HBbox to the VBox container

        if (fields) {   //if we call the withFieldPane method we will add the fieldpane to the VBox
            TitledPane fieldPane = classBox.createFieldPane();    
            box.getChildren().add(fieldPane);
        }

        if (methods) {  //if we call the withMethodPane method we will add the methodPane to the VBox
            TitledPane methodPane = classBox.createMethodPane();
            box.getChildren().add(methodPane);
        }
        
        return classBox;    //returns the classBox
    }
}