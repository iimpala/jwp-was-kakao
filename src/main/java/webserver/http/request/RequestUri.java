package webserver.http.request;

public class RequestUri {
    private static final String QUERY_STRING_DELIMITER = "\\?";
    private static final int PATH_INDEX = 0;
    private static final int QUERY_STRING_INDEX = 1;

    private final String path;
    private final String queryString;

    private RequestUri(String path, String queryString) {
        this.path = path;
        this.queryString = queryString;
    }

    public static RequestUri of(String requestUri) {
        String path = requestUri;
        String queryString = "";

        if (requestUri.contains(QUERY_STRING_DELIMITER)) {
            String[] splitRequestUri = requestUri.split(QUERY_STRING_DELIMITER);
            path = splitRequestUri[PATH_INDEX];
            queryString = splitRequestUri[QUERY_STRING_INDEX];
        }

        return new RequestUri(path, queryString);
    }

    public String getPath() {
        return path;
    }

    public String getQueryString() {
        return queryString;
    }
}
