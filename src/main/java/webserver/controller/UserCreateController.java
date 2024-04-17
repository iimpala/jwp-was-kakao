package webserver.controller;

import service.UserCreateDto;
import service.UserService;
import utils.parser.DataParser;
import webserver.http.request.HttpRequest;
import webserver.http.response.HttpResponse;
import webserver.http.response.HttpResponseFactory;

import java.util.Map;

public class UserCreateController extends AbstractController {
    private final UserService userService;

    public UserCreateController(UserService userService) {
        this.userService = userService;
    }

    @Override
    protected HttpResponse doGet(HttpRequest request) {
        saveUser(request.getQueryParams());
        return HttpResponseFactory.redirect(request, "/index.html");
    }

    @Override
    protected HttpResponse doPost(HttpRequest request) {
        String contentType = request.getHeader().getContentType();
        DataParser parser = DataParser.get(contentType);

        String body = request.getBody();
        saveUser(parser.parse(body));

        return HttpResponseFactory.redirect(request, "/index.html");
    }

    private void saveUser(Map<String, String> param) {
        UserCreateDto userCreateDto = new UserCreateDto(
                param.getOrDefault("userId", ""),
                param.getOrDefault("password", ""),
                param.getOrDefault("name", ""),
                param.getOrDefault("email", "")
        );
        userService.save(userCreateDto);
    }
}

