package nguyenthanhtung.datn.repository.payment.implement;

import lombok.RequiredArgsConstructor;
import nguyenthanhtung.datn.constants.ConstantQuery.payment.ConstantQueryPayment;
import nguyenthanhtung.datn.dto.payment.request.PaymentCreateRequestDTO;
import nguyenthanhtung.datn.repository.payment.PaymentRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ImplPaymentRepository implements PaymentRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void createPayment(PaymentCreateRequestDTO dto) {
        jdbcTemplate.update(ConstantQueryPayment.CREATE_PAYMENT, dto.getId(),
                dto.getTotalmoney(), dto.getPaymentmethod(), dto.getUserid());
    }
}
