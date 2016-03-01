package controllers;

import java.sql.SQLException;

import cache.SessionCache;
import com.google.inject.Inject;
import dto.UnittypeDTO;
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
        return ok(Json.toJson(unittypes.createeUnittype(session("uuid"), Json.fromJson(request().body().asJson(), UnittypeDTO.class))));
    }

    @Security.Authenticated(Authenticated.class)
    @BodyParser.Of(BodyParser.Json.class)
    public Result updateUnittype() throws SQLException {
        return ok(Json.toJson(unittypes.updateUnittype(session("uuid"), Json.fromJson(request().body().asJson(), UnittypeDTO.class))));
    }

    @Security.Authenticated(Authenticated.class)
    public Result deleteUnittype(Integer id) throws SQLException {
        unittypes.deleteUnittype(session("uuid"), id);
        return noContent();
    }
}
