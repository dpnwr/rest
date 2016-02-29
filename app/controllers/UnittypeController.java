package controllers;

import java.sql.SQLException;

import com.google.inject.Inject;
import dto.UnittypeDTO;
import play.libs.Json;
import play.mvc.*;
import service.UnittypeService;

public class UnittypeController extends Controller {

	@Inject UnittypeService unittypes;

	public Result getUnittype(Integer id) {
		return ok(Json.toJson(unittypes.getUnittype(id)));
	}

    public Result listUnittypes() {
		return ok(Json.toJson(unittypes.getUnittypes()));
	}

    @BodyParser.Of(BodyParser.Json.class)
    public Result createUnittype() throws SQLException {
		return ok(Json.toJson(unittypes.createeUnittype(Json.fromJson(request().body().asJson(), UnittypeDTO.class))));
	}

    @BodyParser.Of(BodyParser.Json.class)
    public Result updateUnittype() throws SQLException {
		return ok(Json.toJson(unittypes.updateUnittype(Json.fromJson(request().body().asJson(), UnittypeDTO.class))));
	}

    public Result deleteUnittype(Integer id) throws SQLException {
		unittypes.deleteUnittype(id);
        return noContent();
	}
}
