/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mavericksoft.qepi.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 *
 * @author lsegovia
 */
public class Client extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        //Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLDocument.fxml"));
        Parent root = loader.load();
        FXMLDocumentController controllerPrincipal = loader.getController();
        controllerPrincipal.setStagePrincipal(stage);
        
        Scene scene = new Scene(root);
        //Image applicationIcon = new Image(getClass().getResourceAsStream("/com/mavericksoft/qepi/recurso/qepi33.png"));
        Image applicationIcon = new Image(getClass().getResourceAsStream("qepi3333.png"));
        stage.getIcons().add(applicationIcon);      
        stage.setTitle("Qëpi");       
        stage.setScene(scene);
        stage.resizableProperty().setValue(Boolean.FALSE);
        //stage.initStyle(StageStyle.UTILITY);
        
        /*
        stage.initStyle(StageStyle.UTILITY);
        stage.initStyle(StageStyle.DECORATED);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.initStyle(StageStyle.UNIFIED);
        stage.initStyle(StageStyle.TRANSPARENT);
        */
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
