package webserver;

import service.UserService;
import webserver.controller.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class HandlerMapping {
    private final Map<String, Controller> handlerMapping;

    public HandlerMapping() {
        Map<String, Controller> handlerMapping = new HashMap<>();
        UserService userService = new UserService();

        handlerMapping.put("/user/create", new UserCreateController(userService));
        handlerMapping.put("/user/login", new UserLoginController(userService));

        this.handlerMapping = handlerMapping;
    }

    public Controller getController(String path) {
        Optional<Controller> optionalController = handlerMapping.keySet().stream()
                .filter(path::equals)
                .map(handlerMapping::get)
                .findFirst();

        return optionalController.orElseGet(ResourceController::new);
    }
}
