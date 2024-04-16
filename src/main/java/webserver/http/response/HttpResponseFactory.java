package webserver.http.response;

import webserver.http.request.HttpRequest;

public class HttpResponseFactory {

    private static final String HTTP_VERSION = "HTTP/1.1";
    private static final byte[] EMPTY_BODY = "".getBytes();
    private static final String PROTOCOL = "http://";

    public static HttpResponse ok(byte[] body) {
        StatusLine statusLine = new StatusLine(HTTP_VERSION, HttpStatusCode.OK);

        HttpResponseHeader header = new HttpResponseHeader();
        header.addHeader("Content-Length", String.valueOf(body.length));

        return new HttpResponse(statusLine, header, body);
    }

    public static HttpResponse redirect(HttpRequest request, String redirectUri) {
        StatusLine statusLine = new StatusLine(HTTP_VERSION, HttpStatusCode.FOUND);

        HttpResponseHeader header = new HttpResponseHeader();
        String host = request.getHeader().getHost();
        header.addHeader("Location", PROTOCOL + host + redirectUri);

        return new HttpResponse(statusLine, header, EMPTY_BODY);
    }

    public static HttpResponse notFound() {
        StatusLine statusLine = new StatusLine(HTTP_VERSION, HttpStatusCode.NOT_FOUND);
        HttpResponseHeader header = new HttpResponseHeader();
        return new HttpResponse(statusLine, header, EMPTY_BODY);
    }

    public static HttpResponse badRequest() {
        StatusLine statusLine = new StatusLine(HTTP_VERSION, HttpStatusCode.BAD_REQUEST);
        HttpResponseHeader header = new HttpResponseHeader();
        return new HttpResponse(statusLine, header, EMPTY_BODY);
    }
}
