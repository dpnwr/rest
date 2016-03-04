package dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import play.data.validation.Constraints;

@Data
@NoArgsConstructor
public class LoginDTO {
    @Constraints.Required
    private String username;
    @Constraints.Required
    private String password;
}
