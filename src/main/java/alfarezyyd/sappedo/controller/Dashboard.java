package alfarezyyd.sappedo.controller;

import alfarezyyd.sappedo.AppConnection;
import alfarezyyd.sappedo.helper.CommonHelper;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class Dashboard {
  @FXML
  public HBox hboxProduct;
  @FXML
  public HBox hboxHome;
  @FXML
  private HBox hboxOrder;

  @FXML
  private Label dateNow;

  @FXML
  private Label totalProduct;

  @FXML
  private Label totalTransaction;

  @FXML
  private Label totalUser;

  @FXML
  private void routeHome(MouseEvent event) throws IOException {
    // Dapatkan stage saat ini dan sembunyikan
    Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    currentStage.hide();

    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("Market.fxml"));
    Scene scene = new Scene(fxmlLoader.load());
    currentStage.setTitle("Manajemen Produk!");
    currentStage.setScene(scene);
    currentStage.show();
  }

  @FXML
  private void routeProduct(MouseEvent event) throws IOException {
    // Dapatkan stage saat ini dan sembunyikan
    Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    currentStage.hide();

    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("Bicycle.fxml"));
    Scene scene = new Scene(fxmlLoader.load());
    currentStage.setTitle("Manajemen Produk!");
    currentStage.setScene(scene);
    currentStage.show();
  }

  @FXML
  private void routeOrder(MouseEvent event) throws IOException {
    // Dapatkan stage saat ini dan sembunyikan
    Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    currentStage.hide();

    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("Transaction.fxml"));
    Scene scene = new Scene(fxmlLoader.load());
    currentStage.setTitle("Manajemen Produk!");
    currentStage.setScene(scene);
    currentStage.show();
  }

  @FXML
  private void routeUser(MouseEvent event) throws IOException {
    // Dapatkan stage saat ini dan sembunyikan
    Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    currentStage.hide();

    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("User.fxml"));
    Scene scene = new Scene(fxmlLoader.load());
    currentStage.setTitle("Manajemen Produk!");
    currentStage.setScene(scene);
    currentStage.show();
  }

  @FXML
  public void initialize() {
    // Set tanggal saat ini
    dateNow.setText(LocalDate.now().toString());

    // Mengambil data total produk, total transaksi, dan total pengguna dari database
    int totalProducts = getTotalProducts();
    int totalTransactions = getTotalTransactions();
    int totalUsers = getTotalUsers();

    // Set data total produk, total transaksi, dan total pengguna
    totalProduct.setText(String.valueOf(totalProducts));
    totalTransaction.setText(String.valueOf(totalTransactions));
    totalUser.setText(String.valueOf(totalUsers));
  }

  // Metode untuk mendapatkan total produk dari database
  private int getTotalProducts() {
    try (Connection connection = AppConnection.getConnection()) {
      PreparedStatement preparedStatement = connection.prepareStatement("SELECT COUNT(*) FROM bicycles");
      ResultSet resultSet = preparedStatement.executeQuery();
      if (resultSet.next()) {
        return resultSet.getInt(1);
      }
      ;
    } catch (SQLException e) {
      CommonHelper.showAlert("Error", "Aplikasi mengalamai error tidak terduga", Alert.AlertType.ERROR);
      return 0;
    }
    return 0;
  }

  // Metode untuk mendapatkan total transaksi dari database
  private int getTotalTransactions() {
    try (Connection connection = AppConnection.getConnection()) {
      PreparedStatement preparedStatement = connection.prepareStatement("SELECT COUNT(*) FROM transactions");
      ResultSet resultSet = preparedStatement.executeQuery();
      if (resultSet.next()) {
        return resultSet.getInt(1);
      }
      ;
    } catch (SQLException e) {
      CommonHelper.showAlert("Error", "Aplikasi mengalamai error tidak terduga", Alert.AlertType.ERROR);
      return 0;
    }
    return 0; // Contoh nilai total transaksi
  }

  // Metode untuk mendapatkan total pengguna dari database
  private int getTotalUsers() {
    try (Connection connection = AppConnection.getConnection()) {
      PreparedStatement preparedStatement = connection.prepareStatement("SELECT COUNT(*) FROM users");
      ResultSet resultSet = preparedStatement.executeQuery();
      if (resultSet.next()) {
        System.out.println(resultSet.getInt(1));
        return resultSet.getInt(1);
      } else {
        return 0;
      }
    } catch (SQLException e) {
      CommonHelper.showAlert("Error", "Aplikasi mengalamai error tidak terduga", Alert.AlertType.ERROR);
      return 0;
    }
  }
}
