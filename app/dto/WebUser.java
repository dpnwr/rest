package dto;

import com.owera.xaps.dbi.User;
import com.owera.xaps.dbi.Users;
import lombok.Getter;
import lombok.Setter;

public class WebUser extends User {

    @Setter
    @Getter
    private boolean authenticated = false;

    public WebUser(User user, boolean authenticated) {
        super(user);
        this.setAuthenticated(authenticated);
    }

    @Override
    public String getAccess() {
        String access = super.getAccess();
        if (access == null)
            return Users.ACCESS_ADMIN;
        return access;
    }
}