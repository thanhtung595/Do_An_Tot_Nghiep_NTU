package nguyenthanhtung.datn.controller.invoice;

import lombok.RequiredArgsConstructor;
import nguyenthanhtung.datn.dto.base.BaseResponse;
import nguyenthanhtung.datn.service.invoice.InvoiceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/invoice")
@RequiredArgsConstructor
public class InvoiceController {

    private final InvoiceService invoiceService;

    @GetMapping()
    public ResponseEntity<BaseResponse> getInvoice() {
        String subject = "@Get Invoice";

        BaseResponse response = invoiceService.getInvoiceByIdUser();
        response.setSubjectFunction(subject);

        return ResponseEntity.status(response.getHttpStatusCode()).body(response);
    }

    @GetMapping("/by-id/{id}")
    public ResponseEntity<BaseResponse> getInvoiceByIdUserAndInvoice(@PathVariable("id") int id) {
        String subject = "@Get Invoice By Id";

        BaseResponse response = invoiceService.getInvoiceByIdUserAndInvoice(id);
        response.setSubjectFunction(subject);

        return ResponseEntity.status(response.getHttpStatusCode()).body(response);
    }

    @GetMapping("/history/by-id/{id}")
    public ResponseEntity<BaseResponse> getInvoiceByIdHistory(@PathVariable("id") int id) {
        String subject = "@Get Invoice By Id History";

        BaseResponse response = invoiceService.getInvoiceById(id);
        response.setSubjectFunction(subject);

        return ResponseEntity.status(response.getHttpStatusCode()).body(response);
    }
}
