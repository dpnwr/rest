package service;

import java.sql.SQLException;

import cache.SessionCache;
import com.owera.common.db.NoAvailableConnectionException;
import com.owera.xaps.dbi.*;
import play.Play;

import com.google.inject.Singleton;

import com.owera.common.db.ConnectionProperties;
import play.api.Application;

/**
 * We initialize the XAPS object when the server starts up
 *
 * @author Jarl André Hübenthal
 */
@Singleton
public class XAPSLoader {

    public ConnectionProperties getConnectionProperties() throws SQLException, ClassNotFoundException {
        String connectionUrl = Play.application().configuration().getString("xaps.db.url", null);
        if (connectionUrl == null) {
            throw new IllegalArgumentException("No xaps.db.url in application properties");
        }
        ConnectionProperties connectionProperties = new ConnectionProperties();
        try {
            connectionProperties.setUrl(connectionUrl.substring(connectionUrl.indexOf("@") + 1));
            connectionProperties.setUser(connectionUrl.substring(0, connectionUrl.indexOf("/")));
            connectionProperties.setPassword(connectionUrl.substring(connectionUrl.indexOf("/") + 1, connectionUrl.indexOf("@")));
        } catch (StringIndexOutOfBoundsException seoobe) {
            throw new IllegalArgumentException(connectionUrl + " is not on a correct database-config-format (<user>/<password>@<jdbc-url>");
        }
        if (connectionProperties.getUrl().contains("mysql")) {
            connectionProperties.setDriver("com.mysql.jdbc.Driver"); // This class must be specified in the classpath (dynamically loaded)
        } else {
            throw new IllegalArgumentException("The url is not pointing to a MySQL database");
        }
        return connectionProperties;
    }

    public DBI getDBI(String sessionId) throws SQLException {
        DBI dbi = SessionCache.getDBI(sessionId);
        try {
            int sessionTimeoutSecs = getSessionTimeout() * 60;
            if (dbi == null || dbi.isFinished()) {
                ConnectionProperties cp = SessionCache.getXAPSConnectionProperties();
                if (cp == null)
                    return null;
                Identity ident = getIdentity(sessionId);
                ConnectionProperties syscp = SessionCache.getSyslogConnectionProperties();
                if (syscp == null)
                    return null;
                Syslog syslog = new Syslog(syscp, ident);
                dbi = new DBI(sessionTimeoutSecs, cp, syslog);
                SessionCache.putDBI(sessionId, dbi, sessionTimeoutSecs);
            }
            //Monitor.setLastDBILogin(null);
        } catch (Throwable t) {
            //Monitor.setLastDBILogin(t);
            if (t instanceof SQLException)
                throw (SQLException) t;
            if (t instanceof RuntimeException)
                throw (RuntimeException) t;
        }
        return dbi;
    }

    public XAPS getXAPS(String sessionId) throws SQLException {
        DBI dbi = getDBI(sessionId);
        if (dbi != null)
            return dbi.getXaps();
        return null;
    }

    public int getSessionTimeout() {
        return Play.application().configuration().getInt("xaps.session.timeout");
    }

    public Identity getIdentity(String sessionId) throws SQLException {
        User user = SessionCache.getSessionData(sessionId).getUser();
        if (user == null)
            user = getDefaultUser();
        return new Identity(SyslogConstants.FACILITY_WEB, Application.class.getPackage().getImplementationVersion(), user);
    }

    private User getDefaultUser() throws SQLException {
        Users users = new Users(SessionCache.getXAPSConnectionProperties());
        User user = new User("anonymous", null, null, false, users);
        return user;
    }

    public XAPSUnit getXAPSUnit(String sessionId) throws SQLException {
        ConnectionProperties cp = SessionCache.getXAPSConnectionProperties();
        if (cp == null)
            return null;
        XAPS xaps = getDBI(sessionId).getXaps();
        if (xaps == null)
            return null;
        Identity ident = getIdentity(sessionId);
        ConnectionProperties syscp = SessionCache.getSyslogConnectionProperties();
        if (syscp == null)
            return null;
        Syslog syslog = new Syslog(syscp, ident);
        return new XAPSUnit(cp, xaps, syslog);
    }
}
