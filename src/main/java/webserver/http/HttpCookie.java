package webserver.http;

import java.util.*;
import java.util.stream.Collectors;

public class HttpCookie {
    private static final String COOKIE_DELIMITER = ";";
    private static final String COOKIE_KEY_VALUE_DELIMITER = "=";
    private static final int COOKIE_KEY_INDEX = 0;
    private static final int COOKIE_VALUE_INDEX = 1;

    private final Map<String, String> cookie;

    public HttpCookie(Map<String, String> cookie) {
        this.cookie = cookie;
    }

    public HttpCookie() {
        this(new HashMap<>());
    }

    public static HttpCookie of(String header) {
        String[] cookieStrings = header.split(COOKIE_DELIMITER);

        Map<String, String> cookie = Arrays.stream(cookieStrings)
                .map(cookieString -> cookieString.split(COOKIE_KEY_VALUE_DELIMITER))
                .collect(Collectors.toMap(
                        cookieEntry -> cookieEntry[COOKIE_KEY_INDEX],
                        cookieEntry -> cookieEntry[COOKIE_VALUE_INDEX]
                ));

        return new HttpCookie(cookie);
    }

    public void setCookie(String key, String value) {
        cookie.put(key, value);
    }

    public String getCookie(String key) {
        return cookie.get(key);
    }

    public List<String> toSetCookieHeaderValues() {
        return cookie.entrySet().stream()
                .map(entry -> entry.getKey() + COOKIE_KEY_VALUE_DELIMITER + entry.getValue())
                .collect(Collectors.toList());
    }
}
