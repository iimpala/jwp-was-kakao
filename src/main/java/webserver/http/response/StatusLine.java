package webserver.http.response;

public class StatusLine {

    private final String httpVersion;
    private final HttpStatusCode statusCode;

    public StatusLine(String httpVersion, HttpStatusCode statusCode) {
        this.httpVersion = httpVersion;
        this.statusCode = statusCode;
    }

    @Override
    public String toString() {
        return httpVersion + " " +
                statusCode.getStatusCode() + " " +
                statusCode.name() + " \r\n";
    }
}
