package controllers;

import play.mvc.*;
import play.mvc.Http.*;

public class Authenticated extends Security.Authenticator {

    @Override
    public String getUsername(Context ctx) {
        return ctx.session().get("username");
    }

    @Override
    public Result onUnauthorized(Context ctx) {
        return unauthorized("You are not logged in");
    }
}
