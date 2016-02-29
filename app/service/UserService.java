package service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.owera.xaps.dbi.Users;

import java.sql.SQLException;

@Singleton
public class UserService {

    private
    @Inject
    XAPSLoader xapsLoader;

    private Users users = null;

    public UserService() {
    }

    public Users getUsers() throws SQLException {
        synchronized (this) {
            if (users == null) {
                users = new Users(xapsLoader.getConnectionProperties());
            }
        }
        return users;
    }
}
