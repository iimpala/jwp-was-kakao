package webserver.http;

public class HttpCookie {

    private static final String COOKIE_DELIMITER = ";";
    private static final String COOKIE_KEY_VALUE_DELIMITER = "=";
    private static final int COOKIE_KEY_INDEX = 0;
    private static final int COOKIE_VALUE_INDEX = 1;

    private final String name;
    private final String value;
    private final String path;

    public HttpCookie(String name, String value, String path) {
        this.name = name;
        this.value = value;
        this.path = path;
    }

    public static HttpCookie of(String cookiePair) {
        String[] cookieEntries = cookiePair.split(COOKIE_KEY_VALUE_DELIMITER);
        return new HttpCookie(cookieEntries[COOKIE_KEY_INDEX], cookieEntries[COOKIE_VALUE_INDEX], "");
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(name).append(COOKIE_KEY_VALUE_DELIMITER).append(value).append(" ");

        if (!(path.isEmpty() || path.isBlank())) {
            sb.append(COOKIE_DELIMITER)
                    .append("Path").append(COOKIE_KEY_VALUE_DELIMITER).append(path);
        }

        return sb.toString();
    }
}
