package webserver.http.request;

import utils.IOUtils;
import utils.parser.DataFormat;
import utils.parser.DataParser;
import webserver.http.HttpCookie;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class HttpRequestFactory {
    private static final String COOKIE_DELIMITER = ";";
    private static final DataParser parser = DataParser.get(DataFormat.URL_ENCODED);

    public static HttpRequest createRequest(BufferedReader reader) throws Exception {
        RequestLine requestLine = RequestLine.of(reader.readLine());

        String queryString = requestLine.getQueryString();
        Map<String, String> queryParams = parser.parse(queryString);

        HttpRequestHeader header = new HttpRequestHeader(getHeaderLines(reader));
        List<HttpCookie> cookie = parseCookies(header.getCookie());

        int contentLength = header.getContentLength();
        String body = getRequestBody(reader, contentLength);

        return new HttpRequest(requestLine, header, queryParams, cookie, body);
    }

    private static List<HttpCookie> parseCookies(String cookieHeader) {
        if (cookieHeader.isEmpty() || cookieHeader.isBlank()) {
            return new ArrayList<>();
        }

        String[] cookiePairs = cookieHeader.split(COOKIE_DELIMITER);

        return Arrays.stream(cookiePairs)
                .map(HttpCookie::of)
                .collect(Collectors.toList());
    }

    private static String getRequestBody(BufferedReader reader, int contentLength) throws IOException {
        String body = IOUtils.readData(reader, contentLength);

        if (body.length() != contentLength) {
            throw new RuntimeException("Illegal Content-Length");
        }

        return body;
    }

    private static List<String> getHeaderLines(BufferedReader reader) throws IOException {
        List<String> headerLines = new ArrayList<>();

        String line;
        while ((line = reader.readLine()) != null && !line.isEmpty()) {
            headerLines.add(line);
        }

        return headerLines;
    }
}
