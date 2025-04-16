SELECT_Schedule_BY_DOCTOR_WORĐAY = """
    SELECT 1 FROM Schedule
    WHERE doctorid = %s 
    AND workday = %s
    AND starttime <= %s
    AND endtime >= %s
"""

CREATE_Schedule = """
INSERT INTO Appointments(patientid, doctorid, appointmentdate, status, notes) VALUES
(%s, %s, NOW(), 'Đã đặt', %s)
RETURNING ID
"""

CREATE_MedicalRecords = """
INSERT INTO MedicalRecords(PatientID, DoctorID, AppointmentID, Diagnosis, Symptom) VALUES
(%s, %s, %s, '', %s)
"""