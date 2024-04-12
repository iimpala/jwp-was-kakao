package webserver.request;

import java.util.Map;

public class HttpRequest {

    private final RequestLine requestLine;
    private final HttpRequestHeader header;
    private final String body;
    private final Map<String, String> queryParams;

    public HttpRequest(RequestLine requestLine, HttpRequestHeader header, String body, Map<String, String> queryParams) {
        this.requestLine = requestLine;
        this.header = header;
        this.body = body;
        this.queryParams = queryParams;
    }

    public RequestLine getRequestLine() {
        return requestLine;
    }

    public String getMethod() {
        return requestLine.getMethod();
    }

    public String getPath() {
        return requestLine.getPath();
    }

    public String getQueryString() {
        return requestLine.getQueryString();
    }

    public Map<String, String> getQueryParams() {
        return Map.copyOf(queryParams);
    }

    public HttpRequestHeader getHeader() {
        return header;
    }

    public String getBody() {
        return body;
    }
}
