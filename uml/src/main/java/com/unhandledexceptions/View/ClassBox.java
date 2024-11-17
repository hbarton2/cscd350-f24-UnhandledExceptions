package com.unhandledexceptions.View;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import com.unhandledexceptions.Controller.BaseController;
import com.unhandledexceptions.Model.ClassItem;
import com.unhandledexceptions.Model.FieldItem;
import com.unhandledexceptions.Model.MethodItem;
import com.unhandledexceptions.Model.ParameterItem;

import javafx.scene.control.TitledPane;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Pair;
import javafx.scene.control.ButtonBar;

/*
    fieldsList = list of fields {field1 field 2}

 * List
 *      [1] param1 param2 for method 1
 *      [2] param1 param2 for method 2
 */


public class ClassBox extends StackPane implements PropertyChangeListener
{
    private String className;
    private VBox dragBox;
    BaseController baseController;
    AnchorPane anchorPane;
    TitledPane methodsPane;
    TitledPane fieldsPane;
    Circle ranchor;
    private ClassItem classItem; // ClassItem object associated with the ClassBox to add listeners

    public ClassBox(AnchorPane anchorPane, BaseController baseController, String classNameIn, double boxWidth,
     double boxHeight, ClassItem classItem)
    {
        this.anchorPane = anchorPane;
        this.baseController = baseController;
        this.className = classNameIn;
        this.classItem = classItem;
        this.classItem.addPropertyChangeListener(this);
        this.ranchor = new Circle(5);
        this.ranchor.setFill(Color.BLACK);
        this.ranchor.setVisible(false);
        anchorPane.getChildren().add(this.ranchor);
        // createClassBox(classNameIn, boxWidth, boxHeight);
    }

    @SuppressWarnings("unchecked")
    public ListView<String> getFieldsList()
    {
        return (ListView<String>) fieldsPane.getContent();
    }   

    @SuppressWarnings("unchecked")
    public ListView<TitledPane> getMethodsList()
    {
        return (ListView<TitledPane>) methodsPane.getContent();
    }

    public void Remove(AnchorPane anchorPane)
    {
        anchorPane.getChildren().remove(this);
    }

    public AnchorPane getAnchorPane()
    {
        return this.anchorPane;
    }

    public void mouseMoved(MouseEvent event)
    {
        // Get the bounds of the classBox relative to the AnchorPane
        Bounds classBoxBounds = localToParent(getBoundsInLocal());

        // Check if the mouse is near the classBox
        double threshold = 100.0; // Distance threshold for "nearby"
        boolean isNear = classBoxBounds.intersects(event.getX() - threshold, event.getY() - threshold, threshold * 2, threshold * 2);

        if (isNear) 
        {
            // Calculate the closest position for the ranchor on the edge of the classBox
            double closestX = Math.min(Math.max(event.getX(), classBoxBounds.getMinX()), classBoxBounds.getMaxX());
            double closestY = Math.min(Math.max(event.getY(), classBoxBounds.getMinY()), classBoxBounds.getMaxY());
    
            double spacing = 15;
            
            // Snap the ranchor to the nearest edge
            if (event.getX() < classBoxBounds.getMinX() + spacing) {
                closestX = classBoxBounds.getMinX() - spacing; // Left edge
            } else if (event.getX() > classBoxBounds.getMaxX() - spacing) {
                closestX = classBoxBounds.getMaxX() + spacing; // Right edge
            }
            if (event.getY() < classBoxBounds.getMinY() + spacing) {
                closestY = classBoxBounds.getMinY() - spacing; // Top edge
            } else if (event.getY() > classBoxBounds.getMaxY() - spacing) {
                closestY = classBoxBounds.getMaxY() + spacing; // Bottom edge
            }
    
            // Set the ranchor's position
            ranchor.setCenterX(closestX);
            ranchor.setCenterY(closestY);
            ranchor.setVisible(true);
        } 
        else 
        {
            ranchor.setVisible(false); // Hide the ranchor if the mouse is not near
        }
    }

