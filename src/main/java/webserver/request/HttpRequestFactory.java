package webserver.request;

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
        String body = IOUtils.readData(reader, contentLength);

        return new HttpRequest(requestLine, header, body, queryParams);
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
