package nguyenthanhtung.datn.constants;

import nguyenthanhtung.datn.security.EndpointMatcher;
import org.springframework.http.HttpMethod;

import java.util.List;

public class SecurityConstants {

    public static final List<EndpointMatcher> IGNORED_PATHS_SECURITY = List.of(
            new EndpointMatcher(HttpMethod.GET, "/api/feedback/**"),
            new EndpointMatcher(HttpMethod.GET, "/api/doctor"),
            new EndpointMatcher(HttpMethod.POST, "/api/auth/login"),
            new EndpointMatcher(HttpMethod.POST, "/api/auth/register"),
            new EndpointMatcher(HttpMethod.GET, "/api/header"),
            new EndpointMatcher(HttpMethod.GET, "/api/services"),
            new EndpointMatcher(HttpMethod.GET, "/public/"),
            new EndpointMatcher(HttpMethod.GET, "/swagger-ui/"),
            new EndpointMatcher(HttpMethod.GET, "/v3/api-docs/"),
            new EndpointMatcher(HttpMethod.GET, "/ws-notification/**"),
            new EndpointMatcher(HttpMethod.POST, "/ws-notification/**")
    );

    public static final List<EndpointMatcher> IGNORED_PATHS_FILTER = List.of(
            new EndpointMatcher(HttpMethod.GET, "/api/feedback/**"),
            new EndpointMatcher(HttpMethod.GET, "/api/doctor"),
            new EndpointMatcher(HttpMethod.POST, "/api/auth/login"),
            new EndpointMatcher(HttpMethod.POST, "/api/auth/register"),
            new EndpointMatcher(HttpMethod.GET, "/api/services"),
            new EndpointMatcher(HttpMethod.GET, "/public/"),
            new EndpointMatcher(HttpMethod.GET, "/swagger-ui/"),
            new EndpointMatcher(HttpMethod.GET, "/v3/api-docs/"),
            new EndpointMatcher(HttpMethod.GET, "/ws-notification/**"),
            new EndpointMatcher(HttpMethod.POST, "/ws-notification/**")
    );

}
