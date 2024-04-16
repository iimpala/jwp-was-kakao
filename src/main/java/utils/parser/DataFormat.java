package utils.parser;

import webserver.ContentType;

import java.util.Arrays;
import java.util.Optional;

public enum DataFormat {
    URL_ENCODED(ContentType.APPLICATION_FORM_URLENCODED),
    JSON(ContentType.APPLICATION_JSON),
    ;

    private final ContentType contentType;

    DataFormat(ContentType contentType) {
        this.contentType = contentType;
    }

    public static DataFormat ofContentType(ContentType contentType) {
        Optional<DataFormat> optionalDataFormat = Arrays.stream(DataFormat.values())
                .filter(dataFormat -> dataFormat.contentType.equals(contentType))
                .findFirst();
        return optionalDataFormat.orElseThrow(IllegalArgumentException::new);
    }
}
