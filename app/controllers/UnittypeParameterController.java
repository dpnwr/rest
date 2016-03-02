package controllers;

import java.sql.SQLException;

import com.google.inject.Inject;
import dto.UnittypeParameterDTO;
import play.data.Form;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import service.UnittypeParameterService;

import static play.data.Form.form;

public class UnittypeParameterController extends Controller {

    @Inject
    private UnittypeParameterService unittypeParameters;

    @Security.Authenticated(Authenticated.class)
    public Result getUnittypeParameter(Integer unittypeId, Integer paramId) {
        UnittypeParameterDTO unittypeParameter = unittypeParameters.getUnittypeParameter(session("uuid"), unittypeId, paramId);
        if (unittypeParameter == null) {
            return notFound("No such unittypeParameterId: " + paramId);
        }
        return ok(Json.toJson(unittypeParameter));
    }

    @Security.Authenticated(Authenticated.class)
    public Result listUnittypeParameters(Integer unittypeId) {
        return ok(Json.toJson(unittypeParameters.getUnittypeParameters(session("uuid"), unittypeId)));
    }

    @Security.Authenticated(Authenticated.class)
    @BodyParser.Of(BodyParser.Json.class)
    public Result createUnittypeParameter(Integer unittypeId) throws SQLException {
        Form<UnittypeParameterDTO> form = form(UnittypeParameterDTO.class).bindFromRequest();
        if (form.hasErrors()) {
            return badRequest(form.errorsAsJson());
        }
        return ok(Json.toJson(unittypeParameters.createeUnittypeParameter(session("uuid"), unittypeId, form.get())));
    }

    @Security.Authenticated(Authenticated.class)
    @BodyParser.Of(BodyParser.Json.class)
    public Result updateUnittypeParameter(Integer unittypeId) throws SQLException {
        Form<UnittypeParameterDTO> form = form(UnittypeParameterDTO.class).bindFromRequest();
        if (form.hasErrors()) {
            return badRequest(form.errorsAsJson());
        }
        UnittypeParameterDTO paramObject = form.get();
        UnittypeParameterDTO unittypeParameter = unittypeParameters.updateUnittypeParameter(session("uuid"), unittypeId, paramObject);
        if (unittypeParameter == null) {
            return notFound("No such unittypeParameterId: " + paramObject.getId());
        }
        return ok(Json.toJson(unittypeParameter));
    }

    @Security.Authenticated(Authenticated.class)
    public Result deleteUnittypeParameter(Integer unittypeId, Integer paramId) throws SQLException {
        unittypeParameters.deleteUnittypeParameter(session("uuid"), unittypeId, paramId);
        return noContent();
    }
}
