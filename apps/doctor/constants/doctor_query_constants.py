REGISTER_DOCTOR_BY_USERNAME_PASSWORDHASH = """
    INSERT INTO Users(Username, PasswordHash, RoleID) VALUES
    (%s, %s, 2)
    RETURNING ID
"""

CREATE_MedicalRecords = """
INSERT INTO MedicalRecords(PatientID, DoctorID, AppointmentID, Diagnosis, Symptom) VALUES
(%s, %s, %s, '', %s)
"""

SELECT_USER_BY_ID = """
    SELECT * FROM USERS 
        INNER JOIN PATIENTS
        ON USERS.ID = PATIENTS.UserID
        INNER JOIN ROLES
        ON USERS.RoleID = ROLES.ID
    WHERE USERS.ID = %s
"""

CREATE_Schedule = """
INSERT INTO Appointments(patientid, doctorid, appointmentdate, status, notes) VALUES
(%s, %s, NOW(), 'Đã đặt', %s)
RETURNING ID
"""

CHECK_USER_REGISTER_EXIT = """
    SELECT COUNT(*) 
    FROM Users
    WHERE Username = %s
"""

CREATE_DOCTOR = """
    INSERT INTO Doctors(FullName, PhoneNumber, Email, Address, UserID, DepartmentID, Experience, image) VALUES
        (%s, %s, %s, %s, %s, %s, %s, 'img/doctor/avatar_default.jpg')
    RETURNING ID
"""

SELECT_DOCTOR_ALL = """
    SELECT  
        u.username
        ,d.id
        ,d.fullname
        ,d.email
        ,d.phonenumber
        ,d.experience
        ,d.image
        ,d.address
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
    FROM Doctors d
        INNER JOIN Users u
            ON d.userid = u.id
        INNER JOIN Departments detm
            ON d.departmentid = detm.id
        LEFT JOIN Schedule s
            ON d.id = s.doctorid
    GROUP BY 
        d.id
        ,u.username
        ,detm.name
        ,detm.id
"""

INSERT_SCHEDULE = """
    INSERT INTO Schedule(doctorid, workday, starttime, endtime) VALUES
    (%s, %s, %s, %s)
    RETURNING ID
"""

UPDATE_DOCTOR_BY_ID = """
    UPDATE Doctors 
    SET 
        fullname = %s
        ,email = %s
        ,phonenumber = %s
        ,address = %s
        ,experience = %s
        ,departmentid = %s
    WHERE Id = %s
"""

DELETE_SCHEDULE_BT_ID_DOCTOR = """
    DELETE FROM Schedule
    WHERE 
        doctorid = %s
"""

SELECT_Schedule_BY_DOCTOR_WORĐAY = """
    SELECT 1 FROM Schedule
    WHERE doctorid = %s 
    AND workday = %s
    AND starttime <= %s
    AND endtime >= %s
"""