package controllers;

import cache.SessionCache;
import cache.SessionData;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;
import com.owera.common.db.ConnectionProperties;
import com.owera.xaps.dbi.DBI;
import dto.LoginDTO;
import dto.WebUser;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import service.LoginService;
import service.XAPSLoader;

import java.sql.SQLException;


public class LoginController extends Controller {

    @Inject
    private LoginService loginService;

    @Inject
    private XAPSLoader xapsLoader;

    public Result authenticate() throws SQLException, ClassNotFoundException {
        JsonNode json = request().body().asJson();
        LoginDTO login = Json.fromJson(json, LoginDTO.class);
        if (!login.isValid()) {
            return unauthorized("Missing username or password");
        } else {
            if (SessionCache.getXAPSConnectionProperties() == null) {
                ConnectionProperties properties = xapsLoader.getConnectionProperties();
                SessionCache.putXAPSConnectionProperties(properties);
                SessionCache.putSyslogConnectionProperties(properties);
            }
            WebUser authenticatedUser = loginService.authenticateUser(login);
            if (authenticatedUser.isAuthenticated()) {
                String uuid = session("uuid");
                if (uuid == null) {
                    uuid = java.util.UUID.randomUUID().toString() + authenticatedUser.getUsername();
                }
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
}
