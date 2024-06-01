package alfarezyyd.sappedo.controller;

import alfarezyyd.sappedo.AppConnection;
import alfarezyyd.sappedo.helper.CommonHelper;
import alfarezyyd.sappedo.model.UserModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;

public class User {
  @FXML
  private TableView<UserModel> tableViewTransaction;
  @FXML
  private TableColumn<UserModel, Integer> columnId;
  @FXML
  private TableColumn<UserModel, String> columnUsername;
  @FXML
  private TableColumn<UserModel, String> columnFullName;
  @FXML
  private TextField inputUsername;
  @FXML
  private TextField inputFullName;
  @FXML
  private PasswordField inputPassword;
  @FXML
  private PasswordField inputConfirmationPassword;
  @FXML
  private ImageView imagePreview;

  private final ObservableList<UserModel> userObservableList = FXCollections.observableArrayList();
  private Integer userId;
  private String userAvatar;

  @FXML
  public void initialize() {
    columnId.setCellValueFactory(new PropertyValueFactory<>("id"));
    columnUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
    columnFullName.setCellValueFactory(new PropertyValueFactory<>("fullName"));

    tableViewTransaction.setItems(userObservableList);

    tableViewTransaction.setOnMouseClicked(event -> {
      UserModel selectedUser = tableViewTransaction.getSelectionModel().getSelectedItem();
      if (selectedUser != null) {
        inputUsername.setText(selectedUser.getUsername());
        inputFullName.setText(selectedUser.getFullName());
        inputPassword.setText(selectedUser.getPassword());
        inputConfirmationPassword.setText(selectedUser.getPassword());
        userId = selectedUser.getId();
        userAvatar = selectedUser.getAvatar();
        imagePreview.setImage(new Image(selectedUser.getAvatar()));
      }
    });

    loadUsersFromDatabase();
  }

  private void loadUsersFromDatabase() {
    try (Connection connection = AppConnection.getConnection()) {
      String query = "SELECT * FROM users";
      PreparedStatement preparedStatement = connection.prepareStatement(query);
      ResultSet resultSet = preparedStatement.executeQuery();

      while (resultSet.next()) {
        int id = resultSet.getInt("id");
        String username = resultSet.getString("username");
        String fullName = resultSet.getString("full_name");
        String password = resultSet.getString("password");
        String avatar = resultSet.getString("avatar");

        UserModel user = new UserModel(id, username, fullName, password, avatar);
        userObservableList.add(user);
      }
    } catch (SQLException e) {
      CommonHelper.showAlert("Error", "Aplikasi mengalami error tidak terduga", Alert.AlertType.ERROR);
    }
  }

  @FXML
  public void handleSubmitTransaction() {
    String username = inputUsername.getText().trim();
    String fullName = inputFullName.getText().trim();
    String password = inputPassword.getText().trim();
    String confirmationPassword = inputConfirmationPassword.getText().trim();

    if (username.isEmpty() || fullName.isEmpty() || password.isEmpty() || confirmationPassword.isEmpty() || userAvatar == null) {
      CommonHelper.showAlert("Error", "Semua kolom harus terisi", Alert.AlertType.ERROR);
      return;
    }

    if (!password.equals(confirmationPassword)) {
      CommonHelper.showAlert("Error", "Password dan konfirmasi password harus sama", Alert.AlertType.ERROR);
      return;
    }


    byte[] hashedPassword = null;
    try {
      SecureRandom random = new SecureRandom();
      byte[] salt = new byte[16];
      random.nextBytes(salt);
      MessageDigest messageDigest = MessageDigest.getInstance("SHA-512");
      messageDigest.update(salt);
      hashedPassword = messageDigest.digest(password.getBytes(StandardCharsets.UTF_8));

    } catch (NoSuchAlgorithmException e) {
      CommonHelper.showAlert("Error", "Terjadi error yang tidak terduga", Alert.AlertType.ERROR);
    }
    try (Connection connection = AppConnection.getConnection()) {
      PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO users (username, full_name, password, avatar) VALUES (?, ?, ?, ?)");
      preparedStatement.setString(1, username);
      preparedStatement.setString(2, fullName);
      preparedStatement.setString(3, Arrays.toString(hashedPassword));
      preparedStatement.setString(4, userAvatar);
      int i = preparedStatement.executeUpdate();
      if (i > 0) {
        CommonHelper.showAlert("Success", "Pengguna berhasil ditambahkan", Alert.AlertType.INFORMATION);
        UserModel newUser = new UserModel(null, username, fullName, password, userAvatar);
        userObservableList.add(newUser);
        tableViewTransaction.refresh();
      } else {
        CommonHelper.showAlert("Error", "Pengguna gagal ditambahkan", Alert.AlertType.ERROR);
      }
    } catch (SQLException e) {
      CommonHelper.showAlert("Error", "Aplikasi mengalami error tidak terduga", Alert.AlertType.ERROR);
    }
  }

