package it.uniroma2.ispw.decluttify.view.controller.JavaFX;

import javafx.fxml.Initializable;
import java.net.URL;
import java.util.ResourceBundle;

public class MyInventoryController extends GraphicController implements Initializable {
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.isInSidebar = true;
    }
}
