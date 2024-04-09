package webserver;

import java.io.*;
import java.net.Socket;
import java.net.URISyntaxException;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.UserDto;
import service.UserService;
import utils.FileIoUtils;
import utils.HttpRequestParser;

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

            List<String> headerLines = getHeaderLines(reader);
            HttpRequest request = HttpRequestParser.parse(headerLines);

            HttpResponse response = handleRequest(request);

            DataOutputStream dos = new DataOutputStream(out);
            sendResponse(dos, response);
        } catch (IOException | URISyntaxException e) {
            logger.error(e.getMessage());
        }
    }

    private HttpResponse handleRequest(HttpRequest request) throws IOException, URISyntaxException {
        Map<String, String> queryParams = request.getQueryParams();
        String requestUri = request.getRequestUri();

        Map<String, String> responseHeader = new HashMap<>();

        if (queryParams.isEmpty()) {
            String extension = HttpRequestParser.parseExt(requestUri);
            String pathPrefix = "html".equals(extension) ? "./templates" : "./static";
            byte[] body = FileIoUtils.loadFileFromClasspath(pathPrefix + requestUri);

            responseHeader.put("Content-Type", "text/" + extension + ";charset=utf-8");
            responseHeader.put("Content-Length", String.valueOf(body.length));

            return new HttpResponse("HTTP/1.1", 200, "OK",
                    responseHeader, body);

        } else {
            if (requestUri.equals("/user/create")) {
                UserDto userDto = new UserDto(
                        queryParams.getOrDefault("userId", ""),
                        queryParams.getOrDefault("password", ""),
                        queryParams.getOrDefault("name", ""),
                        queryParams.getOrDefault("email", "")
                );

                UserService service = new UserService();
                service.save(userDto);
            }

            byte[] body = "ok".getBytes();
            responseHeader.put("Content-Type", "text/plain;charset=utf-8");
            responseHeader.put("Content-Length", String.valueOf(body.length));

            return new HttpResponse("HTTP/1.1", 200, "OK",
                    responseHeader, body);
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
