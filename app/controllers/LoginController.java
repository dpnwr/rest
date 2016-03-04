package controllers;

import cache.SessionCache;
import cache.SessionData;
import com.google.inject.Inject;
import com.owera.common.db.ConnectionProperties;
import dto.LoginDTO;
import dto.WebUser;
import play.data.Form;
import play.mvc.BodyParser;
import play.mvc.Result;
import service.LoginService;
import service.XAPSLoader;

import javax.xml.bind.annotation.adapters.HexBinaryAdapter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

import static play.data.Form.form;


public class LoginController extends BaseController {

    @Inject
    private LoginService loginService;

    @Inject
    private XAPSLoader xapsLoader;

    @BodyParser.Of(BodyParser.Json.class)
    public Result authenticate() throws SQLException, ClassNotFoundException, NoSuchAlgorithmException {
        Form<LoginDTO> loginForm = form(LoginDTO.class).bindFromRequest();
        if (loginForm.hasErrors()) {
            return unauthorized("Missing username or password");
        } else {
            LoginDTO login = loginForm.get();
            login.setPassword(toSHA1(login.getPassword()));
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

    private String toSHA1(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        return new HexBinaryAdapter().marshal((md.digest(password.getBytes())));
    }
}
