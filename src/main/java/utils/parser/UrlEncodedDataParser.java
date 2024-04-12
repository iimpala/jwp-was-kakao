package utils.parser;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class UrlEncodedDataParser implements DataParser {
    private static final String DATA_DELIMITER = "\\&";
    private static final String KEY_VALUE_DELIMITER = "=";
    private static final int KEY_INDEX = 0;
    private static final int VALUE_INDEX = 1;
    private static final String DEFAULT_VALUE = "";
    private static UrlEncodedDataParser instance;

    private UrlEncodedDataParser() {
    }

    public static synchronized UrlEncodedDataParser getInstance() {
        if (instance == null) {
            instance = new UrlEncodedDataParser();
        }
        return instance;
    }

    @Override
    public Map<String, String> parse(String dataString) {
        String[] dataEntries = dataString.split(DATA_DELIMITER);

        return Arrays.stream(dataEntries)
                .map(entry -> entry.split(KEY_VALUE_DELIMITER))
                .collect(Collectors.toMap(
                        element -> element[KEY_INDEX],
                        element -> ifValuePresent(element) ? element[VALUE_INDEX] : DEFAULT_VALUE
                ));
    }

    private static boolean ifValuePresent(String[] element) {
        return element.length == 2;
    }
}
