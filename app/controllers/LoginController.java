package controllers;

import static cache.SessionCache.*;
import com.google.inject.Inject;
import com.owera.common.db.ConnectionProperties;
import dto.LoginDTO;
import dto.WebUser;
import play.data.Form;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import service.LoginService;
import service.XAPSLoader;

import javax.xml.bind.annotation.adapters.HexBinaryAdapter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

import static play.data.Form.form;


public class LoginController extends Controller {

    @Inject
    private LoginService loginService;

    @BodyParser.Of(BodyParser.Json.class)
    public Result authenticate() throws SQLException, ClassNotFoundException, NoSuchAlgorithmException {
        Form<LoginDTO> loginForm = form(LoginDTO.class).bindFromRequest();
        if (loginForm.hasErrors()) {
            return unauthorized("Missing username or password");
        } else {
            LoginDTO login = loginForm.get();
            login.setPassword(toSHA1(login.getPassword()));
            if (getXAPSConnectionProperties() == null) {
                ConnectionProperties properties = XAPSLoader.getConnectionProperties();
                putXAPSConnectionProperties(properties);
                putSyslogConnectionProperties(properties);
            }
            WebUser authenticatedUser = loginService.authenticateUser(login);
            if (authenticatedUser.isAuthenticated()) {
                String uuid = java.util.UUID.randomUUID().toString() + authenticatedUser.getUsername();
                putUser(uuid, authenticatedUser);
                XAPSLoader.getDBI(uuid);
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
