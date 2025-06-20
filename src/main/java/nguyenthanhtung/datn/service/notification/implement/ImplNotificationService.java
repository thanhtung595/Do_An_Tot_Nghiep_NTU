package nguyenthanhtung.datn.service.notification.implement;

import lombok.RequiredArgsConstructor;
import nguyenthanhtung.datn.config.requestThreadLocal.RequestThreadLocalContext;
import nguyenthanhtung.datn.config.requestThreadLocal.RequestUserThreadLocal;
import nguyenthanhtung.datn.constants.ContantApplication;
import nguyenthanhtung.datn.dto.base.BaseResponse;
import nguyenthanhtung.datn.dto.notification.request.CreateNotificationDTO;
import nguyenthanhtung.datn.dto.notification.request.UpdateIsReadNotificationDTO;
import nguyenthanhtung.datn.entity.base.RepositoryResult;
import nguyenthanhtung.datn.exception.SQLDebugException;
import nguyenthanhtung.datn.repository.notification.NotificationRepository;
import nguyenthanhtung.datn.service.notification.NotificationService;
import nguyenthanhtung.datn.util.IdQueryUserUtils;
import nguyenthanhtung.datn.util.WebSocket.WebSocketService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class ImplNotificationService implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final WebSocketService webSocketService;
    private final IdQueryUserUtils idQueryUserUtils;

    @Override
    public BaseResponse getAll(boolean isDoctor, int idDoctor) {
        BaseResponse response = new BaseResponse();
        List<Map<String, Object>> data = null;
        if (!isDoctor){
            RequestUserThreadLocal currentUser = RequestThreadLocalContext.get();
            if (Objects.equals(currentUser.getRole(), ContantApplication.RolePatient)){
                data = notificationRepository.getNotificationByIdUserPatients(currentUser.getUserId());
            } else if (Objects.equals(currentUser.getRole(), ContantApplication.RoleDoctor)){
                data = notificationRepository.getNotificationByIdUserDoctor(currentUser.getUserId());
            }

        } else {
            data = notificationRepository.getNotificationByIdUserDoctor(idDoctor);
        }

        response.setHttpStatusCode(ContantApplication.HttpStatus_OK);
        response.setStatus(ContantApplication.StatusSuccess);
        response.setMessage(ContantApplication.MessageSuccess);
        response.addData("notification", data);
        return response;
    }

    @Override
    public BaseResponse create(CreateNotificationDTO createNotificationDTO) {
        BaseResponse response = new BaseResponse();

        try {
             notificationRepository.createNotification(createNotificationDTO);

            response.setHttpStatusCode(ContantApplication.HttpStatus_OK);
            response.setStatus(ContantApplication.StatusSuccess);
            response.setMessage(ContantApplication.MessageSuccess);
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            response.setHttpStatusCode(ContantApplication.HttpStatus_Internal_Server_Error);
            response.setStatus(ContantApplication.StatusErrorBugException);
            response.setMessage(e.getMessage());
        }
        return response;
    }

    @Override
    public BaseResponse createRequest(CreateNotificationDTO dto) {
        BaseResponse response = new BaseResponse();
        CreateNotificationDTO createNotificationDTO = new CreateNotificationDTO();

        String message;
        try {
            RequestUserThreadLocal currentUser = RequestThreadLocalContext.get();

            // Tao moi
            createNotificationDTO.setTitle(dto.getTitle());
            createNotificationDTO.setContent(dto.getContent());
            createNotificationDTO.setIdType(dto.getIdType());
            createNotificationDTO.setPatientID(dto.getPatientID());
            createNotificationDTO.setDoctorid(currentUser.getUserId());
            createNotificationDTO.setNameRole(ContantApplication.RolePatient);
            createNotificationDTO.setStatus("Đã gửi");
            createNotificationDTO.setUserfrom(dto.getUserfrom());
            createNotificationDTO.setUserto(dto.getUserto());

            RepositoryResult resultNotification = notificationRepository.createNotification(createNotificationDTO);
            // Xử lý các lỗi cụ thể
            if (!Objects.equals(resultNotification.getStatusCode(), ContantApplication.StatusSuccess)){
                throw new SQLDebugException(response.getMessage());
            }

            // Tao thong bao moi
            BaseResponse wsResponseDoctor = new BaseResponse();
            wsResponseDoctor.setHttpStatusCode(ContantApplication.HttpStatus_OK);
            wsResponseDoctor.setStatus(ContantApplication.StatusSuccess);
            wsResponseDoctor.setMessage("Bạn có 1 thông báo mới.");
            Integer idPatient = idQueryUserUtils.getIdUserByPatient(dto.getPatientID());

            // Gui thong bao di
            webSocketService.sendNotificationToCurrentUser(wsResponseDoctor, idPatient);
            // Yeu cau cap nhat so luong thong bao
            BaseResponse countNotificationisWsDoctor = this.countNotificationisWsDoctor(idPatient);

            webSocketService.sendNotificationUpdateCountNotification(countNotificationisWsDoctor, idPatient);
            // Yeu cau cap nhat du lieu thong bao
            webSocketService.sendBodyNotification(response, idPatient);

            response.setHttpStatusCode(ContantApplication.HttpStatus_OK);
            response.setStatus(ContantApplication.StatusSuccess);
            response.setMessage(ContantApplication.MessageSuccess);
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            response.setHttpStatusCode(ContantApplication.HttpStatus_Internal_Server_Error);
            response.setStatus(ContantApplication.StatusErrorBugException);
            response.setMessage(e.getMessage());
        }
        return response;
    }

    @Override
    public BaseResponse countNotificationisWsDoctor(Integer idUser) {
        BaseResponse response = new BaseResponse();
        Integer data = null;
        data = notificationRepository.countNotificationDoctor(idUser);

        response.setHttpStatusCode(ContantApplication.HttpStatus_OK);
        response.setStatus(ContantApplication.StatusSuccess);
        response.setMessage(ContantApplication.MessageSuccess);
        response.addData("notification", data);
        return response;
    }

    @Override
    public BaseResponse countNotificationisWsPatient(Integer idUser) {
        BaseResponse response = new BaseResponse();
        Integer data = null;
        data = notificationRepository.countNotificationPatients(idUser);

        response.setHttpStatusCode(ContantApplication.HttpStatus_OK);
        response.setStatus(ContantApplication.StatusSuccess);
        response.setMessage(ContantApplication.MessageSuccess);
        response.addData("notification", data);
        return response;
    }

    @Override
    public BaseResponse countNotification() {
        BaseResponse response = new BaseResponse();
        Integer data = null;
        RequestUserThreadLocal currentUser = RequestThreadLocalContext.get();
        if (Objects.equals(currentUser.getRole(), ContantApplication.RolePatient)){
            data = notificationRepository.countNotificationPatients(currentUser.getUserId());
        } else if (Objects.equals(currentUser.getRole(), ContantApplication.RoleDoctor)){
            data = notificationRepository.countNotificationDoctor(currentUser.getUserId());
        }

        response.setHttpStatusCode(ContantApplication.HttpStatus_OK);
        response.setStatus(ContantApplication.StatusSuccess);
        response.setMessage(ContantApplication.MessageSuccess);
        response.addData("notification", data);
        return response;
    }

    @Override
    public BaseResponse updateIsRead(UpdateIsReadNotificationDTO updateIsReadNotificationDTO) {
        BaseResponse response = new BaseResponse();

        try {
            notificationRepository.updateIsRead(updateIsReadNotificationDTO);

            response.setHttpStatusCode(ContantApplication.HttpStatus_OK);
            response.setStatus(ContantApplication.StatusSuccess);
            response.setMessage(ContantApplication.MessageSuccess);

            BaseResponse wsResponse = this.countNotification();
            webSocketService.sendNotificationUpdateCountNotification(wsResponse);

        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            response.setHttpStatusCode(ContantApplication.HttpStatus_Internal_Server_Error);
            response.setStatus(ContantApplication.StatusErrorBugException);
            response.setMessage(e.getMessage());
        }
        return response;
    }

}
