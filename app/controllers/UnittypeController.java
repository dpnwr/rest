package controllers;

import java.sql.SQLException;
import java.util.Optional;

import com.google.inject.Inject;
import dto.UnittypeDTO;
import play.data.Form;

import static play.data.Form.*;

import play.libs.Json;
import play.mvc.*;
import service.UnittypeService;

public class UnittypeController extends BaseController {

    @Inject
    private UnittypeService unittypes;

    @Security.Authenticated(Authenticated.class)
    public Result getUnittype(Integer id) {
        return Optional.ofNullable(unittypes.getUnittype(session("uuid"), id))
                .map(unittypeDTO -> ok(Json.toJson(unittypeDTO)))
                .orElse(notFound("No such unittypeId: " + id));
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
        return Optional.ofNullable(unittypes.updateUnittype(session("uuid"), form.get()))
                .map(unittypeDTO -> ok(Json.toJson(unittypeDTO)))
                .orElse(notFound("No such unittypeId: " + form.get().getId()));
    }

    @Security.Authenticated(Authenticated.class)
    public Result deleteUnittype(Integer id) throws SQLException {
        unittypes.deleteUnittype(session("uuid"), id);
        return noContent();
    }
}
