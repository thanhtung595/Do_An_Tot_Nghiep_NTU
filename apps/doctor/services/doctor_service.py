import re
from django.db import connection, transaction
from django.contrib.auth.hashers import make_password
import logging
from django.conf import settings
import jwt
from datetime import datetime
from django.utils import timezone
from core.services_core.logutils import log_utils
from apps.doctor.constants.doctor_query_constants import (
    REGISTER_DOCTOR_BY_USERNAME_PASSWORDHASH, 
    CREATE_MedicalRecords, 
    SELECT_USER_BY_ID, 
    CREATE_Schedule, 
    CHECK_USER_REGISTER_EXIT, 
    CREATE_DOCTOR, 
    SELECT_DOCTOR_ALL, 
    INSERT_SCHEDULE, 
    UPDATE_DOCTOR_BY_ID, 
    DELETE_SCHEDULE_BT_ID_DOCTOR, 
    SELECT_Schedule_BY_DOCTOR_WORĐAY
)
from apps.doctor.dtos.doctor_dto import DoctorDTO
from models.appointment_dto import Appointment

logger = logging.getLogger(__name__)

class DoctorService:
    @staticmethod
    def extract_days(input_str):    
        valid_days = ["2", "3", "4", "5", "6", "7", "CN"]
        invalid_pattern = r'Thứ\s*(?!2|3|4|5|6|7|CN)\d+|Thứ\s*(\d{2,})|Thứ\d'
        if re.search(invalid_pattern, input_str):
            return None

        pattern = r'Thứ\s*(2|3|4|5|6|7|CN)'
        matches = re.findall(pattern, input_str)

        if not matches:
            return None

        unique_days = list(dict.fromkeys(matches))
        sorted_days = sorted(unique_days, key=lambda x: valid_days.index(x))
        return sorted_days
    
    @staticmethod
    def extract_times(input_str):
        pattern = r'((?:[01]\d|2[0-3]):[0-5]\d) (AM|PM)'
        matches = re.findall(pattern, input_str)
        
        if not matches or len(matches) != 2:
            return None
        
        times = [time[0] for time in matches]
        return times

    @staticmethod
    def update_doctor(data: DoctorDTO):
        try:
            if not data.username:
                return None, "Cần nhập tài khoản", 400

            list_day = DoctorService.extract_days(data.workdays)
            if list_day is None:
                return None, "Cần nhập đúng format ngày thứ làm việc", 400
            
            list_time = DoctorService.extract_times(data.timeonline)
            if list_time is None:
                return None, "Cần nhập đúng format giờ làm việc", 400

            with transaction.atomic():
                with connection.cursor() as cursor:
                    cursor.execute(UPDATE_DOCTOR_BY_ID, [data.fullname, data.email, data.phone, data.address, data.experience, data.departmentID, data.idDoctor])
                
                with connection.cursor() as cursor:
                    cursor.execute(DELETE_SCHEDULE_BT_ID_DOCTOR, [data.idDoctor])

                for day in list_day:
                    with connection.cursor() as cursor:
                        cursor.execute(INSERT_SCHEDULE, [data.idDoctor, day, list_time[0], list_time[1]])
                        schedule = cursor.fetchone()
                        if schedule == 0:
                            raise ValueError("Server error: Tạo lịch làm việc bác sỹ.")

            return True, "Update successful", 200
        except Exception as e:
            logger.error(f"Update Doctor: {str(e)}")
            return None, "Internal Server Error", 500

    @staticmethod
    def create_doctor(data: DoctorDTO, user_id: str):
        subject = 'register'
        try:
            msg = "username = %s - password = %s", [data.username, data.password]
            log_utils.LogStart(subject, msg)

            if not data.username or not data.password:
                return None, "Username and password are required", 400

            with transaction.atomic():
                list_day = DoctorService.extract_days(data.workdays)
                if list_day is None:
                    return None, "Cần nhập đúng format ngày thứ làm việc", 400
                
                list_time = DoctorService.extract_times(data.timeonline)
                if list_time is None:
                    return None, "Cần nhập đúng format giờ làm việc", 400

                with connection.cursor() as cursor:
                    cursor.execute(CHECK_USER_REGISTER_EXIT, [data.username])
                    userExit = cursor.fetchone()
                    userExit = userExit[0]

                if userExit > 0:
                    return None, "Tài khoản đã tồn tại.", 409

                hashed_password = make_password(data.password)

                with connection.cursor() as cursor:
                    cursor.execute(REGISTER_DOCTOR_BY_USERNAME_PASSWORDHASH, [data.username, hashed_password])
                    userID = cursor.fetchone()

                if userID:
                    with connection.cursor() as cursor:
                        cursor.execute(CREATE_DOCTOR, [data.fullname, data.phone, data.email, data.address, userID, data.departmentID, data.experience])
                        doctorId = cursor.fetchone()
                    if doctorId == 0:
                        raise ValueError("Server error: Truy vấn tạo doctor")
                    
                    for day in list_day:
                        with connection.cursor() as cursor:
                            cursor.execute(INSERT_SCHEDULE, [doctorId, day, list_time[0], list_time[1]])
                            schedule = cursor.fetchone()
                            if schedule == 0:
                                raise ValueError("Server error: Tạo lịch làm việc bác sỹ.")
                else:
                    raise ValueError("Server error: Kiểm tra tạo tài khoản có thành công")
                    
            log_utils.LogEnd(subject)
            return True, "Register successful", 200

        except Exception as e:
            error_message = f"{str(e)}"
            log_utils.LogError(subject, error_message)

            if 'doctors_phonenumber_key' in error_message.lower():
                return None, "Số điện thoại đã tồn tại", 409

            if 'doctors_email_key' in error_message.lower():
                return None, "Email đã tồn tại", 409
            
            logger.error(f"subject: {str(e)}")
            return None, "Internal Server Error", 500

    @staticmethod
    def get_all_doctors():
        try:
            with connection.cursor() as cursor:
                cursor.execute(SELECT_DOCTOR_ALL)
                doctor = cursor.fetchall()

            if not doctor:
                return [], "No doctors found", 200

            doctor_list = [
                dict(zip([col[0] for col in cursor.description], row)) 
                for row in doctor
            ]

            return doctor_list, "Success", 200
        except Exception as e:
            logger.error(f"Get doctors error: {str(e)}")
            return None, "Internal Server Error", 500

    @staticmethod
    def book_appointment(data: Appointment, user_id: str):
        subject = 'bookAppointment'
        try:
            workday_date = datetime.strptime(data.workday, '%Y-%m-%d').date()
            today = timezone.now().date()

            if workday_date < today:
                return None, "Ngày đã qua vui lòng chọn ngày tiếp theo.", 400

            weekday_number = workday_date.isoweekday() + 1
            weekday_str = str(weekday_number)

            with transaction.atomic():
                with connection.cursor() as cursor:
                    cursor.execute(SELECT_USER_BY_ID, [user_id])    
                    userExit = cursor.fetchone()

                if userExit[15] != 'patient':
                    return None, "Tài khoản hiện tại không phải là bệnh nhân.", 400
                
                with connection.cursor() as cursor:
                    cursor.execute(SELECT_Schedule_BY_DOCTOR_WORĐAY, [data.doctorID, weekday_str, data.timeOnline, data.timeOnline])
                    result = cursor.fetchone()
                
                if not result:
                    return None, f"Với ngày {workday_date} và giờ {data.timeOnline} không thuộc giờ hành chính của bác sỹ.", 400
                
                with connection.cursor() as cursor:
                    cursor.execute(CREATE_Schedule, [userExit[4], data.doctorID, data.note])
                    schedule = cursor.fetchone()

                    if schedule == 0:
                        raise ValueError("Server error: Đặt lịch khám.")
                    
                with connection.cursor() as cursor:
                    cursor.execute(CREATE_MedicalRecords, [userExit[4], data.doctorID, schedule, data.diagnosis])
                    
            log_utils.LogEnd(subject)
            return True, "Create successful", 200

        except Exception as e:
            error_message = f"{str(e)}"
            log_utils.LogError(subject, error_message)
            logger.error(f"subject: {str(e)}")
            return None, "Internal Server Error", 500 