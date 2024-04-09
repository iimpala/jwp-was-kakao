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
            String[] splitUri = requestUri.split("\\?");
            requestUri = splitUri[0];
            queryParams = parseQueryString(splitUri[1]);
        }

        return new HttpRequest(method, requestUri, queryParams, httpVersion, null, null);
    }

    private static Map<String, String> parseQueryString(String queryString) {
        Map<String, String> queryParams = new HashMap<>();
        String[] splitParams = queryString.split("\\&");

        for (String splitParam : splitParams) {
            String[] splitParamElements = splitParam.split("=");
            String key = splitParamElements[0];
            String value = splitParamElements.length < 2 ? "" : splitParamElements[1];

            queryParams.put(key, value);
        }

        return queryParams;
    }

    public static String parseExt(String requestUri) {
        String[] splitResource = requestUri.split("\\.");
        return splitResource.length < 2 ? null : splitResource[splitResource.length - 1];
    }
}
