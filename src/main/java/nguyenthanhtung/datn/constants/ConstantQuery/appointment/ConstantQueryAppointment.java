package nguyenthanhtung.datn.constants.ConstantQuery.appointment;

public class ConstantQueryAppointment {

    public static final String SELECT_ALL = """
        SELECT
            ats.id AS id,
            us.id AS patientsID,
            usDt.id AS doctorID,
            ats.id AS appointmentID,
            mr.id as medicalRecordsID,
            TO_CHAR(ats.appointmentdate, 'yyyy-MM-dd') AS appointmentdate,
            ats.notes AS notes,
            usDt.username AS doctorName,
            us.username AS patientsName,
            mr.diagnosis,
            mr.treatmentplan,
            mr.symptom,
            ats.status
        FROM Appointments ats
            INNER JOIN patients pts
            ON ats.patientid = pts.id
            INNER JOIN USERS us
            ON pts.userid = us.id
            INNER JOIN MedicalRecords mr
            ON mr.PatientID = pts.id
            AND mr.AppointmentID = ats.id
            INNER JOIN Doctors dt
            ON ats.doctorid = dt.id
            INNER JOIN USERS usDt
            ON dt.userid = usDt.id
    """;

    public static final String SELECT_APPOINTMENTS_BY_ID_PATIENTS = """
        SELECT
            ats.id AS appointmentsID,
            us.id AS patientsID,
            usDt.id AS doctorID,
            TO_CHAR(ats.appointmentdate, 'yyyy-MM-dd') AS appointmentdate,
            ats.notes AS notes,
            usDt.username AS doctorName,
            mr.diagnosis,
            mr.treatmentplan,
            mr.symptom,
            ats.status,
            'Chưa thanh toán' AS paymentStatus
        FROM Appointments ats
            INNER JOIN patients pts
            ON ats.patientid = pts.id
            INNER JOIN USERS us
            ON pts.userid = us.id
            INNER JOIN MedicalRecords mr
            ON mr.PatientID = pts.id
            AND mr.AppointmentID = ats.id
            INNER JOIN Doctors dt
            ON ats.doctorid = dt.id
            INNER JOIN USERS usDt
            ON dt.userid = usDt.id
        WHERE us.id= ?
    """;

    public static final String SELECT_APPOINTMENTS_BY_ID_DOCTOR = """
        SELECT
            ats.id AS appointmentsID,
            us.id AS patientsID,
            usDt.id AS doctorID,
            TO_CHAR(ats.appointmentdate, 'yyyy-MM-dd') AS appointmentdate,
            ats.notes AS notes,
            us.username AS patientName,
            mr.diagnosis,
            mr.treatmentplan,
            mr.symptom,
            ats.status,
            'Chưa thanh toán' AS paymentStatus
        FROM Appointments ats
            INNER JOIN patients pts
            ON ats.patientid = pts.id
            INNER JOIN USERS us
            ON pts.userid = us.id
            INNER JOIN MedicalRecords mr
            ON mr.PatientID = pts.id
            AND mr.AppointmentID = ats.id
            INNER JOIN Doctors dt
            ON ats.doctorid = dt.id
            INNER JOIN USERS usDt
            ON dt.userid = usDt.id
        WHERE usDt.id= ?
    """;

    public static final String SELECT_PATIENT_HISTORY = """
        SELECT
            ats.id AS id,
            TO_CHAR(ats.appointmentdate, 'YYYY-MM-DD') AS date,
            TO_CHAR(ats.appointmentdate, 'HH24:MI:SS') AS time,
            ats.notes AS notes,
            pts.fullname AS patientName,
            pts.phonenumber AS patientPhonenumber,
            usDt.fullname AS doctorName,
            mr.diagnosis AS diagnosis,
            pts.id as patientid,
            dt.id as doctorid,
            mr.symptom AS symptom,
            ats.status AS status,
            TO_CHAR(ats.checkagain, 'YYYY-MM-DD') AS nextAppointment
        FROM Appointments ats
            INNER JOIN patients pts
            ON ats.patientid = pts.id
            INNER JOIN USERS us
            ON pts.userid = us.id
            INNER JOIN MedicalRecords mr
            ON mr.PatientID = pts.id
            AND mr.AppointmentID = ats.id
            INNER JOIN Doctors dt
            ON ats.doctorid = dt.id
            INNER JOIN USERS usDt
            ON dt.userid = usDt.id
        WHERE usDt.id= ?;
    """;

    public static final String UPDATE_STATUS_APPOINTMENT = """
        UPDATE Appointments
        SET status = ?
        WHERE id = ?;
    """;

    public static final String GET_APPOINTMENT_RECODR_BY_ID = """
        SELECT
            ats.id AS id,
            dt.id as doctorid,
            pts.fullname as patientname,
            ats.patientid as patientid,
            TO_CHAR(ats.appointmentdate, 'YYYY-MM-DD') AS date,
            TO_CHAR(ats.appointmentdate, 'HH24:MI:SS') AS time,
            mr.symptom as symptom,
            mr.diagnosis as diagnosis,
            ats.status as status,
            mr.treatmentplan as treatmentplan,
            TO_CHAR(ats.checkagain, 'YYYY-MM-DD') AS nextAppointment
        FROM Appointments ats
            INNER JOIN patients pts
            ON ats.patientid = pts.id
            INNER JOIN USERS us
            ON pts.userid = us.id
            INNER JOIN MedicalRecords mr
            ON mr.PatientID = pts.id
            AND mr.AppointmentID = ats.id
            INNER JOIN Doctors dt
            ON ats.doctorid = dt.id
            INNER JOIN USERS usDt
            ON dt.userid = usDt.id
        WHERE ats.id= ?;
    """;

    public static final String CHECK_INCOICE_PAYMENT = """
        SELECT * FROM Invoice
            WHERE patientid = ?;
    """;

    public static final String CREATE_INVOICE = """
        INSERT INTO Invoice(PatientID, TotalAmount, Status, CreatedAt) VALUES
        (:PatientID, :TotalAmount, 'Chưa thanh toán', NOW())
        RETURNING ID;
    """;

    public static final String CREATE_INVOICE_MEDICIES = """
        INSERT INTO MedicinesInvoice(InvoiceID, IdMedicines, Quantity, Price) VALUES
        (?, ?, 0, 0);
    """;

    public static final String UPDATE_MR_STATUS_CHECKAGAIN = """
        UPDATE Appointments
        SET Status = ?, CheckAgain = ?
        WHERE id = ?;
    """;

    public static final String UPDATE_MR_DTS = """
        UPDATE MedicalRecords
        SET Diagnosis = ?, TreatmentPlan = ?, Symptom = ?
        WHERE appointmentid = ?;
    """;

    public static final String DELETE_USE_SERVICE = """
        DELETE FROM UseServices
        WHERE PatientID = ?;
    """;

    public static final String DELETE_INVOICE_MEDICIES = """
        DELETE FROM MedicinesInvoice
        WHERE InvoiceID = ?;
    """;

    public static final String UPDATE_TOTAL_MOUNT = """
        UPDATE Invoice
        SET TotalAmount = ?
        WHERE id = ?;
    """;
}
