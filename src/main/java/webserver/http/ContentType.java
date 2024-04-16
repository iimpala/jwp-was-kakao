package webserver.http;

import java.util.Arrays;
import java.util.Optional;

public enum ContentType {

    APPLICATION_FORM_URLENCODED("application/x-www-form-urlencoded"),
    APPLICATION_JSON("application/json"),
    APPLICATION_JAVASCRIPT("application/javascript"),
    TEXT_JAVASCRIPT("text/javascript"),
    TEXT_CSS("text/css"),
    TEXT_HTML("text/html"),
    TEXT_PLAIN("text/plain"),
    ;
    private final String value;

    ContentType(String value) {
        this.value = value;
    }

    public static ContentType of(String value) {
        Optional<ContentType> optionalContentType = Arrays.stream(ContentType.values())
                .filter(contentType -> contentType.value.equals(value))
                .findFirst();

        return optionalContentType.orElseThrow(IllegalArgumentException::new);
    }
}
