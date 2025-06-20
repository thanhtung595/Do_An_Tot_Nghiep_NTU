package nguyenthanhtung.datn.controller.payment;

import lombok.RequiredArgsConstructor;
import nguyenthanhtung.datn.dto.base.BaseResponse;
import nguyenthanhtung.datn.dto.payment.request.PaymentCreateRequestDTO;
import nguyenthanhtung.datn.service.payment.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping()
    public ResponseEntity<BaseResponse> createPayment(@RequestBody PaymentCreateRequestDTO dto) {
        String subject = "@Post Payment";

        BaseResponse response = paymentService.createPayment(dto);
        response.setSubjectFunction(subject);

        return ResponseEntity.status(response.getHttpStatusCode()).body(response);
    }
}
