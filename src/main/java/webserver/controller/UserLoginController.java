package webserver.controller;

import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.UserLoginDto;
import service.UserService;
import utils.parser.DataParser;
import webserver.http.HttpCookie;
import webserver.http.HttpSession;
import webserver.http.HttpSessionManager;
import webserver.http.request.HttpRequest;
import webserver.http.response.HttpResponse;
import webserver.http.response.HttpResponseFactory;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class UserLoginController extends AbstractController {
    private static final Logger logger = LoggerFactory.getLogger(UserLoginController.class);

    private final HttpSessionManager sessionManager = HttpSessionManager.getInstance();
    private final UserService userService;

    public UserLoginController(UserService userService) {
        this.userService = userService;
    }

    @Override
    protected HttpResponse doGet(HttpRequest request) {
        HttpCookie cookie = request.getCookie().getOrDefault("JSESSIONID", new HttpCookie("JSESSIONID", "", ""));

        String jSessionId = cookie.getName();
        Optional<HttpSession> session = sessionManager.getSession(jSessionId);
        if (session.isEmpty()) {
            return HttpResponseFactory.redirect(request, "/user/login.html");
        }

        return HttpResponseFactory.redirect(request, "/index.html");
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

            HttpSession session = createUserSession(user);

            HttpResponse response = HttpResponseFactory.redirect(request, "/index.html");
            response.addCookie(new HttpCookie("JSESSIONID", session.getId(), "/"));

            return response;
        } catch (Exception e) {
            logger.error(e.getMessage());
            return HttpResponseFactory.redirect(request, "/user/login_failed.html");
        }
    }

    private HttpSession createUserSession(User user) {
        UUID jSessionId = UUID.randomUUID();
        HttpSession session = new HttpSession(jSessionId.toString());
        session.setAttribute("user", user);

        sessionManager.add(session);
        return session;
    }
}
