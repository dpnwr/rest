package service;

import cache.SessionCache;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.owera.xaps.dbi.Users;

import java.sql.SQLException;

@Singleton
public class UserService {

    private Users users = null;

    public UserService() {
    }

    public Users getUsers() throws SQLException {
        synchronized (this) {
            if (users == null) {
                users = new Users(SessionCache.getXAPSConnectionProperties());
            }
        }
        return users;
    }
}
