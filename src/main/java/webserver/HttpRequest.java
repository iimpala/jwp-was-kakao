package webserver;

import java.util.Map;

public class HttpRequest {

    private final HttpHeader header;
    private String body;
    private final Map<String, String> queryParams;

    public HttpRequest(HttpHeader header, String body, Map<String, String> queryParams) {
        this.header = header;
        this.body = body;
        this.queryParams = queryParams;
    }

    public Map<String, String> getQueryParams() {
        return queryParams;
    }

    public HttpHeader getHeader() {
        return header;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
