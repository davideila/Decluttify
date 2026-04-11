package it.uniroma2.ispw.decluttify;

import it.uniroma2.ispw.decluttify.persistence.PersistenceManager;
import it.uniroma2.ispw.decluttify.utils.ConfigReader;
import it.uniroma2.ispw.decluttify.view.controller.CLI.NavigatorManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class DecluttifyLauncher extends Application {
    private static final double RESIZE_CONSTANT = 0.75;

    public static void main(String[] args){
        if(args.length > 0){
            switch(args[0]){
                case "CLI":
                    NavigatorManager.getInstance().start();
                    break;
                case "GUI":
                    Application.launch(args);
                    break;
                default:
                    Application.launch(args);
            }
        }
        else {
            String viewType = ConfigReader.getInstance().getViewType();
            switch (viewType) {
                case "CLI":
                    NavigatorManager.getInstance().start();
                    break;
                case "GUI":
                    Application.launch(args);
                    break;
                default:
                    Application.launch(args);
            }
        }
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/uniroma2/ispw/decluttify/views/MainView.fxml"));
        Parent root = loader.load();
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        double screenWidth = screenBounds.getWidth();
        double screenHeight = screenBounds.getHeight();
        double sceneWidth = screenWidth * RESIZE_CONSTANT;
        double sceneHeight = screenHeight * RESIZE_CONSTANT;
        stage.setResizable(false);
        Scene scene = new Scene(root, sceneWidth, sceneHeight);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop() {
        //Clean connection to DB
        PersistenceManager.getInstance().closeConnection();
        System.out.println("App closing: Connection closed.");
    }
}
