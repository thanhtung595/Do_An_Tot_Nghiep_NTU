package nguyenthanhtung.datn.service.payment.implement;

import lombok.RequiredArgsConstructor;
import nguyenthanhtung.datn.config.requestThreadLocal.RequestThreadLocalContext;
import nguyenthanhtung.datn.config.requestThreadLocal.RequestUserThreadLocal;
import nguyenthanhtung.datn.constants.ContantApplication;
import nguyenthanhtung.datn.dto.base.BaseResponse;
import nguyenthanhtung.datn.dto.notification.request.CreateNotificationDTO;
import nguyenthanhtung.datn.dto.payment.request.PaymentCreateRequestDTO;
import nguyenthanhtung.datn.entity.base.RepositoryResult;
import nguyenthanhtung.datn.exception.SQLDebugException;
import nguyenthanhtung.datn.repository.invoice.InvoiceRepository;
import nguyenthanhtung.datn.repository.notification.NotificationRepository;
import nguyenthanhtung.datn.repository.payment.PaymentRepository;
import nguyenthanhtung.datn.service.notification.NotificationService;
import nguyenthanhtung.datn.service.payment.PaymentService;
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
public class ImplPaymentService implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final NotificationRepository notificationRepository;
    private final NotificationService notificationService;
    private final WebSocketService webSocketService;
    private final IdQueryUserUtils idQueryUserUtils;
    private final InvoiceRepository invoiceRepository;

    @Override
    public BaseResponse createPayment(PaymentCreateRequestDTO dto) {
        BaseResponse response = new BaseResponse();
        CreateNotificationDTO createNotificationDTO = new CreateNotificationDTO();

        String message;
        try {
            RequestUserThreadLocal currentUser = RequestThreadLocalContext.get();
            dto.setUserid(currentUser.getUserId());
            paymentRepository.createPayment(dto);
            invoiceRepository.updateStatusInvoice(dto.getId(), "Đã thanh toán");

            // Tao moi
            String title = "Thanh toán hóa đơn";
            message = String.format("Bạn đã thanh toán hóa đơn #%s bằng %s.",
                    dto.getId(), dto.getPaymentmethod());

            createNotificationDTO.setTitle(title);
            createNotificationDTO.setContent(message);
            createNotificationDTO.setIdType(4);
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
