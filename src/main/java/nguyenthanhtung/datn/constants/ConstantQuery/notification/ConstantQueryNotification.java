package nguyenthanhtung.datn.constants.ConstantQuery.notification;

public class ConstantQueryNotification {

    public static final String SELECT_NOTIFICATION_BY_ID_PATIENTS_ID_NFT = """
        SELECT
          nft.id as id,
          pt.fullname as patientName,
          usdt.fullname as doctorName,
          pt.phonenumber as phone,
          tpnft.NameType as type,
          nft.title as title,
          nft.content as content,
          nft.isRead as isRead,
          nft.status as status,
          TO_CHAR(nft.date, 'YYYY-MM-DD"T"HH24:MI:SS') as date,
          nft.appointmentdate as appointmentDate,
          nft.appointmenttime as appointmentTime,
          nft.userto as userto,
          nft.userfrom as userfrom
        FROM Notification nft
          INNER JOIN Patients pt
          ON nft.patientid = pt.id
          INNER JOIN TypeNotification tpnft
          ON nft.idtype = tpnft.id
          INNER JOIN Users uspt
          ON pt.userid = uspt.id
          INNER JOIN Doctors dt
          ON nft.doctorid = dt.id
          INNER JOIN Users usdt
          ON dt.userid = usdt.id
        WHERE uspt.id = ?
        AND nft.namerole = 'patient'
        AND nft.id = ?;
    """;

    public static final String SELECT_NOTIFICATION_BY_ID_PATIENTS = """
        SELECT
          nft.id as id,
          pt.fullname as patientName,
          usdt.fullname as doctorName,
          pt.phonenumber as phone,
          tpnft.NameType as type,
          nft.title as title,
          nft.content as content,
          nft.isRead as isRead,
          nft.status as status,
          TO_CHAR(nft.date, 'YYYY-MM-DD"T"HH24:MI:SS') as date,
          nft.appointmentdate as appointmentDate,
          nft.appointmenttime as appointmentTime,
          nft.userto as userto,
          nft.userfrom as userfrom
        FROM Notification nft
          INNER JOIN Patients pt
          ON nft.patientid = pt.id
          INNER JOIN TypeNotification tpnft
          ON nft.idtype = tpnft.id
          INNER JOIN Users uspt
          ON pt.userid = uspt.id
          INNER JOIN Doctors dt
          ON nft.doctorid = dt.id
          INNER JOIN Users usdt
          ON dt.userid = usdt.id
        WHERE uspt.id = ?
        AND nft.namerole = 'patient';
    """;

    public static final String SELECT_NOTIFICATION_BY_ID_DOCTOR = """
        SELECT
           nft.id as id,
           pt.fullname as patientName,
           usdt.fullname as doctorName,
           pt.phonenumber as phone,
           tpnft.NameType as type,
           nft.title as title,
           nft.content as content,
           nft.isRead as isRead,
           nft.status as status,
           TO_CHAR(nft.date, 'YYYY-MM-DD"T"HH24:MI:SS') as date,
           nft.appointmentdate as appointmentDate,
           nft.appointmenttime as appointmentTime,
          nft.userto as userto,
          nft.userfrom as userfrom
         FROM Notification nft
           INNER JOIN Patients pt
           ON nft.patientid = pt.id
           INNER JOIN TypeNotification tpnft
           ON nft.idtype = tpnft.id
           INNER JOIN Users uspt
           ON pt.userid = uspt.id
           INNER JOIN Doctors dt
           ON nft.doctorid = dt.id
           INNER JOIN Users usdt
           ON dt.userid = usdt.id
         WHERE usdt.id = ?
         AND nft.namerole = 'doctor';
    """;

    public static final String CREATE_NOTIFICATION = """
        INSERT INTO notification(title, content, idtype, isread, appointmentdate, appointmenttime, patientid, doctorid, namerole, date, userfrom, userto) VALUES
        (:title, :content, :idtype, false, :appointmentdate, :appointmenttime, :patientid, :doctorid, :namerole, NOW(), :userfrom, :userto)
        RETURNING ID;
    """;

    public static final String COUNT_NOTIFICATION_PATIENTS = """
        SELECT
          COUNT(*)
        FROM Notification nft
          INNER JOIN Patients pt
          ON nft.patientid = pt.id
          INNER JOIN TypeNotification tpnft
          ON nft.idtype = tpnft.id
          INNER JOIN Users uspt
          ON pt.userid = uspt.id
          INNER JOIN Doctors dt
          ON nft.doctorid = dt.id
          INNER JOIN Users usdt
          ON dt.userid = usdt.id
        WHERE uspt.id = ?
        AND nft.namerole = 'patient'
        AND nft.isread = false;
    """;

    public static final String COUNT_NOTIFICATION_DOCTOR = """
        SELECT
           COUNT(*)
        FROM Notification nft
           INNER JOIN Patients pt
           ON nft.patientid = pt.id
           INNER JOIN TypeNotification tpnft
           ON nft.idtype = tpnft.id
           INNER JOIN Users uspt
           ON pt.userid = uspt.id
           INNER JOIN Doctors dt
           ON nft.doctorid = dt.id
           INNER JOIN Users usdt
           ON dt.userid = usdt.id
          WHERE usdt.id = ?
        AND nft.namerole = 'doctor'
        AND nft.isread = false;
    """;

    public static final String UPDATE_ISREAD = """
        UPDATE Notification SET isread = true
        WHERE id = ?;
    """;
}
