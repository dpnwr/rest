package controllers;

import com.google.inject.Inject;
import dto.ProfileParameterDTO;
import dto.ProfileParameterDTO;
import play.data.Form;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import service.ProfileParameterService;
import service.ProfileParameterService;

import java.sql.SQLException;
import java.util.Optional;

import static play.data.Form.form;

public class ProfileParameterController extends Controller {

    @Inject
    private ProfileParameterService profileParameters;

    @Security.Authenticated(Authenticated.class)
    public Result getProfileParameter(Integer profileId, Integer paramId) {
        return Optional.ofNullable(profileParameters.getProfileParameter(session("uuid"), profileId, paramId))
                .map(profileParameterDTO -> ok(Json.toJson(profileParameterDTO)))
                .orElse(notFound("No such profileParameterId: " + paramId));
    }

    @Security.Authenticated(Authenticated.class)
    public Result listProfileParameters(Integer profileId) {
        return ok(Json.toJson(profileParameters.getProfileParameters(session("uuid"), profileId)));
    }

    @Security.Authenticated(Authenticated.class)
    @BodyParser.Of(BodyParser.Json.class)
    public Result createProfileParameter(Integer profileId) throws SQLException {
        Form<ProfileParameterDTO> form = form(ProfileParameterDTO.class).bindFromRequest();
        if (form.hasErrors()) {
            return badRequest(form.errorsAsJson());
        }
        return ok(Json.toJson(profileParameters.createProfileParameter(session("uuid"), profileId, form.get())));
    }

    @Security.Authenticated(Authenticated.class)
    @BodyParser.Of(BodyParser.Json.class)
    public Result updateProfileParameter(Integer profileId) throws SQLException {
        Form<ProfileParameterDTO> form = form(ProfileParameterDTO.class).bindFromRequest();
        if (form.hasErrors()) {
            return badRequest(form.errorsAsJson());
        }
        ProfileParameterDTO paramObject = form.get();
        return Optional.ofNullable(profileParameters.updateProfileParameter(session("uuid"), profileId, paramObject))
                .map(profileParameterDTO -> ok(Json.toJson(profileParameterDTO)))
                .orElse(notFound("No such profileParameterId: " + paramObject.getId()));
    }

    @Security.Authenticated(Authenticated.class)
    public Result deleteProfileParameter(Integer unittypeId, Integer paramId) throws SQLException {
        profileParameters.deleteProfileParameter(session("uuid"), unittypeId, paramId);
        return noContent();
    }
}
