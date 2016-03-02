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
import java.util.Arrays;

import static play.data.Form.form;

public class UnitController extends Controller {

    @Inject
    private UnitService profiles;

    @Security.Authenticated(Authenticated.class)
    public Result getUnit(Integer unittypeId, Integer profileId, String unitId) throws SQLException {
        UnitDTO unit = profiles.getUnit(session("uuid"), unittypeId, profileId, unitId);
        if (unit == null) {
            return notFound("No such unitId: " + unitId);
        }
        return ok(Json.toJson(unit));
    }

    @Security.Authenticated(Authenticated.class)
    public Result searchUnits(Integer unittypeId, Integer profileId, String searchTerms) throws SQLException {
        UnitDTO[] units = profiles.searchForUnits(session("uuid"), unittypeId, profileId, searchTerms);
        return ok(Json.toJson(units));
    }

    @Security.Authenticated(Authenticated.class)
    public Result createUnit(Integer unittypeId, Integer profileId, String unitId) throws SQLException {
        UnitDTO unit = profiles.createUnit(session("uuid"), unitId, profileId);
        return ok(Json.toJson(unit));
    }

    @Security.Authenticated(Authenticated.class)
    @BodyParser.Of(BodyParser.Json.class)
    public Result updateUnit(Integer unittypeId, Integer profileId) throws SQLException {
        Form<UnitDTO> form = form(UnitDTO.class).bindFromRequest();
        if (form.hasErrors()) {
            return badRequest(form.errorsAsJson());
        }
        UnitDTO unit = profiles.updateUnit(session("uuid"), form.get(), unittypeId, profileId);
        return ok(Json.toJson(unit));
    }

    @Security.Authenticated(Authenticated.class)
    public Result deleteUnit(Integer unittypeId, Integer profileId, String unitId) throws SQLException {
        profiles.deleteUnit(session("uuid"), unitId);
        return noContent();
    }
}
