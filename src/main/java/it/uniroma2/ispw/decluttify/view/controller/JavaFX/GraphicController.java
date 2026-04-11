package it.uniroma2.ispw.decluttify.view.controller.JavaFX;

import it.uniroma2.ispw.decluttify.exception.DAOException;
import it.uniroma2.ispw.decluttify.exception.ModelException;
import it.uniroma2.ispw.decluttify.utils.AlertProvider;
import javafx.scene.Parent;

public abstract class GraphicController {
    Parent view;
    boolean isInSidebar;

    public void setView(Parent view) {
        this.view = view;
    }

    // Get the view from the controller
    public Parent getView() {
        return view;
    }

    public void setInSidebar(boolean isInSidebar) {
        this.isInSidebar = isInSidebar;
    }

    public boolean isInSidebar() {
        return isInSidebar;
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
}


