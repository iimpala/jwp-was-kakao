package webserver.http.request;

import webserver.http.HttpCookie;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpRequestHeader {
    private static final String HEADER_DELIMITER = ": ";
    private static final int HEADER_KEY_INDEX = 0;
    private static final int HEADER_VALUE_INDEX = 1;

    private final Map<String, String> headers;
    private final HttpCookie cookie;

    public HttpRequestHeader(Map<String, String> headers, HttpCookie cookie) {
        this.headers = headers;
        this.cookie = cookie;
    }

    public HttpRequestHeader(Map<String, String> headers) {
        this(headers, parseCookie(headers));
    }

    public HttpRequestHeader(List<String> headerString) {
        this(parseHeaders(headerString));
    }

    private static Map<String, String> parseHeaders(List<String> headerString) {
        HashMap<String, String> headers = new HashMap<>();

        for (String headerLine : headerString) {
            String[] header = headerLine.split(HEADER_DELIMITER);
            headers.put(header[HEADER_KEY_INDEX], header[HEADER_VALUE_INDEX]);
        }

        return headers;
    }

    private static HttpCookie parseCookie(Map<String, String> headers) {
        if (headers.containsKey("Cookie")) {
            return HttpCookie.of(headers.get("Cookie"));
        }

        return new HttpCookie();
    }

    public String getContentType() {
        return headers.get("Content-Type");
    }

    public int getContentLength() {
        return Integer.parseInt(headers.getOrDefault("Content-Length", "0"));
    }

    public String getHost() {
        return headers.get("Host");
    }

    public String getConnection() {
        return headers.get("Connection");
    }

    public String getAccept() {
        return headers.get("Accept");
    }

    public HttpCookie getCookie() {
        return cookie;
    }
}
