package webserver.response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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


    public byte[] getBytes() {
        byte[] responseHeader = (String.valueOf(statusLine) + header).getBytes();

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            out.write(responseHeader);
            out.write(body);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }

        return out.toByteArray();
    }
}
