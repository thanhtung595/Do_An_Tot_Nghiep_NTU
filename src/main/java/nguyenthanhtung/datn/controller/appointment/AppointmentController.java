package nguyenthanhtung.datn.controller.appointment;

import lombok.RequiredArgsConstructor;
import nguyenthanhtung.datn.dto.appointment.request.RequestSaveAppointmentRecordDTO;
import nguyenthanhtung.datn.dto.appointment.request.RequestUpdateStatusAppointment;
import nguyenthanhtung.datn.dto.base.BaseResponse;
import nguyenthanhtung.datn.service.appointment.AppointmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/appointment")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;

    @GetMapping()
    public ResponseEntity<BaseResponse> getAppointmentsByUser() {
        String subject = "@Get Appointments ByUser";

        BaseResponse response = appointmentService.getAppointmentsByUser();
        response.setSubjectFunction(subject);

        return ResponseEntity.status(response.getHttpStatusCode()).body(response);
    }

    @PreAuthorize("hasRole('DOCTOR')")
    @GetMapping("/medical-record/{id}")
    public ResponseEntity<BaseResponse> getAppointmentRecordById(@PathVariable("id") int id) {
        String subject = "@Get Appointments Record";

        BaseResponse response = appointmentService.getAppointmentRecordById(id);
        response.setSubjectFunction(subject);

        return ResponseEntity.status(response.getHttpStatusCode()).body(response);
    }

    @PreAuthorize("hasRole('DOCTOR')")
    @GetMapping("/patient-history")
    public ResponseEntity<BaseResponse> getPatientHistory() {
        String subject = "@Get Patient History";

        BaseResponse response = appointmentService.getPatientHistory();
        response.setSubjectFunction(subject);

        return ResponseEntity.status(response.getHttpStatusCode()).body(response);
    }

    @PreAuthorize("hasRole('DOCTOR')")
    @PutMapping("/update-status-appointment")
    public ResponseEntity<BaseResponse> updateStatusAppointment(@RequestBody RequestUpdateStatusAppointment requestUpdateStatusAppointment) {
        String subject = "@Put Update Status Appointment";

        BaseResponse response = appointmentService.updateStatusAppointment(requestUpdateStatusAppointment);
        response.setHttpStatusCode(200);
        response.setSubjectFunction(subject);
        response.addData("data", requestUpdateStatusAppointment);

        return ResponseEntity.status(response.getHttpStatusCode()).body(response);
    }

    @PreAuthorize("hasRole('DOCTOR')")
    @PutMapping("/save-medical-record")
    public ResponseEntity<BaseResponse> saveMedicalRecord(@RequestBody RequestSaveAppointmentRecordDTO dto) {
        String subject = "@Put Save Medical Record";

        BaseResponse response = appointmentService.saveMedicalRecord(dto);
        response.setSubjectFunction(subject);

        return ResponseEntity.status(response.getHttpStatusCode()).body(response);
    }
}
