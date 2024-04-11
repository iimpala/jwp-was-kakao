package webserver;

import java.util.Map;

public class HttpHeader {

    private final RequestLine requestLine;
    private final Map<String, String> headers;

    public HttpHeader(RequestLine requestLine, Map<String, String> headers) {
        this.requestLine = requestLine;
        this.headers = headers;
    }

    public int getContentLength() {
        return Integer.parseInt(headers.get("Content-Length"));
    }

    public RequestLine getRequestLine() {
        return requestLine;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }
}
