package service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.owera.xaps.dbi.Users;

import java.sql.SQLException;

@Singleton
public class UserService {
    @Inject
    private XAPSLoader xapsLoader;

    private Users users;

    public UserService() throws SQLException {
        users = new Users(xapsLoader.getConnectionProperties());
    }

    public Users getUsers() {
        return users;
    }
}
