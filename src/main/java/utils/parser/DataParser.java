package utils.parser;

import webserver.ContentType;

import java.util.Map;

public interface DataParser {

    static DataParser get(String contentTypeHeader) {
        ContentType contentType = ContentType.of(contentTypeHeader);
        DataFormat dataFormat = DataFormat.ofContentType(contentType);

        if (dataFormat.equals(DataFormat.URL_ENCODED)) {
            return UrlEncodedDataParser.getInstance();
        }

        return null;
    }

    static DataParser get(DataFormat dataFormat) {
        if (dataFormat.equals(DataFormat.URL_ENCODED)) {
            return UrlEncodedDataParser.getInstance();
        }

        return null;
    }
    Map<String, String> parse(String data);
}
