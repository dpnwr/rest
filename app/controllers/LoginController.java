package controllers;

import cache.SessionCache;
import cache.SessionData;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;
import com.owera.common.db.ConnectionProperties;
import dto.LoginDTO;
import dto.WebUser;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import service.LoginService;
import service.XAPSLoader;

import javax.xml.bind.annotation.adapters.HexBinaryAdapter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;


public class LoginController extends Controller {

    @Inject
    private LoginService loginService;

    @Inject
    private XAPSLoader xapsLoader;

    public Result authenticate() throws SQLException, ClassNotFoundException, NoSuchAlgorithmException {
        JsonNode json = request().body().asJson();
        LoginDTO login = Json.fromJson(json, LoginDTO.class);
        if (!login.isValid()) {
            return unauthorized("Missing username or password");
        } else {
            login.password = getSHA1(login.password);
            if (SessionCache.getXAPSConnectionProperties() == null) {
                ConnectionProperties properties = xapsLoader.getConnectionProperties();
                SessionCache.putXAPSConnectionProperties(properties);
                SessionCache.putSyslogConnectionProperties(properties);
            }
            WebUser authenticatedUser = loginService.authenticateUser(login);
            if (authenticatedUser.isAuthenticated()) {
                String uuid = java.util.UUID.randomUUID().toString() + authenticatedUser.getUsername();
                SessionData sessionData = SessionCache.getSessionData(uuid);
                sessionData.setUser(authenticatedUser);
                xapsLoader.getDBI(uuid);
                session().clear();
                session("uuid", uuid);
                session("username", authenticatedUser.getUsername());
                session("access", authenticatedUser.getAccess());
                session("admin", authenticatedUser.isAdmin() + "");
                return ok();
            } else {
                return unauthorized("Wrong username or password");
            }
        }
    }

    private String getSHA1(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        return new HexBinaryAdapter().marshal((md.digest(password.getBytes())));
    }
}
