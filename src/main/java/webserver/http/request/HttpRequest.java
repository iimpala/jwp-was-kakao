package webserver.http.request;

import webserver.http.HttpCookie;

import java.util.Map;

public class HttpRequest {

    private final RequestLine requestLine;
    private final HttpRequestHeader header;
    private final Map<String, String> queryParams;
    private final Map<String, HttpCookie> cookie;
    private final String body;

    public HttpRequest(RequestLine requestLine,
                       HttpRequestHeader header,
                       Map<String, String> queryParams,
                       Map<String, HttpCookie> cookie,
                       String body) {
        this.requestLine = requestLine;
        this.header = header;
        this.queryParams = queryParams;
        this.cookie = cookie;
        this.body = body;
    }

    public boolean isGet() {
        return HttpRequestMethod.GET.equals(this.getMethod());
    }

    public boolean isPost() {
        return HttpRequestMethod.POST.equals(this.getMethod());
    }

    public RequestLine getRequestLine() {
        return requestLine;
    }

    public HttpRequestMethod getMethod() {
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

    public Map<String, HttpCookie> getCookie() {
        return cookie;
    }

    public String getBody() {
        return body;
    }
}
