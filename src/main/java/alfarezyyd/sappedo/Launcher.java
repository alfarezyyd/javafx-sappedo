package alfarezyyd.sappedo;

import atlantafx.base.theme.PrimerDark;
import atlantafx.base.theme.PrimerLight;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Launcher extends Application {
  @Override
  public void start(Stage stage) throws IOException {
    Application.setUserAgentStylesheet(new PrimerLight().getUserAgentStylesheet());
    Application.setUserAgentStylesheet(new PrimerDark().getUserAgentStylesheet());
    FXMLLoader fxmlLoader = new FXMLLoader(Launcher.class.getResource("Login.fxml"));
    Scene scene = new Scene(fxmlLoader.load());
    stage.setTitle("Hello!");
    stage.setScene(scene);
    stage.show();
  }

  public static void main(String[] args) {
    launch();
  }
}