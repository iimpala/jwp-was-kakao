package webserver.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.http.request.HttpRequest;
import webserver.http.response.HttpResponse;
import webserver.http.response.HttpResponseFactory;

public class AbstractController implements Controller {
    private static final Logger logger = LoggerFactory.getLogger(AbstractController.class);

    @Override
    public HttpResponse service(HttpRequest request) {
        try {
            return doService(request);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return HttpResponseFactory.badRequest();
        }
    }

    private HttpResponse doService(HttpRequest request) {
        if (request.isGet()) {
            return doGet(request);
        }
        if (request.isPost()) {
            return doPost(request);
        }
        return HttpResponseFactory.badRequest();
    }

    protected HttpResponse doGet(HttpRequest request) {
        throw new IllegalStateException();
    }

    protected HttpResponse doPost(HttpRequest request) {
        throw new IllegalStateException();
    }
}
