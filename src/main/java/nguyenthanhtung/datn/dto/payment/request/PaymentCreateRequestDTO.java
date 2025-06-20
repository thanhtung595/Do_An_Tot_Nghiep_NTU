package nguyenthanhtung.datn.dto.payment.request;

import lombok.Data;

@Data
public class PaymentCreateRequestDTO {
    int id;
    int patientid;
    double totalmoney;
    String paymentmethod;
    Integer userid;
    String patientname;
    int doctorid;
}
