SELECT_Appointments_BY_ID_Patients = """
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
    WHERE us.id= %s
"""

SELECT_Appointments_BY_ID_Doctor = """
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
    WHERE usDt.id= %s
"""

SELECT_ALL = """
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
"""

UPDATE_Medicalrecords_Appointment = """
    UPDATE Appointments
        SET status = %s, notes = %s
    WHERE id = %s;
    UPDATE medicalrecords
        SET diagnosis = %s, symptom = %s 
    WHERE id = %s;
"""