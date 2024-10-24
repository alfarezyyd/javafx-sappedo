package alfarezyyd.sappedo.controller;

import alfarezyyd.sappedo.AppConnection;
import alfarezyyd.sappedo.helper.CommonHelper;
import alfarezyyd.sappedo.model.BicycleModel;
import alfarezyyd.sappedo.model.TransactionModel;
import javafx.beans.property.SimpleStringProperty;
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
import javafx.stage.Stage;
import javafx.util.StringConverter;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Objects;

public class Transaction {

  @FXML
  public TableView<TransactionModel> tableViewTransaction;
  @FXML
  public TableColumn<TransactionModel, String> columnDate;
  @FXML
  public TableColumn<TransactionModel, String> columnName;
  @FXML
  public TableColumn<TransactionModel, String> columnProduct;
  @FXML
  public TableColumn<TransactionModel, Integer> columnQuantity;
  @FXML
  public TableColumn<TransactionModel, Integer> columnTotalPrice;
  @FXML
  public TableColumn<TransactionModel, Integer> columnPayment;
  @FXML
  public TextField inputName;
  @FXML
  public TextField inputQuantity;
  @FXML
  public TextField inputTotalPrice;
  @FXML
  public TextField inputPayment;
  @FXML
  public DatePicker inputDate;
  @FXML
  public ComboBox<BicycleModel> comboBoxProduct;


  private final ObservableList<BicycleModel> bicycleObservableList = FXCollections.observableArrayList();
  private final ObservableList<TransactionModel> transactionObservableList = FXCollections.observableArrayList();
  @FXML
  public ImageView imagePreview;
  private Integer transactionId;
  int counterInc = 1;

