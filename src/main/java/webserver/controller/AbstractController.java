package webserver.controller;

import webserver.http.request.HttpRequest;
import webserver.http.response.HttpResponse;
import webserver.http.response.HttpResponseFactory;

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
