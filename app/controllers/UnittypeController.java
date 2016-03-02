package controllers;

import java.sql.SQLException;
import com.google.inject.Inject;
import dto.UnittypeDTO;
import play.data.Form;
import static play.data.Form.*;
import play.libs.Json;
import play.mvc.*;
import service.UnittypeService;

public class UnittypeController extends Controller {

    @Inject
    private UnittypeService unittypes;

    @Security.Authenticated(Authenticated.class)
    public Result getUnittype(Integer id) {
        return ok(Json.toJson(unittypes.getUnittype(session("uuid"), id)));
    }

    @Security.Authenticated(Authenticated.class)
    public Result listUnittypes() {
        return ok(Json.toJson(unittypes.getUnittypes(session("uuid"))));
    }

    @Security.Authenticated(Authenticated.class)
    @BodyParser.Of(BodyParser.Json.class)
    public Result createUnittype() throws SQLException {
        Form<UnittypeDTO> form = form(UnittypeDTO.class).bindFromRequest();
        if (form.hasErrors()) {
            return badRequest(form.errorsAsJson());
        }
        return ok(Json.toJson(unittypes.createeUnittype(session("uuid"), form.get())));
    }

    @Security.Authenticated(Authenticated.class)
    @BodyParser.Of(BodyParser.Json.class)
    public Result updateUnittype() throws SQLException {
        Form<UnittypeDTO> form = form(UnittypeDTO.class).bindFromRequest();
        if (form.hasErrors()) {
            return badRequest(form.errorsAsJson());
        }
        return ok(Json.toJson(unittypes.updateUnittype(session("uuid"), form.get())));
    }

    @Security.Authenticated(Authenticated.class)
    public Result deleteUnittype(Integer id) throws SQLException {
        unittypes.deleteUnittype(session("uuid"), id);
        return noContent();
    }
}
