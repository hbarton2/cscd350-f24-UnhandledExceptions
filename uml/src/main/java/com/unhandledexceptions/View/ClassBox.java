package com.unhandledexceptions.View;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputDialog;
import java.util.Optional;

import com.unhandledexceptions.Controller.ClassBoxEventHandler;

import javafx.scene.control.TitledPane;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;

public class ClassBox extends StackPane {
    private ClassBoxEventHandler eventHandler;

    final double RANCHOR_VIEW_DISTANCE = 50; // Distance threshold for visibility

    private VBox dragBox;

    // Relationship anchors
    private Rectangle[] ranchors = new Rectangle[4];
    // private double offsetX;
    // private double offsetY;

    public ClassBox(String classNameIn, double boxWidth, double boxHeight, ClassBoxEventHandler eventHandler) {
        this.eventHandler = eventHandler;
        createClassBox(classNameIn, boxWidth, boxHeight);
    }

    public void Update()
    {
        //update
    }

    // ============================================================================================
    // method to set the event handler for the class box
    public void setEventHandler(ClassBoxEventHandler eventHandler) {
        this.eventHandler = eventHandler;
    }

    private void createClassBox(String classNameIn, double boxWidth, double boxHeight) {
        // getStyleClass().add("class-box"); // Add CSS style class for the box

        // create structure with boxes
        VBox vbase = new VBox();
        vbase.setSpacing(10);
        vbase.setAlignment(Pos.CENTER);
        // vbase.setStyle("-fx-background-color: lightblue;"); < debugging
        HBox hbase = new HBox();
        hbase.setSpacing(10);
        hbase.setAlignment(Pos.CENTER);
        // hbase.setStyle("-fx-background-color: red;"); < debugging

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
        className.setId("classNameLabel");
        className.getStyleClass().add("class-name-label"); // Add CSS class for the class name label
        className.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                String oldName = className.getText();
                NameClicked(oldName, className);
            }
        });

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button deleteButton = createDeleteButton();

        nameAndLink.getChildren().addAll(className, spacer, deleteButton);

        TitledPane fieldsPane = createFieldPane();
        TitledPane methodsPane = createMethodPane();

        // ranchors (relationship anchors)
        for (int i = 0; i < 4; i++) {
            ranchors[i] = new Rectangle(10, 10);
            ranchors[i].setFill(Color.BLACK);
            int index = i;
            ranchors[i].setOnMouseClicked(event -> AddRelation(index));
        }

        // structure
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
    }

    private void AddRelation(int index) {
        System.out.println("clicked: " + index);
    }

    private void NameClicked(String oldName, Label className) {

        TextInputDialog input = new TextInputDialog();
        input.setTitle("Rename Class");
        input.setHeaderText("Enter the class name");
        input.setContentText("Class name: ");

        Optional<String> result = input.showAndWait();

        if (result.isPresent()) {
            String newName = result.get();
            eventHandler.onClassNameClicked(oldName, newName, this);
        }
    }

    public VBox getDragBox() {
        return this.dragBox;
    }

    public Rectangle getRanchor(int i)
    {
        return this.ranchors[i];
    }

    public Rectangle[] getRanchors()
    {
        return this.ranchors;
    }

    public Rectangle getClickedRanchor(MouseEvent event)
    {
        for (Rectangle ranchor : ranchors)
            if (ranchor.contains(event.getX(), event.getY()))
                return ranchor;
        return null;
    }

    // ================================================================================================================================================================
    // method to create a delete button, sets the action to call
    // the controller to delete the class
    // action will also call an alert box to warn user
    private Button createDeleteButton() {
        // create a button with an image
        Button deleteButton = new Button();
        ImageView deleteImage = new ImageView("/images/trash-can-icon.png");
        // set the size of the image
        deleteImage.setFitHeight(12);
        deleteImage.setFitWidth(18);
        // set the background of the image to transparent
        deleteImage.setStyle("-fx-background-color: transparent;");
        // set the graphic of the button to the image
        deleteButton.setGraphic(deleteImage);
        // set the style class of the button to a transparent button
        deleteButton.getStyleClass().add("transparent-button-delete");

        deleteButton.setOnAction(event -> {
            Alert warning = deleteClassWarning();

            // get confirmation for delete
            Optional<ButtonType> result = warning.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                // user accepted, get the classBox name
                String className = getClassBoxName();

                // call controller delete passing this class box and the name
                eventHandler.onDeleteButtonClicked(this, className);

            } else {
                // user denied, cancel action
                return;
            }
        });

        return deleteButton;
    }

    // ================================================================================================================================================================
    // method to create a dialog box for user input of class name
    public static String classNameDialog() {
        // creates a dialog box for user input
        TextInputDialog dialog = new TextInputDialog();
        // sets the title, header, and content of the dialog box
        dialog.setTitle("Add Class");
        dialog.setHeaderText("Enter the class name");
        dialog.setContentText("Class name: ");

        // shows the dialog box and waits for user input
        Optional<String> result = dialog.showAndWait();

        if (result.isPresent()) {
            return result.get();
        }
        return null;
    }

    // ================================================================================================================================================================
    // method to rename a class box label
    public static void renameClassLabel(String newName, ClassBox classBox) {
        // gets the label from the classBox object
        Label nameLabel = (Label) classBox.lookup("#classNameLabel");

        // if the label exists, rename
        if (nameLabel != null) {
            nameLabel.setText(newName);
        } else {
            // TODO not sure how to handle this, error?
            System.out.println("Null label?");
        }
    }

    private TitledPane createMethodPane() {
        // Create TitledPane for methods
        TitledPane methodsPane = new TitledPane();
        methodsPane.setExpanded(false);
        methodsPane.setMaxHeight(250);
        methodsPane.getStyleClass().add("titled-pane");

        // List of each methods TitledPane that will hold the individual method panes
        ListView<TitledPane> methodsList = new ListView<>();
        methodsList.getStyleClass().add("list-view"); // Apply CSS class for the methods ListView

        // Create the button for adding Methods
        Button addMethodsButton = new Button("+");
        addMethodsButton.getStyleClass().add("transparent-button");
        // when pressed, open dialog for user input
        addMethodsButton.setOnAction(e -> {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Add Method");
            dialog.setHeaderText("Enter the Method name");
            dialog.setContentText("Method:");

            Optional<String> result = dialog.showAndWait();
            result.ifPresent(method -> {
                TitledPane newMethodPane = createNewMethod(method);
                methodsList.getItems().add(newMethodPane);
            });
        });

        // HARD CODED SPACING OF TITLE AND BUTTON
        HBox methodsTitleBox = new HBox(139);// spacing between objects
        methodsTitleBox.setAlignment(Pos.CENTER_LEFT);
        methodsTitleBox.getStyleClass().add("fields-title-box");

        // Create a label for "Methods:"
        Label methodsLabel = new Label("Methods:");

        // Adds label and button to HBox, sets to methodsPane graphic.
        methodsTitleBox.getChildren().addAll(methodsLabel, addMethodsButton);
        methodsPane.setGraphic(methodsTitleBox);

        /******************* Add button for individual methods ************************/
        // Create the button for adding params

        // Adds methods TitlePane's List to the methodPane
        methodsPane.setContent(methodsList);

        return methodsPane;
    }

    private TitledPane createNewMethod(String methodName) {
        TitledPane singleMethodPane = new TitledPane();
        singleMethodPane.setExpanded(false);
        // instead of static setting height, increase and decrease based on # of
        // methods/params/fields.
        singleMethodPane.setMaxWidth(225);
        singleMethodPane.setMaxHeight(150);
        singleMethodPane.getStyleClass().add("fields-title-box");
        ListView<String> methodParamList = new ListView<>();
        methodParamList.getStyleClass().add("list-view");

        // The TitledPane requires a ListView as content, and the ListView requires an
        // ObservableList.
        ObservableList<String> params = FXCollections.observableArrayList();
        methodParamList.setItems(params);

        // TODO: fix issue where setting content here causes the list to be able to be
        // expanded without items in it. set after an item is added?
        singleMethodPane.setContent(methodParamList);

        // Add params button
        Button addParamsButton = new Button("+");
        addParamsButton.getStyleClass().add("transparent-button");
        // when pressed, open dialog for user input
        addParamsButton.setOnAction(e -> {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Add Parameter:");
            dialog.setHeaderText("Enter the Parameter type and name");
            dialog.setContentText("Parameter:");
            Optional<String> result = dialog.showAndWait();
            result.ifPresent(param -> {
                params.add(param);
            });
        });

        HBox titleBox = new HBox(90);
        Label singleMethodName = new Label(methodName);
        titleBox.setAlignment(Pos.CENTER_LEFT);
        titleBox.getStyleClass().add("fields-title-box");
        titleBox.getChildren().addAll(singleMethodName, addParamsButton);
        singleMethodPane.setGraphic(titleBox);
        ;

        return singleMethodPane;
    }

    private TitledPane createFieldPane() {
        TitledPane fieldsPane = new TitledPane();
        fieldsPane.setExpanded(false);
        fieldsPane.setMaxHeight(150);
        fieldsPane.getStyleClass().add("titled-pane");
        ListView<String> fieldsList = new ListView<>();
        fieldsList.getStyleClass().add("list-view"); // Apply CSS class for the fields ListView

        // The TitledPane requires a ListView as content, and the ListView requires an
        // ObservableList.
        ObservableList<String> fields = FXCollections.observableArrayList();
        fieldsList.setItems(fields);

        // TODO: fix issue where setting content here causes the list to be able to be
        // expanded without items in it. set after an item is added?
        fieldsPane.setContent(fieldsList); //

        // Create the button for adding fields
        Button addFieldButton = new Button("+");
        addFieldButton.getStyleClass().add("transparent-button");
        addFieldButton.setOnAction(e -> {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Add Field");
            dialog.setHeaderText("Enter the field name and type");
            dialog.setContentText("Field:");

            Optional<String> result = dialog.showAndWait();
            result.ifPresent(field -> {
                fields.add(field);
            });
        });

        // Create an HBox to hold the fields label and the add button
        // HARD CODED SPACING OF TITLE AND BUTTON
        HBox fieldsTitleBox = new HBox(160); // Add spacing between label and button
        Label fieldsLabel = new Label("Fields");
        fieldsTitleBox.setAlignment(Pos.CENTER_LEFT); // Align items to the left
        fieldsTitleBox.getStyleClass().add("fields-title-box");
        fieldsTitleBox.getChildren().addAll(fieldsLabel, addFieldButton);
        fieldsPane.setGraphic(fieldsTitleBox);

        return fieldsPane;
    }

    // ================================================================================================================================================================
    // method to display an error message
    public static void showError(String errorMessage) {
        // create an alert box
        Alert alert = new Alert(AlertType.ERROR);
        // set title and header as an error
        alert.setTitle("Class Error");
        alert.setHeaderText("A Class Error Occured");
        // set the context text as the error message string
        alert.setContentText(errorMessage);

        // displays until user acknowledges
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                // nothing else to do after accepting
            }
        });
    }

    // method to display warning when deleting a class box
    private Alert deleteClassWarning() {
        // create an alert box
        Alert alert = new Alert(AlertType.CONFIRMATION);
        // set the title and header as a warning
        alert.setTitle("Warning");
        alert.setHeaderText("Are you sure you want to delete this class?");
        alert.setContentText("This action can not be undone");

        // return the alert to be instantiated by delete action
        return alert;
    }

    // helper method to retrieve the class name from the label
    private String getClassBoxName() {
        Label classNameLabel = (Label) this.lookup("#classNameLabel");

        // check if the label exists
        if (classNameLabel != null) {
            // retrieve the name from the label
            String className = classNameLabel.getText();

            // return the class name
            return className;
        } else {
            // return null if the label does not exist
            return null;
        }
    }

}
