package webserver.http.response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.http.HttpCookie;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HttpResponse {
    private static final Logger logger = LoggerFactory.getLogger(HttpResponse.class);

    private final StatusLine statusLine;
    private final HttpResponseHeader header;
    private final List<HttpCookie> cookies;
    private final byte[] body;

    public HttpResponse(StatusLine statusLine, HttpResponseHeader header, List<HttpCookie> cookies, byte[] body) {
        this.statusLine = statusLine;
        this.header = header;
        this.cookies = cookies;
        this.body = body;
    }

    public HttpResponse(StatusLine statusLine, HttpResponseHeader header, byte[] body) {
        this.statusLine = statusLine;
        this.header = header;
        this.cookies = new ArrayList<>();
        this.body = body;
    }

    public void addHeader(String key, String value) {
        header.addHeader(key, value);
    }

    public void addCookie(HttpCookie cookie) {
        cookies.add(cookie);
    }

    public byte[] getBytes() {
        byte[] responseHeader = getResponseHeader();
        byte[] cookieHeader = getCookieHeader();

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            out.write(responseHeader);
            out.write(cookieHeader);
            out.write("\r\n".getBytes());
            out.write(body);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }

        return out.toByteArray();
    }

    private byte[] getResponseHeader() {
        return (String.valueOf(statusLine) + header).getBytes();
    }

    private byte[] getCookieHeader() {
        StringBuilder sb = new StringBuilder();
        cookies.forEach(cookie -> sb.append("Set-Cookie: ").append(cookie).append("\r\n"));

        return sb.toString().getBytes();
    }
}
