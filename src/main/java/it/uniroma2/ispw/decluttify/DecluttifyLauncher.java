package it.uniroma2.ispw.decluttify;

import it.uniroma2.ispw.decluttify.persistence.PersistenceManager;
import it.uniroma2.ispw.decluttify.utils.ConfigReader;
import it.uniroma2.ispw.decluttify.utils.SessionManager;
import it.uniroma2.ispw.decluttify.view.controller.JavaFX.JFXNavigatorManager;
import it.uniroma2.ispw.decluttify.view.controller.Navigator;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class DecluttifyLauncher extends Application {

    public static void main(String[] args){
        if(args.length > 0){
            switch(args[0]){
                case "CLI":
                    Navigator navigator = new it.uniroma2.ispw.decluttify.view.controller.CLI.NavigatorManager(new SessionManager());
                    navigator.start();
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
                    Navigator navigator = new it.uniroma2.ispw.decluttify.view.controller.CLI.NavigatorManager(new SessionManager());
                    navigator.start();
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
        JFXNavigatorManager navigatorManager = new JFXNavigatorManager(stage);
        navigatorManager.start();
    }

    @Override
    public void stop() {
        System.out.println("App closing.");
    }
}
