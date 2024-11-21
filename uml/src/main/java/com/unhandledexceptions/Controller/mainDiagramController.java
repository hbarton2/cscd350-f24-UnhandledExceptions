package com.unhandledexceptions.Controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.imageio.ImageIO;

import com.unhandledexceptions.Model.ClassItem;
import com.unhandledexceptions.Model.Data;
import com.unhandledexceptions.Model.RelationshipItem;
import com.unhandledexceptions.View.ClassBox;
import com.unhandledexceptions.View.ClassBoxBasicBuilder;
import com.unhandledexceptions.View.RelationLine;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Scale;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.ToolBar;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.embed.swing.SwingFXUtils;

public class mainDiagramController
{
    //private mainDiagramController controller;
    
    private Data data;
    private BaseController baseController;
    private final double boxWidth = 200; // Width of the boxes
    private final double boxHeight = 300; // Height of the content box
    private RelationLine placingRelation;
    private double offsetX; // Used for dragging Class boxes
    private double offsetY;
    private boolean darkMode = false;
    private boolean partyMode = false;
    private final Color lightColor = Color.rgb(211, 211, 211);
    private final Color darkColor = Color.rgb(27, 70, 160);

    //styling declarations (needed to toggle between style sheets for fxml objects)
    @FXML
    private VBox rootVBox;
    @FXML 
    private ToolBar toolBar;
    @FXML
    private MenuBar menuBar;
    @FXML
    private ImageView undoImageView, redoImageView, darkModeImageView, lightModeImageView, partyModeImageView, screenshotImageView;
    @FXML
    private Button addClassButton;
    @FXML
    private Menu fileMenu, helpMenu;

    public mainDiagramController() {
        //controller = this;
        data = new Data();
        baseController = new BaseController(data);

    }

    @FXML private StackPane bgpane;
    @FXML private AnchorPane anchorPane;
    @FXML private ScrollPane scrollPane;
    @FXML private Menu addClassMenu;
    @FXML private Menu openRecentMenu;

    // Zoom in/out variables
    @FXML
    private final Scale scaleTransform = new Scale(1.0, 1.0);
    private double zoomFactor = 1.05;
    private final double minZoom = 0.4;
    private final double maxZoom = 1.5;

    @FXML private void initialize()
    {
        //controller = this;
        data = new Data();
        baseController = new BaseController(data);
        anchorPane.getTransforms().add(scaleTransform);
        scrollPane.addEventFilter(ScrollEvent.SCROLL, this::handleZoom);
        anchorPane.setOnMouseMoved(event -> mouseMove(event));
        anchorPane.setOnMouseClicked(event -> mouseClick(event));
        scrollPane.getStyleClass().add("scroll-pane");
        anchorPane.getStyleClass().add("anchor-pane");

        rootVBox.getStyleClass().add("vbox");
    }

    public void newMenuClick()
    {
        data.Clear();

        ClearAll();
    }

    public void openRecentMenuShowing()
    {
        //clear items
        openRecentMenu.getItems().clear();

        // Get the current working directory
        File dir = new File(System.getProperty("user.dir"));

        // Filter for .json files
        File[] jsonFiles = dir.listFiles((directory, name) -> name.endsWith(".json"));
        if (jsonFiles != null) {
            Arrays.stream(jsonFiles).forEach(file -> {
                // Create a menu item for each .json file
                MenuItem fileItem = new MenuItem(file.getName());
                
                // Set an action for each menu item (e.g., to open the file)
                fileItem.setOnAction(event -> {
                    newMenuClick();
                    data.Load(file.getAbsolutePath());
                    LoadAll();
                });
                
                // Add the menu item to the "Open Recent" submenu
                openRecentMenu.getItems().add(fileItem);
            });
        }
    }

    public void saveMenuClick()
    {
        data.Save(anchorPane);
    }

    public void saveAsMenuClick()
    {
        data.SaveAs(anchorPane);
    }

