package controllers;

import java.sql.SQLException;
import java.util.Optional;

import com.google.inject.Inject;
import dto.UnittypeParameterDTO;
import play.data.Form;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Result;
import play.mvc.Security;
import service.UnittypeParameterService;

import static play.data.Form.form;

public class UnittypeParameterController extends BaseController {

    @Inject
    private UnittypeParameterService unittypeParameters;

    @Security.Authenticated(Authenticated.class)
    public Result getUnittypeParameter(Integer unittypeId, Integer paramId) {
        return Optional.ofNullable(unittypeParameters.getUnittypeParameter(session("uuid"), unittypeId, paramId))
                .map(unittypeParameterDTO -> ok(Json.toJson(unittypeParameterDTO)))
                .orElse(notFound("No such unittypeParameterId: " + paramId));
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
        return Optional.ofNullable(unittypeParameters.updateUnittypeParameter(session("uuid"), unittypeId, form.get()))
                .map(unittypeParameterDTO -> ok(Json.toJson(unittypeParameterDTO)))
                .orElse(notFound("No such unittypeParameterId: " + form.get().getId()));
    }

    @Security.Authenticated(Authenticated.class)
    public Result deleteUnittypeParameter(Integer unittypeId, Integer paramId) throws SQLException {
        unittypeParameters.deleteUnittypeParameter(session("uuid"), unittypeId, paramId);
        return noContent();
    }
}
