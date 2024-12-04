package com.unhandledexceptions.View;

import java.io.PrintStream;

import com.unhandledexceptions.Controller.BaseController;
import com.unhandledexceptions.Controller.mainDiagramController;
import com.unhandledexceptions.Model.Data;

import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.WritableImage;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class GUI extends Application
{
    private static BaseController baseController;
    private static boolean screenshotMode;
    // File name for screenshot
    private static String screenshotFileName;
    //holds reference to the main stage (the GUI window)
    private static Stage stage;

    // Save original streams
    private static final PrintStream originalOut = System.out;
    private static final PrintStream originalErr = System.err;

    public static void setBaseController(BaseController controller)
    {
        baseController = controller;
    }

    public static void setScreenshotMode(boolean value)
    {
        screenshotMode = value;
    }

    public static void setScreenshotFile(String fileName) {
        screenshotFileName = fileName;
    }

    @Override
    public void start(Stage startUp) throws Exception
    {
        stage = startUp;

        //load fxml
        FXMLLoader fxmlLoader = new FXMLLoader(GUI.class.getResource("/fxml/" + "mainDiagram" + ".fxml"));       
        Parent root = fxmlLoader.load();

        //inject a baseController manually if it has been set
        mainDiagramController controller = fxmlLoader.getController();
        if (baseController == null)
            controller.setBaseController(new BaseController(new Data()));
        else
            controller.setBaseController(baseController);
        
        //setup scene
        Scene scene = new Scene(root);
        stage.setTitle("Unhandled Exceptions UML");
        stage.setScene(scene);

        //if screenshotmode, run it silently, take the shot, exit
        if (screenshotMode)
        {
            stage.initStyle(StageStyle.UTILITY);
            stage.show();
            stage.setOpacity(0);
            controller.LoadAll();

            PauseTransition delay = new PauseTransition(Duration.millis(100));

            delay.setOnFinished(event -> {
                controller.screenshotFromCLI(screenshotFileName);

                Platform.exit();
        
                // Restore original streams
                System.setOut(originalOut);
                System.setErr(originalErr);
            });

            delay.play();
        }
        else
        {    
            stage.show();
            stage.setAlwaysOnTop(true);
            stage.setAlwaysOnTop(false);
            stage.requestFocus();
        }
    }

    public static void main()
    {
        if (screenshotMode)
        {
            // Suppress JavaFX console output if in screenshot mode
            System.setErr(new PrintStream(new NullOutputStream()));
            System.setOut(new PrintStream(new NullOutputStream()));
        }

        launch();
    }

    // Custom OutputStream to suppress output
    private static class NullOutputStream extends java.io.OutputStream {
        @Override
        public void write(int b) {
            // Do nothing
        }
    }
}
