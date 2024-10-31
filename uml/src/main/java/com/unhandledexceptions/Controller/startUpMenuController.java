package com.unhandledexceptions.Controller;

import java.io.IOException;

import com.unhandledexceptions.View.GUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class startUpMenuController {
    
    @FXML
    private Button btnOpenFile;

    @FXML
    private Button btnNew;

    @FXML
    public void btnOpenFile(){
        
    }

    @FXML
    public void btnNew(){
        try {
            GUI.setRoot("newDiagram");
        } catch (IOException e) { 
            e.printStackTrace();
        }
    }

    @FXML
    public void quit(ActionEvent event){
        System.exit(0);
    }
    
}
