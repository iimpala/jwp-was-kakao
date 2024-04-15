package webserver;

import service.UserDto;
import service.UserService;
import utils.FileIoUtils;
import utils.parser.DataFormat;
import utils.parser.DataParser;
import webserver.request.HttpRequest;
import webserver.response.*;

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

        HttpResponse response = HttpResponseFactory.ok(body);
        response.addHeader("Content-Type", "text/" + extension + ";charset=utf-8");

        return response;
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

            return HttpResponseFactory.redirect(request, "/index.html");
        }

        return HttpResponseFactory.badRequest();
    }

    public static String parseExt(String requestUri) {
        String[] splitResource = requestUri.split("\\.");
        return splitResource.length >= 2 ? splitResource[splitResource.length - 1] : null;
    }
}
