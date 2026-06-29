package com.qwertyfox;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;

public class FXMain extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Parent parent = FXMLLoader.load(getClass().getResource("/Frances.fxml"));
        stage.setTitle("Frances");
        stage.setScene(new Scene(parent, 350, 210));
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
