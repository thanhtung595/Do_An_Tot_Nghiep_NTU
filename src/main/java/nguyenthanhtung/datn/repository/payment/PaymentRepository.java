package nguyenthanhtung.datn.repository.payment;

import nguyenthanhtung.datn.dto.payment.request.PaymentCreateRequestDTO;

public interface PaymentRepository {
    void createPayment(PaymentCreateRequestDTO dto);
}
