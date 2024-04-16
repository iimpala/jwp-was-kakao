package webserver.response;

public enum HttpStatusCode {
    OK(200),
    FOUND(302),
    BAD_REQUEST(400),
    NOT_FOUND(404),
    ;

    private final int statusCode;

    HttpStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
