package utils;

import webserver.HttpRequest;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpRequestParser {

    public static HttpRequest parse(List<String> headerString) {
        String[] requestLine = headerString.get(0).split(" ");

        String method = requestLine[0];
        String requestUri = URLDecoder.decode(requestLine[1], StandardCharsets.UTF_8);
        String httpVersion = requestLine[2];

        Map<String, String> queryParams = new HashMap<>();
        if (requestUri.contains("?")) {
            String[] parsed = requestUri.split("\\?");
            requestUri = parsed[0];
            queryParams = parseQueryString(parsed[1]);
        }

        return new HttpRequest(method, requestUri, queryParams, httpVersion, null, null);
    }

    private static Map<String, String> parseQueryString(String queryString) {
        Map<String, String> queryParams = new HashMap<>();
        String[] params = queryString.split("\\&");

        for (String paramString : params) {
            String[] param = paramString.split("=");
            String key = param[0];
            String value = param.length < 2 ? "" : param[1];

            queryParams.put(key, value);
        }

        return queryParams;
    }
}
