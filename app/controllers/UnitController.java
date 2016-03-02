package controllers;

import com.google.inject.Inject;
import dto.UnitDTO;
import play.Logger;
import play.data.Form;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import service.UnitService;

import java.sql.SQLException;

import static play.data.Form.form;

public class UnitController extends Controller {

    @Inject
    private UnitService unitService;

    @Security.Authenticated(Authenticated.class)
    public Result getUnit(String unitId) throws SQLException {
        UnitDTO unit = unitService.getUnit(session("uuid"), unitId);
        if (unit == null) {
            return notFound("No such unitId: " + unitId);
        }
        return ok(Json.toJson(unit));
    }

    @Security.Authenticated(Authenticated.class)
    public Result searchUnits() throws SQLException {
        Integer unittypeId = getIntegerOrNull(request().getQueryString("unittypeId"));
        Integer profileId = getIntegerOrNull(request().getQueryString("profileId"));
        String searchTerms = request().getQueryString("searchTerms");
        UnitDTO[] units = unitService.searchForUnits(session("uuid"), unittypeId, profileId, searchTerms);
        return ok(Json.toJson(units));
    }

    private Integer getIntegerOrNull(String param) {
        try {
            return Integer.valueOf(param);
        } catch (NumberFormatException e) {
            Logger.warn("Could not make Integer from " + param);
            return null;
        }
    }

    @Security.Authenticated(Authenticated.class)
    public Result createUnit(Integer profileId, String unitId) throws SQLException {
        UnitDTO unit = unitService.createUnit(session("uuid"), unitId, profileId);
        return ok(Json.toJson(unit));
    }

    @Security.Authenticated(Authenticated.class)
    @BodyParser.Of(BodyParser.Json.class)
    public Result updateUnit() throws SQLException {
        Form<UnitDTO> form = form(UnitDTO.class).bindFromRequest();
        if (form.hasErrors()) {
            return badRequest(form.errorsAsJson());
        }
        UnitDTO unit = unitService.updateUnit(session("uuid"), form.get());
        return ok(Json.toJson(unit));
    }

    @Security.Authenticated(Authenticated.class)
    public Result deleteUnit(String unitId) throws SQLException {
        unitService.deleteUnit(session("uuid"), unitId);
        return noContent();
    }
}
