package webserver.controller;

import webserver.request.HttpRequest;
import webserver.response.HttpResponse;
import webserver.response.HttpResponseFactory;

public class AbstractController implements Controller{

    @Override
    public HttpResponse service(HttpRequest request) {
        if (request.isGet()) {
            return doGet(request);
        }
        if (request.isPost()) {
            return doPost(request);
        }
        return HttpResponseFactory.badRequest();
    }

    protected HttpResponse doGet(HttpRequest request) {
        return HttpResponseFactory.badRequest();
    }

    protected HttpResponse doPost(HttpRequest request) {
        return HttpResponseFactory.badRequest();
    }
}
