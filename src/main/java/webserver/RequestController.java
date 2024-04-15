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
import java.util.Optional;

public class RequestController {
    private final DataParser parser = DataParser.get(DataFormat.URL_ENCODED);
    private final UserService service = new UserService();

    public HttpResponse service(HttpRequest request) throws IOException, URISyntaxException {
        if (request.isGet()) {
            return doGet(request);
        }

        if (request.isPost()) {
            return doPost(request);
        }

        return HttpResponseFactory.badRequest();
    }

    private HttpResponse doGet(HttpRequest request) throws IOException, URISyntaxException {
        String path = request.getPath();
        String extension = parseExt(path);

        Optional<Resource> optionalResource = Resource.ofExt(extension);

        if (optionalResource.isPresent()) {
            Resource resource = optionalResource.get();
            return serveResource(resource, path, extension);
        }

        if ("/user/create".equals(path)) {
            return saveUser(request, request.getQueryParams());
        }

        return HttpResponseFactory.badRequest();
    }

    private HttpResponse doPost(HttpRequest request) {
        if ("/user/create".equals(request.getPath())) {
            String body = request.getBody();
            return saveUser(request, parser.parse(body));
        }

        return HttpResponseFactory.badRequest();
    }

    private HttpResponse serveResource(Resource resource, String path, String extension) throws IOException, URISyntaxException {
        byte[] body = FileIoUtils.loadFileFromClasspath(resource.getPrefix() + path);

        HttpResponse response = HttpResponseFactory.ok(body);
        response.addHeader("Content-Type", "text/" + extension + ";charset=utf-8");

        return response;
    }

    private static String parseExt(String requestUri) {
        String[] splitResource = requestUri.split("\\.");
        return splitResource.length >= 2 ? splitResource[splitResource.length - 1] : null;
    }

    private HttpResponse saveUser(HttpRequest request, Map<String, String> param) {
        UserDto userDto = new UserDto(
                param.getOrDefault("userId", ""),
                param.getOrDefault("password", ""),
                param.getOrDefault("name", ""),
                param.getOrDefault("email", "")
        );
        service.save(userDto);

        return HttpResponseFactory.redirect(request, "/index.html");
    }
}
