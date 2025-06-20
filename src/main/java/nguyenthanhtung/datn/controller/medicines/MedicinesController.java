package nguyenthanhtung.datn.controller.medicines;

import lombok.RequiredArgsConstructor;
import nguyenthanhtung.datn.dto.base.BaseResponse;
import nguyenthanhtung.datn.service.medicines.MedicinesService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/medicines")
@RequiredArgsConstructor
public class MedicinesController {

    private final MedicinesService medicinesService;

    @GetMapping()
    public ResponseEntity<BaseResponse> getMedicines() {
        String subject = "@Get Medicines";

        BaseResponse response = medicinesService.getMedicines();
        response.setSubjectFunction(subject);

        return ResponseEntity.status(response.getHttpStatusCode()).body(response);
    }

    @GetMapping("/medicines-patientid/{id}")
    public ResponseEntity<BaseResponse> getListNameMedicinesInvoiceByIdPatient(@PathVariable("id") int id) {
        String subject = "@Get Medicines Patientid";

        BaseResponse response = medicinesService.getListNameMedicinesInvoiceByIdPatient(id);
        response.setSubjectFunction(subject);

        return ResponseEntity.status(response.getHttpStatusCode()).body(response);
    }

}