    public void Update()
    {
        //rename class: NameClicked, renameClassLabel
        //delete class: createDeleteButton

        //get class item object
        ClassItem classItem = baseController.getData().getClassItems().get(className.toLowerCase().trim());
        if (classItem == null)
        {
            removeRelationLines();
        }else{
            //rename class
            renameClassLabel(classItem.getName());
            //update fields
            updateFields();
            //update methods
            updateMethods();

            setLayoutX(classItem.getX());
            setLayoutY(classItem.getY());
        }

    }

    public void updateFields(){
        // get class item object
        ClassItem classItem = baseController.getData().getClassItems().get(className.toLowerCase().trim());
        // get the field items
        HashMap<String, FieldItem> fieldItems = classItem.getFieldItems();
        // get the field pane
        @SuppressWarnings("unchecked")
        ListView<String> fieldsList = (ListView<String>) fieldsPane.getContent();
        // clear the fields list
        clearFields();
        // create a new observable list for the fields
        ObservableList<String> fields = FXCollections.observableArrayList();
        // add each field item to the fields list
        for (FieldItem fieldItem : fieldItems.values()) {
            fields.add(fieldItem.toString());
        }
        // set the items in the fields list
        fieldsList.setItems(fields);
    }

    public void updateMethods(){
        // get class item object
        ClassItem classItem = baseController.getData().getClassItems().get(className.toLowerCase().trim());
        // clear existing methods
        clearMethods();
        // get the method items
        HashMap<String, MethodItem> methodItems = classItem.getMethodItems();   //Hashmap <String, FieldItem>
        for (HashMap.Entry<String, MethodItem> methodItem : methodItems.entrySet()) 
        {
            // get the parameters for each method
            HashMap<String, ParameterItem> parameterItems = methodItem.getValue().getParameters();
            ObservableList<String> params = FXCollections.observableArrayList();
            for (ParameterItem parameterItem : parameterItems.values()) {
                params.add(parameterItem.toString());
            }
            // add the method to the methods pane
            addMethod(methodItem.getKey(), methodItem.getValue().getType() , params);
        }
    }

    public void removeRelationLines(){
         //remove any relationlines
         List<RelationLine> nodesToRemove = new ArrayList<>();
         for (Node node : anchorPane.getChildren()) {
             if(node instanceof RelationLine) {
                 RelationLine line = (RelationLine) node;
                 if (line.getSource().equals(this) || line.getDest().equals(this)) {
                     nodesToRemove.add(line);
                 }
             }
         }
         // remove the lines
         for (RelationLine line : nodesToRemove)
         {
             line.Remove(true);
         }

         AnchorPane anchorPane = this.getAnchorPane();
         anchorPane.getChildren().remove(this);
    }

    private void NameClicked(String oldName, Label className) {

        TextInputDialog input = new TextInputDialog();
        input.setTitle("Rename Class");
        input.setHeaderText("Enter the class name");
        input.setContentText("Class name: ");

        Optional<String> result = input.showAndWait();

        if (result.isPresent()) {
            String newName = result.get();
            oldName = oldName.trim().toLowerCase();
            newName = newName.trim().toLowerCase();
    
            String modelUpdated = baseController.RenameClassListener(oldName, newName);
            // parse result for either successful rename or failure
            if (modelUpdated == "good")
            {
                this.className = newName;
            }
            else
            {
                showError(modelUpdated);
            }
        }
    }

    public HBox nameAndDelete(String className){
        HBox hbox = new HBox();
        hbox.setAlignment(Pos.CENTER_LEFT);
        // Create and style the class name label
        Label classNameLabel = new Label();
        classNameLabel.setText(className);
        classNameLabel.setId("classNameLabel");
        classNameLabel.getStyleClass().add("class-name-label"); // Add CSS class for the class name label
        classNameLabel.setOnMouseClicked(event -> {  //"rename" event
            if (event.getClickCount() == 2) {
                String oldName = classNameLabel.getText();
                NameClicked(oldName, classNameLabel);
            }
        });

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button deleteButton = createDeleteButton();

        hbox.getChildren().addAll(classNameLabel, spacer, deleteButton);
        return hbox;
    }

