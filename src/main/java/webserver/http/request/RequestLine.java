package webserver.http.request;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

public class RequestLine {
    private static final String REQUEST_LINE_DELIMITER = " ";
    private static final int METHOD_INDEX = 0;
    private static final int PATH_INDEX = 1;
    private static final int VERSION_INDEX = 2;

    private final HttpRequestMethod method;
    private final RequestUri requestUri;
    private final String httpVersion;

    private RequestLine(HttpRequestMethod method, RequestUri requestUri, String httpVersion) {
        this.method = method;
        this.requestUri = requestUri;
        this.httpVersion = httpVersion;
    }

    public static RequestLine of(String requestLine) {
        String[] requestLineElements = requestLine.split(REQUEST_LINE_DELIMITER);

        String method = requestLineElements[METHOD_INDEX];
        String requestUri = URLDecoder.decode(requestLineElements[PATH_INDEX], StandardCharsets.UTF_8);
        String httpVersion = requestLineElements[VERSION_INDEX];

        return new RequestLine(HttpRequestMethod.valueOf(method), RequestUri.of(requestUri), httpVersion);
    }

    public HttpRequestMethod getMethod() {
        return method;
    }

    public String getPath() {
        return requestUri.getPath();
    }

    public String getQueryString() {
        return requestUri.getQueryString();
    }

    public String getHttpVersion() {
        return httpVersion;
    }
}
