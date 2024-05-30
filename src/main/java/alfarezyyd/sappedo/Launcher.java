package alfarezyyd.sappedo;

import atlantafx.base.theme.PrimerLight;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class Launcher extends Application {
  @Override
  public void start(Stage stage) throws IOException, SQLException {
    AppConnection.initializeConnection();
    Application.setUserAgentStylesheet(new PrimerLight().getUserAgentStylesheet());
    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("Login.fxml"));
    Scene scene = new Scene(fxmlLoader.load());
    stage.setTitle("Aplikasi Sappedo!");
    stage.setScene(scene);
    stage.show();
  }

  public static void main(String[] args) {
    launch();
  }
}