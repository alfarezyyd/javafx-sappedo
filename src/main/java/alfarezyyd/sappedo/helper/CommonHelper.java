package alfarezyyd.sappedo.helper;

import javafx.scene.control.Alert;

public class CommonHelper {
  public static void showAlert(String title, String message, Alert.AlertType alertType) {
    Alert alert = new Alert(alertType);
    alert.setTitle(title);
    alert.setContentText(message);
    alert.showAndWait();
  }
}
