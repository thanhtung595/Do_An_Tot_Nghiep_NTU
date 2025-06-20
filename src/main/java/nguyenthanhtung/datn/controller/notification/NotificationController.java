package nguyenthanhtung.datn.controller.notification;

import lombok.RequiredArgsConstructor;
import nguyenthanhtung.datn.dto.base.BaseResponse;
import nguyenthanhtung.datn.dto.notification.request.CreateNotificationDTO;
import nguyenthanhtung.datn.dto.notification.request.UpdateIsReadNotificationDTO;
import nguyenthanhtung.datn.service.notification.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notification")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping()
    public ResponseEntity<BaseResponse> getHeader() {
        String subject = "@Get Notification";

        BaseResponse response = notificationService.getAll(false, 0);
        response.setSubjectFunction(subject);

        return ResponseEntity.status(response.getHttpStatusCode()).body(response);
    }

    @GetMapping("/count-isread")
    public ResponseEntity<BaseResponse> getCountIsRead() {
        String subject = "@Get Notification Count IsRead";

        BaseResponse response = notificationService.countNotification();
        response.setSubjectFunction(subject);

        return ResponseEntity.status(response.getHttpStatusCode()).body(response);
    }

    @PutMapping("/update-isread")
    public ResponseEntity<BaseResponse> updateIsRead(@RequestBody UpdateIsReadNotificationDTO updateIsReadNotificationDTO) {
        String subject = "@Put Update IsRead";

        BaseResponse response = notificationService.updateIsRead(updateIsReadNotificationDTO);
        response.setSubjectFunction(subject);

        return ResponseEntity.status(response.getHttpStatusCode()).body(response);
    }

    @PostMapping()
    public ResponseEntity<BaseResponse> createNotification(@RequestBody CreateNotificationDTO createNotificationDTO) {
        String subject = "@Post Notification";

        BaseResponse response = notificationService.createRequest(createNotificationDTO);
        response.setSubjectFunction(subject);

        return ResponseEntity.status(response.getHttpStatusCode()).body(response);
    }
}
