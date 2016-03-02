package controllers;

import com.google.inject.Inject;
import dto.ProfileDTO;
import dto.UnitDTO;
import play.data.Form;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import service.ProfileService;
import service.UnitService;

import java.sql.SQLException;

import static play.data.Form.form;

public class UnitController extends Controller {

    @Inject
    private UnitService profiles;

    @Security.Authenticated(Authenticated.class)
    public Result getUnit(Integer unittypeId, Integer profileId, String unitId) throws SQLException {
        return ok(Json.toJson(profiles.getUnit(session("uuid"), unittypeId, profileId, unitId)));
    }

    @Security.Authenticated(Authenticated.class)
    public Result searchUnits(Integer unittypeId, Integer profileId, String searchTerms) throws SQLException {
        return ok(Json.toJson(profiles.searchForUnits(session("uuid"), unittypeId, profileId, searchTerms)));
    }

    @Security.Authenticated(Authenticated.class)
    public Result createUnit(Integer unittypeId, Integer profileId, String unitId) throws SQLException {
        return ok(Json.toJson(profiles.createUnit(session("uuid"), unitId, profileId)));
    }

    @Security.Authenticated(Authenticated.class)
    @BodyParser.Of(BodyParser.Json.class)
    public Result updateUnit(Integer unittypeId, Integer profileId) throws SQLException {
        Form<UnitDTO> form = form(UnitDTO.class).bindFromRequest();
        if (form.hasErrors()) {
            return badRequest(form.errorsAsJson());
        }
        return ok(Json.toJson(profiles.updateUnit(session("uuid"), form.get(), unittypeId, profileId)));
    }

    @Security.Authenticated(Authenticated.class)
    public Result deleteUnit(Integer unittypeId, Integer profileId, String unitId) throws SQLException {
        profiles.deleteUnit(session("uuid"), unitId);
        return noContent();
    }
}
