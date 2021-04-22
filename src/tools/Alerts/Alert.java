package tools.Alerts;

import java.awt.*;

public class Alert {
    private Alert alertType;
    private String title;
    private String headerText;
    private String contenttext;

    public Alert(javafx.scene.control.Alert.AlertType alertType, String title, String headerText, String contenttext) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contenttext);
        Toolkit.getDefaultToolkit().beep();
        alert.showAndWait();
    }
}
