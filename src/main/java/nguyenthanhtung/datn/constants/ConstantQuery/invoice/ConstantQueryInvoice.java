package nguyenthanhtung.datn.constants.ConstantQuery.invoice;

public class ConstantQueryInvoice {

    public static final String GET_ALL_INVOICE = """
        SELECT
          ivc.id as id,
          ivc.patientid as patientid,
          pts.fullname as patientName,
          EXTRACT(YEAR FROM AGE(NOW(), pts.age))::int as age,
          pts.gender as gender,
          pts.address as address,
          pts.phonenumber as phone,
          mcr.symptom as symptoms,
          mcr.diagnosis as diagnosis,
          mcr.treatmentplan as notes,
          dtm.name as treatment,
          TO_CHAR(atm.checkagain, 'YYYY-MM-DD') as nextAppointment,
          TO_CHAR(ivc.createdat, 'YYYY-MM-DD') as date,
          ivc.totalamount as totalMoney,
          CASE
            WHEN ivc.status = 'Chưa thanh toán' THEN false
          ELSE true
        END AS isPaid
        FROM invoice ivc
        INNER JOIN patients pts
        ON ivc.patientid = pts.id
        INNER JOIN medicalrecords mcr
        ON mcr.patientid = pts.id
        INNER JOIN doctors dt
        ON mcr.doctorid = dt.id
        INNER JOIN departments dtm
        ON dt.departmentid = dtm.id
        INNER JOIN appointments atm
        ON mcr.appointmentid = atm.id
        WHERE pts.userid = ?;
    """;

    public static final String GET__INVOICE_BY_ID = """
        SELECT
          ivc.id as id,
          ivc.patientid as patientid,
          pts.fullname as patientName,
          EXTRACT(YEAR FROM AGE(NOW(), pts.age))::int as age,
          pts.gender as gender,
          pts.address as address,
          pts.phonenumber as phone,
          mcr.symptom as symptoms,
          mcr.diagnosis as diagnosis,
          mcr.treatmentplan as notes,
          dtm.name as treatment,
          TO_CHAR(atm.checkagain, 'YYYY-MM-DD') as nextAppointment,
          TO_CHAR(ivc.createdat, 'YYYY-MM-DD') as date,
          ivc.totalamount as totalMoney,
          CASE
            WHEN ivc.status = 'Chưa thanh toán' THEN false
          ELSE true
        END AS isPaid
        FROM invoice ivc
        INNER JOIN patients pts
        ON ivc.patientid = pts.id
        INNER JOIN medicalrecords mcr
        ON mcr.patientid = pts.id
        INNER JOIN doctors dt
        ON mcr.doctorid = dt.id
        INNER JOIN departments dtm
        ON dt.departmentid = dtm.id
        INNER JOIN appointments atm
        ON mcr.appointmentid = atm.id
        WHERE atm.id = ?;
    """;

    public static final String GET_BY_ID_INVOICE = """
        SELECT
          ivc.id as id,
          ivc.patientid as patientid,
          pts.fullname as patientName,
          EXTRACT(YEAR FROM AGE(NOW(), pts.age))::int as age,
          pts.gender as gender,
          pts.address as address,
          pts.phonenumber as phone,
          mcr.symptom as symptoms,
          mcr.diagnosis as diagnosis,
          mcr.treatmentplan as notes,
          dtm.name as treatment,
          TO_CHAR(atm.checkagain, 'YYYY-MM-DD') as nextAppointment,
          TO_CHAR(ivc.createdat, 'YYYY-MM-DD') as date,
          ivc.totalamount as totalMoney,
          CASE
            WHEN ivc.status = 'Chưa thanh toán' THEN false
              ELSE true
            END AS isPaid,
          dt.id as doctorid
        FROM invoice ivc
        INNER JOIN patients pts
        ON ivc.patientid = pts.id
        INNER JOIN medicalrecords mcr
        ON mcr.patientid = pts.id
        INNER JOIN doctors dt
        ON mcr.doctorid = dt.id
        INNER JOIN departments dtm
        ON dt.departmentid = dtm.id
        INNER JOIN appointments atm
        ON mcr.appointmentid = atm.id
        WHERE pts.userid = ?
        AND ivc.id = ?;
    """;

    public static final String UPDATE_STATUS_INVOICE = """
        UPDATE invoice
        SET status = ?
        WHERE id = ?;
    """;
}
