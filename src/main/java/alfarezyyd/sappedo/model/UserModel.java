package alfarezyyd.sappedo.model;

public class UserModel {
    private Integer id;
    private String username;
    private String fullName;
    private String password;
    private String avatar;
private Boolean isAdmin;
    public UserModel(Integer id, String username, String fullName, String password, String avatar, Boolean isAdmin) {
        this.id = id;
        this.username = username;
        this.fullName = fullName;
        this.password = password;
        this.avatar = avatar;
      this.isAdmin = isAdmin;
    }

    // Getter dan Setter
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
