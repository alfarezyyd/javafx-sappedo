package alfarezyyd.sappedo.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;

public class Dashboard {
  @FXML
  public HBox hboxProduct;
  @FXML
  public HBox hboxHome;
  @FXML
  private HBox hboxOrder;

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
    Scene scene = new Scene(fxmlLoader. load());
    currentStage.setTitle("Manajemen Produk!");
    currentStage.setScene(scene);
    currentStage.show();
  }
}
