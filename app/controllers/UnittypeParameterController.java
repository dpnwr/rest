package controllers;

import java.sql.SQLException;

import com.google.inject.Inject;
import dto.UnittypeParameterDTO;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import service.UnittypeParameterService;

public class UnittypeParameterController extends Controller {

	@Inject UnittypeParameterService unittypeParameters;
	
	public Result getUnittypeParameter(Integer unittypeId, Integer paramId) {
		return ok(Json.toJson(unittypeParameters.getUnittypeParameter(unittypeId, paramId)));
	}
	
	public Result listUnittypeParameters(Integer unittypeId) {
		return ok(Json.toJson(unittypeParameters.getUnittypeParameters(unittypeId)));
	}

    @BodyParser.Of(BodyParser.Json.class)
	public Result createUnittypeParameter(Integer unittypeId) throws SQLException {
		return ok(Json.toJson(unittypeParameters.createeUnittypeParameter(unittypeId, Json.fromJson(request().body().asJson(), UnittypeParameterDTO.class))));
	}

    @BodyParser.Of(BodyParser.Json.class)
	public Result updateUnittypeParameter(Integer unittypeId) throws SQLException {
		return ok(Json.toJson(unittypeParameters.updateUnittypeParameter(unittypeId, Json.fromJson(request().body().asJson(), UnittypeParameterDTO.class))));
	}
	
	public Result deleteUnittypeParameter(Integer unittypeId, Integer paramId) throws SQLException {
		unittypeParameters.deleteUnittypeParameter(unittypeId, paramId);
		return noContent();
	}
}
