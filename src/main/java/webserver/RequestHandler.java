package webserver;

import java.io.*;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.controller.Controller;
import webserver.http.request.HttpRequest;
import webserver.http.request.HttpRequestFactory;
import webserver.http.response.HttpResponse;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
    private final HandlerMapping handlerMapping = new HandlerMapping();
    private final Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {

            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            DataOutputStream dos = new DataOutputStream(out);

            HttpRequest request = HttpRequestFactory.createRequest(reader);

            Controller controller = handlerMapping.getController(request.getPath());
            HttpResponse response = controller.service(request);

            sendResponse(dos, response);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
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
