package controllers;

import java.sql.SQLException;

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
        return ok(Json.toJson(unittypes.getUnittype(id)));
    }

    @Security.Authenticated(Authenticated.class)
    public Result listUnittypes() {
        System.out.println(request().username());
        return ok(Json.toJson(unittypes.getUnittypes()));
    }

    @Security.Authenticated(Authenticated.class)
    @BodyParser.Of(BodyParser.Json.class)
    public Result createUnittype() throws SQLException {
        return ok(Json.toJson(unittypes.createeUnittype(Json.fromJson(request().body().asJson(), UnittypeDTO.class))));
    }

    @Security.Authenticated(Authenticated.class)
    @BodyParser.Of(BodyParser.Json.class)
    public Result updateUnittype() throws SQLException {
        return ok(Json.toJson(unittypes.updateUnittype(Json.fromJson(request().body().asJson(), UnittypeDTO.class))));
    }

    @Security.Authenticated(Authenticated.class)
    public Result deleteUnittype(Integer id) throws SQLException {
        unittypes.deleteUnittype(id);
        return noContent();
    }
}
