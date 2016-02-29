package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;
import dto.LoginDTO;
import dto.WebUser;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import service.LoginService;

import java.sql.SQLException;


public class LoginController extends Controller {

    @Inject
    private LoginService loginService;

    public Result authenticate() throws SQLException {
        JsonNode json = request().body().asJson();
        LoginDTO login = Json.fromJson(json, LoginDTO.class);
        if (!login.isValid()) {
            return unauthorized("Missing username or password");
        } else {
            WebUser authenticatedUser = loginService.authenticateUser(login);
            if (authenticatedUser.isAuthenticated()) {
                session().clear();
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
