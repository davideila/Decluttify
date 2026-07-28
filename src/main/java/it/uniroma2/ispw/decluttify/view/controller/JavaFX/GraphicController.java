package it.uniroma2.ispw.decluttify.view.controller.JavaFX;

import it.uniroma2.ispw.decluttify.exception.DAOException;
import it.uniroma2.ispw.decluttify.exception.ModelException;
import it.uniroma2.ispw.decluttify.utils.AlertProvider;
import it.uniroma2.ispw.decluttify.utils.SessionManager;
import it.uniroma2.ispw.decluttify.view.controller.Navigator;
import it.uniroma2.ispw.decluttify.view.controller.ViewType;
import javafx.fxml.FXML;
import javafx.scene.Parent;

public abstract class GraphicController {
    protected boolean isInSidebar;
    protected Navigator navigator;
    protected SessionManager sessionManager;
    protected ViewType viewType;

    @FXML
    Parent rootView;

    public GraphicController(Navigator navigator, SessionManager sm, ViewType viewType) {
        this.navigator = navigator;
        this.sessionManager = sm;
        this.viewType = viewType;
        switch(viewType) {
            case OFFER_FORM -> this.isInSidebar = false;
            case MY_BARTERS ->  this.isInSidebar = true;
            case LOGIN ->  this.isInSidebar = false;
            case MY_OFFERS ->   this.isInSidebar = true;
            case ITEM_DETAILS ->   this.isInSidebar = false;
            case ITEM_BROWSER ->   this.isInSidebar = true;
            case null, default ->  this.isInSidebar = false; //exception
        }
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

    public ViewType getViewType() {
        return viewType;
    }
}


