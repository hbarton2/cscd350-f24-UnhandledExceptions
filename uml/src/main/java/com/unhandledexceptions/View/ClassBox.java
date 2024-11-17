package com.unhandledexceptions.View;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.scene.shape.Rectangle;
import javafx.util.Pair;
import javafx.scene.paint.Color;
import javafx.scene.control.ButtonBar;

/*
    fieldsList = list of fields {field1 field 2}

 * List
 *      [1] param1 param2 for method 1
 *      [2] param1 param2 for method 2
 */


public class ClassBox extends StackPane 
{
    private String className;
    final double RANCHOR_VIEW_DISTANCE = 50; // Distance threshold for visibility
    private VBox dragBox;
    BaseController baseController;
    private Rectangle[] ranchors = new Rectangle[4]; // Relationship anchors
    AnchorPane anchorPane;
    TitledPane methodsPane;
    TitledPane fieldsPane;
    private ClassItem classItem; // ClassItem object associated with the ClassBox to add listeners

    public ClassBox(AnchorPane anchorPane, BaseController baseController, String classNameIn, double boxWidth,
     double boxHeight, Rectangle[] ranchors, ClassItem classItem)
    {
        this.anchorPane = anchorPane;
        this.baseController = baseController;
        this.className = classNameIn;
        this.ranchors = ranchors;
        this.classItem = classItem;
        this.classItem.addPropertyChangeListener(this);
        // createClassBox(classNameIn, boxWidth, boxHeight);
    }

    public void Remove(AnchorPane anchorPane)
    {
        anchorPane.getChildren().remove(this);
    }

    public void Update()
    {
        //rename class: NameClicked, renameClassLabel
        //delete class: createDeleteButton

        //get class item object
        ClassItem classItem = baseController.getData().getClassItems().get(className.toLowerCase().trim());
        if (classItem == null)
        {
            //remove any relationlines
            List<RelationLine> nodesToRemove = new ArrayList<>();
            for (Node node : anchorPane.getChildren()) {
                if(node instanceof RelationLine) {
                    RelationLine line = (RelationLine) node;
                    if (line.getC1().equals(this) || line.getC2().equals(this)) {
                        nodesToRemove.add(line);
                    }
                }
            }

            // remove the lines
            for (RelationLine line : nodesToRemove)
            {
                line.Remove();
            }


            AnchorPane anchorPane = (AnchorPane) getParent();
            anchorPane.getChildren().remove(this);
            return;
        }

        //name
        renameClassLabel(classItem.getName());
        
        //fields
        clearFields();
        HashMap<String, FieldItem> fieldItems = classItem.getFieldItems();
        ObservableList<String> fields = FXCollections.observableArrayList();
        
        // Add each FieldItem's toString() result to the fields list
        for (FieldItem fieldItem : fieldItems.values()) {
            fields.add(fieldItem.toString());
        }

        addFields(fields);

        //methods
        clearMethods();
        HashMap<String, MethodItem> methodItems = classItem.getMethodItems();   //Hashmap <String, FieldItem>
        for (HashMap.Entry<String, MethodItem> methodItem : methodItems.entrySet()) 
        //methodItem.getValue() -> MethodItem(String methodName, String type)
        //
        {
            HashMap<String, ParameterItem> parameterItems = methodItem.getValue().getParameters();
            ObservableList<String> params = FXCollections.observableArrayList();
            for (ParameterItem parameterItem : parameterItems.values()) {
                params.add(parameterItem.toString());
            }
            addMethod(methodItem.getKey(), methodItem.getValue().getType() , params);
        }
    }


