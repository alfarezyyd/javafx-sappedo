package alfarezyyd.sappedo.controller;

import alfarezyyd.sappedo.AppConnection;
import alfarezyyd.sappedo.helper.CommonHelper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Login {

  public PasswordField passwordInput;
  public TextField usernameInput;

  public void handleLogin(ActionEvent actionEvent) {
    try (Connection connection = AppConnection.getConnection()) {
      PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM users WHERE username = ? AND password = ?");
      preparedStatement.setString(1, usernameInput.getText());
      preparedStatement.setString(2, passwordInput.getText());
      ResultSet resultSet = preparedStatement.executeQuery();
      if (resultSet.next()) {
        Stage loginStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        loginStage.hide();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("MenuBar.fxml"));
        Stage bicycleStage = new Stage();
        bicycleStage.setTitle("Menu Bar");
        Scene scene = new Scene(fxmlLoader.load());
        bicycleStage.setScene(scene);
        bicycleStage.show();
      } else {
        CommonHelper.showAlert("Login Gagal", "Silahkan Cek Username dan Password Anda", Alert.AlertType.ERROR);
      }
    } catch (SQLException | IOException e) {
      throw new RuntimeException(e);
    }

  }
}
