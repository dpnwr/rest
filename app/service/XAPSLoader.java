package service;

import java.sql.SQLException;

import play.Play;

import com.google.inject.Singleton;

import com.owera.common.db.ConnectionProperties;
import com.owera.xaps.dbi.Identity;
import com.owera.xaps.dbi.Syslog;
import com.owera.xaps.dbi.SyslogConstants;
import com.owera.xaps.dbi.User;
import com.owera.xaps.dbi.Users;
import com.owera.xaps.dbi.XAPS;

/**
 * We initialize the XAPS object when the server starts up
 *
 * @author Jarl André Hübenthal
 */
@Singleton
public class XAPSLoader {
    private XAPS xaps;

    public XAPSLoader() throws SQLException, ClassNotFoundException {
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
        Users users = new Users(connectionProperties);
        User user = new User("default", null, null, true, users);
        Identity identity = new Identity(SyslogConstants.FACILITY_WEB, "3.0", user);
        Syslog syslog = new Syslog(connectionProperties, identity);
        xaps = new XAPS(connectionProperties, syslog);
    }

    public XAPS getXaps() {
        return xaps;
    }
}
