package alfarezyyd.sappedo;

import com.mysql.cj.jdbc.Driver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class AppConnection {
  private static final String jdbcUrl = "jdbc:mysql://localhost:3306/javafx_sappedo";
  private static Driver mysqlDriver;


  public static Connection getConnection() throws SQLException {
    return DriverManager.getConnection(AppConnection.jdbcUrl, "root", "root");
  }

  public static void initializeConnection() {
    try {
      AppConnection.mysqlDriver = new com.mysql.cj.jdbc.Driver();
      DriverManager.registerDriver(AppConnection.mysqlDriver);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}
