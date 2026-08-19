package it.uniroma2.ispw.decluttify;

import it.uniroma2.ispw.decluttify.utils.ConfigReader;
import it.uniroma2.ispw.decluttify.utils.SessionManager;
import it.uniroma2.ispw.decluttify.view.controller.CLI.CLINavigatorManager;
import it.uniroma2.ispw.decluttify.view.controller.JavaFX.JFXNavigatorManager;
import it.uniroma2.ispw.decluttify.view.controller.Navigator;
import javafx.application.Application;
import javafx.stage.Stage;

public class DecluttifyLauncher extends Application {

    public static void main(String[] args){
        String viewType = (args.length > 0) ? args[0] : ConfigReader.getInstance().getViewType();
        switch (viewType.toUpperCase()) {
            case "CLI":
                SessionManager sessionManager = new SessionManager();
                Navigator navigator = new CLINavigatorManager(sessionManager);
                navigator.start();
                break;
            case "GUI":
                Application.launch(args);
                break;
            default:
                Application.launch(args);
        }
    }

    @Override
    public void start(Stage stage) throws Exception {
        SessionManager sessionManager = new SessionManager();
        JFXNavigatorManager navigatorManager = new JFXNavigatorManager(stage, sessionManager);
        navigatorManager.start();
    }

    @Override
    public void stop() {
        System.out.println("App closing.");
    }
}
