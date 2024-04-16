package webserver;

import java.util.Arrays;
import java.util.Optional;

public enum Resource {
    HTML("./templates", "html"),
    JAVASCRIPT("./static", "js"),
    CSS("./static", "css"),
    WOFF("./static", "woff"),
    TTF("./static", "ttf"),
    ICO("./template", "ico")
    ;

    private final String prefix;
    private final String ext;

    Resource(String path, String ext) {
        this.prefix = path;
        this.ext = ext;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getExt() {
        return ext;
    }

    public static Optional<Resource> ofExt(String extension) {
        return Arrays.stream(Resource.values())
                .filter(resource -> resource.getExt().equals(extension))
                .findFirst();
    }
}
