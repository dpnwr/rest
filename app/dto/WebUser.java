package dto;

import com.owera.xaps.dbi.User;
import com.owera.xaps.dbi.Users;

public class WebUser extends User {

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

    public boolean isAuthenticated() {
        return authenticated;
    }

    public void setAuthenticated(boolean authenticated) {
        this.authenticated = authenticated;
    }


}