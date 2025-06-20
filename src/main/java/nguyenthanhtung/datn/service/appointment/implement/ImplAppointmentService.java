package nguyenthanhtung.datn.service.appointment.implement;

import lombok.RequiredArgsConstructor;
import nguyenthanhtung.datn.constants.ContantApplication;
import nguyenthanhtung.datn.dto.appointment.request.RequestSaveAppointmentRecordDTO;
import nguyenthanhtung.datn.dto.appointment.request.RequestUpdateStatusAppointment;
import nguyenthanhtung.datn.dto.base.BaseResponse;
import nguyenthanhtung.datn.dto.notification.request.CreateNotificationDTO;
import nguyenthanhtung.datn.entity.base.RepositoryResult;
import nguyenthanhtung.datn.exception.SQLDebugException;
import nguyenthanhtung.datn.repository.appointment.AppointmentRepository;
import nguyenthanhtung.datn.repository.medicines.MedicinesRepository;
import nguyenthanhtung.datn.repository.notification.NotificationRepository;
import nguyenthanhtung.datn.repository.services.ServicesRepository;
import nguyenthanhtung.datn.service.appointment.AppointmentService;
import nguyenthanhtung.datn.service.notification.NotificationService;
import nguyenthanhtung.datn.util.IdQueryUserUtils;
import nguyenthanhtung.datn.util.TextUtils;
import nguyenthanhtung.datn.util.WebSocket.WebSocketService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class ImplAppointmentService implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final NotificationRepository notificationRepository;
    private final NotificationService notificationService;
    private final WebSocketService webSocketService;
    private final IdQueryUserUtils idQueryUserUtils;
    private final ServicesRepository servicesRepository;
    private final MedicinesRepository medicinesRepository;

    @Override
    public BaseResponse getAppointmentsByUser() {
        BaseResponse response = new BaseResponse();
        try {
            List<Map<String, Object>> data = appointmentRepository.getAppointmentsByUser();

            response.setHttpStatusCode(ContantApplication.HttpStatus_OK);
            response.setStatus(ContantApplication.StatusSuccess);
            response.setMessage(ContantApplication.MessageSuccess);
            response.addData("appointments", data);

        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            response.setHttpStatusCode(ContantApplication.HttpStatus_Internal_Server_Error);
            response.setStatus(ContantApplication.StatusErrorBugException);
            response.setMessage(e.getMessage());
        }

        return response;
    }

    @Override
    public BaseResponse getPatientHistory() {
        BaseResponse response = new BaseResponse();
        try {
            List<Map<String, Object>> data = appointmentRepository.getPatientHistory();

            response.setHttpStatusCode(ContantApplication.HttpStatus_OK);
            response.setStatus(ContantApplication.StatusSuccess);
            response.setMessage(ContantApplication.MessageSuccess);
            response.addData("appointments", data);
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            response.setHttpStatusCode(ContantApplication.HttpStatus_Internal_Server_Error);
            response.setStatus(ContantApplication.StatusErrorBugException);
            response.setMessage(e.getMessage());
        }

        return response;
    }

    @Override
    public BaseResponse updateStatusAppointment(RequestUpdateStatusAppointment requestUpdateStatusAppointment) {
        BaseResponse response = new BaseResponse();
        RepositoryResult result = new RepositoryResult();
        String message;
        try {
            if (requestUpdateStatusAppointment.getId() == 0
                || TextUtils.isNullOrBlank(requestUpdateStatusAppointment.getStatus())){
                response.setHttpStatusCode(ContantApplication.HttpStatus_BAD_REQUEST);
                response.setStatus(ContantApplication.StatusErrorClient);
                response.setMessage("Lỗi: status không hợp lệ.");
                return response;
            }

            appointmentRepository.updateStatusAppointment(requestUpdateStatusAppointment);

            CreateNotificationDTO createNotificationDTO = new CreateNotificationDTO();
            createNotificationDTO.setTitle("Lịch khám bệnh đã được duyệt.");
            message = String.format("Đơn khám đã được duyệt. Bạn vui lòng đến phòng khám vào ngày %s lúc %s.",
                    requestUpdateStatusAppointment.getDate(),
                    requestUpdateStatusAppointment.getTime());
            createNotificationDTO.setContent(message);
            createNotificationDTO.setIdType(1);
            createNotificationDTO.setPatientID(requestUpdateStatusAppointment.getPatientid());
            // Parse đúng định dạng
            LocalDate date = LocalDate.parse(requestUpdateStatusAppointment.getDate());
            createNotificationDTO.setAppointmentDate(date);
            createNotificationDTO.setAppointmentTime(requestUpdateStatusAppointment.getTime());
            createNotificationDTO.setDoctorid(requestUpdateStatusAppointment.getDoctorid());
            createNotificationDTO.setNameRole(ContantApplication.RolePatient);
            createNotificationDTO.setStatus("Đã duyệt");
            createNotificationDTO.setUserfrom(requestUpdateStatusAppointment.getDoctorname());
            createNotificationDTO.setUserto(requestUpdateStatusAppointment.getPatientname());

            result = notificationRepository.createNotification(createNotificationDTO);
            // Xử lý các lỗi cụ thể
            if (!Objects.equals(result.getStatusCode(), ContantApplication.StatusSuccess)){
                throw new SQLDebugException(response.getMessage());
            }

            BaseResponse wsResponseDoctor = new BaseResponse();
            wsResponseDoctor.setHttpStatusCode(ContantApplication.HttpStatus_OK);
            wsResponseDoctor.setStatus(ContantApplication.StatusSuccess);
            wsResponseDoctor.setMessage("Bạn có 1 thông báo mới.");
            Integer idPatient = idQueryUserUtils.getIdUserByPatient(requestUpdateStatusAppointment.getPatientid());

            webSocketService.sendNotificationToCurrentUser(wsResponseDoctor, idPatient);

            BaseResponse countNotificationisWsDoctor = notificationService.countNotificationisWsPatient(idPatient);
            webSocketService.sendNotificationUpdateCountNotification(countNotificationisWsDoctor, idPatient);

            List<Map<String, Object>> getNotificationByIdUserPatientsAndIdNFT =
                    notificationRepository.getNotificationByIdUserPatientsAndIdNFT(idPatient, result.getIdResult());

            BaseResponse wsResponseBodyNotification = new BaseResponse();
            wsResponseBodyNotification.setHttpStatusCode(ContantApplication.HttpStatus_OK);
            wsResponseBodyNotification.setStatus(ContantApplication.StatusSuccess);
            wsResponseBodyNotification.addData("notification", getNotificationByIdUserPatientsAndIdNFT);
            webSocketService.sendBodyNotification(wsResponseBodyNotification, idPatient);

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
    public BaseResponse getAppointmentRecordById(int idAppointment) {
        BaseResponse response = new BaseResponse();
        try {
            Map<String, Object> data = appointmentRepository.getAppointmentRecordById(idAppointment);
            int patientId = (int) data.get("patientid");

            List<Map<String, Object>> services = servicesRepository.getUseServiceByIdPatient(patientId);
            List<Map<String, Object>> medicines = medicinesRepository.getMedicinesInvoiceByIdPatient(patientId);

            data.put("services", services);
            data.put("medicines", medicines);

            response.setHttpStatusCode(ContantApplication.HttpStatus_OK);
            response.setStatus(ContantApplication.StatusSuccess);
            response.setMessage(ContantApplication.MessageSuccess);
            response.addData("appointment", data);
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            response.setHttpStatusCode(ContantApplication.HttpStatus_Internal_Server_Error);
            response.setStatus(ContantApplication.StatusErrorBugException);
            response.setMessage(e.getMessage());
        }

        return response;
    }

    @Override
    public BaseResponse saveMedicalRecord(RequestSaveAppointmentRecordDTO dto) {
        BaseResponse response = new BaseResponse();
        String message;
        String title;
        int typeResult = 2;

        try {
            boolean result = appointmentRepository.saveMedicalRecord(dto);

            // Tao moi
            if (result && !Objects.equals(dto.getStatus(), "Đã duyệt")){
                title = "Hóa đơn mới";
                message = "Bạn có một hóa đơn bệnh án mới cần được thanh toán.";
            } else if (result && Objects.equals(dto.getStatus(), "Đã duyệt")){
                title = "Hóa đơn mới";
                message = "Hóa đơn bệnh án của bạn đã được tạo mới.";
            }
            // Update
            else {
                title = "Hóa đơn được cập nhật";
                message = "Hóa đơn bệnh án của bạn đã được cập nhật lại.";
            }

            CreateNotificationDTO createNotificationDTO = new CreateNotificationDTO();
            createNotificationDTO.setTitle(title);
            createNotificationDTO.setContent(message);
            createNotificationDTO.setIdType(typeResult);
            createNotificationDTO.setPatientID(dto.getPatientid());
            createNotificationDTO.setDoctorid(dto.getDoctorid());
            createNotificationDTO.setNameRole(ContantApplication.RolePatient);
            createNotificationDTO.setStatus("Đã gửi");
            createNotificationDTO.setUserfrom("Phòng khám Thiên An");
            createNotificationDTO.setUserto(dto.getPatientname());

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
            Integer idPatient = idQueryUserUtils.getIdUserByPatient(dto.getPatientid());

            // Gui thong bao di
            webSocketService.sendNotificationToCurrentUser(wsResponseDoctor, idPatient);
            // Yeu cau cap nhat so luong thong bao
            BaseResponse countNotificationisWsDoctor = notificationService.countNotificationisWsPatient(idPatient);
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
}
