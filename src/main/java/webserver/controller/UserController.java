package webserver.controller;

import service.UserDto;
import service.UserService;
import utils.parser.DataFormat;
import utils.parser.DataParser;
import webserver.request.HttpRequest;
import webserver.response.HttpResponse;
import webserver.response.HttpResponseFactory;

import java.util.Map;
import java.util.Optional;

public class UserController extends AbstractController {
    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @Override
    protected HttpResponse doGet(HttpRequest request) {
        return saveUser(request, request.getQueryParams());
    }

    @Override
    protected HttpResponse doPost(HttpRequest request) {
        DataParser parser = getParserFromContentType(request);

        String body = request.getBody();
        return saveUser(request, parser.parse(body));
    }

    private DataParser getParserFromContentType(HttpRequest request) {
        String contentType = request.getHeader().getContentType();
        Optional<DataFormat> optionalDataFormat = DataFormat.ofContentType(contentType);
        DataFormat dataFormat = optionalDataFormat.orElseThrow(IllegalArgumentException::new);

        return DataParser.get(dataFormat);
    }

    private HttpResponse saveUser(HttpRequest request, Map<String, String> param) {
        UserDto userDto = new UserDto(
                param.getOrDefault("userId", ""),
                param.getOrDefault("password", ""),
                param.getOrDefault("name", ""),
                param.getOrDefault("email", "")
        );
        service.save(userDto);

        return HttpResponseFactory.redirect(request, "/index.html");
    }
}

