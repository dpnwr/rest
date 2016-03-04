package controllers;

import com.google.inject.Inject;
import dto.ProfileDTO;
import play.data.Form;
import play.libs.Json;

import play.mvc.*;
import service.ProfileService;

import java.sql.SQLException;
import java.util.Optional;

import static play.data.Form.form;

public class ProfileController extends BaseController {

    @Inject
    private ProfileService profiles;

    @Security.Authenticated(Authenticated.class)
    public Result getProfile(Integer unittypeId, Integer profileId) {
        return Optional.ofNullable(profiles.getProfile(session("uuid"), profileId, unittypeId))
                .map(profileDTO -> ok(Json.toJson(profileDTO)))
                .orElse(notFound("No such profileId: " + profileId));
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
        ProfileDTO profileDTO = form.get();
        String uuid = session("uuid");
        if (profiles.profileExists(uuid, unittypeId, profileDTO.getName())) {
            return status(Http.Status.CONFLICT, "Profile exists");
        }
        return ok(Json.toJson(profiles.createeProfile(uuid, profileDTO, unittypeId)));
    }

    @Security.Authenticated(Authenticated.class)
    @BodyParser.Of(BodyParser.Json.class)
    public Result updateProfile(Integer unittypeId) throws SQLException {
        Form<ProfileDTO> form = form(ProfileDTO.class).bindFromRequest();
        if (form.hasErrors()) {
            return badRequest(form.errorsAsJson());
        }
        if (form.get().getId() == null) {
            return badRequest("Missing profile Id");
        }
        return Optional.ofNullable(profiles.updateProfile(session("uuid"), form.get(), unittypeId))
                .map(profileDTO -> ok(Json.toJson(profileDTO)))
                .orElse(notFound("No such profileId: " + form.get().getId()));
    }

    @Security.Authenticated(Authenticated.class)
    public Result deleteProfile(Integer unittypeId, Integer profileId) throws SQLException {
        profiles.deleteProfile(session("uuid"), profileId, unittypeId);
        return noContent();
    }
}
