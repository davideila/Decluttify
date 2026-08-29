package it.uniroma2.ispw.decluttify;

import it.uniroma2.ispw.decluttify.exception.DecluttifyException;
import it.uniroma2.ispw.decluttify.utils.ConfigReader;
import it.uniroma2.ispw.decluttify.utils.SessionManager;
import it.uniroma2.ispw.decluttify.view.controller.cli.CLINavigatorManager;
import it.uniroma2.ispw.decluttify.view.controller.jfx.JFXNavigatorManager;
import it.uniroma2.ispw.decluttify.view.controller.Navigator;
import javafx.application.Application;
import javafx.stage.Stage;

public class DecluttifyLauncher extends Application {

    public static void main(String[] args){
        try {
            String viewType = (args.length > 0) ? args[0] : ConfigReader.getInstance().getViewType();
            switch (viewType.toUpperCase().trim()) {
                case "cli":
                    SessionManager sessionManager = new SessionManager();
                    Navigator navigator = new CLINavigatorManager(sessionManager);
                    navigator.start();
                    break;
                case "JFX":
                    Application.launch(args);
                    break;
                default:
                    Application.launch(args);
            }
        } catch (DecluttifyException e) {
            System.err.println("Fatal error - Cannot start application: " + e.getMessage());
            System.exit(1);
        } catch (Exception e) {
            System.err.println("Unexpected error at application start: " + e.getMessage());
            System.exit(1);
        }
    }

    @Override
    public void start(Stage stage) throws Exception {
        try {
            SessionManager sessionManager = new SessionManager();
            JFXNavigatorManager navigatorManager = new JFXNavigatorManager(stage, sessionManager);
            navigatorManager.start();
        } catch (Exception e) {
            System.out.println("Cannot initialize JFX UI: "+ e.getMessage());
        }
    }

    @Override
    public void stop() {
        System.out.println("App closing.");
    }
}
