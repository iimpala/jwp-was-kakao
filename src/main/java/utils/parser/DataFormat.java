package utils.parser;

import java.util.Arrays;
import java.util.Optional;

public enum DataFormat {
    URL_ENCODED("application/x-www-form-urlencoded"),
    JSON("application/json"),
    ;

    private final String contentType;

    DataFormat(String contentType) {
        this.contentType = contentType;
    }

    public static Optional<DataFormat> ofContentType(String contentType) {
        return Arrays.stream(DataFormat.values())
                .filter(dataFormat -> dataFormat.contentType.equals(contentType))
                .findFirst();
    }
}
