package webserver.controller;

import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.UserLoginDto;
import service.UserService;
import utils.parser.DataParser;
import webserver.request.HttpRequest;
import webserver.response.HttpResponse;
import webserver.response.HttpResponseFactory;

import java.util.Map;

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
            logger.info("Login Success : {}", user);
            return HttpResponseFactory.redirect(request, "/index.html");
        } catch (Exception e) {
            logger.error(e.getMessage());
            return HttpResponseFactory.redirect(request, "/user/login_failed.html");
        }
    }
}
