package webserver;

import service.UserDto;
import service.UserService;
import utils.FileIoUtils;
import utils.parser.DataFormat;
import utils.parser.DataParser;
import webserver.request.HttpRequest;
import webserver.response.HttpResponse;
import webserver.response.HttpResponseHeader;
import webserver.response.HttpStatusCode;
import webserver.response.StatusLine;

import java.io.IOException;
import java.net.URISyntaxException;
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
        String path = request.getPath();
        String extension = parseExt(path);

        String pathPrefix = "html".equals(extension) ? "./templates" : "./static";
        byte[] body = FileIoUtils.loadFileFromClasspath(pathPrefix + path);

        HttpResponseHeader header = new HttpResponseHeader();
        header.addHeader("Content-Type", "text/" + extension + ";charset=utf-8");
        header.addHeader("Content-Length", String.valueOf(body.length));

        StatusLine statusLine = new StatusLine("HTTP/1.1", HttpStatusCode.OK);
        return new HttpResponse(statusLine, header, body);
    }

    private HttpResponse doPost(HttpRequest request) {
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

            HttpResponseHeader header = new HttpResponseHeader();

            String host = request.getHeader().getHost();
            header.addHeader("Location", "http://" + host + "/index.html");

            StatusLine statusLine = new StatusLine("HTTP/1.1", HttpStatusCode.FOUND);
            return new HttpResponse(statusLine, header, "".getBytes());
        }

        return null;
    }

    public static String parseExt(String requestUri) {
        String[] splitResource = requestUri.split("\\.");
        return splitResource.length >= 2 ? splitResource[splitResource.length - 1] : null;
    }
}
