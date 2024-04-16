package webserver.http.response;

import java.util.HashMap;
import java.util.Map;

public class HttpResponseHeader {
    private final String HEADER_KEY_VALUE_DELIMITER = ": ";

    private final Map<String, String> headers;

    public HttpResponseHeader(Map<String, String> headers) {
        this.headers = headers;
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        headers.forEach((key, value) -> sb.append(key).append(HEADER_KEY_VALUE_DELIMITER).append(value).append("\r\n"));

        return sb.toString();
    }
}
