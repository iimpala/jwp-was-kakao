package webserver.controller;

import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.UserLoginDto;
import service.UserService;
import utils.parser.DataParser;
import webserver.http.HttpCookie;
import webserver.http.request.HttpRequest;
import webserver.http.response.HttpResponse;
import webserver.http.response.HttpResponseFactory;

import java.util.Map;
import java.util.UUID;

public class UserLoginController extends AbstractController {
    private static final Logger logger = LoggerFactory.getLogger(UserLoginController.class);

    private final UserService userService;

    public UserLoginController(UserService userService) {
        this.userService = userService;
    }

    @Override
    protected HttpResponse doPost(HttpRequest request) {
        String contentType = request.getHeader().getContentType();
        DataParser parser = DataParser.get(contentType);

        Map<String, String> param = parser.parse(request.getBody());
        UserLoginDto userLoginDto = new UserLoginDto(
                param.getOrDefault("userId", ""),
                param.getOrDefault("password", "")
        );

        try {
            User user = userService.login(userLoginDto);
            UUID jSessionId = UUID.randomUUID();

            HttpResponse response = HttpResponseFactory.redirect(request, "/index.html");
            response.addCookie(new HttpCookie("JSESSIONID", jSessionId.toString(), "/"));
            response.addCookie(new HttpCookie("logined", "true", "/"));

            return response;
        } catch (Exception e) {
            logger.error(e.getMessage());
            return HttpResponseFactory.redirect(request, "/user/login_failed.html");
        }
    }
}
