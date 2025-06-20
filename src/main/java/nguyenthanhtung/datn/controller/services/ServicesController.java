package nguyenthanhtung.datn.controller.services;

import lombok.RequiredArgsConstructor;
import nguyenthanhtung.datn.dto.base.BaseResponse;
import nguyenthanhtung.datn.dto.services.UpdateServiceRequestDTO;
import nguyenthanhtung.datn.service.services.ServicesService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/services")
@RequiredArgsConstructor
public class ServicesController {

    private final ServicesService servicesService;

    @GetMapping()
    public ResponseEntity<BaseResponse> getService() {
        String subject = "@Get Service";

        BaseResponse response = servicesService.getServices();
        response.setSubjectFunction(subject);

        return ResponseEntity.status(response.getHttpStatusCode()).body(response);
    }

    @PutMapping()
    public ResponseEntity<BaseResponse> updateService(@RequestBody UpdateServiceRequestDTO updateServiceRequestDTO) {
        String subject = "@Put Service";

        BaseResponse response = servicesService.update(updateServiceRequestDTO);
        response.setSubjectFunction(subject);

        return ResponseEntity.status(response.getHttpStatusCode()).body(response);
    }

    @GetMapping("/service-patientid/{id}")
    public ResponseEntity<BaseResponse> getListNameServiceInvoiceByIdPatient(@PathVariable("id") int id) {
        String subject = "@Get Service Patientid";

        BaseResponse response = servicesService.getListNameServiceInvoiceByIdPatient(id);
        response.setSubjectFunction(subject);

        return ResponseEntity.status(response.getHttpStatusCode()).body(response);
    }
}