    public void openMenuClick()
    {
        String result = data.Load(anchorPane);

        if (result.equals("good"))
        {
            //clear all
            newMenuClick();

            LoadAll();   
        }
    }

    private void ClearAll()
    {
        ArrayList<Node> children = new ArrayList<>();

        for (Node child : anchorPane.getChildren())
            children.add(child);

        anchorPane.getChildren().removeAll(children);
    }

    private void LoadAll()
    {
        //load classes
        HashMap<String, ClassItem> classItems = data.getClassItems();
        for (Map.Entry<String, ClassItem> entry : classItems.entrySet()) {
            ClassBox classBox = addClass(entry.getKey());
            classBox.setLayoutX(entry.getValue().getX());
            classBox.setLayoutY(entry.getValue().getY());
            classBox.Update();
        }

        // load relationships
        HashMap<String, RelationshipItem> relationItems = data.getRelationshipItems();
        for (Map.Entry<String, RelationshipItem> entry : relationItems.entrySet()) {
            ClassBox source = getClassBoxByName(entry.getValue().getSource().getName());
            int sourceLoc = entry.getValue().getSourceLoc();
            ClassBox dest = getClassBoxByName(entry.getValue().getDestination().getName());
            int destLoc = entry.getValue().getDestLoc();

            if (source != null && dest != null) {
                RelationLine rLine = new RelationLine(baseController, anchorPane);
                rLine.setStart(source, sourceLoc);
                rLine.setEnd(dest, destLoc);
                rLine.setType(entry.getValue().getType());
                anchorPane.getChildren().add(rLine);

                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        rLine.Update(scaleTransform, partyMode);
                    }
                });
            }
        }
    }

    public ClassBox addClass(String className)
    {
        //creates a classBoxBuilder calls adds the panes we need, then builds it.
        ClassBoxBasicBuilder classBoxBuilder = new ClassBoxBasicBuilder(anchorPane, baseController, className, boxWidth, boxHeight, data.getClassItems().get(className.toLowerCase().trim()));
        classBoxBuilder.withFieldPane();
        classBoxBuilder.withMethodPane();
        ClassBox classBox = classBoxBuilder.build();

        classBox.setEffect(effectHelper());

        anchorPane.getChildren().add(classBox);

        classBox.setOnMouseClicked(event -> {
            event.consume();
        });
        // setup mouse drag
        classBox.getDragBox().setOnMousePressed(event -> {
                classBox.toFront();
                offsetX = (event.getSceneX() / scaleTransform.getX()) - classBox.getLayoutX();
                offsetY = (event.getSceneY() / scaleTransform.getY()) - classBox.getLayoutY();
                event.consume();
            });
    
        classBox.getDragBox().setOnMouseDragged(event -> {
            double newX = (event.getSceneX() / scaleTransform.getX()) - offsetX;
            double newY = (event.getSceneY() / scaleTransform.getY()) - offsetY;
            
            classBox.setLayoutX(newX);
            classBox.setLayoutY(newY);
            data.getClassItems().get(classBox.getClassName().toLowerCase().trim()).setX(newX);
            data.getClassItems().get(classBox.getClassName().toLowerCase().trim()).setY(newY);

            classBox.setEffect(effectHelper());

            adjustAnchorPaneSize(newX, newY, classBox);
            updateRelationLines();
        });

        // setup ranchor events
        for (int i = 0; i < 4; i++) {
            int index = i;
            classBox.getRanchor(i).setOnMouseClicked(event -> {
                ranchorClick(event, classBox, index);
                event.consume();
            });
        }

        return classBox;
    }

    @FXML
    public void resetZoom(ActionEvent event) {
        event.consume();
        scaleTransform.setX(1.0);
        scaleTransform.setY(1.0);

        anchorPane.setScaleX(1.0);
        anchorPane.setScaleY(1.0);
        anchorPane.layout();

        // reset the scroll pane to the top-left corner
        // scrollPane.setVvalue(0); // Scroll to the top
        // scrollPane.setHvalue(0); // Scroll to the left
    }

    @FXML
    public void quitMenuClick(ActionEvent event) {
        System.exit(0);
    }

    private void updateRelationLines() {
        for (Node node : anchorPane.getChildren()) {
            if (node instanceof RelationLine) {
                RelationLine line = (RelationLine) node;
                line.Update(scaleTransform, partyMode);
            }
        }
    }

    private void mouseMove(MouseEvent event) {
        if (placingRelation != null)
        {
            placingRelation.Update(scaleTransform, event, partyMode);
        }
        else
        {
            for (Node node : anchorPane.getChildren()) {
                if (node instanceof RelationLine) {
                    RelationLine line = (RelationLine) node;
                    line.mouseMoved(event, scaleTransform);
                }
            }
        }
    }

    // mouse click on background event
    private void mouseClick(MouseEvent event)
    {
        if (placingRelation != null && event.getButton() == MouseButton.PRIMARY)
        {
            //user is dragging around a relation line and has clicked the background
            //add a new class to hook the relation line to.
            baseController.getCareTaker().saveState();
            baseController.getCareTaker().Lock();
            ClassBox classBox = onAddClassClicked();
            baseController.getCareTaker().Unlock();
            if (classBox == null) return;

            new Thread(() -> {
                try {
                    Thread.sleep(60);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                int i = (placingRelation.getI1() + 2) % 4;
                Rectangle r = classBox.getRanchor(i);
                double newX = event.getSceneX() - r.getBoundsInParent().getMinX();
                double newY = event.getSceneY() - r.getBoundsInParent().getMinY() - 35;
                classBox.setLayoutX(newX / scaleTransform.getX());
                classBox.setLayoutY(newY / scaleTransform.getY());

                placingRelation.setEnd(classBox, i);
                placingRelation.Update(scaleTransform, partyMode);
                baseController.getCareTaker().Lock();
                placingRelation.Save(); //update model
                baseController.getCareTaker().Unlock();
                placingRelation = null;
            }).start();
        } else if (placingRelation != null && event.getButton() != MouseButton.PRIMARY) {
            // user is dragging around a relation line and has rightclicked the background
            // stop placing the relation line. just delete it
            // need to find an alternative for our right mouse button challenged friends
            //anchorPane.getChildren().remove(placingRelation);
            placingRelation.Remove(true);
            placingRelation = null;
        } else if (placingRelation == null && event.getButton() == MouseButton.PRIMARY) {
            // user is not dragging around a relation line and has clicked the background
            // addclass
            // ClassBox classBox = onAddClassClicked();
            // if (classBox == null) return;
            // classBox.setLayoutX(event.getSceneX() / scaleTransform.getX());
            // classBox.setLayoutY(event.getSceneY() / scaleTransform.getY());

            
            //takeScreenshot();
        }

    }

    // mouse click on relation anchor event
    private void ranchorClick(MouseEvent event, ClassBox classBox, int index) {
        if (placingRelation == null) {
            RelationLine line = new RelationLine(baseController, anchorPane);
            line.setStart(classBox, index);

            line.Update(scaleTransform, event, partyMode);
            anchorPane.getChildren().add(line);
            placingRelation = line;
        } else {
            placingRelation.setEnd(classBox, index);
            placingRelation.Update(scaleTransform, partyMode);
            placingRelation.Save(); //update model
            placingRelation = null;
        }
    }

    private ClassBox getClassBoxByName(String className) {
        for (Node node : anchorPane.getChildren()) {
            if (node instanceof ClassBox) {
                ClassBox classBox = (ClassBox) node;
                if (classBox.getClassName().equals(className))
                    return classBox;
            }
        }
        return null;
    }

    private void adjustAnchorPaneSize(double newX, double newY, ClassBox classBox) {
        // Expand AnchorPane if object goes beyond current bounds
        if (newX < 0) {
            anchorPane.setPrefWidth(anchorPane.getPrefWidth() - newX);
            classBox.setLayoutX(0);
        }
        if (newY < 0) {
            anchorPane.setPrefHeight(anchorPane.getPrefHeight() - newY);
            classBox.setLayoutY(0);
        }

        if (newX + boxWidth > anchorPane.getPrefWidth()) {
            anchorPane.setPrefWidth(newX + boxWidth);
        }

        if (newY + boxHeight > anchorPane.getPrefHeight()) {
            anchorPane.setPrefHeight(newY + boxHeight);
        }
    }

    private void handleZoom(ScrollEvent event) {
        if (event.isControlDown()) {

            double deltaY = event.getDeltaY();

            if (deltaY < 0) {
                scaleTransform.setX(Math.max(minZoom, scaleTransform.getX() / zoomFactor));
                scaleTransform.setY(Math.max(minZoom, scaleTransform.getY() / zoomFactor));
            } else {
                scaleTransform.setX(Math.min(maxZoom, scaleTransform.getX() * zoomFactor));
                scaleTransform.setY(Math.min(maxZoom, scaleTransform.getY() * zoomFactor));
            }

            event.consume();
        }
    }
    // ================================================================================================================================================================
    // Method to handle the add class button

    public ClassBox onAddClassClicked() {
        // displays dialog box prompting for class name
        String className = ClassBox.classNameDialog();

        // Check if className is null or blank
        if (className == null || className.trim().isEmpty()) {
            System.out.println("Class name cannot be null or blank.");
            return null;
        }

        // gets the result of adding the class
        String result = baseController.AddClassListener(className);
        // parse result for either successful rename or failure
        if (result == "good")
        {
            //adds classbox to the view
            ClassBox classBox = addClass(className);
            classBox.Update();
            return classBox;
        } else {
            ClassBox.showError(result);
            return null;
        }
    }

    public void onDeleteButtonClicked(ClassBox classBox, String className) {
        // Pass className to method and attempt to delete
        String result = baseController.RemoveClassListener(className);
        // parse result for either successful rename or failure
        if (result == "good") {
            anchorPane.getChildren().remove(classBox);
        } else {
            ClassBox.showError(result);
        }
    }

    // when the undo button is clicked
    public void onUndoClicked() {
        String result = baseController.undoListener();
        if (result.equals("good"))
        {
            ClearAll();
            LoadAll();
        }
    }

    // when the redo button is clicked
    public void onRedoClicked() {
        String result = baseController.redoListener();
        if (result.equals("good"))
        {
            ClearAll();
            LoadAll();
        }
    }
    //TODO: change the color of 'file' and 'help' menus to white when dark mode is enabled
    // dark mode button
    @FXML
    public void onDarkModeClicked() {
        // toggle dark mode
        if(!darkMode){
            // remove light theme and add dark theme
            rootVBox.getStylesheets().remove(getClass().getResource("/css/classBoxStyle.css").toExternalForm());
            rootVBox.getStylesheets().add(getClass().getResource("/css/darktheme.css").toExternalForm());
            // add dark theme to the menu bar
            menuBar.getStyleClass().add("menubar-dark");
            // add dark theme to the file and help menus
            fileMenu.getStyleClass().add("menu-dark");
            helpMenu.getStyleClass().add("menu-dark");
            // add dark theme to the toolbar
            toolBar.getStyleClass().add("toolbar-dark");
            // add dark theme to the add class button
            addClassButton.getStyleClass().add("dark-theme-button");
            // change the images to dark mode
            undoImageView.setImage(new Image(getClass().getResourceAsStream("/images/undo-arrow-dark.png")));
            redoImageView.setImage(new Image(getClass().getResourceAsStream("/images/redo-arrow-dark.png")));
            screenshotImageView.setImage(new Image(getClass().getResourceAsStream("/images/snapshot-icon-dark.png")));
            darkModeImageView.setImage(new Image(getClass().getResourceAsStream("/images/dark-mode-toggle-dark.png")));
            lightModeImageView.setImage(new Image(getClass().getResourceAsStream("/images/light-mode-toggle-dark.png")));

            // change the party mode image to dark mode
            if(partyMode){
                partyModeImageView.setImage(new Image(getClass().getResourceAsStream("/images/party-mode-on.png")));
            } else {
            partyModeImageView.setImage(new Image(getClass().getResourceAsStream("/images/party-mode-toggle-dark.png")));
            }

            darkMode = true;
        }
    }

    // light mode button
    @FXML
    public void onLightModeClicked() {
        if(darkMode){
            // remove dark theme and add light theme
            rootVBox.getStylesheets().remove(getClass().getResource("/css/darktheme.css").toExternalForm());
            rootVBox.getStylesheets().add(getClass().getResource("/css/classBoxStyle.css").toExternalForm());
            // remove dark theme from the menu bar
            menuBar.getStyleClass().remove("menubar-dark");
            // remove dark theme from the file and help menus
            fileMenu.getStyleClass().remove("menu-dark");
            helpMenu.getStyleClass().remove("menu-dark");
            // remove dark theme from the toolbar
            toolBar.getStyleClass().remove("toolbar-dark");
            // remove dark theme from the add class button
            addClassButton.getStyleClass().remove("dark-theme-button");
            // change the images to light mode
            undoImageView.setImage(new Image(getClass().getResourceAsStream("/images/undo-arrow.png")));
            redoImageView.setImage(new Image(getClass().getResourceAsStream("/images/redo-arrow.png")));
            screenshotImageView.setImage(new Image(getClass().getResourceAsStream("/images/snapshot-icon.png")));
            darkModeImageView.setImage(new Image(getClass().getResourceAsStream("/images/dark-mode-toggle.png")));
            lightModeImageView.setImage(new Image(getClass().getResourceAsStream("/images/light-mode-toggle.png")));

            // change the party mode image to light mode
            if(partyMode){
                partyModeImageView.setImage(new Image(getClass().getResourceAsStream("/images/party-mode-on.png")));
            } else {
            partyModeImageView.setImage(new Image(getClass().getResourceAsStream("/images/party-mode-toggle.png")));
            }
            darkMode = false;
        }
    }

    @FXML
    public void onPartyModeClicked() {
        if(!partyMode){
            partyMode = true;
            partyModeImageView.setImage(new Image(getClass().getResourceAsStream("/images/party-mode-on.png")));
        } else {
            partyMode = false;
            if(darkMode){
                partyModeImageView.setImage(new Image(getClass().getResourceAsStream("/images/party-mode-toggle-dark.png")));
            } else {
                partyModeImageView.setImage(new Image(getClass().getResourceAsStream("/images/party-mode-toggle.png")));
            }
        }
    }

    public DropShadow effectHelper(){
        DropShadow shadow = new DropShadow();

        Color color = (darkMode) ? darkColor : lightColor;

        shadow.setOffsetX(5);
        shadow.setOffsetY(5);
        shadow.setRadius(10);

        if(partyMode){
            int max = 255;
            int min = 0;

            int r= (int)(Math.random() * (max - min + 1)) + min;
            int g = (int)(Math.random() * (max - min + 1)) + min;
            int b = (int)(Math.random() * (max - min + 1)) + min;

            color = Color.rgb(r, g, b);
        }

        shadow.setColor(color);

        return shadow;
    }

    @FXML
    public void onTakeScreenshot() {
        takeScreenshot();
    }

    private void takeScreenshot() {

        TextInputDialog dialog = new TextInputDialog("screenshot");
        dialog.setTitle("Save Screenshot");
        dialog.setHeaderText("Enter the name of the screenshot");
        dialog.setContentText("File Name:");

        Optional<String> result = dialog.showAndWait();
        if(result.isPresent()){
            String filename = result.get();
            if(!filename.endsWith(".png")){
                filename += ".png";
            }
    
            double width = anchorPane.getWidth();
            double height = anchorPane.getHeight();

            WritableImage image = new WritableImage((int) width, (int) height);
            anchorPane.snapshot(new SnapshotParameters(), image);
            File file = new File(filename);

            try {
                ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
