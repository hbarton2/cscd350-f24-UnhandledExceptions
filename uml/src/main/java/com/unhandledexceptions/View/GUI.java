package com.unhandledexceptions.View;

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

public class GUI
{
    Data data = new Data();

    public GUI(Data data)
    {
        this.data = data;
    }

    public void Run()
    {

    }
}
