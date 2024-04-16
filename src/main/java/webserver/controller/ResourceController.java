package webserver.controller;

import utils.FileIoUtils;
import webserver.Resource;
import webserver.request.HttpRequest;
import webserver.response.*;

import java.util.Optional;

public class ResourceController extends AbstractController {

    @Override
    protected HttpResponse doGet(HttpRequest request) {
        String path = request.getPath();
        String extension = parseExt(path);

        System.out.println("path = " + path);

        Optional<Resource> optionalResource = Resource.ofExt(extension);

        if (optionalResource.isPresent()) {
            Resource resource = optionalResource.get();
            return serveResource(resource, path, extension);
        }

        return HttpResponseFactory.badRequest();
    }

    private HttpResponse serveResource(Resource resource, String path, String extension) {
        try {
            byte[] body = FileIoUtils.loadFileFromClasspath(resource.getPrefix() + path);
            HttpResponse response = HttpResponseFactory.ok(body);
            response.addHeader("Content-Type", "text/" + extension + ";charset=utf-8");

            return response;
        } catch (Exception ignore) {
            return HttpResponseFactory.badRequest();
        }
    }

    private String parseExt(String requestUri) {
        String[] splitResource = requestUri.split("\\.");
        return splitResource.length >= 2 ? splitResource[splitResource.length - 1] : null;
    }
}
