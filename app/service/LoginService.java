package service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.owera.xaps.dbi.User;
import com.owera.xaps.dbi.Users;
import dto.LoginDTO;
import dto.WebUser;
import play.Logger;

import java.sql.SQLException;

@Singleton
public class LoginService {

    @Inject
    private XAPSLoader xapsLoader;

    public WebUser authenticateUser(LoginDTO login) throws SQLException {
        final WebUser dbUser;
        Users users = new Users(xapsLoader.getConnectionProperties());
        String username = login.username;
        String password = login.password;
        User userObject = users.getUnprotected(username);
        if (userObject != null) {
            Logger.debug("Password from database [" + userObject.getSecret().toUpperCase() + "]");
            Logger.debug("Password from user     [" + password.toUpperCase() + "]");
            boolean authenticated = userObject.getSecret().toUpperCase().equals(password.toUpperCase());
            if (authenticated)
                Logger.debug("Found user with name " + username + ", password matched - login is accepted");
            else
                Logger.warn("Found user with name " + username + ", password did not match");
            dbUser = new WebUser(userObject, authenticated);
        } else {
            Logger.warn("Did not find user with name " + username);
            dbUser = new WebUser(userObject, false);
        }
        return dbUser;
    }
}
