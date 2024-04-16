package webserver.http.request;

import utils.IOUtils;
import utils.parser.DataFormat;
import utils.parser.DataParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HttpRequestFactory {
    private static final DataParser parser = DataParser.get(DataFormat.URL_ENCODED);

    public static HttpRequest createRequest(BufferedReader reader) throws Exception {
        RequestLine requestLine = RequestLine.of(reader.readLine());

        String queryString = requestLine.getQueryString();
        Map<String, String> queryParams = parser.parse(queryString);

        HttpRequestHeader header = new HttpRequestHeader(getHeaderLines(reader));

        int contentLength = header.getContentLength();
        String body = getRequestBody(reader, contentLength);

        return new HttpRequest(requestLine, header, body, queryParams);
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
