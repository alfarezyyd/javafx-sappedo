package alfarezyyd.sappedo.controller;

import alfarezyyd.sappedo.AppConnection;
import alfarezyyd.sappedo.helper.CommonHelper;
import alfarezyyd.sappedo.model.BicycleModel;
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
import java.sql.*;
import java.util.HashMap;
import java.util.Objects;

public class Bicycle {

  @FXML
  public TableView<BicycleModel> tableViewBicycle;
  @FXML
  public TableColumn<BicycleModel, Integer> columnId;
  @FXML
  public TableColumn<BicycleModel, String> columnName;
  @FXML
  public TableColumn<BicycleModel, Integer> columnPrice;
  @FXML
  public TableColumn<BicycleModel, Integer> columnStock;
  @FXML
  public TextField inputName;
  @FXML
  public TextField inputPrice;
  @FXML
  public TextField inputStock;
  private final ObservableList<BicycleModel> bicycleObservableList = FXCollections.observableArrayList();
  @FXML
  public ImageView imagePreview;
  @FXML
  public ColorPicker inputColor;
  private Integer bicycleId;
  private String imagePath;
  int counterInc = 1;

  @FXML
  public void initialize() {
    columnId.setCellValueFactory(new PropertyValueFactory<>("id"));
    columnName.setCellValueFactory(new PropertyValueFactory<>("name"));
    columnPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
    columnStock.setCellValueFactory(new PropertyValueFactory<>("stock"));
    // Menambahkan event handler untuk setiap kali baris diklik
    tableViewBicycle.setOnMouseClicked(event -> {
      // Dapatkan item yang dipilih dari TableView
      BicycleModel selectedBicycle = tableViewBicycle.getSelectionModel().getSelectedItem();

      // Pastikan item yang dipilih tidak null
      if (selectedBicycle != null) {
        // Isi TextField dengan nilai-nilai dari item yang dipilih
        inputName.setText(selectedBicycle.getName());
        inputPrice.setText(String.valueOf(selectedBicycle.getPrice()));
        inputStock.setText(String.valueOf(selectedBicycle.getStock()));
        bicycleId = selectedBicycle.getModelId();
        File file = new File(String.valueOf(selectedBicycle.getImagePath()));
        Image image = new Image(file.toURI().toString());
        imagePreview.setImage(image);
      }
    });


    tableViewBicycle.setItems(bicycleObservableList);
    loadDataFromDatabase();
  }

