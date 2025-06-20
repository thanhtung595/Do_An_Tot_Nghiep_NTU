package nguyenthanhtung.datn.util.Jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import nguyenthanhtung.datn.config.requestThreadLocal.RequestThreadLocalContext;
import nguyenthanhtung.datn.config.requestThreadLocal.RequestUserThreadLocal;
import nguyenthanhtung.datn.constants.ContantApplication;
import nguyenthanhtung.datn.constants.SecurityConstants;
import nguyenthanhtung.datn.dto.base.BaseResponse;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final String PATH_API_HEADER = "/api/header";

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            String path = request.getRequestURI();

            String header = request.getHeader("Authorization");

            if (header != null && header.startsWith("Bearer ")) {
                String token = header.substring(7);

                try {
                    if (jwtTokenProvider.validateToken(token)) {
                        Claims claims = jwtTokenProvider.getClaims(token);
                        String role = (String) claims.get(ContantApplication.ClaimRoleUser);

                        Integer userId = Integer.parseInt(claims.get(ContantApplication.ClaimUserID).toString());
                        String roleName = claims.get(ContantApplication.ClaimRoleUser).toString();
                        RequestUserThreadLocal requestUserThreadLocal = new RequestUserThreadLocal();
                        requestUserThreadLocal.setUserId(userId);
                        requestUserThreadLocal.setRole(roleName);
                        RequestThreadLocalContext.set(requestUserThreadLocal);

                        List<GrantedAuthority> authorities = List.of(
                                new SimpleGrantedAuthority("ROLE_" + role.toUpperCase())
                        );

                        Authentication authentication = new UsernamePasswordAuthenticationToken(
                                claims.getSubject(),
                                null,
                                authorities
                        );

                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    } else {
                        if (!path.equals(PATH_API_HEADER)){
                            sendErrorResponse(response, "Thiếu Authorization header hoặc định dạng không hợp lệ", ContantApplication.HttpStatus_Unauthorized);
                            return;
                        }
                    }
                } catch (Exception ex) {
                    if (!path.equals(PATH_API_HEADER)){
                        sendErrorResponse(response, "Token không hợp lệ hoặc đã hết hạn", ContantApplication.HttpStatus_Unauthorized);
                        return;
                    }
                }
            } else {
                if (!path.equals(PATH_API_HEADER)){
                    sendErrorResponse(response, "Thiếu Authorization header hoặc định dạng không hợp lệ", ContantApplication.HttpStatus_Unauthorized);
                    return;
                }
            }

            filterChain.doFilter(request, response);
        } finally {
            RequestThreadLocalContext.clear();
            SecurityContextHolder.clearContext();
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        // Bỏ qua nếu là WebSocket handshake
        if (path.startsWith("/ws-notification")) {
            return true;
        }
        HttpMethod method;
        try {
            method = HttpMethod.valueOf(request.getMethod());
        } catch (IllegalArgumentException e) {
            return false; // method không hợp lệ → không bỏ qua filter
        }

        return SecurityConstants.IGNORED_PATHS_FILTER.stream()
                .anyMatch(endpoint -> endpoint.matches(method, path));
    }

    private void sendErrorResponse(HttpServletResponse response, String message, int httpCode) throws IOException {
        String subject = "JWT Filter";
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setSubjectFunction(subject);
        baseResponse.setHttpStatusCode(httpCode);
        baseResponse.setStatus(ContantApplication.StatusErrorClient);
        baseResponse.setMessage(message);

        response.setStatus(httpCode);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        ObjectMapper mapper = new ObjectMapper();
        String jsonResponse = mapper.writeValueAsString(baseResponse);
        response.getWriter().write(jsonResponse);
    }
}
