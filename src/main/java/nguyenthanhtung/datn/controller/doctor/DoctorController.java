package nguyenthanhtung.datn.controller.doctor;

import lombok.RequiredArgsConstructor;
import nguyenthanhtung.datn.dto.base.BaseResponse;
import nguyenthanhtung.datn.dto.doctor.request.RequestDoctor;
import nguyenthanhtung.datn.dto.doctor.request.RequestDoctorAppointment;
import nguyenthanhtung.datn.service.doctor.DoctorService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/doctor")
@RequiredArgsConstructor
public class DoctorController {

    private final DoctorService doctorService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping()
    public ResponseEntity<BaseResponse> createDoctor(@RequestBody RequestDoctor requestDoctor) {
        String subject = "@Post Doctor";

        BaseResponse response = doctorService.createDoctor(requestDoctor);
        response.setSubjectFunction(subject);

        return ResponseEntity.status(response.getHttpStatusCode()).body(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping()
    public ResponseEntity<BaseResponse> updateDoctor(@RequestBody RequestDoctor requestDoctor) {
        String subject = "@Put Doctor";

        BaseResponse response = doctorService.updateDoctor(requestDoctor);
        response.setSubjectFunction(subject);

        return ResponseEntity.status(response.getHttpStatusCode()).body(response);
    }

    @GetMapping()
    public ResponseEntity<BaseResponse> getDoctor() {
        String subject = "@Get Doctor";

        BaseResponse response = doctorService.getAllDoctor();
        response.setSubjectFunction(subject);

        return ResponseEntity.status(response.getHttpStatusCode()).body(response);
    }

    @PreAuthorize("hasRole('PATIENT')")
    @PostMapping("/appointment")
    public ResponseEntity<BaseResponse> bookAppointment(@RequestBody RequestDoctorAppointment requestDoctorAppointment) {
        String subject = "@Post BookAppointment";

        BaseResponse response = doctorService.bookAppointment(requestDoctorAppointment);
        response.setSubjectFunction(subject);

        return ResponseEntity.status(response.getHttpStatusCode()).body(response);
    }
}
