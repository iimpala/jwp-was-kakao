package webserver;

import service.UserDto;
import service.UserService;
import utils.FileIoUtils;
import utils.parser.DataFormat;
import utils.parser.DataParser;
import webserver.request.HttpRequest;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

public class RequestController {
    private final DataParser parser = DataParser.get(DataFormat.URL_ENCODED);
    private final UserService service = new UserService();

    public HttpResponse service(HttpRequest request) throws IOException, URISyntaxException {
        if ("GET".equals(request.getMethod())) {
            return doGet(request);
        }

        if ("POST".equals(request.getMethod())) {
            return doPost(request);
        }

        return null;
    }

    private HttpResponse doGet(HttpRequest request) throws IOException, URISyntaxException {
        Map<String, String> responseHeader = new HashMap<>();

        String path = request.getPath();
        String extension = parseExt(path);

        String pathPrefix = "html".equals(extension) ? "./templates" : "./static";
        byte[] body = FileIoUtils.loadFileFromClasspath(pathPrefix + path);

        responseHeader.put("Content-Type", "text/" + extension + ";charset=utf-8");
        responseHeader.put("Content-Length", String.valueOf(body.length));

        return new HttpResponse("HTTP/1.1", 200, "OK", responseHeader, body);
    }

    private HttpResponse doPost(HttpRequest request) {
        Map<String, String> responseHeader = new HashMap<>();

        if ("/user/create".equals(request.getPath())) {
            String body = request.getBody();
            Map<String, String> params = parser.parse(body);

            UserDto userDto = new UserDto(
                    params.getOrDefault("userId", ""),
                    params.getOrDefault("password", ""),
                    params.getOrDefault("name", ""),
                    params.getOrDefault("email", "")
            );

            service.save(userDto);

            String host = request.getHeader().getHost();
            responseHeader.put("Location", "http://" + host + "/index.html");

            return new HttpResponse("HTTP/1.1", 302, "FOUND",
                    responseHeader, "".getBytes());
        }

        return null;
    }

    public static String parseExt(String requestUri) {
        String[] splitResource = requestUri.split("\\.");
        return splitResource.length >= 2 ? splitResource[splitResource.length - 1] : null;
    }
}
