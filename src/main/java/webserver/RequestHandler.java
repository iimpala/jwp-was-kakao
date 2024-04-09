package webserver;

import java.io.*;
import java.net.Socket;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.FileIoUtils;
import utils.HttpRequestParser;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {

            BufferedReader reader = new BufferedReader(new InputStreamReader(in));

            List<String> headerLines = getHeaderLines(reader);
            HttpRequest request = HttpRequestParser.parse(headerLines);
            String requestUri = request.getRequestUri();
            String extension = HttpRequestParser.parseExt(requestUri);

            String pathPrefix = "html".equals(extension) ? "./templates" : "./static";

            byte[] bytes = "".getBytes();
            if (extension != null) {
                bytes = FileIoUtils.loadFileFromClasspath(pathPrefix + requestUri);
            }

            DataOutputStream dos = new DataOutputStream(out);
            response200Header(dos, bytes.length, extension);
            responseBody(dos, bytes);
        } catch (IOException | URISyntaxException e) {
            logger.error(e.getMessage());
        }
    }

    private List<String> getHeaderLines(BufferedReader reader) throws IOException {
        List<String> headerLines = new ArrayList<>();

        String line;
        while ((line = reader.readLine()) != null && !line.isEmpty()) {
            headerLines.add(line);
        }

        return headerLines;
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent, String ext) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/" + ext + ";charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
