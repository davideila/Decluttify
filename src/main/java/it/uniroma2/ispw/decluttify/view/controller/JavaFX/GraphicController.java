package it.uniroma2.ispw.decluttify.view.controller.JavaFX;

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
}


