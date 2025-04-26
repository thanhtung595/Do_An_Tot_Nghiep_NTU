from django.db import connection
import logging
from apps.appointment.constants.appointment_query_constants import SELECT_ALL, SELECT_Appointments_BY_ID_Patients, SELECT_Appointments_BY_ID_Doctor
from apps.appointment.dtos.appointment_dto import AppointmentUpdate

logger = logging.getLogger(__name__)

class AppointmentService:
    @staticmethod
    def get_appointments_by_user(user_id, role):
        try:
            if role == "patient":
                with connection.cursor() as cursor:
                    cursor.execute(SELECT_Appointments_BY_ID_Patients, [user_id])
                    patient_appointments = cursor.fetchall()
                    data = [
                        dict(zip([col[0] for col in cursor.description], row)) 
                        for row in patient_appointments
                    ]
                    return data
            elif role == "doctor":
                with connection.cursor() as cursor:
                    cursor.execute(SELECT_Appointments_BY_ID_Doctor, [user_id])
                    doctor_appointments = cursor.fetchall()
                    data = [
                        dict(zip([col[0] for col in cursor.description], row)) 
                        for row in doctor_appointments
                    ]
                    return data
            return []
        except Exception as e:
            logger.error(f"Get appointments error: {str(e)}")
            raise

    @staticmethod
    def get_all_appointments():
        try:
            with connection.cursor() as cursor:
                cursor.execute(SELECT_ALL)
                appointments = cursor.fetchall()
                if not appointments:
                    return []
                data_list = [
                    dict(zip([col[0] for col in cursor.description], row)) 
                    for row in appointments
                ]
                return data_list
        except Exception as e:
            logger.error(f"Get all appointments error: {str(e)}")
            raise

    @staticmethod
    def update_appointment(data):
        try:
            with connection.cursor() as cursor:
                cursor.execute(
                    "UPDATE Appointments SET status = %s, notes = %s WHERE id = %s",
                    [data.status, data.notes, data.appointmentid]
                )
                cursor.execute(
                    "UPDATE medicalrecords SET diagnosis = %s, symptom = %s WHERE id = %s",
                    [data.diagnosis, data.symptom, data.medicalrecordsid]
                )
        except Exception as e:
            logger.error(f"Update appointment error: {str(e)}")
            raise 