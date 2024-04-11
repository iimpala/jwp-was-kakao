package webserver;

public class RequestLine {

    private final String method;
    private final String requestUri;
    private final String httpVersion;

    public RequestLine(String method, String requestUri, String httpVersion) {
        this.method = method;
        this.requestUri = requestUri;
        this.httpVersion = httpVersion;
    }

    public String getMethod() {
        return method;
    }

    public String getRequestUri() {
        return requestUri;
    }

    public String getHttpVersion() {
        return httpVersion;
    }
}
