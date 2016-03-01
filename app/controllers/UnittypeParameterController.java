package controllers;

import java.sql.SQLException;

import com.google.inject.Inject;
import dto.UnittypeParameterDTO;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import service.UnittypeParameterService;

public class UnittypeParameterController extends Controller {

    @Inject
    private UnittypeParameterService unittypeParameters;

    @Security.Authenticated(Authenticated.class)
    public Result getUnittypeParameter(Integer unittypeId, Integer paramId) {
        return ok(Json.toJson(unittypeParameters.getUnittypeParameter(session("uuid"), unittypeId, paramId)));
    }

    @Security.Authenticated(Authenticated.class)
    public Result listUnittypeParameters(Integer unittypeId) {
        return ok(Json.toJson(unittypeParameters.getUnittypeParameters(session("uuid"), unittypeId)));
    }

    @Security.Authenticated(Authenticated.class)
    @BodyParser.Of(BodyParser.Json.class)
    public Result createUnittypeParameter(Integer unittypeId) throws SQLException {
        return ok(Json.toJson(unittypeParameters.createeUnittypeParameter(session("uuid"), unittypeId, Json.fromJson(request().body().asJson(), UnittypeParameterDTO.class))));
    }

    @Security.Authenticated(Authenticated.class)
    @BodyParser.Of(BodyParser.Json.class)
    public Result updateUnittypeParameter(Integer unittypeId) throws SQLException {
        return ok(Json.toJson(unittypeParameters.updateUnittypeParameter(session("uuid"), unittypeId, Json.fromJson(request().body().asJson(), UnittypeParameterDTO.class))));
    }

    @Security.Authenticated(Authenticated.class)
    public Result deleteUnittypeParameter(Integer unittypeId, Integer paramId) throws SQLException {
        unittypeParameters.deleteUnittypeParameter(session("uuid"), unittypeId, paramId);
        return noContent();
    }
}
