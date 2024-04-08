package webserver;

import java.io.*;
import java.net.Socket;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import db.DataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.FileIoUtils;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {

            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line = reader.readLine();

            // path 가져오기
            String path = "";
            if (!"".equals(line)) {
                String[] headerLine = line.split(" ");
                path = URLDecoder.decode(headerLine[1], StandardCharsets.UTF_8);
            }

            if (path.contains("?")) {
                //user 회원가입
                String[] userUrl = path.split("\\?");
                String userInfo = userUrl[1];

                String userId = userInfo.split("\\&")[0].split("=")[1];
                String password = userInfo.split("\\&")[1].split("=")[1];
                String name = userInfo.split("\\&")[2].split("=")[1];
                String email = userInfo.split("\\&")[3].split("=")[1];

                User user = new User(userId, password, name, email);
                DataBase.addUser(user);

                System.out.println("found: " + DataBase.findUserById(userId));
                byte[] bytes = "ok".getBytes();
                String ext = "text";

                // response message 만들기
                DataOutputStream dos = new DataOutputStream(out);
                response200Header(dos, bytes.length, ext);
                responseBody(dos, bytes);

            } else {
                String[] parsedPath = path.split("\\.");
                String ext = parsedPath[parsedPath.length - 1];

                String pathPrefix = "./";
                if ("html".equals(ext)) {
                    pathPrefix += "templates";
                } else {
                    pathPrefix += "static";
                }


                // index.html 읽기
                byte[] bytes = FileIoUtils.loadFileFromClasspath(pathPrefix + path);

                // response message 만들기
                DataOutputStream dos = new DataOutputStream(out);
                response200Header(dos, bytes.length, ext);
                responseBody(dos, bytes);
            }

        } catch (IOException | URISyntaxException e) {
            logger.error(e.getMessage());
        }
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent, String ext) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/"+ ext +";charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
