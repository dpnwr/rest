package controllers;

import com.google.inject.Inject;
import dto.UnitParameterDTO;
import play.data.Form;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Result;
import play.mvc.Security;
import service.UnitParameterService;

import java.sql.SQLException;
import java.util.Optional;

import static play.data.Form.form;

public class UnitParameterController extends BaseController {

    @Inject
    private UnitParameterService unitParameters;

    @Security.Authenticated(Authenticated.class)
    public Result getUnitParameter(String unitId, Integer paramId) throws SQLException {
        return Optional.ofNullable(unitParameters.getUnitParameter(session("uuid"), unitId, paramId))
                .map(unitParameterDTO -> ok(Json.toJson(unitParameterDTO)))
                .orElse(notFound("No such profileParameterId: " + paramId));
    }

    @Security.Authenticated(Authenticated.class)
    public Result listUnitParameters(String unitId) throws SQLException {
        return ok(Json.toJson(unitParameters.getUnitParameters(session("uuid"), unitId)));
    }

    @Security.Authenticated(Authenticated.class)
    @BodyParser.Of(BodyParser.Json.class)
    public Result createOrUpdateUnitParameter(String unitId) throws SQLException {
        Form<UnitParameterDTO> form = form(UnitParameterDTO.class).bindFromRequest();
        if (form.hasErrors()) {
            return badRequest(form.errorsAsJson());
        }
        return ok(Json.toJson(unitParameters.createOrUpdateUnitParameter(session("uuid"), unitId, form.get())));
    }

    @Security.Authenticated(Authenticated.class)
    public Result deleteUnitParameter(String unitId, Integer paramId) throws SQLException {
        unitParameters.deleteUnitParameter(session("uuid"), unitId, paramId);
        return noContent();
    }
}
