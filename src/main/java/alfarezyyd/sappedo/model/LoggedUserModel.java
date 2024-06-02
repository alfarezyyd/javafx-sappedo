package alfarezyyd.sappedo.model;

public class LoggedUserModel {
  private static LoggedUserModel instance;
  private String username;
  private String fullName;
  private String avatar;
  private Boolean isAdmin;

  // Private constructor untuk mencegah pembuatan objek secara langsung
  private LoggedUserModel() {
  }

  // Metode untuk mendapatkan instance dari kelas singleton User
  public static LoggedUserModel getInstance() {
    if (instance == null) {
      instance = new LoggedUserModel();
    }
    return instance;
  }

  // Setter untuk mengatur informasi pengguna yang sedang login
  public void setUser(String username, String fullName, String avatar, Boolean isAdmin) {
    this.username = username;
    this.fullName = fullName;
    this.avatar = avatar;
    this.isAdmin = isAdmin;
  }

  // Getter untuk mendapatkan username pengguna yang sedang login
  public String getUsername() {
    return username;
  }

  // Getter untuk mendapatkan nama lengkap pengguna yang sedang login
  public String getFullName() {
    return fullName;
  }

  public String getAvatar() {
    return avatar;
  }

  // Metode untuk logout pengguna dengan menghapus informasi login
  public void logout() {
    username = null;
    fullName = null;
  }

  // Metode untuk memeriksa apakah ada pengguna yang login saat ini
  public boolean isLoggedIn() {
    return username != null && fullName != null && avatar != null;
  }

  public Boolean isAdmin() {
    return isAdmin;
  }
}
