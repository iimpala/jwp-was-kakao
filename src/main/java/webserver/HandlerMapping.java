package webserver;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.io.ClassPathTemplateLoader;
import com.github.jknack.handlebars.io.TemplateLoader;
import service.UserService;
import webserver.controller.*;

import java.util.HashMap;
import java.util.Map;

public class HandlerMapping {
    private final Map<String, Controller> handlerMapping;

    public HandlerMapping() {
        Map<String, Controller> handlerMapping = new HashMap<>();
        UserService userService = new UserService();
        Handlebars handlebars = initHandlebars();

        handlerMapping.put("/user/create", new UserCreateController(userService));
        handlerMapping.put("/user/login", new UserLoginController(userService));
        handlerMapping.put("/user/list", new UserListController(handlebars, userService));

        this.handlerMapping = handlerMapping;
    }

    private Handlebars initHandlebars() {
        TemplateLoader loader = new ClassPathTemplateLoader();
        loader.setPrefix("/templates");
        loader.setSuffix(".html");
        return new Handlebars(loader);
    }

    public Controller getController(String path) {
        return handlerMapping.getOrDefault(path, new ResourceController());
    }
}