    /* Still needed?
    private void FieldClicked(String oldName, Label className){
        TextInputDialog input = new TextInputDialog();
        input.setTitle("Rename Field");
        input.setHeaderText("Enter the field name");
        input.setContentText("Field name: ");

        Button okButton = (Button) input.getDialogPane().lookupButton(ButtonType.OK);
        okButton.setDisable(true);

        input.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
            okButton.setDisable(newValue.trim().isEmpty());
        });

        Optional<String> result = input.showAndWait();

        if (result.isPresent()) {
            String newName = result.get();
            oldName = oldName.trim().toLowerCase();
            newName = newName.trim().toLowerCase();
    
            String modelUpdated = baseController.RenameFieldListener(className.getText(), oldName, newName);
            // parse result for either successful rename or failure
            if (!(modelUpdated == "good"))
            {
                showError(modelUpdated);
            }
        }
    }
     */

    public String getClassName()
    {
        return this.className;
    }

    public void setClassName(String className)
    {
        this.className = className;
    }

    public VBox getDragBox() {
        return this.dragBox;
    }

    public void setDragBox(VBox dragBox){
        this.dragBox = dragBox;
    }

    public Circle getRanchor()
    {
        return this.ranchor;
    }

    /* method to create a delete button, sets the action to call
     * the controller to delete the class
     * action will also call an alert box to warn user
     */
    public Button createDeleteButton() {
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
                String modelUpdated = baseController.RemoveClassListener(className);
                // parse result for either successful rename or failure
                if (!(modelUpdated == "good"))
                {
                    showError(modelUpdated);
                }

            } else {
                // user denied, cancel action
                return;
            }
        });

        return deleteButton;
    }

    // method to create a dialog box for user input of class name
    public static String classNameDialog() {
        // creates a dialog box for user input
        TextInputDialog dialog = new TextInputDialog();
        // sets the title, header, and content of the dialog box
        dialog.setTitle("Add Class");
        dialog.setHeaderText("Enter the class name");
        dialog.setContentText("Class name: ");

        //creates a reference variable okButton for the Ok button on the dialog box, and disables it
        Button okButton = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
        okButton.setDisable(true);

        // Add a listener to the text field to enable the OK button when input is not empty
        /*
         * the listener is triggers whenever the textProperty of the dialog editor box is changed, 
         * when "newValue.trim().isEmpty()" returns true (no contents in the dialog box), okButton.setDisable is set to true (disabled)
         * when "newValue.trim().isEmpty()" returns false (contents in the dialog box), okButton.setDisable is set to false (enabled)
         */
        dialog.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
            okButton.setDisable(newValue.trim().isEmpty());
        });

        // shows the dialog box and waits for user input
        Optional<String> result = dialog.showAndWait();

        if (result.isPresent()) {
            return result.get();
        }
        return null;
    }
    
    // method to rename a class box label
    public void renameClassLabel(String newName) {
        // gets the label from the classBox object
        Label nameLabel = (Label) lookup("#classNameLabel");

        // if the label exists, rename
        if (nameLabel != null){
            this.className = newName;
            nameLabel.setText(newName);
        }
    }

    public void clearMethods()
    {
        @SuppressWarnings("unchecked")
        ListView<TitledPane> methodsList = (ListView<TitledPane>) methodsPane.getContent();
        methodsList.getItems().clear();
    }

    public void addMethod(String methodName, String methodType, ObservableList<String> params)
    {
        TitledPane newMethodPane = createNewMethod(methodName, methodType);  //create new box with list for params
        @SuppressWarnings("unchecked")
        ListView<TitledPane> methodsList = (ListView<TitledPane>) methodsPane.getContent(); //extract the list the new box goes on
        methodsList.getItems().add(newMethodPane); //add box to extracted list
        @SuppressWarnings("unchecked")
        ListView<String> methodParamList = (ListView<String>) newMethodPane.getContent();//extract paramlistview from newMethodPane
        methodParamList.setItems(params);
    }

    public TitledPane createMethodPane() {
        // Create TitledPane for methods
        methodsPane = new TitledPane();
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
            Pair<String, String> result = createInputDialogs("Method");
            if(result != null){
                String type = result.getKey().toLowerCase();
                String name = result.getValue().toLowerCase();
                //String typeName = type + " " + name;    
                String updateModel = baseController.AddMethodListener(className, name, type);   
                if (!(updateModel == "good"))
                {
                    showError(updateModel);
                } 
            }
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

    private TitledPane createNewMethod(String methodName, String methodType) {
        TitledPane singleMethodPane = new TitledPane();
        singleMethodPane.setExpanded(true);
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
            Pair<String, String> userInput = createInputDialogs("Parameters");

            if(userInput != null){
                String type = userInput.getKey().toLowerCase();
                String name = userInput.getValue().toLowerCase();
                //String typeName = type + " " + name;    
                String result = baseController.AddParameterListener(className, methodName, type, name);   
                if (!(result == "good"))
                {
                    showError(result);
                }
            } else {    //prints to terminal that user canceled dialog box for adding field
                System.out.println("Dialog was canceled");
            }

        });

         // Enable double-click renaming for items in the ListView
         methodParamList.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) { // Detect double-click
                String selectedItem = methodParamList.getSelectionModel().getSelectedItem();
                if (selectedItem != null) {
                    // Split the selected item to get old name and type
                    String[] parts = selectedItem.split(" "); // Assuming the format is "name - type"
                    if (parts.length == 2) {
                        //String oldParamType = parts[0].trim();
                        String oldParamName = parts[1].trim();

                        // Create input dialog to get new name and type
                        Pair<String, String> userInput = createInputDialogs("Field");

                        if (userInput != null) {
                            String newParamName = userInput.getValue().toLowerCase();
                            String newParamType = userInput.getKey().toLowerCase();

                            // Call UpdateField to update the model
                            UpdateParam(methodName, newParamName, oldParamName, newParamType);
                        } else {
                            System.out.println("Dialog was canceled");
                        }
                    }
                }
            }
        });
    

        HBox titleBox = new HBox(30);
        Label singleMethodName = new Label(methodName);
        Label singleMethodType = new Label(methodType);

        singleMethodName.setOnMouseClicked(event -> {
            if(event.getClickCount() == 2){
                String oldName = singleMethodName.getText();
                MethodNameClicked(oldName);
            }
        });

        singleMethodType.setOnMouseClicked(event -> {
            if(event.getClickCount() == 2){
                String oldType = singleMethodType.getText();
                MethodTypeClicked(singleMethodName.getText(), oldType);
            }
        });

        titleBox.setAlignment(Pos.CENTER_LEFT);
        titleBox.getStyleClass().add("fields-title-box");
        titleBox.getChildren().addAll(singleMethodName, singleMethodType, addParamsButton);
        singleMethodPane.setGraphic(titleBox);
        ;

        return singleMethodPane;
    }

    private void UpdateParam(String methodName, String newParamName, String oldParamName, String newParamType) {
        // Validate input for new field name and type
        if (newParamName.isBlank() || newParamName.isBlank()) {
            ClassBox.showError("Field name and type cannot be empty.");
            return;
        }
    
        // Attempt to rename the field
        String resultType = baseController.RetypeParameterListener(className, methodName, oldParamName, newParamType);
        // Attempt to change the field type
        String resultName = baseController.RenameParameterListener(className, methodName,newParamName, oldParamName);
        //String resultType = baseController.RetypeFieldListener(className, newParamName, newParamType);
    
        // Check the results for both operations
        if ("good".equals(resultName) && "good".equals(resultType)) {
            //Update(); // Update the UI if both updates succeeded
        } else {
            // Show the appropriate error message based on which operation failed
            String errorMessage = !resultName.equals("good") ? resultName : resultType;
            ClassBox.showError(errorMessage);
        }
    }
   
    private void clearFields()
    {
        @SuppressWarnings("unchecked")
        ListView<String> fieldsList = (ListView<String>) fieldsPane.getContent();
        fieldsList.getItems().clear();
    }

    /*
    // currently unused.
    private void addFields(ObservableList<String> fields)
    {
        @SuppressWarnings("unchecked")
        ListView<String> fieldsList = (ListView<String>) fieldsPane.getContent();
        fieldsList.setItems(fields);
    }
    */

    public TitledPane createFieldPane() {
        fieldsPane = new TitledPane();
        fieldsPane.setExpanded(false);
        fieldsPane.setMaxHeight(150);
        fieldsPane.getStyleClass().add("titled-pane");
    
        ListView<String> fieldsList = new ListView<>();
        fieldsList.getStyleClass().add("list-view");
    
        // ObservableList to hold field names
        ObservableList<String> fields = FXCollections.observableArrayList();
        fieldsList.setItems(fields);
    
        // Create the button for adding fields
        Button addFieldButton = new Button("+");
        addFieldButton.getStyleClass().add("transparent-button");
        addFieldButton.setOnAction(e -> {
            Pair<String, String> userInput = createInputDialogs("Field");
    
            if (userInput != null) {
                String type = userInput.getKey().toLowerCase();
                String name = userInput.getValue().toLowerCase();
                if (!type.isBlank() && !name.isBlank()) {
                    // Update your model with the new field
                    baseController.AddFieldListener(className, type, name);
                }
            } else {
                System.out.println("Dialog was canceled");
            }
        });
        
        // Enable double-click renaming for items in the ListView
        fieldsList.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) { // Detect double-click
                String selectedItem = fieldsList.getSelectionModel().getSelectedItem();
                if (selectedItem != null) {
                    // Split the selected item to get old name and type
                    String[] parts = selectedItem.split(" "); // Assuming the format is "name - type"
                    if (parts.length == 2) {
                        String oldFieldName = parts[1].trim();

                        // Create input dialog to get new name and type
                        Pair<String, String> userInput = createInputDialogs("Field");

                        if (userInput != null) {
                            String newFieldName = userInput.getValue().toLowerCase();
                            String newFieldType = userInput.getKey().toLowerCase();

                            // Call UpdateField to update the model
                            UpdateField(oldFieldName, newFieldName, newFieldType);
                        } else {
                            System.out.println("Dialog was canceled");
                        }
                    }
                }
            }
        });
    
        // Create an HBox to hold the fields label and the add button
        HBox fieldsTitleBox = new HBox(160); // Add spacing between label and button
        Label fieldsLabel = new Label("Fields");
        fieldsLabel.setId("fieldTitleBoxLabel");
        fieldsTitleBox.setAlignment(Pos.CENTER_LEFT); // Align items to the left
        fieldsTitleBox.getStyleClass().add("fields-title-box");
        fieldsTitleBox.getChildren().addAll(fieldsLabel, addFieldButton);
        fieldsPane.setGraphic(fieldsTitleBox);
    
        fieldsPane.setContent(fieldsList); // Set the content of the TitledPane
    
        return fieldsPane;
    }
    
    private void UpdateField(String oldFieldName, String newFieldName, String newFieldType) {
        // Validate input for new field name and type
        if (newFieldName.isBlank() || newFieldType.isBlank()) {
            ClassBox.showError("Field name and type cannot be empty.");
            return;
        }
    
        // Attempt to rename the field
        String resultName = baseController.RenameFieldListener(className, oldFieldName, newFieldName);
        // Attempt to change the field type
        String resultType = baseController.RetypeFieldListener(className, newFieldName, newFieldType);
    
        // Check the results for both operations
        if (!("good".equals(resultName)) || !("good".equals(resultType))) {
            String errorMessage = !resultName.equals("good") ? resultName : resultType;
            showError(errorMessage);
        }
    }
    
    

    private void MethodNameClicked(String oldName){
        TextInputDialog input = new TextInputDialog();
        input.setTitle("Rename Method");
        input.setHeaderText("Enter the method name");
        input.setContentText("Method name: ");

        Button okButton = (Button) input.getDialogPane().lookupButton(ButtonType.OK);
        okButton.setDisable(true);

        input.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
            okButton.setDisable(newValue.trim().isEmpty());
        });

        Optional<String> result = input.showAndWait();

        if (result.isPresent()) {
            String newName = result.get();
            oldName = oldName.trim().toLowerCase();
            newName = newName.trim().toLowerCase();
    
            String modelUpdated = baseController.RenameMethodListener(className, oldName, newName);
            // parse result for either successful rename or failure
            if (!(modelUpdated == "good"))
            {
                showError(modelUpdated);
            }
            
        }
    }

    private void MethodTypeClicked(String methodName ,String oldName) {
        TextInputDialog input = new TextInputDialog();
        input.setTitle("Retype Method");
        input.setHeaderText("Enter the method type");
        input.setContentText("Method type: ");

        Button okButton = (Button) input.getDialogPane().lookupButton(ButtonType.OK);
        okButton.setDisable(true);

        input.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
            okButton.setDisable(newValue.trim().isEmpty());
        });

        Optional<String> result = input.showAndWait();

        if (result.isPresent()) {
            String newType = result.get();
            newType = newType.trim().toLowerCase();
    
            String modelUpdated = baseController.RetypeMethodListener(className, methodName,newType);
            // parse result for either successful rename or failure
            if (modelUpdated == "good")
            {
                showError(modelUpdated);
            }
            
        }
    }

    

    public Pair<String, String> createInputDialogs(String promptName) {
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Add promptName");
        dialog.setHeaderText("Enter the " + promptName.toLowerCase() + " and name.");

        // Set the button types
        ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        // Create the labels and fields
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        // ComboBox<String> typeComboBox = new ComboBox<>();
        // setupComboBox(typeComboBox);

        //Text for type input
        TextField firstInputField = new TextField();
        firstInputField.setPromptText("Type");
        //Text for name input
        TextField secondInputField = new TextField();
        secondInputField.setPromptText("Name");


        grid.add(new Label(promptName.trim() + " type" + ":"), 0, 0);
        grid.add(firstInputField, 1, 0);
        grid.add(new Label("Name:"), 0, 1);
        grid.add(secondInputField, 1, 1);

        dialog.getDialogPane().setContent(grid);

        // Enable/Disable the Add button depending on whether fields are empty
        Node addButton = dialog.getDialogPane().lookupButton(addButtonType);
        addButton.setDisable(true);

        // Add listeners to fields to enable Add button when both fields have text
        firstInputField.textProperty().addListener((observable, oldValue, newValue) -> {
            addButton.setDisable(firstInputField.getText().isEmpty()|| secondInputField.getText().isEmpty());
        });
        secondInputField.textProperty().addListener((observable, oldValue, newValue) -> {
            addButton.setDisable(firstInputField.getText().isEmpty()|| secondInputField.getText().isEmpty());
        });

        // Convert the result to a pair of strings when the Add button is clicked
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                return new Pair<>(firstInputField.getText(), secondInputField.getText());
            }
            return null;
        });
    
        Optional<Pair<String, String>> result = dialog.showAndWait();
        return result.orElse(null);  // Returns the result if present, otherwise null
    }

    // public void setupComboBox(ComboBox<String> typeComboBox) {
    //      // Convert enum values to a List of Strings
    //     List<String> typeList = Arrays.stream(DataType.values())
    //         .map(Enum::name) // Convert enum to its string name
    //         .collect(Collectors.toList());

    //     typeComboBox.getItems().addAll(typeList); // Set the list of DataTypes in the ComboBox
    //     typeComboBox.setPromptText("Select Type");
    // }

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

    /*
     * PropertyChangeListener method to listen for changes in the model and update
     * Source is the classItem related to the classBox object
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch(evt.getPropertyName()) {
            case "classItem":
                //redraws the classbox
                Update();
                break;
            case "name":
                //renames the label of the classbox
                renameClassLabel((String) evt.getNewValue());
                break;
            case "field":
                //updates the titlepane for fields
                updateFields();
                break;
            case "method":
                //updates the titlepane for methods
                updateMethods();
                break;
            case "removeBox":
                //removes the classbox
                Update();
                break;
            case "parameterChange":
                //updates the titlepane for methods which contains parameters
                updateMethods();
                break;
            default:
                break;
        }
        
    }

}