  @FXML
  public void handleUpdateTransaction() {
    String username = inputUsername.getText().trim();
    String fullName = inputFullName.getText().trim();
    String password = inputPassword.getText().trim();
    String confirmationPassword = inputConfirmationPassword.getText().trim();

    if (username.isEmpty() || fullName.isEmpty() || password.isEmpty() || confirmationPassword.isEmpty() || userAvatar == null) {
      CommonHelper.showAlert("Error", "Semua kolom harus terisi", Alert.AlertType.ERROR);
      return;
    }

    if (!password.equals(confirmationPassword)) {
      CommonHelper.showAlert("Error", "Password dan konfirmasi password harus sama", Alert.AlertType.ERROR);
      return;
    }

    try (Connection connection = AppConnection.getConnection()) {
      PreparedStatement preparedStatement = connection.prepareStatement("UPDATE users SET username = ?, full_name = ?, password = ?, avatar = ? WHERE id = ?");
      preparedStatement.setString(1, username);
      preparedStatement.setString(2, fullName);
      preparedStatement.setString(3, password);
      preparedStatement.setString(4, userAvatar);
      preparedStatement.setInt(5, userId);
      int i = preparedStatement.executeUpdate();
      if (i > 0) {
        CommonHelper.showAlert("Success", "Pengguna berhasil diperbarui", Alert.AlertType.INFORMATION);
        UserModel updatedUser = new UserModel(userId, username, fullName, password, userAvatar);
        for (int index = 0; index < userObservableList.size(); index++) {
          if (userObservableList.get(index).getId().equals(userId)) {
            userObservableList.set(index, updatedUser);
            break;
          }
        }
        tableViewTransaction.refresh();
      } else {
        CommonHelper.showAlert("Error", "Gagal memperbarui pengguna", Alert.AlertType.ERROR);
      }
    } catch (SQLException e) {
      CommonHelper.showAlert("Error", "Aplikasi mengalami error yang tidak terduga", Alert.AlertType.ERROR);
    }
  }

  @FXML
  public void handleDeleteTransaction() {
    if (userId == null) {
      CommonHelper.showAlert("Error", "Pilih pengguna terlebih dahulu", Alert.AlertType.ERROR);
      return;
    }

    try (Connection connection = AppConnection.getConnection()) {
      PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM users WHERE id = ?");
      preparedStatement.setInt(1, userId);
      int i = preparedStatement.executeUpdate();
      if (i > 0) {
        CommonHelper.showAlert("Success", "Pengguna berhasil dihapus", Alert.AlertType.INFORMATION);
        int index = -1;
        for (int j = 0; j < userObservableList.size(); j++) {
          if (userObservableList.get(j).getId().equals(userId)) {
            index = j;
            break;
          }
        }

        if (index != -1) {
          userObservableList.remove(index);
          tableViewTransaction.refresh();
        }
      } else {
        CommonHelper.showAlert("Error", "Gagal menghapus pengguna", Alert.AlertType.ERROR);
      }
    } catch (SQLException e) {
      CommonHelper.showAlert("Error", "Aplikasi mengalami error tidak terduga", Alert.AlertType.ERROR);
    }
  }

  @FXML
  public void handleUploadImage() {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Upload Image");
    File file = fileChooser.showOpenDialog(null);

    if (file != null) {
      userAvatar = file.toURI().toString();
      imagePreview.setImage(new Image(userAvatar));
    }
  }

  @FXML
  public void handleBackIntoMenu(ActionEvent actionEvent) throws IOException {
    Stage loginStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
    loginStage.hide();
    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("Dashboard.fxml"));
    Stage bicycleStage = new Stage();
    bicycleStage.setTitle("Dashboard");
    Scene scene = new Scene(fxmlLoader.load());
    bicycleStage.setScene(scene);
    bicycleStage.show();
  }

  public void handlePrintAction(ActionEvent actionEvent) {
    JasperPrint jasperPrint;
    try {
      jasperPrint = JasperFillManager.fillReport("report/UserReport.jasper",
          new HashMap<>(),
          AppConnection.getConnection());
      JasperViewer jasperViewer = new JasperViewer(jasperPrint, true);
      jasperViewer.setTitle("Laporan Data Pengguna");
      jasperViewer.setVisible(true);
    } catch (JRException | SQLException e) {
      CommonHelper.showAlert("Error", "Aplikasi mengalami error: " + e.getMessage(), Alert.AlertType.ERROR);
    }
  }
}
