package nguyenthanhtung.datn.repository.notification.implement;

import lombok.RequiredArgsConstructor;
import nguyenthanhtung.datn.constants.ConstantQuery.doctor.ConstantQueryDoctor;
import nguyenthanhtung.datn.constants.ConstantQuery.notification.ConstantQueryNotification;
import nguyenthanhtung.datn.constants.ContantApplication;
import nguyenthanhtung.datn.dto.notification.request.CreateNotificationDTO;
import nguyenthanhtung.datn.dto.notification.request.UpdateIsReadNotificationDTO;
import nguyenthanhtung.datn.entity.base.RepositoryResult;
import nguyenthanhtung.datn.repository.notification.NotificationRepository;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static nguyenthanhtung.datn.util.JdbcUtils.safeQueryForSingleId;

@Repository
@RequiredArgsConstructor
public class ImplNotificationRepository implements NotificationRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Map<String, Object>> getNotificationByIdUserPatients(int idUser) {
        return jdbcTemplate.query(ConstantQueryNotification.SELECT_NOTIFICATION_BY_ID_PATIENTS, new ColumnMapRowMapper(), idUser);
    }

    @Override
    public List<Map<String, Object>> getNotificationByIdUserPatientsAndIdNFT(int idUser, int idNFT) {
        return jdbcTemplate.query(ConstantQueryNotification.SELECT_NOTIFICATION_BY_ID_PATIENTS_ID_NFT, new ColumnMapRowMapper(), idUser, idNFT);
    }

    @Override
    public List<Map<String, Object>> getNotificationByIdUserDoctor(int idUser) {
        return jdbcTemplate.query(ConstantQueryNotification.SELECT_NOTIFICATION_BY_ID_DOCTOR, new ColumnMapRowMapper(), idUser);
    }

    @Override
    public RepositoryResult createNotification(CreateNotificationDTO createNotificationDTO) {
        RepositoryResult result = new RepositoryResult();

        // Tao tham so create Notification
        Map<String, Object> paramsCreateNotification = new HashMap<>();
        paramsCreateNotification.put("title", createNotificationDTO.getTitle());
        paramsCreateNotification.put("content", createNotificationDTO.getContent());
        paramsCreateNotification.put("idtype", createNotificationDTO.getIdType());
        paramsCreateNotification.put("appointmentdate", createNotificationDTO.getAppointmentDate());
        paramsCreateNotification.put("appointmenttime", createNotificationDTO.getAppointmentTime());
        paramsCreateNotification.put("patientid", createNotificationDTO.getPatientID());
        paramsCreateNotification.put("doctorid", createNotificationDTO.getDoctorid());
        paramsCreateNotification.put("namerole", createNotificationDTO.getNameRole());
        paramsCreateNotification.put("status", createNotificationDTO.getStatus());
        paramsCreateNotification.put("userfrom", createNotificationDTO.getUserfrom());
        paramsCreateNotification.put("userto", createNotificationDTO.getUserto());

        Integer idNotification = namedParameterJdbcTemplate.queryForObject(
                ConstantQueryNotification.CREATE_NOTIFICATION,
                new MapSqlParameterSource(paramsCreateNotification),
                Integer.class
        );

        result.setStatusCode(ContantApplication.StatusSuccess);
        result.setMessage(ContantApplication.MessageSuccess);
        result.setIdResult(idNotification);
        return result;
    }

    @Override
    public Integer countNotificationPatients(int idUser) {
        return safeQueryForSingleId(jdbcTemplate, ConstantQueryNotification.COUNT_NOTIFICATION_PATIENTS, new Object[]{idUser});
    }

    @Override
    public Integer countNotificationDoctor(int idUser) {
        return safeQueryForSingleId(jdbcTemplate, ConstantQueryNotification.COUNT_NOTIFICATION_DOCTOR, new Object[]{idUser});
    }

    @Override
    public void updateIsRead(UpdateIsReadNotificationDTO updateIsReadNotificationDTO) {
        jdbcTemplate.update(
                ConstantQueryNotification.UPDATE_ISREAD,
                updateIsReadNotificationDTO.getId()
        );
    }
}
