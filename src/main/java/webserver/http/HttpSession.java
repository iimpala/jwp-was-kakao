package webserver.http;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class HttpSession {

    private final String id;
    private final Map<String, Object> attributes = new HashMap<>();

    public HttpSession(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public Optional<Object> getAttribute(String name) {
        if (attributes.containsKey(name)) {
            return Optional.of(attributes.get(name));
        }
        return Optional.empty();
    }

    public void setAttribute(String name, Object value) {
        attributes.put(name, value);
    }
}
