package nguyenthanhtung.datn.repository.doctor.implement;

import lombok.RequiredArgsConstructor;
import nguyenthanhtung.datn.constants.ConstantQuery.doctor.ConstantQueryDoctor;
import nguyenthanhtung.datn.constants.ContantApplication;
import nguyenthanhtung.datn.dto.doctor.request.RequestDoctor;
import nguyenthanhtung.datn.dto.doctor.request.RequestDoctorAppointment;
import nguyenthanhtung.datn.entity.base.RepositoryResult;
import nguyenthanhtung.datn.repository.doctor.DoctorRepository;
import nguyenthanhtung.datn.util.JdbcUtils;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static nguyenthanhtung.datn.util.TextUtils.convertStringToTime;

@Repository
@RequiredArgsConstructor
public class ImplDoctorRepository implements DoctorRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final JdbcTemplate jdbcTemplate;
    private final PasswordEncoder passwordEncoder;

    @Override
    public RepositoryResult createDoctor(RequestDoctor requestDoctor, List<String> listDay, List<LocalTime> listTime) {
        RepositoryResult result = new RepositoryResult();

        // Tao tham so check user
        Map<String, Object> paramsCheckUsers = new HashMap<>();
        paramsCheckUsers.put("username", requestDoctor.getUsername());
        paramsCheckUsers.put("email", requestDoctor.getEmail());
        paramsCheckUsers.put("phonenumber", requestDoctor.getPhonenumber());

        // Truy van danh sach cac field ton tai
        List<String> existingFields = namedParameterJdbcTemplate.query(
                ConstantQueryDoctor.CHECK_USER_REGISTER_EXIT,
                paramsCheckUsers,
                (rs, rowNum) -> rs.getString("field")
        );

        // Kiem tra ton tai dua tung field
        if (existingFields.contains("username")) {
            result.setStatusCode(ContantApplication.StatusWarning_Conflict);
            result.setMessage("Tài khoản đã tồn tại");
            return result;
        }

        if (existingFields.contains("email")) {
            result.setStatusCode(ContantApplication.StatusWarning_Conflict);
            result.setMessage("Email đã tồn tại");
            return result;
        }

        if (existingFields.contains("phonenumber")) {
            result.setStatusCode(ContantApplication.StatusWarning_Conflict);
            result.setMessage("Số điện thoại đã tồn tại");
            return result;
        }

        // Tao tham so create user
        Map<String, Object> paramsCreateUsers = new HashMap<>();
        paramsCreateUsers.put("username", requestDoctor.getUsername());
        paramsCreateUsers.put("passwordhash", passwordEncoder.encode(requestDoctor.getPassword()));
        paramsCreateUsers.put("fullname", requestDoctor.getFullname());
        paramsCreateUsers.put("phonenumber", requestDoctor.getPhonenumber());
        paramsCreateUsers.put("email", requestDoctor.getEmail());
        paramsCreateUsers.put("address", requestDoctor.getAddress());
        paramsCreateUsers.put("image", "default.jpg");
        paramsCreateUsers.put("gender", "Khác");
        paramsCreateUsers.put("roleid", 2);

        // Tạo tài khoản người dùng
        Integer idUser = namedParameterJdbcTemplate.queryForObject(
                ConstantQueryDoctor.CREATE_USER,
                new MapSqlParameterSource(paramsCreateUsers),
                Integer.class
        );

        if (idUser == null || idUser == 0) {
            result.setStatusCode(ContantApplication.StatusErrorBugSQL);
            result.setMessage("Bug SQL: Không tạo được user (idUser null hoặc 0)");
            return result;
        }

        // Tao tham so create bác sĩ
        Map<String, Object> paramsCreateDoctor = new HashMap<>();
        paramsCreateDoctor.put("specialization", "");
        paramsCreateDoctor.put("userid", idUser);
        paramsCreateDoctor.put("departmentid", requestDoctor.getDepartmentid());
        paramsCreateDoctor.put("experience", requestDoctor.getExperience());

        // Tạo thông tin bác sĩ
        Integer idDoctor = namedParameterJdbcTemplate.queryForObject(
                ConstantQueryDoctor.CREATE_DOCTOR,
                new MapSqlParameterSource(paramsCreateDoctor),
                Integer.class
        );

        if (idDoctor == null || idDoctor == 0) {
            result.setStatusCode(ContantApplication.StatusErrorBugSQL);
            result.setMessage("Bug SQL: Không tạo được doctor (idDoctor null hoặc 0)");
            return result;
        }

        // Tao tham so lịch làm việc
        for (String day : listDay){
            Map<String, Object> paramsSchedule = new HashMap<>();
            paramsSchedule.put("doctorid", idDoctor);
            paramsSchedule.put("workday", day);
            paramsSchedule.put("starttime", listTime.getFirst());
            paramsSchedule.put("endtime", listTime.getLast());

            // Tạo lịch làm việc
            Integer idSchedule = namedParameterJdbcTemplate.queryForObject(
                    ConstantQueryDoctor.INSERT_SCHEDULE,
                    new MapSqlParameterSource(paramsSchedule),
                    Integer.class
            );

            if (idSchedule == null || idSchedule == 0) {
                result.setStatusCode(ContantApplication.StatusErrorBugSQL);
                result.setMessage("Bug SQL: Không tạo được lịch làm việc bác sỹ (idSchedule null hoặc 0)");
                return result;
            }
        }

        result.setStatusCode(ContantApplication.StatusSuccess);
        result.setMessage(ContantApplication.MessageSuccess);
        return result;
    }

    @Override
    public RepositoryResult updateDoctor(RequestDoctor requestDoctor, List<String> listDay, List<LocalTime> listTime) {
        RepositoryResult result = new RepositoryResult();

        // Tao tham so update doctor
        Map<String, Object> paramsUpdateDoctor = new HashMap<>();
        paramsUpdateDoctor.put("id", requestDoctor.getId());
        paramsUpdateDoctor.put("departmentid", requestDoctor.getDepartmentid());
        paramsUpdateDoctor.put("experience", requestDoctor.getExperience());

        // update tài khoản doctor
        Integer idUser = namedParameterJdbcTemplate.queryForObject(
                ConstantQueryDoctor.UPDATE_DOCTOR_BY_ID,
                new MapSqlParameterSource(paramsUpdateDoctor),
                Integer.class
        );

        if (idUser == null || idUser == 0) {
            result.setStatusCode(ContantApplication.StatusErrorBugSQL);
            result.setMessage("Bug SQL: Không update được doctor (idDoctor null hoặc 0)");
            return result;
        }

        // Tao tham so update user
        Map<String, Object> paramsUpdateUsers = new HashMap<>();
        paramsUpdateUsers.put("id", idUser);
        paramsUpdateUsers.put("username", requestDoctor.getUsername());
        paramsUpdateUsers.put("fullname", requestDoctor.getFullname());
        paramsUpdateUsers.put("phonenumber", requestDoctor.getPhonenumber());
        paramsUpdateUsers.put("email", requestDoctor.getEmail());
        paramsUpdateUsers.put("address", requestDoctor.getAddress());

        // Cập nhật thông tin user
        namedParameterJdbcTemplate.update(
                ConstantQueryDoctor.UPDATE_USER_DOCTOR_BY_ID,
                paramsUpdateUsers
        );

        // Xóa lịch làm việc cũ
        jdbcTemplate.update(ConstantQueryDoctor.DELETE_SCHEDULE_BT_ID_DOCTOR,requestDoctor.getId());

        // Tao tham so lịch làm việc
        for (String day : listDay){
            Map<String, Object> paramsSchedule = new HashMap<>();
            paramsSchedule.put("doctorid", requestDoctor.getId());
            paramsSchedule.put("workday", day);
            paramsSchedule.put("starttime", listTime.getFirst());
            paramsSchedule.put("endtime", listTime.getLast());

            // Tạo lịch làm việc
            Integer idSchedule = namedParameterJdbcTemplate.queryForObject(
                    ConstantQueryDoctor.INSERT_SCHEDULE,
                    new MapSqlParameterSource(paramsSchedule),
                    Integer.class
            );

            if (idSchedule == null || idSchedule == 0) {
                result.setStatusCode(ContantApplication.StatusErrorBugSQL);
                result.setMessage("Bug SQL: Không tạo được lịch làm việc bác sỹ (idSchedule null hoặc 0)");
                return result;
            }
        }

        result.setStatusCode(ContantApplication.StatusSuccess);
        result.setMessage(ContantApplication.MessageSuccess);
        return result;
    }

    @Override
    public List<Map<String, Object>> getAllDoctor() {
        return jdbcTemplate.query(ConstantQueryDoctor.SELECT_DOCTOR_ALL, new ColumnMapRowMapper());
    }

    @Override
    public RepositoryResult bookAppointment(RequestDoctorAppointment requestDoctorAppointment) {
        RepositoryResult result = new RepositoryResult();
        // Check da co lich kham voi bac sy

        // Parse thành LocalDate và LocalTime
        LocalDate date = LocalDate.parse(requestDoctorAppointment.getWorkday());
        LocalTime time = LocalTime.parse(requestDoctorAppointment.getTimeOnline());

        // Kết hợp lại thành LocalDateTime
        LocalDateTime dateTime = LocalDateTime.of(date, time);

        // Convert sang Timestamp để insert vào PostgreSQL
        Timestamp timestamp = Timestamp.valueOf(dateTime);

        // Tao tham so check lich lam viec
        Map<String, Object> paramsAppointmentDate = new HashMap<>();

        paramsAppointmentDate.put("doctorid", requestDoctorAppointment.getDoctorID());
        paramsAppointmentDate.put("appointmentdate", timestamp);

        // Check AppointmentDate
        Integer idAppointmentDate = JdbcUtils.safeQueryForSingleId(
                namedParameterJdbcTemplate,
                ConstantQueryDoctor.CHECK_BY_DOCTOR_APPOINTMENT_DATE,
                new MapSqlParameterSource(paramsAppointmentDate)
        );

        if (idAppointmentDate != null && idAppointmentDate != 0) {
            result.setStatusCode(ContantApplication.StatusSuccess_No_Data);
            result.setMessage(String.format("Với ngày %s và giờ %s bác sỹ đã có lịch khám khác.",
                    requestDoctorAppointment.getWorkday(),
                    requestDoctorAppointment.getTimeOnline()));

            return result;
        }

        // Tao tham create Patient
        Map<String, Object> paramsCreatePatient = new HashMap<>();
        paramsCreatePatient.put("userid", requestDoctorAppointment.getUserID());
        paramsCreatePatient.put("fullname", requestDoctorAppointment.getFullname());
        paramsCreatePatient.put("age", requestDoctorAppointment.getAge());
        paramsCreatePatient.put("gender", requestDoctorAppointment.getGender());
        paramsCreatePatient.put("address", requestDoctorAppointment.getAddress());
        paramsCreatePatient.put("phonenumber", requestDoctorAppointment.getPhonenumber());

        // Check Schedule
        Integer idPatient = JdbcUtils.safeQueryForSingleId(
                namedParameterJdbcTemplate,
                ConstantQueryDoctor.CREATE_PATIENTS,
                new MapSqlParameterSource(paramsCreatePatient)
        );

        if (idPatient == null || idPatient == 0) {
            result.setStatusCode(ContantApplication.StatusErrorBugSQL);
            result.setMessage("Not ID PATIENTS.");

            return result;
        }

        // Tao tham so check lich lam viec
        Map<String, Object> paramsScheduleUsers = new HashMap<>();
        paramsScheduleUsers.put("doctorid", requestDoctorAppointment.getDoctorID());
        paramsScheduleUsers.put("workday", requestDoctorAppointment.getWorkdayConvert());
        paramsScheduleUsers.put("starttime", convertStringToTime(requestDoctorAppointment.getTimeOnline()));
        paramsScheduleUsers.put("endtime", convertStringToTime(requestDoctorAppointment.getTimeOnline()));

        // Check Schedule
        Integer idSchedule = JdbcUtils.safeQueryForSingleId(
                namedParameterJdbcTemplate,
                ConstantQueryDoctor.SELECT_SCHEDULE_BY_DOCTOR_WORDAY,
                new MapSqlParameterSource(paramsScheduleUsers)
        );

        if (idSchedule == null || idSchedule == 0) {
            result.setStatusCode(ContantApplication.StatusSuccess_No_Data);
            result.setMessage(String.format("Với ngày %s và giờ %s không thuộc giờ hành chính của bác sỹ.",
                    requestDoctorAppointment.getWorkday(),
                    requestDoctorAppointment.getTimeOnline()));

            return result;
        }

        // Tao tham so lịch hẹn



        Map<String, Object> paramsCreateSchedule = new HashMap<>();
        paramsCreateSchedule.put("patientid", idPatient);
        paramsCreateSchedule.put("doctorid", requestDoctorAppointment.getDoctorID());
        paramsCreateSchedule.put("notes", requestDoctorAppointment.getNote());
        paramsCreateSchedule.put("appointmentdate", timestamp);

        // Tạo lịch hẹn
        Integer idScheduleUser = namedParameterJdbcTemplate.queryForObject(
                ConstantQueryDoctor.CREATE_APPOINTMENTS,
                new MapSqlParameterSource(paramsCreateSchedule),
                Integer.class
        );

        if (idScheduleUser == null || idScheduleUser == 0) {
            result.setStatusCode(ContantApplication.StatusErrorBugSQL);
            result.setMessage("Bug SQL: Không tạo được lịch hẹn (idSchedule null hoặc 0)");

            return result;
        }

        // Tạo tham so hồ sơ bệnh án
        Map<String, Object> paramsCreateMedicalRecords = new HashMap<>();
        paramsCreateMedicalRecords.put("PatientID", idPatient);
        paramsCreateMedicalRecords.put("DoctorID", requestDoctorAppointment.getDoctorID());
        paramsCreateMedicalRecords.put("AppointmentID", idScheduleUser);
        paramsCreateMedicalRecords.put("Symptom", requestDoctorAppointment.getDiagnosis());

        // Tạo hồ sơ bệnh án
        Integer idMedicalRecords = namedParameterJdbcTemplate.queryForObject(
                ConstantQueryDoctor.CREATE_MEDICAL_RECORDS,
                new MapSqlParameterSource(paramsCreateMedicalRecords),
                Integer.class
        );

        if (idMedicalRecords == null || idMedicalRecords == 0) {
            result.setStatusCode(ContantApplication.StatusErrorBugSQL);
            result.setMessage("Bug SQL: Không tạo hồ sơ bệnh án (idMedicalRecords null hoặc 0)");

            return result;
        }

        result.setStatusCode(ContantApplication.StatusSuccess);
        result.setMessage(ContantApplication.MessageSuccess);
        result.setIdResult(idPatient);
        return result;
    }
}
