package nguyenthanhtung.datn.service.doctor.implement;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import nguyenthanhtung.datn.config.requestThreadLocal.RequestThreadLocalContext;
import nguyenthanhtung.datn.config.requestThreadLocal.RequestUserThreadLocal;
import nguyenthanhtung.datn.constants.ContantApplication;
import nguyenthanhtung.datn.dto.base.BaseResponse;
import nguyenthanhtung.datn.dto.doctor.request.RequestDoctor;
import nguyenthanhtung.datn.dto.doctor.request.RequestDoctorAppointment;
import nguyenthanhtung.datn.dto.notification.request.CreateNotificationDTO;
import nguyenthanhtung.datn.entity.base.RepositoryResult;
import nguyenthanhtung.datn.exception.ConflictException;
import nguyenthanhtung.datn.exception.NotFoundSQLException;
import nguyenthanhtung.datn.exception.SQLDebugException;
import nguyenthanhtung.datn.repository.doctor.DoctorRepository;
import nguyenthanhtung.datn.service.doctor.DoctorService;
import nguyenthanhtung.datn.service.notification.NotificationService;
import nguyenthanhtung.datn.util.IdQueryUserUtils;
import nguyenthanhtung.datn.util.TextUtils;
import nguyenthanhtung.datn.util.WebSocket.WebSocketService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class ImplDoctorService implements DoctorService {

    private final DoctorRepository doctorRepository;
    private final NotificationService notificationService;
    private final WebSocketService webSocketService;
    private final IdQueryUserUtils idQueryUserUtils;

    @Override
    public BaseResponse getAllDoctor() {
        BaseResponse response = new BaseResponse();
        try {
            List<Map<String, Object>> data = doctorRepository.getAllDoctor();
            for (Map<String, Object> item : data){
                if (item.containsKey("image")) {
                    item.compute("image", (k, oldValue) -> System.getProperty("URL_AWS_STORAGE") + System.getProperty("PATH_AVATAR_DOCTOR") + oldValue);
                }
            }

            response.setHttpStatusCode(ContantApplication.HttpStatus_OK);
            response.setStatus(ContantApplication.StatusSuccess);
            response.setMessage(ContantApplication.MessageSuccess);
            response.addData("doctors", data);
        } catch (Exception e) {
            response.setHttpStatusCode(ContantApplication.HttpStatus_Internal_Server_Error);
            response.setStatus(ContantApplication.StatusErrorBugException);
            response.setMessage(e.getMessage());
        }
        return response;
    }

    @Override
    public BaseResponse createDoctor(RequestDoctor requestDoctor) {
        BaseResponse response = new BaseResponse();
        RepositoryResult result;
        String message;
        try {
            if (TextUtils.isNullOrBlank(requestDoctor.getUsername())
                || TextUtils.isNullOrBlank(requestDoctor.getEmail())
                || TextUtils.isNullOrBlank(requestDoctor.getFullname())
                || TextUtils.isNullOrBlank(requestDoctor.getPhonenumber())
                || TextUtils.isNullOrBlank(requestDoctor.getPassword())){

                message = "Chưa nhập đầy đủ thông tin.";
                response.setHttpStatusCode(ContantApplication.HttpStatus_BAD_REQUEST);
                response.setStatus(ContantApplication.StatusErrorClient);
                response.setMessage(message);

                return response;
            }

            if (requestDoctor.getPassword().length() < 6){
                response.setHttpStatusCode(ContantApplication.HttpStatus_BAD_REQUEST);
                response.setStatus(ContantApplication.StatusErrorClient);
                response.setMessage("Mật khẩu phải có ít nhất 6 ký tự.");
                return response;
            }
            
            // Trích xuất và kiểm tra ngày làm việc
            List<String> listDay = extractDays(requestDoctor.getWorkdays());

            if (listDay == null){
                message = "Cần nhập đúng format ngày thứ làm việc";
                response.setHttpStatusCode(ContantApplication.HttpStatus_BAD_REQUEST);
                response.setStatus(ContantApplication.StatusSuccess_No_Data);
                response.setMessage(message);
                return response;
            }

            // Trích xuất và kiểm tra thời gian làm việc
            List<LocalTime> listTime = extractTimes(requestDoctor.getTimeonline());
            if (listTime == null){
                message = "Cần nhập đúng format giờ làm việc";
                response.setHttpStatusCode(ContantApplication.HttpStatus_BAD_REQUEST);
                response.setStatus(ContantApplication.StatusSuccess_No_Data);
                response.setMessage(message);
                return response;
            }

            // Check tài khoản đã tồn tại
            result = doctorRepository.createDoctor(requestDoctor, listDay, listTime);
            if (Objects.equals(result.getStatusCode(), ContantApplication.StatusWarning_Conflict)){
                throw new ConflictException(result.getMessage());
            }

            // Xử lý các lỗi cụ thể
            if (Objects.equals(result.getStatusCode(), ContantApplication.StatusErrorBugSQL)){
                throw new SQLDebugException(result.getMessage());
            }

            response.setHttpStatusCode(ContantApplication.HttpStatus_OK);
            response.setStatus(ContantApplication.StatusSuccess);
            response.setMessage(ContantApplication.MessageSuccess);
        } catch (ConflictException e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            response.setHttpStatusCode(ContantApplication.HttpStatus_Conflict);
            response.setStatus(ContantApplication.StatusErrorClient);
            response.setMessage(e.getMessage());
        } catch (SQLDebugException e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            response.setHttpStatusCode(ContantApplication.HttpStatus_Internal_Server_Error);
            response.setStatus(ContantApplication.StatusErrorBugSQL);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            response.setHttpStatusCode(ContantApplication.HttpStatus_Internal_Server_Error);
            response.setStatus(ContantApplication.StatusErrorBugException);
            response.setMessage(e.getMessage());
        }

        return response;
    }

    @Override
    public BaseResponse updateDoctor(RequestDoctor requestDoctor) {
        BaseResponse response = new BaseResponse();
        RepositoryResult result;
        String message;
        try {
            if (TextUtils.isNullOrBlank(requestDoctor.getUsername())
                || TextUtils.isNullOrBlank(requestDoctor.getEmail())
                || TextUtils.isNullOrBlank(requestDoctor.getFullname())
                || TextUtils.isNullOrBlank(requestDoctor.getPhonenumber())){

                message = "Chưa nhập đầy đủ thông tin.";
                response.setHttpStatusCode(ContantApplication.HttpStatus_BAD_REQUEST);
                response.setStatus(ContantApplication.StatusErrorClient);
                response.setMessage(message);

                return response;
            }

            // Trích xuất và kiểm tra ngày làm việc
            List<String> listDay = extractDays(requestDoctor.getWorkdays());

            if (listDay == null){
                message = "Cần nhập đúng format ngày thứ làm việc";
                response.setHttpStatusCode(ContantApplication.HttpStatus_BAD_REQUEST);
                response.setStatus(ContantApplication.StatusSuccess_No_Data);
                response.setMessage(message);
                return response;
            }

            // Trích xuất và kiểm tra thời gian làm việc
            List<LocalTime> listTime = extractTimes(requestDoctor.getTimeonline());
            if (listTime == null){
                message = "Cần nhập đúng format giờ làm việc";
                response.setHttpStatusCode(ContantApplication.HttpStatus_BAD_REQUEST);
                response.setStatus(ContantApplication.StatusSuccess_No_Data);
                response.setMessage(message);
                return response;
            }

            // Check tài khoản đã tồn tại
            result = doctorRepository.updateDoctor(requestDoctor, listDay, listTime);

            // Xử lý các lỗi cụ thể
            if (Objects.equals(result.getStatusCode(), ContantApplication.StatusErrorBugSQL)){
                throw new SQLDebugException(result.getMessage());
            }

            response.setHttpStatusCode(ContantApplication.HttpStatus_OK);
            response.setStatus(ContantApplication.StatusSuccess);
            response.setMessage(ContantApplication.MessageSuccess);
        } catch (SQLDebugException e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            response.setHttpStatusCode(ContantApplication.HttpStatus_Internal_Server_Error);
            response.setStatus(ContantApplication.StatusErrorBugSQL);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            response.setHttpStatusCode(ContantApplication.HttpStatus_Internal_Server_Error);
            response.setStatus(ContantApplication.StatusErrorBugException);
            response.setMessage(e.getMessage());
        }

        return response;
    }

//    @Override
//    public BaseResponse bookAppointment(RequestDoctorAppointment requestDoctorAppointment) {
//        BaseResponse response = new BaseResponse();
//
//        response.setHttpStatusCode(ContantApplication.HttpStatus_OK);
//        response.setStatus(ContantApplication.StatusSuccess);
//        response.setMessage("Bạn có 1 thông báo mới.");
//
//        webSocketService.sendNotificationToCurrentUser(response);
//
//        BaseResponse wsResponseDoctor = new BaseResponse();
//
//        wsResponseDoctor.setHttpStatusCode(ContantApplication.HttpStatus_OK);
//        wsResponseDoctor.setStatus(ContantApplication.StatusSuccess);
//        wsResponseDoctor.setMessage("Bạn có 1 thông báo mới.");
//
//        Integer idDoctor = idQueryUserUtils.getIdUserByDoctor(requestDoctorAppointment.getDoctorID());
//
//        webSocketService.sendNotificationToCurrentUser(wsResponseDoctor, idDoctor);
//        return response;
//    }


    @Override
    public BaseResponse bookAppointment(RequestDoctorAppointment requestDoctorAppointment) {
        BaseResponse response = new BaseResponse();
        RepositoryResult result;
        String message;
        try {
            if (TextUtils.isNullOrBlank(requestDoctorAppointment.getDiagnosis())
                || TextUtils.isNullOrBlank(requestDoctorAppointment.getWorkday())
                || TextUtils.isNullOrBlank(requestDoctorAppointment.getTimeOnline())){

                message = "Chưa nhập đầy đủ thông tin.";
                response.setHttpStatusCode(ContantApplication.HttpStatus_BAD_REQUEST);
                response.setStatus(ContantApplication.StatusErrorClient);
                response.setMessage(message);

                return response;
            }

            // Kiểm tra ngày hẹn có hợp lệ không
            ValidationResult validateWorkday = validateWorkday(requestDoctorAppointment.getWorkday());

            if (!validateWorkday.isValid){
                response.setHttpStatusCode(ContantApplication.HttpStatus_BAD_REQUEST);
                response.setStatus(ContantApplication.StatusErrorClient);
                response.setMessage(validateWorkday.message);
            }

            requestDoctorAppointment.setWorkdayConvert(validateWorkday.weekdayStr);

            // id bệnh nhân
            RequestUserThreadLocal currentUser = RequestThreadLocalContext.get();
            int idUser = currentUser.getUserId();
            requestDoctorAppointment.setUserID(idUser);

            result = doctorRepository.bookAppointment(requestDoctorAppointment);

            // Kiểm tra lịch làm việc của bác sĩ
            if (Objects.equals(ContantApplication.StatusSuccess_No_Data, result.getStatusCode())){
                throw new NotFoundSQLException(result.getMessage());
            }

            // Xử lý các lỗi cụ thể
            if (Objects.equals(result.getStatusCode(), ContantApplication.StatusErrorBugSQL)){
                throw new SQLDebugException(result.getMessage());
            }

            CreateNotificationDTO createNotificationDTO = new CreateNotificationDTO();
            createNotificationDTO.setTitle("Đăng ký lịch khám thành công.");
            message = String.format("Bạn đã đặt lịch khám thành công vào ngày %s lúc %s. Hiện tại đang đợi bác sỹ trực ca duyệt đơn. Bạn vui lòng chờ đợi.",
                    requestDoctorAppointment.getWorkday(),
                    requestDoctorAppointment.getTimeOnline());
            createNotificationDTO.setContent(message);
            createNotificationDTO.setIdType(1);
            createNotificationDTO.setPatientID(result.getIdResult());
            LocalDate date = LocalDate.parse(requestDoctorAppointment.getWorkday());
            createNotificationDTO.setAppointmentDate(date);
            createNotificationDTO.setAppointmentTime(requestDoctorAppointment.getTimeOnline());
            createNotificationDTO.setDoctorid(requestDoctorAppointment.getDoctorID());
            createNotificationDTO.setNameRole(ContantApplication.RolePatient);
            createNotificationDTO.setStatus("Đợi duyệt");
            createNotificationDTO.setUserfrom(requestDoctorAppointment.getFullname());
            createNotificationDTO.setUserto(requestDoctorAppointment.getDoctorName());

            response = notificationService.create(createNotificationDTO);
            // Xử lý các lỗi cụ thể
            if (!Objects.equals(response.getStatus(), ContantApplication.StatusSuccess)){
                throw new SQLDebugException(response.getMessage());
            }

            CreateNotificationDTO createNotificationDoctorDTO = new CreateNotificationDTO();
            createNotificationDoctorDTO.setTitle("Lịch đăng ký khám bệnh mới.");
            message = String.format("Bạn có một đơn khám mới vào ngày %s lúc %s. Bạn vui lòng duyệt đơn nếu phù hợp.",
                    requestDoctorAppointment.getWorkday(),
                    requestDoctorAppointment.getTimeOnline());
            createNotificationDoctorDTO.setContent(message);
            createNotificationDoctorDTO.setIdType(1);
            createNotificationDoctorDTO.setPatientID(result.getIdResult());
            LocalDate date2 = LocalDate.parse(requestDoctorAppointment.getWorkday());
            createNotificationDoctorDTO.setAppointmentDate(date2);
            createNotificationDoctorDTO.setAppointmentTime(requestDoctorAppointment.getTimeOnline());
            createNotificationDoctorDTO.setDoctorid(requestDoctorAppointment.getDoctorID());
            createNotificationDoctorDTO.setNameRole(ContantApplication.RoleDoctor);
            createNotificationDTO.setStatus("Đợi duyệt");
            createNotificationDTO.setUserfrom(requestDoctorAppointment.getFullname());
            createNotificationDTO.setUserto(requestDoctorAppointment.getDoctorName());

            response = notificationService.create(createNotificationDoctorDTO);
            // Xử lý các lỗi cụ thể
            if (!Objects.equals(response.getStatus(), ContantApplication.StatusSuccess)){
                throw new SQLDebugException(response.getMessage());
            }

            response.setHttpStatusCode(ContantApplication.HttpStatus_OK);
            response.setStatus(ContantApplication.StatusSuccess);
            response.setMessage("Bạn có 1 thông báo mới.");

            webSocketService.sendNotificationToCurrentUser(response);

            BaseResponse wsResponseDoctor = new BaseResponse();
            wsResponseDoctor.setHttpStatusCode(ContantApplication.HttpStatus_OK);
            wsResponseDoctor.setStatus(ContantApplication.StatusSuccess);
            wsResponseDoctor.setMessage("Bạn có 1 thông báo mới.");
            Integer idDoctor = idQueryUserUtils.getIdUserByDoctor(requestDoctorAppointment.getDoctorID());

            webSocketService.sendNotificationToCurrentUser(wsResponseDoctor, idDoctor);

            BaseResponse countNotificationisWsDoctor = notificationService.countNotificationisWsDoctor(idDoctor);
            webSocketService.sendNotificationUpdateCountNotification(countNotificationisWsDoctor, idDoctor);

            BaseResponse countNotificationisWsPatient = notificationService.countNotification();
            webSocketService.sendNotificationUpdateCountNotification(countNotificationisWsPatient);

            webSocketService.sendBodyNotification(response, idDoctor);
            webSocketService.sendBodyNotification(response, idUser);
        } catch (NotFoundSQLException e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            response.setHttpStatusCode(ContantApplication.HttpStatus_BAD_REQUEST);
            response.setStatus(ContantApplication.StatusSuccess_No_Data);
            response.setMessage(e.getMessage());
        } catch (SQLDebugException e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            response.setHttpStatusCode(ContantApplication.HttpStatus_Internal_Server_Error);
            response.setStatus(ContantApplication.StatusErrorBugSQL);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            response.setHttpStatusCode(ContantApplication.HttpStatus_Internal_Server_Error);
            response.setStatus(ContantApplication.StatusErrorBugException);
            response.setMessage(e.getMessage());
        }

        return response;
    }

    /**
     * Trích xuất các ngày làm việc từ chuỗi đầu vào
     *
     * @param inputStr Chuỗi chứa thông tin ngày làm việc (ví dụ: "Thứ 2, Thứ 4, Thứ 6")
     * @return Danh sách các ngày làm việc đã được sắp xếp hoặc null nếu định dạng không hợp lệ
     */
    private static List<String> extractDays(String inputStr) {
        List<String> validDays = Arrays.asList("2", "3", "4", "5", "6", "7", "CN");
        String invalidPattern = "Thứ\\s*(?!2|3|4|5|6|7|CN)\\d+|Thứ\\s*(\\d{2,})|Thứ\\d";
        Pattern invalidRegex = Pattern.compile(invalidPattern);
        Matcher invalidMatcher = invalidRegex.matcher(inputStr);
        if (invalidMatcher.find()) {
            return null;
        }

        String pattern = "Thứ\\s*(2|3|4|5|6|7|CN)";
        Pattern regex = Pattern.compile(pattern);
        Matcher matcher = regex.matcher(inputStr);

        Set<String> uniqueDays = new LinkedHashSet<>();
        while (matcher.find()) {
            uniqueDays.add(matcher.group(1));
        }

        if (uniqueDays.isEmpty()) {
            return null;
        }

        List<String> sortedDays = new ArrayList<>(uniqueDays);
        sortedDays.sort(Comparator.comparingInt(validDays::indexOf));

        return sortedDays;
    }

    /**
     * Trích xuất thời gian làm việc từ chuỗi đầu vào
     *
     * @param inputStr Chuỗi chứa thời gian làm việc (ví dụ: "08:00 AM - 17:00 PM")
     * @return Danh sách chứa thời gian bắt đầu và kết thúc hoặc null nếu định dạng không hợp lệ
     */
    private static List<LocalTime> extractTimes(String inputStr) {
        String pattern = "((?:[01]\\d|2[0-3]):[0-5]\\d) (AM|PM)";
        Pattern regex = Pattern.compile(pattern);
        Matcher matcher = regex.matcher(inputStr);

        List<LocalTime> times = new ArrayList<>();
        while (matcher.find()) {
            String timePart = matcher.group(1);     // ví dụ: "18:00"
            String meridian = matcher.group(2).toUpperCase(); // "AM" hoặc "PM"

            String[] parts = timePart.split(":");
            int hour = Integer.parseInt(parts[0]);
            int minute = Integer.parseInt(parts[1]);

            // Chuyển đổi sang 24h format thủ công
            if (meridian.equals("AM") && hour == 12) {
                hour = 0;
            } else if (meridian.equals("PM") && hour < 12) {
                hour += 12;
            }

            times.add(LocalTime.of(hour, minute));
        }

        return times.size() == 2 ? times : null;
    }

    public static ValidationResult validateWorkday(String workdayStr) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate workdayDate = LocalDate.parse(workdayStr, formatter);
            LocalDate today = LocalDate.now();

            if (workdayDate.isBefore(today)) {
                return new ValidationResult(false, "Ngày đã qua vui lòng chọn ngày tiếp theo.", 400, -1);
            }

            // ISO: Monday = 1, Sunday = 7 → thêm 1 như Python code
            int weekdayNumber = workdayDate.getDayOfWeek().getValue() + 1;

            return new ValidationResult(true, "Hợp lệ", 200, weekdayNumber);

        } catch (DateTimeParseException e) {
            return new ValidationResult(false, "Định dạng ngày không hợp lệ.", 400, -1);
        }
    }

    public static class ValidationResult{
        public boolean isValid;
        public String message;
        public int statusCode;
        public int weekdayNumber;
        public String weekdayStr;

        public ValidationResult(boolean isValid, String message, int statusCode, int weekdayNumber) {
            this.isValid = isValid;
            this.message = message;
            this.statusCode = statusCode;
            this.weekdayNumber = weekdayNumber;
            this.weekdayStr = String.valueOf(weekdayNumber);
        }
    }
}
