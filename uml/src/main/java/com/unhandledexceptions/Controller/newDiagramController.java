package com.unhandledexceptions.Controller;

import java.io.IOException;

import com.unhandledexceptions.View.GUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class newDiagramController {
    
    @FXML
    private Button btnOpenFile;

    @FXML
    private Button btnNew;

    @FXML
    public void addClass(){
        
    }



    @FXML
    public void quit(ActionEvent event){
        System.exit(0);
    }
    
}
