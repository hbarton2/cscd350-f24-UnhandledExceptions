package com.unhandledexceptions.View;

import java.io.IOException;

import com.unhandledexceptions.Main;
import com.unhandledexceptions.Model.Data;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class GUI extends Application
{
    //unsure how we'll implement data yet. I believe it will be in each scenes controller.
    Data data;
    //holds reference to the main stage (the GUI window)
    private static Stage stage;
    public GUI (){
    }

    public static void setRoot(String fxml) throws IOException{
        setRoot(fxml, stage.getTitle());
    }

    static void setRoot(String fxml, String title) throws IOException{
        Scene scene = new Scene(loadFXML(fxml));
        stage.setTitle(title);
        stage.setScene(scene);
        stage.show();
        stage.setAlwaysOnTop(true);
        stage.setAlwaysOnTop(false);
        stage.requestFocus();
    }

    private static Parent loadFXML (String fxml) throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(GUI.class.getResource("/fxml/" + fxml + ".fxml"));       
        Parent root = fxmlLoader.load();
        return root;
    }
    @Override
    public void start(Stage startUp) throws Exception {
        stage = startUp;
        setRoot("startUpMenu", "Unhandled Exceptions UML");

    }

    public static void main(){
        launch();
    }
}
