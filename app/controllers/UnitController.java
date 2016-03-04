package controllers;

import com.google.inject.Inject;
import dto.UnitDTO;
import play.data.Form;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Result;
import play.mvc.Security;
import service.UnitService;

import java.sql.SQLException;
import java.util.Optional;

import static play.data.Form.form;

public class UnitController extends BaseController {

    @Inject
    private UnitService unitService;

    @Security.Authenticated(Authenticated.class)
    public Result getUnit(String unitId) throws SQLException {
        return Optional.ofNullable(unitService.getUnit(session("uuid"), unitId))
                .map(unitDTO -> ok(Json.toJson(unitDTO)))
                .orElse(notFound("No such unitId: " + unitId));
    }

    @Security.Authenticated(Authenticated.class)
    public Result searchUnits() throws SQLException {
        return ok(Json.toJson(unitService.searchForUnits(session("uuid"),
                getQueryParam("unittypeId").map(Integer::parseInt).orElse(null),
                getQueryParam("profileId").map(Integer::parseInt).orElse(null),
                getQueryParam("searchTerms").orElse(null))));
    }

    @Security.Authenticated(Authenticated.class)
    public Result createUnit() throws SQLException {
        Form<UnitDTO> form = form(UnitDTO.class).bindFromRequest();
        if (form.hasErrors()) {
            return badRequest(form.errorsAsJson());
        }
        return ok(Json.toJson(unitService.createUnit(session("uuid"), form.get())));
    }

    @Security.Authenticated(Authenticated.class)
    @BodyParser.Of(BodyParser.Json.class)
    public Result updateUnit(String unitId) throws SQLException {
        Form<UnitDTO> form = form(UnitDTO.class).bindFromRequest();
        if (form.hasErrors()) {
            return badRequest(form.errorsAsJson());
        }
        return ok(Json.toJson(unitService.updateUnit(session("uuid"), form.get())));
    }

    @Security.Authenticated(Authenticated.class)
    public Result deleteUnit(String unitId) throws SQLException {
        unitService.deleteUnit(session("uuid"), unitId);
        return noContent();
    }
}
