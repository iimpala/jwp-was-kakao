package webserver;

import java.io.*;
import java.net.Socket;
import java.net.URISyntaxException;
import java.util.*;

import db.DataBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.UserDto;
import service.UserService;
import utils.FileIoUtils;
import utils.HttpRequestParser;
import utils.IOUtils;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private final Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {

            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            DataOutputStream dos = new DataOutputStream(out);

            List<String> headerLines = getHeaderLines(reader);

            HttpRequest request = HttpRequestParser.parse(headerLines);

            String body = "";
            if ("POST".equals(request.getHeader().getRequestLine().getMethod())) {
                int contentLength = request.getHeader().getContentLength();
                body = IOUtils.readData(reader, contentLength);
            }
            request.setBody(body);

            HttpResponse response = handleRequest(request);
            sendResponse(dos, response);
        } catch (IOException | URISyntaxException e) {
            logger.error(e.getMessage());
        }
    }

    private HttpResponse handleRequest(HttpRequest request) throws IOException, URISyntaxException {
        Map<String, String> queryParams = request.getQueryParams();
        RequestLine requestLine = request.getHeader().getRequestLine();
        String requestUri = requestLine.getRequestUri();

        Map<String, String> responseHeader = new HashMap<>();

        if ("GET".equals(requestLine.getMethod())) {
            String extension = HttpRequestParser.parseExt(requestUri);
            String pathPrefix = "html".equals(extension) ? "./templates" : "./static";
            byte[] body = FileIoUtils.loadFileFromClasspath(pathPrefix + requestUri);

            responseHeader.put("Content-Type", "text/" + extension + ";charset=utf-8");
            responseHeader.put("Content-Length", String.valueOf(body.length));

            return new HttpResponse("HTTP/1.1", 200, "OK",
                    responseHeader, body);

        } else {
            String body = request.getBody();
            Map<String, String> params = HttpRequestParser.parseUrlEncodedString(body);

            UserDto userDto = new UserDto(
                    params.getOrDefault("userId", ""),
                    params.getOrDefault("password", ""),
                    params.getOrDefault("name", ""),
                    params.getOrDefault("email", "")
            );
            UserService service = new UserService();
            service.save(userDto);

            String host = request.getHeader().getHeaders().get("Host");
            responseHeader.put("Location", "http://" + host + "/index.html");

            return new HttpResponse("HTTP/1.1", 302, "FOUND",
                    responseHeader, "".getBytes());
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

    private void sendResponse(DataOutputStream dos, HttpResponse response) {
        try {
            dos.write(response.getBytes());
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
