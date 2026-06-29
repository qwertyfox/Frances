package com.qwertyfox.controller;

import com.qwertyfox.print.FinalizeDoc;
import com.qwertyfox.print.WriteToDoc;
import com.qwertyfox.processing.ReadSheet2;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.io.File;

public class Controller {

    @FXML
    public VBox vBoxId;
    @FXML
    public Button buttonId;

    public void locateXLSWorkSheet() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Sales Report");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel file","*.xls"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel file","*.xls"));

        File file = fileChooser.showOpenDialog(vBoxId.getScene().getWindow());
        ReadSheet2 readSheet2 = new ReadSheet2();
        readSheet2.readSheet(file.getAbsolutePath());
        WriteToDoc.getInstance().createTable();

        FinalizeDoc finalizeDoc = new FinalizeDoc();
        finalizeDoc.finalizeDoc();

    }


}
