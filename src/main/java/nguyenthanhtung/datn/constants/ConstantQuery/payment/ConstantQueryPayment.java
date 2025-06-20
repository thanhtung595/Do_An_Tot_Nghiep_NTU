package nguyenthanhtung.datn.constants.ConstantQuery.payment;

public class ConstantQueryPayment {

    public static final String CREATE_PAYMENT = """
        INSERT INTO payments(invoiceid, amountpaid, paymentmethod, userid, paidat) VALUES
            (?, ?, ?, ?, NOW());
    """;
}
