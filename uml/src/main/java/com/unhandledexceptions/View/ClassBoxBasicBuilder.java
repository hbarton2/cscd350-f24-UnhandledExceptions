package com.unhandledexceptions.View;

import com.unhandledexceptions.Controller.BaseController;
import com.unhandledexceptions.Model.ClassItem;

import javafx.geometry.Pos;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ClassBoxBasicBuilder implements ClassBoxBuilderInterface {
    private AnchorPane anchorPane;
    private BaseController baseController;
    private String className;
    private double boxWidth;
    private double boxHeight;
    private boolean fields = false;
    private boolean methods = false;
    private ClassItem classItem;

    public ClassBoxBasicBuilder(AnchorPane anchorPane, BaseController baseController, String className, double boxWidth, double boxHeight, ClassItem classItem) {
        this.anchorPane = anchorPane;
        this.baseController = baseController;
        this.className = className;
        this.boxWidth = boxWidth;
        this.boxHeight = boxHeight;
        this.classItem = classItem;
    }

    public VBox setup(){
        //creates a vbox that will actually hold the panes that are displayed
        VBox vbox = new VBox();
        vbox.setId("contentBox");
        vbox.setSpacing(10); // Set spacing between components
        vbox.setAlignment(Pos.CENTER); // Center align the contents of the VBox
        vbox.getStyleClass().add("class-box");

        return vbox;   //returns vbase to be added as a child to a classBox, setting the contents of the classbox object
    }

    public void withFieldPane() {
        this.fields = true;
    }

    public void withMethodPane() {
        this.methods = true;
    }

    public ClassBox build() {
        ClassBox classBox = new ClassBox(anchorPane, baseController, className, boxWidth, boxHeight, classItem);    //creates a new classBox object
        classBox.getChildren().add(setup());    //adds VBox that is holding all of the content to the classBox
        HBox nameAndDelete = classBox.nameAndDelete(this.className);    //creates nameAndDelete part
        VBox contentBox = (VBox) classBox.lookup("#contentBox");   //grabs container VBox that holds the panes
        classBox.setDragBox(contentBox);   //sets dragBox in the classBox that is used for dragging.

        contentBox.getChildren().add(nameAndDelete);   //adds the nameAndDelete HBbox to the VBox container

        if (fields) {   //if we call the withFieldPane method we will add the fieldpane to the VBox
            TitledPane fieldPane = classBox.createFieldPane();    
            contentBox.getChildren().add(fieldPane);
        }

        if (methods) {  //if we call the withMethodPane method we will add the methodPane to the VBox
            TitledPane methodPane = classBox.createMethodPane();
            contentBox.getChildren().add(methodPane);
        }

        return classBox;    //returns the classBox
    }
}