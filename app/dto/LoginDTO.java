package dto;

public class LoginDTO {
    public String username;
    public String password;

    public boolean isValid() {
        return username != null && password != null;
    }
}
