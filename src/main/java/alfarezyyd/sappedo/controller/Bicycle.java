package alfarezyyd.sappedo.controller;

import alfarezyyd.sappedo.AppConnection;
import alfarezyyd.sappedo.helper.CommonHelper;
import alfarezyyd.sappedo.model.BicycleModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
  private Integer bicycleId;
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
        bicycleId = selectedBicycle.getId();
      }
    });


    tableViewBicycle.setItems(bicycleObservableList);
    loadDataFromDatabase();
  }

  private void loadDataFromDatabase() {
    try (Connection connection = AppConnection.getConnection()) {
      String query = "SELECT * FROM bicycles";
      PreparedStatement preparedStatement = connection.prepareStatement(query);
      ResultSet resultSet = preparedStatement.executeQuery();

      while (resultSet.next()) {
        String name = resultSet.getString("name");
        int price = resultSet.getInt("price");
        int stock = resultSet.getInt("stock");

        BicycleModel bicycle = new BicycleModel(counterInc, name, price, stock);
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
    if (name.isEmpty() || priceText.isEmpty() || stockText.isEmpty()) {
      CommonHelper.showAlert("Error", "Semua kolom wajib terisi", Alert.AlertType.ERROR);
      return;
    }
    int price, stock;
    try (Connection connection = AppConnection.getConnection()) {
      price = Integer.parseInt(priceText);
      stock = Integer.parseInt(stockText);
      PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO bicycles (name, price, stock) VALUES (?,?,?)");
      preparedStatement.setString(1, name);
      preparedStatement.setInt(2, price);
      preparedStatement.setInt(3, stock);
      int i = preparedStatement.executeUpdate();
      if (i > 0) {
        CommonHelper.showAlert("Success", "Data sepeda berhasil ditambahkan", Alert.AlertType.INFORMATION);
        // Tambahkan data baru ke dalam ObservableList
        BicycleModel newBicycle = new BicycleModel(counterInc++, name, price, stock);
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
    if (name.isEmpty() || priceText.isEmpty() || stockText.isEmpty()) {
      CommonHelper.showAlert("Error", "Semua kolom wajib terisi", Alert.AlertType.ERROR);
      return;
    }
    int price, stock;
    try (Connection connection = AppConnection.getConnection()) {
      price = Integer.parseInt(priceText);
      stock = Integer.parseInt(stockText);
      PreparedStatement preparedStatement = connection.prepareStatement("UPDATE bicycles SET name = ?, price = ?, stock = ? WHERE id = ?");
      preparedStatement.setString(1, name);
      preparedStatement.setInt(2, price);
      preparedStatement.setInt(3, stock);
      preparedStatement.setInt(4, bicycleId);
      int i = preparedStatement.executeUpdate();
      if (i > 0) {
        CommonHelper.showAlert("Success", "Data sepeda berhasil diupdate", Alert.AlertType.INFORMATION);
        BicycleModel updatedBicycle = new BicycleModel(bicycleId, name, price, stock);

        // Dapatkan indeks item yang akan diperbarui
        int index = bicycleObservableList.indexOf(updatedBicycle);

        // Hapus item lama dari ObservableList
        bicycleObservableList.remove(index);

        // Tambahkan item yang diperbarui kembali ke dalam ObservableList pada indeks yang sama
        bicycleObservableList.add(index, updatedBicycle);

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
      System.out.println(bicycleId);
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
          if (Objects.equals(bicycleObservableList.get(j).getId(), bicycleId)) {
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

}
