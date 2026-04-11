package it.uniroma2.ispw.decluttify.utils;

import javafx.scene.control.Alert;

public class AlertProvider {

    private AlertProvider() {} // non-instantiability with a private constructor

    public static void showInfo(String header, String content) {
        displayAlert(Alert.AlertType.INFORMATION, "Info", header, content);
    }

    public static void showError(String header, String content) {
        displayAlert(Alert.AlertType.ERROR, "Error", header, content);
    }

    private static void displayAlert(Alert.AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

}