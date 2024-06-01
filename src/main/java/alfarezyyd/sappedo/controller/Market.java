package alfarezyyd.sappedo.controller;


import alfarezyyd.sappedo.AppConnection;
import alfarezyyd.sappedo.EventListener;
import alfarezyyd.sappedo.Launcher;
import alfarezyyd.sappedo.helper.CommonHelper;
import alfarezyyd.sappedo.model.BicycleModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class Market implements Initializable {
  @FXML
  private VBox chosenBicycleCard;

  @FXML
  private Label bicycleNameLabel;

  @FXML
  private Label bicyclePriceLabel;

  @FXML
  private ImageView bicycleImg;

  @FXML
  private ScrollPane scroll;

  @FXML
  private GridPane grid;

  private Image bicycleImage;
  private EventListener eventListener;
  private final ObservableList<BicycleModel> bicycleObservableList = FXCollections.observableArrayList();

  private void initializeBicycleData() {
    try (Connection connection = AppConnection.getConnection()) {
      String querySql = "SELECT * FROM bicycles";
      PreparedStatement preparedStatement = connection.prepareStatement(querySql);
      ResultSet resultSet = preparedStatement.executeQuery();

      while (resultSet.next()) {
        String name = resultSet.getString("name");
        int price = resultSet.getInt("price");
        int stock = resultSet.getInt("stock");
        int id = resultSet.getInt("id");
        String imagePath = resultSet.getString("image_path");
        BicycleModel bicycleModel = new BicycleModel(id, name, price, stock, id, imagePath);
        bicycleObservableList.add(bicycleModel);
      }
    } catch (SQLException e) {
      CommonHelper.showAlert("Error", "Failed to load data from database: " + e.getMessage(), Alert.AlertType.ERROR);
    }
  }

  private void setChosenBicycle(BicycleModel bicycleModel) {
    bicycleNameLabel.setText(bicycleModel.getName());
    bicyclePriceLabel.setText(Launcher.CURRENCY + bicycleModel.getPrice());
    File file = new File(String.valueOf(bicycleModel.getImagePath()));
    bicycleImage = new Image(file.toURI().toString());
    bicycleImg.setImage(bicycleImage);
    chosenBicycleCard.setStyle("-fx-background-color: #" + "FB5D03" + ";\n" +
        "    -fx-background-radius: 30;");
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    initializeBicycleData();
    if (!bicycleObservableList.isEmpty()) {
      setChosenBicycle(bicycleObservableList.getFirst());
      eventListener = this::setChosenBicycle;
    }
    int column = 0;
    int row = 1;
    try {
      for (BicycleModel bicycleModel : bicycleObservableList) {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getClassLoader().getResource("Item.fxml"));
        AnchorPane anchorPane = fxmlLoader.load();

        Item itemController = fxmlLoader.getController();
        itemController.setData(bicycleModel, eventListener);

        if (column == 3) {
          column = 0;
          row++;
        }

        grid.add(anchorPane, column++, row); //(child,column,row)
        //set grid width
        grid.setMinWidth(Region.USE_COMPUTED_SIZE);
        grid.setPrefWidth(Region.USE_COMPUTED_SIZE);
        grid.setMaxWidth(Region.USE_PREF_SIZE);

        //set grid height
        grid.setMinHeight(Region.USE_COMPUTED_SIZE);
        grid.setPrefHeight(Region.USE_COMPUTED_SIZE);
        grid.setMaxHeight(Region.USE_PREF_SIZE);

        GridPane.setMargin(anchorPane, new Insets(10));
      }
    } catch (IOException e) {
      CommonHelper.showAlert("Error", "Failed to load data from database: " + e.getMessage(), Alert.AlertType.ERROR);
    }
  }

  public void backIntoMenu(MouseEvent mouseEvent) throws IOException {
    Stage loginStage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
    loginStage.hide();
    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("Dashboard.fxml"));
    Stage bicycleStage = new Stage();
    bicycleStage.setTitle("Dashboard");
    Scene scene = new Scene(fxmlLoader.load());
    bicycleStage.setScene(scene);
    bicycleStage.show();
  }
}