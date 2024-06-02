package alfarezyyd.sappedo.controller;

import alfarezyyd.sappedo.AppConnection;
import alfarezyyd.sappedo.Launcher;
import alfarezyyd.sappedo.helper.CommonHelper;
import alfarezyyd.sappedo.model.BicycleModel;
import alfarezyyd.sappedo.model.LoggedUserModel;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Dashboard {
  @FXML
  public HBox hboxProduct;
  @FXML
  public HBox hboxHome;
  @FXML
  public Label soldProduct;
  @FXML
  public Label revenueLabel;
  @FXML
  public Label soldProductMonth;
  @FXML
  public Label revenueLabelMonth;
  @FXML
  public ImageView avatarUser;
  @FXML
  public Label nameLabel;
  @FXML
  public HBox logoutButton;
  @FXML
  public HBox hboxOrder;
  @FXML
  public HBox hboxUser;

  @FXML
  private Label dateNow;

  @FXML
  private Label totalProduct;

  @FXML
  private Label totalTransaction;

  @FXML
  private Label totalUser;


  @FXML
  private ImageView firstProductImage;
  @FXML
  private Label firstProductName;
  @FXML
  private Label firstProductPrice;
  @FXML
  private Label firstProductTotalOrder;

  @FXML
  private ImageView secondProductImage;
  @FXML
  private Label secondProductName;
  @FXML
  private Label secondProductPrice;
  @FXML
  private Label secondProductTotalOrder;

  @FXML
  private ImageView thirdProductImage;
  @FXML
  private Label thirdProductName;
  @FXML
  private Label thirdProductPrice;
  @FXML
  private Label thirdProductTotalOrder;

  @FXML
  private ImageView fourthProductImage;
  @FXML
  private Label fourthProductName;
  @FXML
  private Label fourthProductPrice;
  @FXML
  private Label fourthProductTotalOrder;

  @FXML
  private ImageView fifthProductImage;
  @FXML
  private Label fifthProductName;
  @FXML
  private Label fifthProductPrice;
  @FXML
  private Label fifthProductTotalOrder;

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

  private void setProductInfo(ImageView imageView, Label nameLabel, Label priceLabel, BicycleModel bicycleModel) {
    File file = new File(String.valueOf(bicycleModel.getImagePath()));
    Image image = new Image(file.toURI().toString());

    imageView.setImage(image);
    imageView.setFitWidth(130);
    imageView.setFitHeight(130);
    nameLabel.setText(bicycleModel.getName());
    priceLabel.setText(String.valueOf(bicycleModel.getPrice()));
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

    soldProduct.setText(Launcher.CURRENCY + " " + calculateProductsSold(false));
    revenueLabel.setText(Launcher.CURRENCY + " " + calculateRevenue(true));
    soldProductMonth.setText(Launcher.CURRENCY + " " + calculateProductsSold(false));
    revenueLabelMonth.setText(Launcher.CURRENCY + " " + calculateRevenue(true));

    // Ambil 5 produk terbaik dari database
    List<BicycleModel> topProducts = getTopProductsFromDatabase();

    if (!topProducts.isEmpty()) {
      setProductInfo(firstProductImage, firstProductName, firstProductPrice, topProducts.getFirst());
    }
    if (topProducts.size() > 1) {
      setProductInfo(secondProductImage, secondProductName, secondProductPrice, topProducts.get(1));
    }
    if (topProducts.size() > 2) {
      setProductInfo(thirdProductImage, thirdProductName, thirdProductPrice, topProducts.get(2));
    }
    if (topProducts.size() > 3) {
      setProductInfo(fourthProductImage, fourthProductName, fourthProductPrice, topProducts.get(3));
    }
    if (topProducts.size() > 4) {
      setProductInfo(fifthProductImage, fifthProductName, fifthProductPrice, topProducts.get(4));
    }

    // Set data total produk, total transaksi, dan total pengguna
    totalProduct.setText(String.valueOf(totalProducts));
    totalTransaction.setText(String.valueOf(totalTransactions));
    totalUser.setText(String.valueOf(totalUsers));
    LoggedUserModel loggedUserModel = LoggedUserModel.getInstance();

    if (!loggedUserModel.isAdmin()) {
      hboxProduct.setVisible(false);
      hboxOrder.setVisible(false);
      hboxUser.setVisible(false);
    }
    nameLabel.setText("Halo, " + loggedUserModel.getFullName());
    File file = new File(String.valueOf(loggedUserModel.getAvatar()));
    Image image = new Image(file.toURI().toString());
    avatarUser.setImage(image);

  }

  // Metode untuk mendapatkan total produk dari database
  private int getTotalProducts() {
    try (Connection connection = AppConnection.getConnection()) {
      PreparedStatement preparedStatement = connection.prepareStatement("SELECT COUNT(*) FROM bicycles");
      ResultSet resultSet = preparedStatement.executeQuery();
      if (resultSet.next()) {
        return resultSet.getInt(1);
      }

    } catch (SQLException e) {
      CommonHelper.showAlert("Error", "Aplikasi mengalamai error tidak terduga", Alert.AlertType.ERROR);
      return 0;
    }
    return 0;
  }

  private List<BicycleModel> getTopProductsFromDatabase() {
    List<BicycleModel> topProducts = new ArrayList<>();
    try (Connection connection = AppConnection.getConnection()) {
      PreparedStatement preparedStatement = connection.prepareStatement("""
           SELECT\s
               b.id,
               b.name,
               b.price,
               b.stock,
               b.image_path,
               SUM(t.quantity) AS total_sold
           FROM\s
               bicycles b
           JOIN\s
               transactions t ON b.id = t.bicycle_id
           GROUP BY\s
               b.id, b.name, b.price, b.stock, b.image_path
           ORDER BY\s
               total_sold DESC
           LIMIT 5;
          \s""");
      ResultSet resultSet = preparedStatement.executeQuery();
      if (resultSet.next()) {
        String name = resultSet.getString("name");
        int price = resultSet.getInt("price");
        int stock = resultSet.getInt("stock");
        int id = resultSet.getInt("id");
        String imagePath = resultSet.getString("image_path");

        BicycleModel bicycleModel = new BicycleModel(null, name, price, stock, id, imagePath, null);
        topProducts.add(bicycleModel);
      }
    } catch (SQLException e) {
      CommonHelper.showAlert("Error", "Aplikasi mengalamai error tidak terduga", Alert.AlertType.ERROR);
    }
    return topProducts;
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
        return resultSet.getInt(1);
      } else {
        return 0;
      }
    } catch (SQLException e) {
      CommonHelper.showAlert("Error", "Aplikasi mengalamai error tidak terduga", Alert.AlertType.ERROR);
      return 0;
    }
  }

  private int calculateProductsSold(boolean isMonth) {
    int productsSold = 0;
    String querySql = null;
    LocalDate localDate = LocalDate.now();
    String format = null;
    // Jika hanya ingin menghitung per hari
    if (!isMonth) {
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
      format = localDate.format(formatter);
      querySql = "SELECT SUM(quantity) AS total_sold FROM transactions WHERE date = ?";
    }
    // Jika ingin menghitung per bulan
    else {
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
      format = localDate.format(formatter);
      querySql = "SELECT SUM(quantity) AS total_sold FROM transactions WHERE DATE_FORMAT(date, '%Y-%m') = ?";
    }

    try (Connection connection = AppConnection.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(querySql)) {

      preparedStatement.setString(1, format);
      ResultSet resultSet = preparedStatement.executeQuery();

      if (resultSet.next()) {
        productsSold = resultSet.getInt("total_sold");
      }
    } catch (SQLException e) {
      CommonHelper.showAlert("Error", "Aplikasi mengalamai error tidak terduga", Alert.AlertType.ERROR);
    }

    return productsSold;
  }

  private int calculateRevenue(boolean isMonth) {
    int revenueToday = 0;
    String querySql = null;
    LocalDate localDate = LocalDate.now();
    String format = null;
    // Jika hanya ingin menghitung per hari
    if (!isMonth) {
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
      format = localDate.format(formatter);
      querySql = "SELECT SUM(total_price) AS total_price FROM transactions WHERE date = ?";
    }
    // Jika ingin menghitung per bulan
    else {
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
      format = localDate.format(formatter);
      querySql = "SELECT SUM(total_price) AS total_price FROM transactions WHERE DATE_FORMAT(date, '%Y-%m') = ?";
    }


    try (Connection connection = AppConnection.getConnection()) {
      PreparedStatement preparedStatement = connection.prepareStatement(querySql);
      preparedStatement.setString(1, format);
      ResultSet resultSet = preparedStatement.executeQuery();

      if (resultSet.next()) {
        revenueToday = resultSet.getInt("total_price");
      }
    } catch (SQLException e) {
      CommonHelper.showAlert("Error", "Aplikasi mengalamai error tidak terduga", Alert.AlertType.ERROR);
    }

    return revenueToday;
  }

  public void handleLoginAction(MouseEvent mouseEvent) throws IOException {
    Stage loginStage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
    loginStage.hide();
    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("Login.fxml"));
    Stage bicycleStage = new Stage();
    bicycleStage.setTitle("Login");
    Scene scene = new Scene(fxmlLoader.load());
    bicycleStage.setScene(scene);
    bicycleStage.show();
  }
}
