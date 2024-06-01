package alfarezyyd.sappedo.controller;

import alfarezyyd.sappedo.EventListener;
import alfarezyyd.sappedo.Launcher;
import alfarezyyd.sappedo.model.BicycleModel;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.io.File;

public class Item {
  @FXML
  private Label bicycleNameLabel;

  @FXML
  private Label bicyclePriceLabel;

  @FXML
  private ImageView imageView;

  @FXML
  private void click(MouseEvent mouseEvent) {
    eventListener.onClickListener(bicycleModel);
  }

  private BicycleModel bicycleModel;
  private EventListener eventListener;

  public void setData(BicycleModel bicycleModel, EventListener eventListener) {
    this.bicycleModel = bicycleModel;
    this.eventListener = eventListener;
    bicycleNameLabel.setText(bicycleModel.getName());
    bicyclePriceLabel.setText(Launcher.CURRENCY + bicycleModel.getPrice());
    File file = new File(String.valueOf(bicycleModel.getImagePath()));
    imageView.setImage(new Image(file.toURI().toString()));
  }
}