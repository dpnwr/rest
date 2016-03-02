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

import static play.data.Form.form;

public class ProfileParameterController extends Controller {

    @Inject
    private ProfileParameterService profileParameters;

    @Security.Authenticated(Authenticated.class)
    public Result getProfileParameter(Integer profileId, Integer paramId) {
        ProfileParameterDTO profileParameter = profileParameters.getProfileParameter(session("uuid"), profileId, paramId);
        if (profileParameter == null) {
            return notFound("No such profileParameterId: " + paramId);
        }
        return ok(Json.toJson(profileParameter));
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
        ProfileParameterDTO profileParameter = profileParameters.updateProfileParameter(session("uuid"), profileId, paramObject);
        if (profileParameter == null) {
            return notFound("No such profileParameterId: " + paramObject.getId());
        }
        return ok(Json.toJson(profileParameter));
    }

    @Security.Authenticated(Authenticated.class)
    public Result deleteProfileParameter(Integer unittypeId, Integer paramId) throws SQLException {
        profileParameters.deleteProfileParameter(session("uuid"), unittypeId, paramId);
        return noContent();
    }
}
