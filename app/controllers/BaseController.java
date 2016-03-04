package controllers;

import play.mvc.Controller;

import java.util.Optional;

public class BaseController extends Controller {

    public Optional<String> getQueryParam(String key) {
        return Optional.ofNullable(request().getQueryString(key));
    }
}
