package nguyenthanhtung.datn.service.notification;

import nguyenthanhtung.datn.dto.base.BaseResponse;
import nguyenthanhtung.datn.dto.notification.request.CreateNotificationDTO;
import nguyenthanhtung.datn.dto.notification.request.UpdateIsReadNotificationDTO;

public interface NotificationService {

    BaseResponse getAll(boolean isDoctor, int idDoctor);
    BaseResponse create(CreateNotificationDTO createNotificationDTO);
    BaseResponse createRequest(CreateNotificationDTO createNotificationDTO);
    BaseResponse countNotification();
    BaseResponse updateIsRead(UpdateIsReadNotificationDTO updateIsReadNotificationDTO);
    BaseResponse countNotificationisWsDoctor(Integer idUser);
    BaseResponse countNotificationisWsPatient(Integer idUser);
}
