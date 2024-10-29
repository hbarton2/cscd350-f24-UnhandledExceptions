package com.unhandledexceptions.Controller;

import java.io.IOException;


import com.unhandledexceptions.View.GUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.control.Label;

public class newDiagramController {
    
    //referenced in .fxml "fx:id='anchorPane'"
    @FXML
    private AnchorPane anchorPane;
    
    private final double boxWidth = 200; // Width of the boxes
    private final double boxHeight = 300; // Height of the content box
    private final double padding = 20; // Space between boxes
    private final double startX = 30; // Starting X position
    private final double startY = 30; // Starting Y position

    @FXML
    public void addClass(){
        //creates frame for classItem box
        StackPane classBox = new StackPane();

        anchorPane.getChildren().add(classBox); 

        classBox.setPrefSize(boxWidth, boxHeight);
        Rectangle rectangle = new Rectangle(boxWidth,boxHeight, Color.gray(0));
        rectangle.setOpacity(.25);
        classBox.getChildren().add(rectangle);


        VBox vbox = new VBox();
        StackPane namePane = new StackPane();
        Rectangle nameBox = new Rectangle(classBox.getWidth(),0, Color.AQUAMARINE);
        nameBox.setHeight(boxHeight/6);
        Label className = new Label("ClassName");
        namePane.getChildren().addAll(nameBox,className);

        Separator separatorLine = new Separator();

        //Field Tree
        TreeView<String> fieldTree = new TreeView<>();
        TreeItem<String> fieldRoot = new TreeItem<>("Fields");
        
        TreeItem<String> field1 = new TreeItem<>("type1 name1");
        TreeItem<String> field2 = new TreeItem<>("type2 name2");
        TreeItem<String> field3 = new TreeItem<>("type3 name3");

        fieldRoot.getChildren().addAll(field1,field2,field3);
        fieldRoot.setExpanded(true);

        fieldTree.setRoot(fieldRoot);
        fieldRoot.setExpanded(true);

        
        
        //Method Tree
        TreeView<String> methodTree = new TreeView<>();
        TreeItem<String> methodRoot = new TreeItem<>("Methods");
        
        TreeItem<String> method1 = new TreeItem<>("name1");
        TreeItem<String> param1 = new TreeItem<>("type1 name1");
        TreeItem<String> param2 = new TreeItem<>("type2 name2");
        method1.getChildren().addAll(param1, param2);

        TreeItem<String> method2 = new TreeItem<>("name2");
        TreeItem<String> param3 = new TreeItem<>("type1 name1");
        TreeItem<String> param4 = new TreeItem<>("type2 name2");
        method2.getChildren().addAll(param3, param4);

        TreeItem<String> method3 = new TreeItem<>("name3");
        TreeItem<String> param5 = new TreeItem<>("type1 name1");
        TreeItem<String> param6 = new TreeItem<>("type2 name2");
        method3.getChildren().addAll(param1, param2);

        methodRoot.getChildren().addAll(method1,method2,method3);
        methodRoot.setExpanded(true);
        method1.getChildren().addAll(param1,param2);
        method2.getChildren().addAll(param3,param4);
        method3.getChildren().addAll(param5,param6);
        
        methodTree.setRoot(methodRoot);

   

        vbox.getChildren().addAll(namePane,separatorLine,fieldTree,methodTree);

        classBox.getChildren().add(vbox);

        adjustBoxPositions();
         
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
