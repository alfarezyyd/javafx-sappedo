package alfarezyyd.sappedo.model;

public class UserModel {
  private Integer id;
  private String username;
  private String fullName;
  private String password;
  private String avatar;

  public UserModel(Integer id, String username, String fullName, String password, String avatar, Boolean isAdmin) {
    this.id = id;
    this.username = username;
    this.fullName = fullName;
    this.password = password;
    this.avatar = avatar;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getUsername() {
    return username;
  }


  public String getFullName() {
    return fullName;
  }


  public String getPassword() {
    return password;
  }


  public String getAvatar() {
    return avatar;
  }

}
