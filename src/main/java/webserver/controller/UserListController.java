package webserver.controller;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import model.Users;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.UserService;
import webserver.http.HttpCookie;
import webserver.http.request.HttpRequest;
import webserver.http.response.HttpResponse;
import webserver.http.response.HttpResponseFactory;

import java.io.IOException;
import java.util.Optional;

public class UserListController extends AbstractController {
    private static final Logger logger = LoggerFactory.getLogger(UserListController.class);

    private final Handlebars handlebars;
    private final UserService userService;

    public UserListController(Handlebars handlebars, UserService userService) {
        this.handlebars = handlebars;
        this.userService = userService;
    }

    @Override
    protected HttpResponse doGet(HttpRequest request) {
        Optional<String> logined = request.getCookie().stream()
                .filter(cookie -> "logined".equals(cookie.getName()))
                .map(HttpCookie::getValue)
                .findFirst();

        if (logined.isEmpty() || "false".equals(logined.get())) {
            return HttpResponseFactory.redirect(request, "/user/login.html");
        }

        Users users = userService.findAll();
        try {
            Template template = handlebars.compile("user/list");
            String body = template.apply(users);
            return HttpResponseFactory.ok(body.getBytes());
        } catch (IOException e) {
            logger.error(e.getMessage());
            return HttpResponseFactory.badRequest();
        }
    }
}
