package webserver.http.response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.http.HttpCookie;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class HttpResponse {
    private static final Logger logger = LoggerFactory.getLogger(HttpResponse.class);

    private final StatusLine statusLine;
    private final HttpResponseHeader header;
    private final byte[] body;

    public HttpResponse(StatusLine statusLine, HttpResponseHeader header, byte[] body) {
        this.statusLine = statusLine;
        this.header = header;
        this.body = body;
    }

    public void addHeader(String key, String value) {
        header.addHeader(key, value);
    }

    public void setCookie(String key, String value) {
        header.getCookie().setCookie(key, value);
    }

    public byte[] getBytes() {
        byte[] responseHeader = (String.valueOf(statusLine) + header).getBytes();

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            out.write(responseHeader);
            out.write("\r\n".getBytes());
            out.write(body);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }

        return out.toByteArray();
    }
}