  @FXML
  public void initialize() {
    columnDate.setCellValueFactory(new PropertyValueFactory<>("date"));
    columnName.setCellValueFactory(new PropertyValueFactory<>("name"));
    columnProduct.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getBicycleModel().getName()));
    columnQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
    columnTotalPrice.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
    columnPayment.setCellValueFactory(new PropertyValueFactory<>("payment"));

    inputDate.setValue(LocalDate.now());
    tableViewTransaction.setItems(transactionObservableList);
    comboBoxProduct.setItems(bicycleObservableList);

    // Menambahkan event handler untuk setiap kali baris diklik
    tableViewTransaction.setOnMouseClicked(event -> {
      // Dapatkan item yang dipilih dari TableView
      TransactionModel selectedTransaction = tableViewTransaction.getSelectionModel().getSelectedItem();

      // Pastikan item yang dipilih tidak null
      if (selectedTransaction != null) {
        // Isi TextField dengan nilai-nilai dari item yang dipilih
        inputName.setText(selectedTransaction.getName());
        transactionId = selectedTransaction.getId();
        inputQuantity.setText(String.valueOf(selectedTransaction.getQuantity()));
        inputPayment.setText(String.valueOf(selectedTransaction.getPayment()));
        inputTotalPrice.setText(String.valueOf(selectedTransaction.getTotalPrice()));
        comboBoxProduct.setValue(selectedTransaction.getBicycleModel());
        File file = new File(String.valueOf(selectedTransaction.getBicycleModel().getImagePath()));
        imagePreview.setImage(new Image(file.toURI().toString()));
      }
    });

    loadBicyclesFromDatabase();

    // Mengatur cell factory dan button cell untuk comboBoxProduct
    comboBoxProduct.setCellFactory((comboBox) -> new ListCell<BicycleModel>() {
      @Override
      protected void updateItem(BicycleModel item, boolean empty) {
        super.updateItem(item, empty);
        if (item == null || empty) {
          setText(null);
        } else {
          setText(item.getName());
        }
      }
    });

    comboBoxProduct.setButtonCell(new ListCell<>() {
      @Override
      protected void updateItem(BicycleModel item, boolean empty) {
        super.updateItem(item, empty);
        if (item == null || empty) {
          setText(null);
        } else {
          setText(item.getName());
        }
      }
    });

    // Listener untuk mendeteksi perubahan nilai
    comboBoxProduct.valueProperty().addListener((observable, oldValue, newValue) -> {
      File file = new File(String.valueOf(newValue.getImagePath()));
      imagePreview.setImage(new Image(file.toURI().toString()));

    });

    comboBoxProduct.setConverter(new StringConverter<>() {
      @Override
      public String toString(BicycleModel object) {
        return object != null ? object.getName() : "";
      }

      @Override
      public BicycleModel fromString(String string) {
        return null; // Not needed for this implementation
      }
    });

    // Listener untuk perubahan quantity
    inputQuantity.textProperty().addListener((observable, oldValue, newValue) -> updateTotalPrice());

    loadTransactionsFromDatabase();
  }

  // Metode untuk memperbarui total harga
  private void updateTotalPrice() {
    try {
      BicycleModel selectedBicycle = comboBoxProduct.getValue();
      if (selectedBicycle != null) {
        int price = selectedBicycle.getPrice();
        int quantity = Integer.parseInt(inputQuantity.getText());
        int totalPrice = price * quantity;
        inputTotalPrice.setText(String.valueOf(totalPrice));
      }
    } catch (NumberFormatException e) {
      // Jika quantity bukan angka yang valid, kosongkan totalPrice
      inputTotalPrice.clear();
    }
  }

  private void loadBicyclesFromDatabase() {
    try (Connection connection = AppConnection.getConnection()) {
      String querySql = "SELECT * FROM bicycles";
      PreparedStatement preparedStatement = connection.prepareStatement(querySql);
      ResultSet resultSet = preparedStatement.executeQuery();

      while (resultSet.next()) {
        int id = resultSet.getInt("id");
        String name = resultSet.getString("name");
        int price = resultSet.getInt("price");
        int stock = resultSet.getInt("stock");
        String imagePath = resultSet.getString("image_path");
        BicycleModel bicycle = new BicycleModel(id, name, price, stock, id, imagePath, null);
        bicycleObservableList.add(bicycle);
      }

    } catch (SQLException e) {
      CommonHelper.showAlert("Error", "Failed to load data from database: " + e.getMessage(), Alert.AlertType.ERROR);
    }
  }

  @FXML
  private void loadTransactionsFromDatabase() {
    try (Connection connection = AppConnection.getConnection()) {
      String query = """
             SELECT t.id, t.date, t.name, t.quantity, t.total_price, t.payment,\s
          b.id AS bicycleId, b.name AS bicycleName, b.price AS bicyclePrice, b.stock AS bicycleStock, b.image_path AS bicycleImagePath, b.bg_color AS bicycleBackgroundColor \s
                       FROM transactions t JOIN bicycles b ON t.bicycle_id = b.id""";
      PreparedStatement preparedStatement = connection.prepareStatement(query);
      ResultSet resultSet = preparedStatement.executeQuery();

      while (resultSet.next()) {
        Integer id = resultSet.getInt("id");
        String date = String.valueOf(resultSet.getDate("date"));
        String name = resultSet.getString("name");
        int quantity = resultSet.getInt("quantity");
        int totalPrice = resultSet.getInt("total_price");
        int payment = resultSet.getInt("payment");

        int bicycleId = resultSet.getInt("bicycleId");
        String bicycleName = resultSet.getString("bicycleName");
        int bicyclePrice = resultSet.getInt("bicyclePrice");
        int bicycleStock = resultSet.getInt("bicycleStock");
        String bicycleImagePath = resultSet.getString("bicycleImagePath");
        String bicycleBackgroundColor = resultSet.getString("bicycleBackgroundColor");

        BicycleModel bicycle = new BicycleModel(bicycleId, bicycleName, bicyclePrice, bicycleStock, bicycleId, bicycleImagePath, bicycleBackgroundColor);
        TransactionModel transaction = new TransactionModel(id, date, name, bicycle, quantity, totalPrice, payment);
        transactionObservableList.add(transaction);
      }

    } catch (SQLException e) {
      CommonHelper.showAlert("Error", "Failed to load data from database: " + e.getMessage(), Alert.AlertType.ERROR);
    }
  }

  @FXML
  public void handleSubmitTransaction() {
    String name = inputName.getText().trim();
    BicycleModel selectedBicycle = comboBoxProduct.getValue();
    String quantityText = inputQuantity.getText().trim();
    String totalPriceText = inputTotalPrice.getText().trim();
    String paymentText = inputPayment.getText().trim();
    String dateText = inputDate.getValue().toString().trim();

    if (name.isEmpty() || selectedBicycle.getName().isEmpty() || quantityText.isEmpty() || totalPriceText.isEmpty() || paymentText.isEmpty()) {
      CommonHelper.showAlert("Error", "Semua kolom wajib terisi", Alert.AlertType.ERROR);
      return;
    }

    int quantity, totalPrice, payment;
    try (Connection connection = AppConnection.getConnection()) {
      quantity = Integer.parseInt(quantityText);
      totalPrice = Integer.parseInt(totalPriceText);
      payment = Integer.parseInt(paymentText);
      if (totalPrice > payment) {
        CommonHelper.showAlert("Error", "Pembayaran kurang dari total harga", Alert.AlertType.ERROR);
        return;
      }
      PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO transactions (date, name, bicycle_id, quantity, total_price, payment) VALUES (?,?,?,?,?,?)");
      preparedStatement.setDate(1, Date.valueOf(dateText));
      preparedStatement.setString(2, name);
      preparedStatement.setInt(3, selectedBicycle.getId());
      preparedStatement.setInt(4, quantity);
      preparedStatement.setInt(5, totalPrice);
      preparedStatement.setInt(6, payment);
      int i = preparedStatement.executeUpdate();
      if (i > 0) {
        PreparedStatement bicyclePreparedStatement = connection.prepareStatement("UPDATE bicycles SET stock = ? WHERE id = ?");
        bicyclePreparedStatement.setInt(1, selectedBicycle.getStock() - 1);
        bicyclePreparedStatement.setInt(2, selectedBicycle.getId());
        int j = bicyclePreparedStatement.executeUpdate();
        if (j > 0) {
          CommonHelper.showAlert("Success", "Transaksi berhasil ditambahkan", Alert.AlertType.INFORMATION);
          TransactionModel newTransaction = new TransactionModel(counterInc++, dateText, name, selectedBicycle, quantity, totalPrice, payment);
          transactionObservableList.add(newTransaction);
          tableViewTransaction.refresh();
        }
      } else {
        CommonHelper.showAlert("Error", "Transaksi gagal ditambahkan", Alert.AlertType.ERROR);
      }
    } catch (NumberFormatException e) {
      CommonHelper.showAlert("Error", "Quantity, Total Price, dan Payment harus angka.", Alert.AlertType.ERROR);
    } catch (SQLException e) {
      CommonHelper.showAlert("Error", "Aplikasi mengalami error tidak terduga", Alert.AlertType.ERROR);
    }
  }

  public void handleUpdateTransaction() {
    String name = inputName.getText().trim();
    BicycleModel selectedBicycle = comboBoxProduct.getValue();
    String quantityText = inputQuantity.getText().trim();
    String totalPriceText = inputTotalPrice.getText().trim();
    String paymentText = inputPayment.getText().trim();
    String dateText = inputDate.toString().trim();

    if (name.isEmpty() || quantityText.isEmpty() || totalPriceText.isEmpty() || paymentText.isEmpty()) {
      CommonHelper.showAlert("Error", "Semua kolom wajib terisi", Alert.AlertType.ERROR);
      return;
    }

    int quantity, totalPrice, payment;
    try (Connection connection = AppConnection.getConnection()) {
      quantity = Integer.parseInt(quantityText);
      totalPrice = Integer.parseInt(totalPriceText);
      payment = Integer.parseInt(paymentText);
      if (totalPrice > payment) {
        CommonHelper.showAlert("Error", "Pembayaran kurang dari total harga", Alert.AlertType.ERROR);
        return;
      }
      PreparedStatement preparedStatement = connection.prepareStatement("UPDATE transactions SET  name = ?, bicycle_id = ?, quantity = ?, total_price = ?, payment = ? WHERE id = ?");
      preparedStatement.setString(1, name);
      preparedStatement.setInt(2, selectedBicycle.getId());
      preparedStatement.setInt(3, quantity);
      preparedStatement.setInt(4, totalPrice);
      preparedStatement.setInt(5, payment);
      preparedStatement.setInt(6, transactionId);
      int i = preparedStatement.executeUpdate();
      if (i > 0) {
        CommonHelper.showAlert("Success", "Transaksi berhasil diupdate", Alert.AlertType.INFORMATION);

        TransactionModel updatedTransaction = new TransactionModel(transactionId, dateText, name, selectedBicycle, quantity, totalPrice, payment);
        // Cari item berdasarkan ID transaksi
        for (int index = 0; index < transactionObservableList.size(); index++) {
          if (transactionObservableList.get(index).getId().equals(transactionId)) {
            // Perbarui item di ObservableList
            transactionObservableList.set(index, updatedTransaction);
            break;
          }
        }
        // Perbarui TableView
        tableViewTransaction.refresh();
      } else {
        CommonHelper.showAlert("Error", "Transaksi gagal diupdate", Alert.AlertType.ERROR);
      }
    } catch (NumberFormatException e) {
      CommonHelper.showAlert("Error", "Quantity, Total Price, dan Payment harus angka.", Alert.AlertType.ERROR);
    } catch (SQLException e) {
      CommonHelper.showAlert("Error", "Aplikasi mengalami error tidak terduga", Alert.AlertType.ERROR);
    }
  }

  public void handleDeleteTransaction() {
    if (transactionId == null) {
      CommonHelper.showAlert("Error", "Pilih data terlebih dahulu", Alert.AlertType.ERROR);
      return;
    }

    try (Connection connection = AppConnection.getConnection()) {
      PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM transactions WHERE id = ?");
      preparedStatement.setInt(1, transactionId);
      int i = preparedStatement.executeUpdate();
      if (i > 0) {
        CommonHelper.showAlert("Success", "Transaksi berhasil dihapus", Alert.AlertType.INFORMATION);

        // Dapatkan indeks item yang akan dihapus
        int index = -1;
        for (int j = 0; j < transactionObservableList.size(); j++) {
          if (Objects.equals(transactionObservableList.get(j).getId(), transactionId)) {
            index = j;
            break;
          }
        }

        if (index != -1) {
          // Hapus item dari ObservableList
          transactionObservableList.remove(index);

          // Perbarui TableView
          tableViewTransaction.refresh();
        }
      } else {
        CommonHelper.showAlert("Error", "Transaksi gagal dihapus", Alert.AlertType.ERROR);
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

  public void handlePrintAction(ActionEvent actionEvent) {
    JasperPrint jasperPrint;
    try {
      jasperPrint = JasperFillManager.fillReport("report/TransactionReport.jasper",
          new HashMap<>(),
          AppConnection.getConnection());
      JasperViewer jasperViewer = new JasperViewer(jasperPrint, false);
      jasperViewer.setTitle("Laporan Data Pengguna");
      jasperViewer.setVisible(true);
    } catch (JRException | SQLException e) {
      CommonHelper.showAlert("Error", "Aplikasi mengalami error: " + e.getMessage(), Alert.AlertType.ERROR);
    }
  }
}
