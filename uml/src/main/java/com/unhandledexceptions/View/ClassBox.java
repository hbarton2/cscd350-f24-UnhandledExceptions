package com.unhandledexceptions.View;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputDialog;
import java.util.Optional;

import com.unhandledexceptions.Controller.ClassBoxEventHandler;

import javafx.scene.control.TitledPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;

public class ClassBox extends StackPane
{
    private ClassBoxEventHandler eventHandler;

    final double RANCHOR_VIEW_DISTANCE = 50;  // Distance threshold for visibility

    private VBox dragBox;
    private Rectangle[] ranchors = new Rectangle[4];
    //private double offsetX;
    //private double offsetY;

    public ClassBox(String classNameIn, double boxWidth, double boxHeight, ClassBoxEventHandler eventHandler)
    {
        this.eventHandler = eventHandler;
        createClassBox(classNameIn, boxWidth, boxHeight);
    }

    public void setEventHandler(ClassBoxEventHandler eventHandler)
    {
        this.eventHandler = eventHandler;
    }

    private void createClassBox(String classNameIn, double boxWidth, double boxHeight)
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

        HBox nameAndLink = new HBox();
        nameAndLink.setAlignment(Pos.CENTER_LEFT);

        // Create and style the class name label
        Label className = new Label(classNameIn);
        className.getStyleClass().add("class-name-label"); // Add CSS class for the class name label
        className.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.SECONDARY) {
                NameClicked(className);
            }
        });


        // Button linkButton = createLinkButton();
        // linkButton.setOnMouseClicked(event -> {
        //     if(event.getButton() == MouseButton.PRIMARY && eventHandler != null)
        //         eventHandler.onLinkButtonClicked(this);
        // });

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        nameAndLink.getChildren().addAll(className, spacer);


        // Create separator line (optional, uncomment if needed)
        // Separator separatorLine = new Separator();

        // Create TitledPane for fields
        TitledPane fieldsPane = new TitledPane();
        fieldsPane.setExpanded(false);
        //fieldsPane.setText("Fields");
        fieldsPane.setMaxHeight(150);
        
        

        ListView<String> fieldsList = new ListView<>();
        fieldsList.getStyleClass().add("list-view"); // Apply CSS class for the fields ListView

        ObservableList<String> dummyFields = FXCollections.observableArrayList(
         "Field1 : int",
            "Field2 : String",
            "Field3 : boolean",
            "Field4 : int"

        );
    // Create the button for adding fields
    Button addButton = new Button("+");
    addButton.getStyleClass().add("transparent-button");
    //when pressed, open dialog for user input
    addButton.setOnAction(e -> {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Add Field");
        dialog.setHeaderText("Enter the field name and type");
        dialog.setContentText("Field:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(field -> fieldsList.getItems().add(field));
    });

    // Create a label for "Fields"
    Label fieldsLabel = new Label("Fields");

    // Create an HBox to hold the fields label and the add button
    //HARD CODED SPACING OF TITLE AND BUTTON
    HBox fieldsTitleBox = new HBox(160); // Add spacing between label and button
    fieldsTitleBox.setAlignment(Pos.CENTER_LEFT); // Align items to the left
    fieldsTitleBox.getStyleClass().add("fields-title-box");
    fieldsTitleBox.getChildren().addAll(fieldsLabel, addButton);

    // Set the fieldsTitleBox HBox as the graphic of the TitledPane
    fieldsPane.setGraphic(fieldsTitleBox);

    //populates the FieldsList and sets the content of the fieldsPane to said list
    fieldsList.setItems(dummyFields);
    fieldsPane.setContent(fieldsList);
    fieldsPane.getStyleClass().add("titled-pane");

    // Create TitledPane for methods
    TitledPane methodsPane = new TitledPane();
    methodsPane.setExpanded(false);
    //methodsPane.setText("Methods");
    methodsPane.setMaxHeight(200);
    methodsPane.getStyleClass().add("titled-pane");
    
    //List that will hold the individual method panes
    ListView<TitledPane> methodsList = new ListView<>();
    methodsList.getStyleClass().add("list-view"); // Apply CSS class for the methods ListView
            
    //method panes to go in methodsList
    TitledPane method1 = new TitledPane();
    method1.setExpanded(false);
    //method1.setText("method1");
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

    //METHODS ADD BUTTON
    // Create the button for adding fields
    Button addMethodsButton = new Button("+");
    addMethodsButton.getStyleClass().add("transparent-button");
    addMethodsButton.setAlignment(Pos.BASELINE_RIGHT);
    //when pressed, open dialog for user input
    addMethodsButton.setOnAction(e -> {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Add Method");
        dialog.setHeaderText("Enter the Method name");
        dialog.setContentText("Method:");

        Optional<String> result = dialog.showAndWait();
        //result.ifPresent(field -> fieldsList.getItems().add(field));
    });

    // Create a label for "Methods:"
    Label methodsLabel = new Label("Methods");

    //HARD CODED SPACING OF TITLE AND BUTTON
    HBox methodsTitleBox = new HBox(143);//spacing between objects
    methodsTitleBox.setAlignment(Pos.CENTER_LEFT);
    methodsTitleBox.getStyleClass().add("fields-title-box");
    methodsTitleBox.getChildren().addAll(methodsLabel, addMethodsButton);
    methodsPane.setGraphic(methodsTitleBox);


    /*******************Add button for individual methods************************/
     // Create the button for adding fields
     Button addParamsButton = new Button("+");
     addParamsButton.getStyleClass().add("transparent-button");
     //when pressed, open dialog for user input
     addParamsButton.setOnAction(e -> {
         TextInputDialog dialog = new TextInputDialog();
         dialog.setTitle("Add Parameter:");
         dialog.setHeaderText("Enter the Parameter type and name");
         dialog.setContentText("Parameter:");
 
         Optional<String> result = dialog.showAndWait();
        //result.ifPresent(field -> fieldsList.getItems().add(field));
     });
 
     // Create a label for "Fields"
     Label individualMethodLabel = new Label("method1:");
 
     // Create an HBox to hold the fields label and the add button
     //HARD CODED SPACING OF TITLE AND BUTTON
     HBox method1TitleBox = new HBox(90); // Add spacing between label and button
     method1TitleBox.setAlignment(Pos.CENTER_LEFT); // Align items to the left
     method1TitleBox.getChildren().addAll(individualMethodLabel, addParamsButton);
 
     // Set the fieldsTitleBox HBox as the graphic of the TitledPane
     method1.setGraphic(method1TitleBox);

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
            int index = i;
            ranchors[i].setOnMouseClicked(event -> AddRelation(index));
        }

        //structure
        vbase.getChildren().addAll(ranchors[0], hbase, ranchors[2]);
        hbase.getChildren().addAll(ranchors[3], vbox, ranchors[1]);

    // Add the components to the VBox
    vbox.getChildren().addAll(nameAndLink, fieldsPane, methodsPane);

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
        // vbox.setOnMouseDragged(event -> {
        //     setLayoutX(event.getSceneX() - offsetX);
        //     setLayoutY(event.getSceneY() - offsetY);
        // });
    }

    private void AddRelation(int index)
    {
        System.out.println("clicked: " + index);
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

    private Button createLinkButton()
    {
        Button linkButton = new Button();
        ImageView linkImage = new ImageView("/images/link-image.png");
        linkImage.setFitHeight(30);
        linkImage.setFitWidth(30);
        linkImage.setStyle("-fx-background-color: transparent;");
        linkButton.setGraphic(linkImage);
        linkButton.getStyleClass().add("transparent-button");

        return linkButton;
    }

    public static String classNameDialog(){
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Add Class");
        dialog.setHeaderText("Enter the class name");
        dialog.setContentText("Class name: ");
        
        Optional<String> result = dialog.showAndWait();

        if(result.isPresent())
        {
            return result.get();
        }
        return null;
    }



    
}
