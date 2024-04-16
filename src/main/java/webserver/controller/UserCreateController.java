package webserver.controller;

import service.UserCreateDto;
import service.UserService;
import utils.parser.DataParser;
import webserver.request.HttpRequest;
import webserver.response.HttpResponse;
import webserver.response.HttpResponseFactory;

import java.util.Map;

public class UserCreateController extends AbstractController {
    private final UserService userService;

    public UserCreateController(UserService userService) {
        this.userService = userService;
    }

    @Override
    protected HttpResponse doGet(HttpRequest request) {
        return saveUser(request, request.getQueryParams());
    }

    @Override
    protected HttpResponse doPost(HttpRequest request) {
        String contentType = request.getHeader().getContentType();
        DataParser parser = DataParser.get(contentType);

        String body = request.getBody();
        return saveUser(request, parser.parse(body));
    }

    private HttpResponse saveUser(HttpRequest request, Map<String, String> param) {
        UserCreateDto userCreateDto = new UserCreateDto(
                param.getOrDefault("userId", ""),
                param.getOrDefault("password", ""),
                param.getOrDefault("name", ""),
                param.getOrDefault("email", "")
        );
        userService.save(userCreateDto);

        return HttpResponseFactory.redirect(request, "/index.html");
    }
}

