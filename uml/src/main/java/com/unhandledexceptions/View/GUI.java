package com.unhandledexceptions.View;

import java.io.IOException;

import com.unhandledexceptions.Controller.BaseController;
import com.unhandledexceptions.Model.Data;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public final class GUI extends Application
{
    //unsure how we'll implement data yet. I believe it will be in each scenes controller.
    Data data;
    BaseController controller;
    //holds reference to the main stage (the GUI window)
    private static Stage stage;
    public GUI (){
        this.controller = new BaseController(data);
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
        setRoot("mainDiagram", "Unhandled Exceptions UML");

    }

    public static void main(){
        launch();
    }
}
