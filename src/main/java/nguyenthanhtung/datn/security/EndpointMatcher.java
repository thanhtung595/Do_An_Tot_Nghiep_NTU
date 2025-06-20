package nguyenthanhtung.datn.security;

import org.springframework.http.HttpMethod;
import org.springframework.util.AntPathMatcher;

public class EndpointMatcher {
    private final HttpMethod method;
    private final String path;
    private static final AntPathMatcher pathMatcher = new AntPathMatcher();

    public EndpointMatcher(HttpMethod method, String path) {
        this.method = method;
        this.path = normalizePath(path);
    }

    public boolean matches(HttpMethod requestMethod, String requestPath) {
        if (!method.equals(requestMethod)) return false;
        return pathMatcher.match(this.path, normalizePath(requestPath));
//        return normalizePath(requestPath).equals(this.path);
    }

    public HttpMethod method() {
        return method;
    }

    public String path() {
        return path;
    }

    private String normalizePath(String path) {
        if (path == null) return "";
        return path.endsWith("/") && path.length() > 1
                ? path.substring(0, path.length() - 1)
                : path;
    }
}

