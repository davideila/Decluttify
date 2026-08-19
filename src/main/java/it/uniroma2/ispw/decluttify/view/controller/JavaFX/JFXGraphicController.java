package it.uniroma2.ispw.decluttify.view.controller.JavaFX;

import it.uniroma2.ispw.decluttify.exception.DAOException;
import it.uniroma2.ispw.decluttify.exception.ModelException;
import it.uniroma2.ispw.decluttify.utils.AlertProvider;
import it.uniroma2.ispw.decluttify.utils.SessionManager;
import it.uniroma2.ispw.decluttify.view.controller.Navigator;
import javafx.fxml.FXML;
import javafx.scene.Parent;

public abstract class JFXGraphicController {
    protected boolean isInSidebar;
    protected Navigator navigator;
    protected SessionManager sessionManager;
    protected JFXViewType viewType;

    @FXML
    Parent rootView;

    public JFXGraphicController(Navigator navigator, SessionManager sm, JFXViewType viewType) {
        this.navigator = navigator;
        this.sessionManager = sm;
        this.viewType = viewType;
    }

    public void init(){};

    // Get the view from the controller
    public Parent getView() {
        return rootView;
    }

    public void setInSidebar(boolean isInSidebar) {
        this.isInSidebar = isInSidebar;
    }

    protected void handleException(Exception e) {
        if (e instanceof DAOException) {
            AlertProvider.showError("System error", "Service not available. Please try again later.");
        } else if (e instanceof ModelException) {
            AlertProvider.showError("Invalid request", e.getMessage());
        } else {
            AlertProvider.showError("Unexpected error", e.getMessage());
        }
        e.printStackTrace();
    }

    public JFXViewType getViewType() {
        return viewType;
    }
}


