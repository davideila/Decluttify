package it.uniroma2.ispw.decluttify.view.controller.JavaFX;

import it.uniroma2.ispw.decluttify.utils.AlertProvider;
import it.uniroma2.ispw.decluttify.utils.SessionManager;
import it.uniroma2.ispw.decluttify.view.controller.Navigator;
import javafx.scene.Parent;

public abstract class JFXGraphicController {
    protected Navigator navigator;
    protected SessionManager sessionManager;
    protected JFXViewType viewType;
    public Parent rootView;

    public JFXGraphicController(Navigator navigator, SessionManager sm, JFXViewType viewType) {
        this.navigator = navigator;
        this.sessionManager = sm;
        this.viewType = viewType;
    }

    public abstract void init();

    // Get the view from the controller
    public Parent getView() {
        return rootView;
    }

    public void setView(Parent view){
        rootView = view;
    }

    protected void handleErrorAlert(String header, Exception e) {
        AlertProvider.showError(header, e.getMessage());
        e.printStackTrace();
    }

    protected void handleSuccessAlert(String content){
        AlertProvider.showInfo("Success", content);
    }

    protected void handleUnexpectedErrorAlert() {
        AlertProvider.showError("System failure", "An unexpected error occurred.");
    }

    public JFXViewType getViewType() {
        return viewType;
    }
}


