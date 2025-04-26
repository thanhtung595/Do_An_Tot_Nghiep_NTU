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
    """
    Lớp dịch vụ xử lý các thao tác liên quan đến bác sĩ
    
    Cung cấp các phương thức để:
    - Tạo và cập nhật thông tin bác sĩ
    - Quản lý lịch làm việc của bác sĩ
    - Đặt lịch hẹn khám bệnh
    - Truy vấn danh sách bác sĩ
    """
    
    @staticmethod
    def extract_days(input_str):    
        """
        Trích xuất các ngày làm việc từ chuỗi đầu vào
        
        Args:
            input_str (str): Chuỗi chứa thông tin ngày làm việc (ví dụ: "Thứ 2, Thứ 4, Thứ 6")
            
        Returns:
            list: Danh sách các ngày làm việc đã được sắp xếp
            None: Nếu định dạng không hợp lệ
            
        Ví dụ:
            >>> extract_days("Thứ 2, Thứ 4, Thứ 6")
            ['2', '4', '6']
        """
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
        """
        Trích xuất thời gian làm việc từ chuỗi đầu vào
        
        Args:
            input_str (str): Chuỗi chứa thời gian làm việc (ví dụ: "08:00 AM - 17:00 PM")
            
        Returns:
            list: Danh sách chứa thời gian bắt đầu và kết thúc
            None: Nếu định dạng không hợp lệ
            
        Ví dụ:
            >>> extract_times("08:00 AM - 17:00 PM")
            ['08:00', '17:00']
        """
        pattern = r'((?:[01]\d|2[0-3]):[0-5]\d) (AM|PM)'
        matches = re.findall(pattern, input_str)
        
        if not matches or len(matches) != 2:
            return None
        
        times = [time[0] for time in matches]
        return times

    @staticmethod
    def update_doctor(data: DoctorDTO):
        """
        Cập nhật thông tin bác sĩ
        
        Args:
            data (DoctorDTO): Đối tượng chứa thông tin cập nhật của bác sĩ
            
        Returns:
            tuple: (result, message, status_code)
                - result: True nếu thành công, None nếu thất bại
                - message: Thông báo kết quả
                - status_code: Mã HTTP trả về
                
        Quy trình:
        1. Kiểm tra thông tin bắt buộc
        2. Trích xuất và kiểm tra ngày làm việc
        3. Trích xuất và kiểm tra thời gian làm việc
        4. Cập nhật thông tin trong database
        5. Xóa lịch làm việc cũ
        6. Tạo lịch làm việc mới
        """
        try:
            # Kiểm tra thông tin bắt buộc
            if not data.username:
                return None, "Cần nhập tài khoản", 400

            # Trích xuất và kiểm tra ngày làm việc
            list_day = DoctorService.extract_days(data.workdays)
            if list_day is None:
                return None, "Cần nhập đúng format ngày thứ làm việc", 400
            
            # Trích xuất và kiểm tra thời gian làm việc
            list_time = DoctorService.extract_times(data.timeonline)
            if list_time is None:
                return None, "Cần nhập đúng format giờ làm việc", 400

            # Cập nhật thông tin trong transaction
            with transaction.atomic():
                # Cập nhật thông tin bác sĩ
                with connection.cursor() as cursor:
                    cursor.execute(UPDATE_DOCTOR_BY_ID, [data.fullname, data.email, data.phone, data.address, data.experience, data.departmentID, data.idDoctor])
                
                # Xóa lịch làm việc cũ
                with connection.cursor() as cursor:
                    cursor.execute(DELETE_SCHEDULE_BT_ID_DOCTOR, [data.idDoctor])

                # Tạo lịch làm việc mới
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
        """
        Tạo bác sĩ mới
        
        Args:
            data (DoctorDTO): Đối tượng chứa thông tin bác sĩ mới
            user_id (str): ID của người dùng tạo bác sĩ
            
        Returns:
            tuple: (result, message, status_code)
                - result: True nếu thành công, None nếu thất bại
                - message: Thông báo kết quả
                - status_code: Mã HTTP trả về
                
        Quy trình:
        1. Kiểm tra thông tin bắt buộc
        2. Trích xuất và kiểm tra ngày làm việc
        3. Trích xuất và kiểm tra thời gian làm việc
        4. Kiểm tra tài khoản đã tồn tại
        5. Mã hóa mật khẩu
        6. Tạo tài khoản người dùng
        7. Tạo thông tin bác sĩ
        8. Tạo lịch làm việc
        """
        subject = 'register'
        try:
            # Ghi log bắt đầu quá trình đăng ký
            msg = "username = %s - password = %s", [data.username, data.password]
            log_utils.LogStart(subject, msg)

            # Kiểm tra thông tin bắt buộc
            if not data.username or not data.password:
                return None, "Username and password are required", 400

            # Tạo bác sĩ trong transaction
            with transaction.atomic():
                # Trích xuất và kiểm tra ngày làm việc
                list_day = DoctorService.extract_days(data.workdays)
                if list_day is None:
                    return None, "Cần nhập đúng format ngày thứ làm việc", 400
                
                # Trích xuất và kiểm tra thời gian làm việc
                list_time = DoctorService.extract_times(data.timeonline)
                if list_time is None:
                    return None, "Cần nhập đúng format giờ làm việc", 400

                # Kiểm tra tài khoản đã tồn tại
                with connection.cursor() as cursor:
                    cursor.execute(CHECK_USER_REGISTER_EXIT, [data.username])
                    userExit = cursor.fetchone()
                    userExit = userExit[0]

                if userExit > 0:
                    return None, "Tài khoản đã tồn tại.", 409

                # Mã hóa mật khẩu
                hashed_password = make_password(data.password)

                # Tạo tài khoản người dùng
                with connection.cursor() as cursor:
                    cursor.execute(REGISTER_DOCTOR_BY_USERNAME_PASSWORDHASH, [data.username, hashed_password])
                    userID = cursor.fetchone()

                if userID:
                    # Tạo thông tin bác sĩ
                    with connection.cursor() as cursor:
                        cursor.execute(CREATE_DOCTOR, [data.fullname, data.phone, data.email, data.address, userID, data.departmentID, data.experience])
                        doctorId = cursor.fetchone()
                    if doctorId == 0:
                        raise ValueError("Server error: Truy vấn tạo doctor")
                    
                    # Tạo lịch làm việc
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

            # Xử lý các lỗi cụ thể
            if 'doctors_phonenumber_key' in error_message.lower():
                return None, "Số điện thoại đã tồn tại", 409

            if 'doctors_email_key' in error_message.lower():
                return None, "Email đã tồn tại", 409
            
            logger.error(f"subject: {str(e)}")
            return None, "Internal Server Error", 500

    @staticmethod
    def get_all_doctors():
        """
        Lấy danh sách tất cả bác sĩ
        
        Returns:
            tuple: (result, message, status_code)
                - result: Danh sách bác sĩ hoặc None nếu có lỗi
                - message: Thông báo kết quả
                - status_code: Mã HTTP trả về
                
        Quy trình:
        1. Truy vấn danh sách bác sĩ từ database
        2. Chuyển đổi kết quả thành danh sách dictionary
        3. Trả về kết quả
        """
        try:
            # Lấy danh sách bác sĩ từ database
            with connection.cursor() as cursor:
                cursor.execute(SELECT_DOCTOR_ALL)
                doctor = cursor.fetchall()

            if not doctor:
                return [], "No doctors found", 200

            # Chuyển đổi kết quả thành danh sách dictionary
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
        """
        Đặt lịch hẹn với bác sĩ
        
        Args:
            data (Appointment): Đối tượng chứa thông tin lịch hẹn
            user_id (str): ID của người dùng đặt lịch
            
        Returns:
            tuple: (result, message, status_code)
                - result: True nếu thành công, None nếu thất bại
                - message: Thông báo kết quả
                - status_code: Mã HTTP trả về
                
        Quy trình:
        1. Kiểm tra ngày hẹn có hợp lệ không
        2. Chuyển đổi ngày thành số thứ tự trong tuần
        3. Kiểm tra người dùng có phải là bệnh nhân không
        4. Kiểm tra lịch làm việc của bác sĩ
        5. Tạo lịch hẹn
        6. Tạo hồ sơ bệnh án
        """
        subject = 'bookAppointment'
        try:
            # Kiểm tra ngày hẹn có hợp lệ không
            workday_date = datetime.strptime(data.workday, '%Y-%m-%d').date()
            today = timezone.now().date()

            if workday_date < today:
                return None, "Ngày đã qua vui lòng chọn ngày tiếp theo.", 400

            # Chuyển đổi ngày thành số thứ tự trong tuần
            weekday_number = workday_date.isoweekday() + 1
            weekday_str = str(weekday_number)

            # Đặt lịch hẹn trong transaction
            with transaction.atomic():
                # Kiểm tra người dùng có phải là bệnh nhân không
                with connection.cursor() as cursor:
                    cursor.execute(SELECT_USER_BY_ID, [user_id])    
                    userExit = cursor.fetchone()

                if userExit[15] != 'patient':
                    return None, "Tài khoản hiện tại không phải là bệnh nhân.", 400
                
                # Kiểm tra lịch làm việc của bác sĩ
                with connection.cursor() as cursor:
                    cursor.execute(SELECT_Schedule_BY_DOCTOR_WORĐAY, [data.doctorID, weekday_str, data.timeOnline, data.timeOnline])
                    result = cursor.fetchone()
                
                if not result:
                    return None, f"Với ngày {workday_date} và giờ {data.timeOnline} không thuộc giờ hành chính của bác sỹ.", 400
                
                # Tạo lịch hẹn
                with connection.cursor() as cursor:
                    cursor.execute(CREATE_Schedule, [userExit[4], data.doctorID, data.note])
                    schedule = cursor.fetchone()

                    if schedule == 0:
                        raise ValueError("Server error: Đặt lịch khám.")
                    
                # Tạo hồ sơ bệnh án
                with connection.cursor() as cursor:
                    cursor.execute(CREATE_MedicalRecords, [userExit[4], data.doctorID, schedule, data.diagnosis])
                    
            log_utils.LogEnd(subject)
            return True, "Create successful", 200

        except Exception as e:
            error_message = f"{str(e)}"
            log_utils.LogError(subject, error_message)
            logger.error(f"subject: {str(e)}")
            return None, "Internal Server Error", 500 