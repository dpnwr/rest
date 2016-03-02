package controllers;

import com.google.inject.Inject;
import dto.ProfileDTO;
import play.data.Form;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import service.ProfileService;

import java.sql.SQLException;

import static play.data.Form.form;

public class ProfileController extends Controller {

    @Inject
    private ProfileService profiles;

    @Security.Authenticated(Authenticated.class)
    public Result getProfile(Integer unittypeId, Integer profileId) {
        return ok(Json.toJson(profiles.getProfile(session("uuid"), profileId, unittypeId)));
    }

    @Security.Authenticated(Authenticated.class)
    public Result listProfiles(Integer unittypeId) {
        return ok(Json.toJson(profiles.getProfiles(session("uuid"), unittypeId)));
    }

    @Security.Authenticated(Authenticated.class)
    @BodyParser.Of(BodyParser.Json.class)
    public Result createProfile(Integer unittypeId) throws SQLException {
        Form<ProfileDTO> form = form(ProfileDTO.class).bindFromRequest();
        if (form.hasErrors()) {
            return badRequest(form.errorsAsJson());
        }
        return ok(Json.toJson(profiles.createeProfile(session("uuid"), form.get(), unittypeId)));
    }

    @Security.Authenticated(Authenticated.class)
    @BodyParser.Of(BodyParser.Json.class)
    public Result updateProfile(Integer unittypeId) throws SQLException {
        Form<ProfileDTO> form = form(ProfileDTO.class).bindFromRequest();
        if (form.hasErrors()) {
            return badRequest(form.errorsAsJson());
        }
        return ok(Json.toJson(profiles.updateProfile(session("uuid"), form.get(), unittypeId)));
    }

    @Security.Authenticated(Authenticated.class)
    public Result deleteProfile(Integer unittypeId, Integer profileId) throws SQLException {
        profiles.deleteProfile(session("uuid"), profileId, unittypeId);
        return noContent();
    }
}
