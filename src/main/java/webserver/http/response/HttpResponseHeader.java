package webserver.http.response;

import webserver.http.HttpCookie;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpResponseHeader {
    private final String HEADER_KEY_VALUE_DELIMITER = ": ";

    private final Map<String, String> headers;
    private final HttpCookie cookie;

    public HttpResponseHeader(Map<String, String> headers, HttpCookie cookie) {
        this.headers = headers;
        this.cookie = cookie;
    }

    public HttpResponseHeader(Map<String, String> headers) {
        this(headers, new HttpCookie());
    }

    public HttpResponseHeader() {
        this(new HashMap<>());
    }

    public void addHeader(String key, String value) {
        headers.put(key, value);
    }

    public String getHeader(String key) {
        return headers.get(key);
    }

    public HttpCookie getCookie() {
        return cookie;
    }

    @Override
    public String toString() {
        List<String> cookieValues = cookie.toSetCookieHeaderValues();

        StringBuilder sb = new StringBuilder();
        headers.forEach((key, value) -> sb.append(key).append(HEADER_KEY_VALUE_DELIMITER).append(value).append("\r\n"));
        cookieValues.forEach(value -> sb.append("Set-Cookie").append(HEADER_KEY_VALUE_DELIMITER).append(value).append("\r\n"));

        return sb.toString();
    }
}
