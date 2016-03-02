package dto;

import play.data.validation.Constraints;

public class LoginDTO {
    @Constraints.Required
    public String username;
    @Constraints.Required
    public String password;
}
