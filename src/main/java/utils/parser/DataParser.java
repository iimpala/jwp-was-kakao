package utils.parser;

import java.util.Map;

public interface DataParser {

    static DataParser get(DataFormat dataFormat) {
        if (dataFormat.equals(DataFormat.URL_ENCODED)) {
            return UrlEncodedDataParser.getInstance();
        }

        return null;
    }
    Map<String, String> parse(String data);
}
