package nguyenthanhtung.datn.repository.notification;

import nguyenthanhtung.datn.dto.notification.request.CreateNotificationDTO;
import nguyenthanhtung.datn.dto.notification.request.UpdateIsReadNotificationDTO;
import nguyenthanhtung.datn.entity.base.RepositoryResult;

import java.util.List;
import java.util.Map;

public interface NotificationRepository {

    List<Map<String, Object>> getNotificationByIdUserPatientsAndIdNFT(int idUser, int idNFT);
    List<Map<String, Object>> getNotificationByIdUserPatients(int idUser);
    List<Map<String, Object>> getNotificationByIdUserDoctor(int idUser);
    RepositoryResult createNotification(CreateNotificationDTO createNotificationDTO);
    Integer countNotificationPatients(int idUser);
    Integer countNotificationDoctor(int idUser);
    void updateIsRead(UpdateIsReadNotificationDTO updateIsReadNotificationDTO);
}