    public void setDragBox(VBox dragBox){
        this.dragBox = dragBox;
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
                Update();
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
        Label classNameLabel = new Label(className);
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
            if (modelUpdated == "good")
            {
                Update();
            }
            else
            {
                showError(modelUpdated);
            }
        }
    }
    
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
                if (modelUpdated == "good")
                {
                    Update();
                }
                else
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
        if (nameLabel != null)
            nameLabel.setText(newName);
    }

    //User for the Update() Method
    public void clearMethods()
    {
        ListView<TitledPane> methodsList = (ListView<TitledPane>) methodsPane.getContent();
        methodsList.getItems().clear();
    }

    //Used for the Update() Method
    public void addMethod(String methodName, String methodType, ObservableList<String> params)
    {
        TitledPane newMethodPane = createNewMethod(methodName, methodType);  //create new box with list for params
        ListView<TitledPane> methodsList = (ListView<TitledPane>) methodsPane.getContent(); //extract the list the new box goes on
        methodsList.getItems().add(newMethodPane); //add box to extracted list
        ListView<String> methodParamList = (ListView<String>) newMethodPane.getContent();//extract paramlistview from newMethodPane
        methodParamList.setItems(params);
    }

    /**
     * Creates and returns a {@link TitledPane} for managing methods. The pane includes
     * a list of methods represented by individual {@code TitledPane} objects and a button
     * for adding new methods.
     *
     * <p>The {@code TitledPane} contains:
     * <ul>
     *   <li>A title bar with a "Methods" label and an "Add Method" button.</li>
     *   <li>A {@link ListView} to display individual method panes.</li>
     * </ul>
     * </p>
     *
     * <p>Key Features:
     * <ul>
     *   <li>Allows the addition of new methods via an input dialog. The user inputs the method's 
     *       name and type, and the model is updated using {@code baseController.AddMethodListener()}.</li>
     *   <li>Displays error messages if invalid input is provided or the addition fails.</li>
     *   <li>Applies custom styling to the pane and its components using predefined CSS classes.</li>
     *   <li>Sets up spacing and alignment for the title bar's label and button.</li>
     * </ul>
     * </p>
     *
     * @return the configured {@code TitledPane} for managing methods.
     */
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
                if (updateModel == "good")
                {
                    Update();   //updates view if model is successfully updated.
                } else {
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

    /**
     * Creates a new {@link TitledPane} to represent a method with its type, name, 
     * and a list of parameters. The pane provides functionality to add new parameters 
     * and edit the method's name, type, or parameters via double-click actions.
     *
     * <p>The {@code TitledPane} includes:
     * <ul>
     *   <li>A title bar with the method type, name, and an "Add Parameter" button.</li>
     *   <li>A {@link ListView} to display the method's parameters.</li>
     *   <li>Double-click functionality for renaming the method's type, name, or parameters.</li>
     * </ul>
     * </p>
     *
     * <p>Key Features:
     * <ul>
     *   <li>Allows the addition of new parameters using an input dialog. Updates the model via 
     *       {@code baseController.AddParameterListener()}.</li>
     *   <li>Supports double-click actions to edit existing parameters by invoking the 
     *       {@code UpdateParam()} method.</li>
     *   <li>Allows editing of the method's name and type via double-click events, calling 
     *       {@code MethodNameClicked()} and {@code MethodTypeClicked()}, respectively.</li>
     *   <li>Validates all input and displays errors for invalid or failed updates.</li>
     * </ul>
     * </p>
     *
     * @param methodName the name of the method.
     * @param methodType the return type of the method.
     * @return the configured {@code TitledPane} for the method.
     */
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

        // expanded without items in it. set after an item is added?
        singleMethodPane.setContent(methodParamList);

        // Add params button
        Button addParamsButton = new Button("+");
        addParamsButton.getStyleClass().add("transparent-button");
        // when pressed, open dialog for user input
        addParamsButton.setOnAction(e -> {
            Pair<String, String> userInput = createInputDialogs("Parameters");

            if(userInput != null){
                String type = (userInput.getKey() != null) ? null : userInput.getKey().toLowerCase();
                String name = (userInput.getValue() != null) ? null : userInput.getValue().toLowerCase();
                //String typeName = type + " " + name;    
                String result = baseController.AddParameterListener(className, methodName, type, name);   
                if (result == "good")
                {

                    Update();   //updates view if model is successfully updated.
                } else {
                    ClassBox.showError(result);
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
                        String oldParamName = parts[1].trim();
                        String oldParamType = parts[0].trim();
                        // Create input dialog to get new name and type
                        Pair<String, String> userInput = createInputDialogs("Field");

                        if (userInput != null) {    //user inputs some data
                            String newParamName = (userInput.getValue() == null) ? oldParamName : userInput.getValue();
                            String newParamType = (userInput.getKey() == null) ? oldParamType : userInput.getKey();

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
        titleBox.getChildren().addAll(singleMethodType, singleMethodName, addParamsButton);
        singleMethodPane.setGraphic(titleBox);
        ;

        return singleMethodPane;
    }

    /**
     * Updates a parameter of a method in the model by renaming it and/or changing its type.
     * Validates the input and updates the UI if the operation succeeds. Displays error
     * messages if validation fails or an update operation is unsuccessful.
     *
     * <p>Key Features:
     * <ul>
     *   <li>Renames the parameter using {@code baseController.RenameParameterListener()} if 
     *       the new parameter name is different from the old one.</li>
     *   <li>Changes the parameter type using {@code baseController.RetypeParameterListener()}.</li>
     *   <li>Updates the UI by calling {@code Update()} if both operations are successful.</li>
     *   <li>Displays an appropriate error message if either operation fails.</li>
     *   <li>Ensures that neither the new parameter name nor type is blank before attempting 
     *       any updates.</li>
     * </ul>
     * </p>
     *
     * @param methodName   the name of the method to which the parameter belongs.
     * @param newParamName the new name for the parameter.
     * @param oldParamName the current name of the parameter.
     * @param newParamType the new type for the parameter.
     */
    private void UpdateParam(String methodName, String newParamName, String oldParamName, String newParamType) {
        // Validate input for new field name and type
        if (newParamName.isBlank() || newParamName.isBlank()) {
            ClassBox.showError("Field name and type cannot be empty.");
            return;
        }
    
        String resultName = null;
         // Attempt to change the field type
        if(!oldParamName.equals(newParamName)){
            resultName = baseController.RenameParameterListener(className, methodName,newParamName, oldParamName);
        }
        // Attempt to rename the field
        String resultType = baseController.RetypeParameterListener(className, methodName, oldParamName, newParamType);
        //String resultType = baseController.RetypeFieldListener(className, newParamName, newParamType);
    
        // Check the results for both operations
        if (("good".equals(resultName) || resultName == null ) && "good".equals(resultType)) {
            Update(); // Update the UI if both updates succeeded
        } else {
            // Show the appropriate error message based on which operation failed
            String errorMessage = !resultName.equals("good") ? resultName : resultType;
            ClassBox.showError(errorMessage);
        }
    }
   
    private void clearFields()
    {
        ListView<String> fieldsList = (ListView<String>) fieldsPane.getContent();
        fieldsList.getItems().clear();
    }

    private void addFields(ObservableList<String> fields)
    {
        ListView<String> fieldsList = (ListView<String>) fieldsPane.getContent();
        fieldsList.setItems(fields);
    }

    /**
     * Creates and returns a {@link TitledPane} that contains a list of fields with functionality
     * to add new fields and rename existing ones. The pane includes a label, an add button, and a 
     * {@link ListView} to display field names. It supports double-clicking a field to rename it 
     * and updating the model accordingly.
     *
     * <p>The {@code TitledPane} is initially collapsed, styled with custom CSS classes, and 
     * restricted to a maximum height of 150 pixels. Fields can be added via an input dialog, and 
     * each field is displayed as a combination of its type and name.</p>
     *
     * <p>Key Features:
     * <ul>
     *   <li>Add new fields using a "+" button. Prompts the user to input a type and name.</li>
     *   <li>Double-click an existing field to rename it via an input dialog.</li>
     *   <li>Updates the model using the {@code baseController.AddFieldListener()} method.</li>
     *   <li>Displays errors if invalid input is provided or if the addition/update fails.</li>
     * </ul>
     * </p>
     *
     * @return the configured {@code TitledPane} for managing fields.
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
                String type = (userInput.getKey() == null) ? null : userInput.getKey().toLowerCase();
                String name = (userInput.getValue() == null) ? null : userInput.getValue().toLowerCase();
                if (type != null && name != null) {
                    // Update your model with the new field
                    String result = baseController.AddFieldListener(className, type, name);
                    if(result.equals("good")){
                        Update();
                    } else {
                        showError(result);
                    }
                } else {
                    showError("Fields must have both Type and Name");
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
                        String oldFieldType = parts[0].trim();
                        String oldFieldName = parts[1].trim();

                        // Create input dialog to get new name and type
                        Pair<String, String> userInput = createInputDialogs("Field");

                        if (userInput != null) {
                            String newFieldName = (userInput.getValue() == null) ? oldFieldName : userInput.getValue();
                            String newFieldType = (userInput.getKey() == null) ? oldFieldType : userInput.getKey();

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
    
    /**
     * Updates a field in the model by renaming it and/or changing its type. 
     * Validates input for the new field name and type, and performs updates 
     * through the appropriate listener methods in the controller.
     *
     * <p>The method performs the following actions:
     * <ul>
     *   <li>Checks that the new field name and type are not blank.</li>
     *   <li>Attempts to rename the field using {@code baseController.RenameFieldListener()} 
     *       if the new name is different from the old name.</li>
     *   <li>Attempts to change the field type using {@code baseController.RetypeFieldListener()}.</li>
     *   <li>Displays error messages if either operation fails, using {@code ClassBox.showError()}.</li>
     *   <li>Updates the UI by calling {@code Update()} if both operations succeed.</li>
     * </ul>
     * </p>
     *
     * @param oldFieldName the current name of the field to be updated.
     * @param newFieldName the new name for the field. Must not be blank.
     * @param newFieldType the new type for the field. Must not be blank.
     */
    private void UpdateField(String oldFieldName, String newFieldName, String newFieldType) {
        // Validate input for new field name and type
        if (newFieldName.isBlank() || newFieldType.isBlank()) {
            ClassBox.showError("Field name and type cannot be empty.");
            return;
        }
        String resultName = null;
        // Attempt to rename the field
        if(!oldFieldName.equals(newFieldName)){
            resultName = baseController.RenameFieldListener(className, oldFieldName, newFieldName);
        }
        // Attempt to change the field type
        String resultType = baseController.RetypeFieldListener(className, newFieldName, newFieldType);
    
        // Check the results for both operations
        if (("good".equals(resultName) || resultName == null ) && "good".equals(resultType)) {
            Update(); // Update the UI if both updates succeeded
        } else {
            // Show the appropriate error message based on which operation failed
            String errorMessage = !resultName.equals("good") ? resultName : resultType;
            ClassBox.showError(errorMessage);
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
            if (modelUpdated == "good")
            {
                Update();
            }
            else
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
                Update();
            }
            else
            {
                showError(modelUpdated);
            }
        }
    }

    
    /**
     * Displays dialog box prompting the user to enter type and name.
     * 
     * <p>This method creates a dialog box with two text input fields for user to input values for type and name.
     * The dialog box has an "Add" button that is enabled when at least one of the input fields has text.
     * If either field is left empty when "Add" it pressed, it returns {@code null} for that field in the resulting
     * {@link Pair}.
     * 
     * @param promptName the name of the prompt to display in the dialog title and header.
     * 
     * @return a {@link Pair} containing the type and name entered by the user, or {@code null} for any field left
     * empty. 
     */
    public Pair<String, String> createInputDialogs(String promptName) {
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Add: " + promptName);
        dialog.setHeaderText("Enter the " + promptName.toLowerCase() + " and name.");

        // Set the button types
        ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        // Create the labels and fields
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

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
            addButton.setDisable(firstInputField.getText().isEmpty()&& secondInputField.getText().isEmpty());
        });
        secondInputField.textProperty().addListener((observable, oldValue, newValue) -> {
            addButton.setDisable(firstInputField.getText().isEmpty()&& secondInputField.getText().isEmpty());
        });

        // Convert the result to a pair of strings when the Add button is clicked
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                String type = firstInputField.getText().isEmpty() ? null : firstInputField.getText();
                String name = secondInputField.getText().isEmpty() ? null : secondInputField.getText();
                return new Pair<>(type, name);
            }
            return null;
        });
    
        Optional<Pair<String, String>> result = dialog.showAndWait();
        return result.orElse(null);  // Returns the result if present, otherwise null
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
