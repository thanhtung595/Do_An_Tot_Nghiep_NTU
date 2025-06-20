package nguyenthanhtung.datn.config.webSocket;

import io.jsonwebtoken.Claims;
import nguyenthanhtung.datn.constants.ContantApplication;
import nguyenthanhtung.datn.util.Jwt.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import java.security.Principal;
import java.util.Objects;

@Component
public class StompAuthChannelInterceptor implements ChannelInterceptor {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            // Lấy token từ session attributes (do AuthHandshakeInterceptor đính kèm)
            String token = (String) Objects.requireNonNull(accessor.getSessionAttributes())
                    .get(AuthHandshakeInterceptor.TOKEN_ATTR);

            if (token == null || !jwtTokenProvider.validateToken(token)) {
                return null; // Huỷ kết nối nếu token không hợp lệ
            }

            Claims claims = jwtTokenProvider.getClaims(token);
            String username = claims.getSubject();
            String role = (String) claims.get(ContantApplication.ClaimRoleUser);

            // Gán Principal để convertAndSendToUser() sử dụng
            Principal principal = () -> username;
            accessor.setUser(principal);
        }

        return message;
    }
}