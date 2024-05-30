package alfarezyyd.sappedo.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class MenuBar {

  @FXML
  public void routeTransaction(MouseEvent mouseEvent) throws IOException {
    Stage loginStage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
    loginStage.hide();
    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("Transaction.fxml"));
    Stage bicycleStage = new Stage();
    bicycleStage.setTitle("Semua Transaksi");
    Scene scene = new Scene(fxmlLoader.load());
    bicycleStage.setScene(scene);
    bicycleStage.show();
  }

  public void routeBicycle(MouseEvent mouseEvent) throws IOException {
    Stage loginStage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
    loginStage.hide();
    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("Bicycle.fxml"));
    Stage bicycleStage = new Stage();
    bicycleStage.setTitle("Semua Sepeda");
    Scene scene = new Scene(fxmlLoader.load());
    bicycleStage.setScene(scene);
    bicycleStage.show();
  }
}
