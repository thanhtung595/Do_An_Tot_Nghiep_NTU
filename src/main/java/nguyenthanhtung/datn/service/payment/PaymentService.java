package nguyenthanhtung.datn.service.payment;

import nguyenthanhtung.datn.dto.base.BaseResponse;
import nguyenthanhtung.datn.dto.payment.request.PaymentCreateRequestDTO;

public interface PaymentService {
    BaseResponse createPayment(PaymentCreateRequestDTO dto);
}
