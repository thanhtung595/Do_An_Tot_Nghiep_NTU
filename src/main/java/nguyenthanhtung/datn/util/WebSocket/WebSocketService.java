package nguyenthanhtung.datn.util.WebSocket;

import lombok.RequiredArgsConstructor;
import nguyenthanhtung.datn.dto.base.BaseResponse;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WebSocketService {

    private final SimpMessagingTemplate messagingTemplate;

    // Gửi thông báo riêng tư đến user hiện tại (dựa trên security context)
    public void sendNotificationToCurrentUser(BaseResponse response) {
        // Lấy username từ security context
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();

        // Gửi tin nhắn riêng tư đến user với queue notifications
        messagingTemplate.convertAndSendToUser(userId, "/queue/notifications", response);
    }

    public void sendNotificationToCurrentUser(BaseResponse response, Integer userId) {
        String userIdStr = String.valueOf(userId);
        // Gửi tin nhắn riêng tư đến user với queue notifications
        messagingTemplate.convertAndSendToUser(userIdStr, "/queue/notifications", response);
    }

    // Gửi thông báo riêng tư đến user hiện tại (dựa trên security context)
    public void sendNotificationUpdateCountNotification(BaseResponse response) {
        // Lấy username từ security context
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        // Gửi tin nhắn riêng tư đến user với queue notifications
        messagingTemplate.convertAndSendToUser(userId, "/queue/count/isread", response);
    }

    public void sendNotificationUpdateCountNotification(BaseResponse response, Integer userId) {
        String userIdStr = String.valueOf(userId);
        // Gửi tin nhắn riêng tư đến user với queue notifications
        messagingTemplate.convertAndSendToUser(userIdStr, "/queue/count/isread", response);
    }

    public void sendBodyNotification(BaseResponse response, Integer userId) {
        String userIdStr = String.valueOf(userId);
        // Gửi tin nhắn riêng tư đến user với queue notifications
        messagingTemplate.convertAndSendToUser(userIdStr, "/queue/body/notification", response);
    }
}
