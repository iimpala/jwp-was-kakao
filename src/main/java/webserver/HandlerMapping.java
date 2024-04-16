package webserver;

import service.UserService;
import webserver.controller.AbstractController;
import webserver.controller.Controller;
import webserver.controller.ResourceController;
import webserver.controller.UserController;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class HandlerMapping {
    private final Map<String, Controller> handlerMapping;

    public HandlerMapping() {
        Map<String, Controller> handlerMapping = new HashMap<>();
        handlerMapping.put("/", new ResourceController());
        handlerMapping.put("/user/create", new UserController(new UserService()));
        this.handlerMapping = handlerMapping;
    }

    public Controller getController(String path) {
        Optional<Controller> optionalController = handlerMapping.keySet().stream()
                .filter(path::startsWith)
                .map(handlerMapping::get)
                .findFirst();

        return optionalController.orElseGet(AbstractController::new);
    }
}
