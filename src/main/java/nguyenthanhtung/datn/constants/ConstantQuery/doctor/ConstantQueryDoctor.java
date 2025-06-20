package nguyenthanhtung.datn.constants.ConstantQuery.doctor;

public class ConstantQueryDoctor {

    public static final String CREATE_USER = """
        INSERT INTO "users" ("username", "passwordhash", "fullname", "phonenumber", "email", "address", "image", "gender", "roleid")
        VALUES (:username, :passwordhash, :fullname, :phonenumber, :email, :address, :image, :gender, :roleid)
        RETURNING ID;
    """;

    public static final String CREATE_DOCTOR = """
        INSERT INTO Doctors(specialization, userid, departmentid, experience)
        VALUES (:specialization, :userid, :departmentid, :experience)
        RETURNING ID;
    """;

    public static final String INSERT_SCHEDULE = """
        INSERT INTO Schedule(doctorid, workday, starttime, endtime) VALUES
        (:doctorid, :workday, :starttime, :endtime)
        RETURNING ID
    """;

    public static final String CREATE_MEDICAL_RECORDS = """
        INSERT INTO MedicalRecords(PatientID, DoctorID, AppointmentID, Diagnosis, Symptom) VALUES
            (:PatientID, :DoctorID, :AppointmentID, '', :Symptom)
        RETURNING ID;
    """;

    public static final String SELECT_USER_BY_ID = """
        SELECT PATIENTS.id FROM USERS
            INNER JOIN PATIENTS
            ON USERS.ID = PATIENTS.UserID
            INNER JOIN ROLES
            ON USERS.RoleID = ROLES.ID
        WHERE USERS.ID = ?
    """;

    public static final String CREATE_APPOINTMENTS = """
        INSERT INTO Appointments(patientid, doctorid, appointmentdate, status, notes, checkagain) VALUES
            (:patientid, :doctorid, :appointmentdate, 'Đợi duyệt', :notes, NULL)
            RETURNING ID
    """;

    public static final String CHECK_USER_REGISTER_EXIT = """
        (SELECT 'username' AS field FROM Users WHERE username = :username LIMIT 1)
        UNION ALL
        (SELECT 'email' FROM Users WHERE email = :email LIMIT 1)
        UNION ALL
        (SELECT 'phonenumber' FROM Users WHERE phonenumber = :phonenumber LIMIT 1);
    """;

    public static final String SELECT_DOCTOR_ALL = """
        SELECT
             u.username
            ,d.id
            ,u.fullname
            ,u.email
            ,u.phonenumber
            ,d.experience
            ,u.image
            ,u.address
            ,detm.name
            ,d.departmentid
            ,CASE
                WHEN COUNT(s.id) = 0 THEN 'Chưa xét lịch'
                ELSE STRING_AGG(CONCAT('Thứ ', s.workday), ' - '  ORDER BY s.workday)
            END AS workdays
            ,CASE
                WHEN COUNT(s.id) = 0 THEN 'Chưa xét lịch'
                ELSE STRING_AGG(
                DISTINCT CONCAT(
                    TO_CHAR(s.starttime, 'HH24:MI AM'),
                    ' - ',
                    TO_CHAR(s.endtime, 'HH24:MI PM')
                ), ' | '
                )
            END AS timeOnline
        FROM Users u
            INNER JOIN Doctors d
                ON d.userid = u.id
            INNER JOIN Departments detm
                ON d.departmentid = detm.id
            LEFT JOIN Schedule s
                ON d.id = s.doctorid
        GROUP BY
            d.id
            ,u.username
            ,u.fullname
            ,u.email
            ,u.phonenumber
            ,u.image
            ,detm.name
            ,u.address
            ,detm.id
    """;

    public static final String UPDATE_DOCTOR_BY_ID = """
        UPDATE doctors
        SET
          departmentid = :departmentid,
          experience = :experience
        WHERE id = :id
        RETURNING userid;
    """;

    public static final String UPDATE_USER_DOCTOR_BY_ID = """
        UPDATE users
        SET
          username = :username,
          fullname = :fullname,
          phonenumber = :phonenumber,
          email = :email,
          address = :address
        WHERE id = :id;
    """;

    public static final String DELETE_SCHEDULE_BT_ID_DOCTOR = """
        DELETE FROM Schedule
        WHERE
            doctorid = ?
    """;

    public static final String SELECT_SCHEDULE_BY_DOCTOR_WORDAY = """
        SELECT id FROM Schedule
        WHERE doctorid = :doctorid
            AND workday = :workday
            AND starttime <= :starttime
            AND endtime >= :endtime
    """;

    public static final String CHECK_BY_DOCTOR_APPOINTMENT_DATE = """
        SELECT id FROM appointments
        WHERE doctorid = :doctorid
        AND appointmentdate = :appointmentdate
        AND status = 'Đợi duyệt';
    """;

    public static final String CREATE_PATIENTS = """
        INSERT INTO PATIENTS(userid, fullname, age, gender, address, phonenumber, createdat) VALUES
        (:userid, :fullname, :age, :gender, :address, :phonenumber, NOW())
        RETURNING ID;
    """;
}
