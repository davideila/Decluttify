module it.uniroma2.ispw.decluttify {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires java.sql;
    requires javafx.graphics;
    requires javafx.base;
    requires password4j;

    opens it.uniroma2.ispw.decluttify to javafx.fxml;
    exports it.uniroma2.ispw.decluttify;
    exports it.uniroma2.ispw.decluttify.utils;
    opens it.uniroma2.ispw.decluttify.utils to javafx.fxml;
    exports it.uniroma2.ispw.decluttify.patterns.State to javafx.fxml, javafx.graphics;
    opens it.uniroma2.ispw.decluttify.patterns.State to javafx.fxml, javafx.graphics;
    exports it.uniroma2.ispw.decluttify.patterns.State.Offer to javafx.fxml, javafx.graphics;
    opens it.uniroma2.ispw.decluttify.patterns.State.Offer to javafx.fxml, javafx.graphics;
    exports it.uniroma2.ispw.decluttify.patterns.Observer to javafx.fxml, javafx.graphics;
    opens it.uniroma2.ispw.decluttify.patterns.Observer to javafx.fxml, javafx.graphics;
    exports it.uniroma2.ispw.decluttify.patterns.State.Offer.states to javafx.fxml, javafx.graphics;
    opens it.uniroma2.ispw.decluttify.patterns.State.Offer.states to javafx.fxml, javafx.graphics;
    opens it.uniroma2.ispw.decluttify.bean to javafx.base, javafx.fxml;
    exports it.uniroma2.ispw.decluttify.view.controller.JavaFX to javafx.fxml, javafx.graphics;
    opens it.uniroma2.ispw.decluttify.view.controller.JavaFX to javafx.fxml, javafx.graphics;
}