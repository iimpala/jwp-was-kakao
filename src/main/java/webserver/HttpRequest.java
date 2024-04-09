package webserver;

import java.util.Map;

public class HttpRequest {

    private final String method;
    private final String requestUri;
    private final Map<String, String> queryParams;
    private final String httpVersion;
    private final Map<String, String> headers;
    private final String body;

    public HttpRequest(String method, String path, Map<String, String> queryParams, String protocol, Map<String, String> headers, String body) {
        this.method = method;
        this.requestUri = path;
        this.queryParams = queryParams;
        this.httpVersion = protocol;
        this.headers = headers;
        this.body = body;
    }

    public String getMethod() {
        return method;
    }

    public String getRequestUri() {
        return requestUri;
    }

    public Map<String, String> getQueryParams() {
        return queryParams;
    }

    public String getHttpVersion() {
        return httpVersion;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public String getBody() {
        return body;
    }
}