  private void loadDataFromDatabase() {
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
        BicycleModel bicycle = new BicycleModel(counterInc, name, price, stock, id, imagePath, null);
        bicycleObservableList.add(bicycle);
        counterInc++;
      }

    } catch (SQLException e) {
      CommonHelper.showAlert("Error", "Failed to load data from database: " + e.getMessage(), Alert.AlertType.ERROR);
    }
  }

  @FXML
  public void handleSubmitBicycle() {
    String name = inputName.getText().trim();
    String priceText = inputPrice.getText().trim();
    String stockText = inputStock.getText().trim();
    String colorPicker = inputColor.getValue().toString();
    if (imagePath == null) {
      CommonHelper.showAlert("Error", "Tolong upload gambar terlebih dahulu", Alert.AlertType.ERROR);
    }
    if (name.isEmpty() || priceText.isEmpty() || stockText.isEmpty()) {
      CommonHelper.showAlert("Error", "Semua kolom wajib terisi", Alert.AlertType.ERROR);
      return;
    }
    int price, stock;
    try (Connection connection = AppConnection.getConnection()) {
      price = Integer.parseInt(priceText);
      stock = Integer.parseInt(stockText);
      PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO bicycles (name, price, stock, image_path, bg_color) VALUES (?,?,?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
      preparedStatement.setString(1, name);
      preparedStatement.setInt(2, price);
      preparedStatement.setInt(3, stock);
      preparedStatement.setString(4, imagePath);
      preparedStatement.setString(5, colorPicker);
      int i = preparedStatement.executeUpdate();
      ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
      if (i > 0) {
        CommonHelper.showAlert("Success", "Data sepeda berhasil ditambahkan", Alert.AlertType.INFORMATION);
        if (generatedKeys.next()) {
          bicycleId = generatedKeys.getInt(1);
        }
        // Tambahkan data baru ke dalam ObservableList
        BicycleModel newBicycle = new BicycleModel(counterInc++, name, price, stock, bicycleId, imagePath, null);
        bicycleObservableList.add(newBicycle);

        // Perbarui TableView
        tableViewBicycle.refresh();
      } else {
        CommonHelper.showAlert("Error", "Data sepeda gagal ditambahkan", Alert.AlertType.ERROR);
      }
    } catch (NumberFormatException e) {
      CommonHelper.showAlert("Error", "Harga dan Stock harus angka.", Alert.AlertType.ERROR);
      return;
    } catch (SQLException e) {
      CommonHelper.showAlert("Error", "Aplikasi mengalami error tidak terduga", Alert.AlertType.ERROR);

    }
  }

  public void handleUpdateBicycle() {
    String name = inputName.getText().trim();
    String priceText = inputPrice.getText().trim();
    String stockText = inputStock.getText().trim();
    String colorPicker = inputColor.getValue().toString();
    if (name.isEmpty() || priceText.isEmpty() || stockText.isEmpty()) {
      CommonHelper.showAlert("Error", "Semua kolom wajib terisi", Alert.AlertType.ERROR);
      return;
    }
    int price, stock;
    try (Connection connection = AppConnection.getConnection()) {
      if (imagePath == null) {
        CommonHelper.showAlert("Error", "Tolong upload gambar terlebih dahulu", Alert.AlertType.ERROR);
      }
      price = Integer.parseInt(priceText);
      stock = Integer.parseInt(stockText);
      PreparedStatement preparedStatement = connection.prepareStatement("UPDATE bicycles SET name = ?, price = ?, stock = ?, image_path = ?, bg_color = ? WHERE id = ?");
      preparedStatement.setString(1, name);
      preparedStatement.setInt(2, price);
      preparedStatement.setInt(3, stock);
      preparedStatement.setString(4, imagePath);
      preparedStatement.setInt(5, bicycleId);
      preparedStatement.setString(6, colorPicker);
      int i = preparedStatement.executeUpdate();
      if (i > 0) {
        CommonHelper.showAlert("Success", "Data sepeda berhasil diupdate", Alert.AlertType.INFORMATION);
        BicycleModel updatedBicycle = new BicycleModel(counterInc++, name, price, stock, bicycleId, imagePath, null);

        for (int index = 0; index < bicycleObservableList.size(); index++) {
          if (bicycleObservableList.get(index).getId().equals(bicycleId)) {
            // Perbarui item di ObservableList
            bicycleObservableList.set(index, updatedBicycle);
            break;
          }
        }
        // Perbarui TableView
        tableViewBicycle.refresh();
      } else {
        CommonHelper.showAlert("Error", "Data sepeda gagal diupdate", Alert.AlertType.ERROR);
      }
    } catch (NumberFormatException e) {
      CommonHelper.showAlert("Error", "Harga dan Stock harus angka.", Alert.AlertType.ERROR);
      return;
    } catch (SQLException e) {
      CommonHelper.showAlert("Error", "Aplikasi mengalami error tidak terduga", Alert.AlertType.ERROR);

    }
  }

  public void handleDeleteBicycle() {
    try (Connection connection = AppConnection.getConnection()) {
      PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM bicycles WHERE id = ?");
      preparedStatement.setInt(1, bicycleId);
      if (bicycleId == null) {
        CommonHelper.showAlert("Error", "Pilih data terlebih dahulu", Alert.AlertType.ERROR);
        return;
      }
      int i = preparedStatement.executeUpdate();
      if (i > 0) {
        CommonHelper.showAlert("Success", "Data sepeda berhasil dihapus", Alert.AlertType.INFORMATION);

        // Dapatkan indeks item yang akan dihapus
        int index = -1;
        for (int j = 0; j < bicycleObservableList.size(); j++) {
          if (Objects.equals(bicycleObservableList.get(j).getModelId(), bicycleId)) {
            index = j;
            break;
          }
        }

        if (index != -1) {
          // Hapus item dari ObservableList
          bicycleObservableList.remove(index);

          // Perbarui TableView
          tableViewBicycle.refresh();
        }
      } else {
        CommonHelper.showAlert("Error", "Data sepeda gagal dihapus", Alert.AlertType.ERROR);
      }
    } catch (SQLException e) {
      CommonHelper.showAlert("Error", "Aplikasi mengalami error tidak terduga", Alert.AlertType.ERROR);

    }


  }


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

  public void handleUploadImage(ActionEvent actionEvent) {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Open Image File");

    // Filter file untuk hanya menampilkan file gambar
    FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif");
    fileChooser.getExtensionFilters().add(extFilter);

    // Buka dialog untuk memilih file
    Stage stage = (Stage) imagePreview.getScene().getWindow();
    File file = fileChooser.showOpenDialog(stage);

    if (file != null) {
      // Jika file dipilih, buat objek Image dan atur ke ImageView
      Image image = new Image(file.toURI().toString());
      imagePreview.setImage(image);
      this.imagePath = file.getAbsolutePath();
    }
  }

  public void handlePrintAction(ActionEvent actionEvent) {
    JasperPrint jasperPrint;
    try {
      jasperPrint = JasperFillManager.fillReport("report/BicycleReport.jasper",
          new HashMap<>(),
          AppConnection.getConnection());
      JasperViewer jasperViewer = new JasperViewer(jasperPrint, false);
      jasperViewer.setTitle("Laporan Data Sepeda");
      jasperViewer.setVisible(true);
    } catch (JRException | SQLException e) {
      CommonHelper.showAlert("Error", "Aplikasi mengalami error: " + e.getMessage(), Alert.AlertType.ERROR);
    }
  }
}
