package webserver.http;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class HttpSessionManager {

    private static final Map<String, HttpSession> SESSIONS = new HashMap<>();
    private static HttpSessionManager instance;

    private HttpSessionManager() {
    }

    public static synchronized HttpSessionManager getInstance() {
        if (instance == null) {
            instance = new HttpSessionManager();
        }
        return instance;
    }

    public void add(HttpSession session) {
        SESSIONS.put(session.getId(), session);
    }

    public Optional<HttpSession> getSession(String id) {
        if (SESSIONS.containsKey(id)) {
            return Optional.of(SESSIONS.get(id));
        }
        return Optional.empty();
    }
}
