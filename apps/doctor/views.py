from django.shortcuts import render
import re
from rest_framework.views import APIView
from rest_framework.response import Response
from django.db import connection, transaction
import logging
from .dtos.doctor_dto import DoctorDTO
from django.contrib.auth.hashers import make_password
from django.conf import settings
import jwt
from models.appointment_dto import Appointment
from ...core.services_core.logutils import log_utils
from .constants.doctor_query_constants import REGISTER_DOCTOR_BY_USERNAME_PASSWORDHASH, CREATE_MedicalRecords, SELECT_USER_BY_ID, CREATE_Schedule, CHECK_USER_REGISTER_EXIT, CREATE_DOCTOR, SELECT_DOCTOR_ALL, INSERT_SCHEDULE, UPDATE_DOCTOR_BY_ID, DELETE_SCHEDULE_BT_ID_DOCTOR, SELECT_Schedule_BY_DOCTOR_WORĐAY
from datetime import datetime
from django.utils import timezone

logger = logging.getLogger(__name__)

class DoctorView(APIView):

    # Hàm kiểm tra và trích xuất các ngày làm việc từ chuỗi đầu vào
    @classmethod
    def extract_days(cls, input_str):    
        # Danh sách các ngày hợp lệ (2-7 và CN)
        valid_days = ["2", "3", "4", "5", "6", "7", "CN"]

        # Regex phát hiện các ngày không hợp lệ
        invalid_pattern = r'Thứ\s*(?!2|3|4|5|6|7|CN)\d+|Thứ\s*(\d{2,})|Thứ\d'
        if re.search(invalid_pattern, input_str):
            return None

        # Regex lấy các thứ hợp lệ
        pattern = r'Thứ\s*(2|3|4|5|6|7|CN)'
        matches = re.findall(pattern, input_str)

        if not matches:
            return None

        # Loại bỏ trùng lặp và sắp xếp theo thứ tự trong tuần
        unique_days = list(dict.fromkeys(matches))
        sorted_days = sorted(unique_days, key=lambda x: valid_days.index(x))

        return sorted_days
    
    # Hàm kiểm tra và trích xuất giờ làm việc từ chuỗi đầu vào
    @classmethod
    def extract_times(cls, input_str):
        # Regex lấy giờ theo format HH:MM AM/PM
        pattern = r'((?:[01]\d|2[0-3]):[0-5]\d) (AM|PM)'
        matches = re.findall(pattern, input_str)
        
        if not matches or len(matches) != 2:
            return None
        
        # Lấy giờ mà không cần AM/PM
        times = [time[0] for time in matches]
        
        return times

    # Hàm cập nhật thông tin bác sĩ
    def put(self, request):
        try:
            # Lấy dữ liệu từ request
            data = DoctorDTO(request.data)
            print(data)

            # Kiểm tra thông tin bắt buộc
            if not data.username:
                return Response({"msg": "Cần nhập tài khoản"}, status=400)

            # Kiểm tra format ngày làm việc và lấy ra thứ làm việc
            list_day = self.extract_days(data.workdays)
            print(list_day)
            if list_day == None:
               return Response({"msg": "Cần nhập đúng format ngày thứ làm việc"}, status=400)
            
            # Kiểm tra format giờ làm việc
            list_time = self.extract_times(data.timeonline)
            print(list_time)
            if list_time == None:
               return Response({"msg": "Cần nhập đúng format giờ làm việc"}, status=400)
                 

            for day in list_day:
                print(day + list_time[0] + list_time[1])

            # Bắt đầu transaction để đảm bảo tính toàn vẹn dữ liệu
            with transaction.atomic():
                # Cập nhật thông tin bác sĩ
                with connection.cursor() as cursor:
                    cursor.execute(UPDATE_DOCTOR_BY_ID, [data.fullname, data.email, data.phone, data.address, data.experience, data.departmentID, data.idDoctor])
                
                # Xóa các lịch làm việc cũ của bác sĩ
                with connection.cursor() as cursor:
                    cursor.execute(DELETE_SCHEDULE_BT_ID_DOCTOR, [data.idDoctor])

                # Tạo lịch làm việc mới cho bác sĩ
                for day in list_day:
                    with connection.cursor() as cursor:
                        cursor.execute(INSERT_SCHEDULE, [data.idDoctor, day, list_time[0], list_time[1]])
                        schedule = cursor.fetchone()
                        if schedule == 0:
                            raise ValueError("Server error: Tạo lịch làm việc bác sỹ.")

            return Response({"msg": "Update successful"})
        except Exception as e:
            logger.error(f"Update Doctor: {str(e)}")
            return Response({"msg": "Internal Server Error"}, status=500)    
    
    # Hàm tạo mới bác sĩ
    def post(self, request):
        subject = 'register'
        
        try:
            # Kiểm tra và lấy thông tin từ token
            access_token = request.COOKIES.get('access_token')
            if not access_token:
               return Response({"msg": "Token is missing"}, status=401)     
            
            # Giải mã token và lấy user_id
            payload = jwt.decode(access_token, settings.SECRET_KEY, algorithms=['HS256'])
            user_id = payload.get('user_id')    

            if not user_id:
                return Response({"msg": "Invalid token"}, status=401)
            
            # Lấy dữ liệu từ request
            data = DoctorDTO(request.data)

            print(data)
            msg = "username = %s - password = %s", [data.username, data.password]
            # Ghi log bắt đầu quá trình đăng ký
            log_utils.LogStart(subject, msg)

            # Kiểm tra thông tin bắt buộc
            if not data.username or not data.password:
                msg = "Username and password are required"
                log_utils.LogWarning(subject, msg)
                return Response({"msg": msg}, status=400)

            # Bắt đầu transaction để đảm bảo tính toàn vẹn dữ liệu
            with transaction.atomic():
                # Kiểm tra format ngày làm việc
                list_day = self.extract_days(data.workdays)
                print(list_day)
                if list_day == None:
                    return Response({"msg": "Cần nhập đúng format ngày thứ làm việc"}, status=400)
                
                # Kiểm tra format giờ làm việc
                list_time = self.extract_times(data.timeonline)
                print(list_time)
                if list_time == None:
                    return Response({"msg": "Cần nhập đúng format giờ làm việc"}, status=400)

                # Kiểm tra tài khoản đã tồn tại chưa
                with connection.cursor() as cursor:
                    cursor.execute(CHECK_USER_REGISTER_EXIT, [data.username])
                    userExit = cursor.fetchone()
                    userExit = userExit[0]  # Lấy giá trị count từ tuple

                if userExit > 0:
                    msg = "Tài khoản đã tồn tại."
                    log_utils.LogWarning(subject, msg)
                    return Response({"msg": msg}, status=409)  # 409: Conflict    

                # Mã hóa mật khẩu trước khi lưu
                hashed_password = make_password(data.password)

                # Tạo tài khoản người dùng mới
                with connection.cursor() as cursor:
                    cursor.execute(REGISTER_DOCTOR_BY_USERNAME_PASSWORDHASH, [data.username, hashed_password])
                    userID = cursor.fetchone()

                # Kiểm tra việc tạo tài khoản
                if userID:
                    # Tạo thông tin bác sĩ
                    with connection.cursor() as cursor:
                        cursor.execute(CREATE_DOCTOR, [data.fullname, data.phone, data.email, data.address, userID, data.departmentID, data.experience])
                        doctorId = cursor.fetchone()
                    if doctorId == 0:
                        raise ValueError("Server error: Truy vấn tạo doctor")
                    
                    # Tạo lịch làm việc cho bác sĩ
                    for day in list_day:
                        with connection.cursor() as cursor:
                            cursor.execute(INSERT_SCHEDULE, [doctorId, day, list_time[0], list_time[1]])
                            schedule = cursor.fetchone()
                            if schedule == 0:
                                raise ValueError("Server error: Tạo lịch làm việc bác sỹ.")
                else:
                    raise ValueError("Server error: Kiểm tra tạo tài khoản có thành công")
                    
            log_utils.LogEnd(subject)
            return Response({"msg": "Register successful"})

        except Exception as e:
            error_message = f"{str(e)}"
            log_utils.LogError(subject, error_message)

            # Xử lý các trường hợp lỗi cụ thể
            if 'doctors_phonenumber_key' in error_message.lower():
                return Response({"msg": "Số điện thoại đã tồn tại"}, status=409) # 409: Conflict  

            if 'doctors_email_key' in error_message.lower():
                return Response({"msg": "Email đã tồn tại"}, status=409) # 409: Conflict 
            
            logger.error(f"subject: {str(e)}")
            return Response({"msg": "Internal Server Error"}, status=500)

    # Hàm lấy danh sách tất cả bác sĩ
    def get(self, request):
        try:
            # Truy vấn dữ liệu từ database
            with connection.cursor() as cursor:
                cursor.execute(SELECT_DOCTOR_ALL)
                doctor = cursor.fetchall()

            # Kiểm tra nếu không có dữ liệu
            if not doctor:
                return Response({"doctors": []}, status=200)

            # Chuyển đổi dữ liệu từ tuple sang dictionary
            doctor_list = [
                dict(zip([col[0] for col in cursor.description], row)) 
                for row in doctor
            ]

            return Response({"doctors": doctor_list})
        except Exception as e:
            logger.error(f"Login error: {str(e)}")
            return Response({"msg": "Internal Server Error"}, status=500)    
        
class DoctorViewClient(APIView):
    def post(self, request):
        subject = 'bookAppointment'
        
        try:
            # Kiểm tra và lấy thông tin từ token
            access_token = request.COOKIES.get('access_token')
            if not access_token:
               return Response({"msg": "Token is missing", "status":401}, status=401)     
            
            # Giải mã token và lấy user_id
            payload = jwt.decode(access_token, settings.SECRET_KEY, algorithms=['HS256'])
            if not payload:
                return Response({"msg": "Invalid token"}, status=401)
            user_id = payload.get('user_id')    

            if not user_id:
                return Response({"msg": "Invalid token"}, status=401)
            
            # Lấy dữ liệu từ request
            data = Appointment(request.data)
            print('workday: ', data.workday)
            print('timeOnline: ', data.timeOnline)
            # Chuyển đổi chuỗi workday thành đối tượng datetime
            workday_date = datetime.strptime(data.workday, '%Y-%m-%d').date()

            # Lấy ngày hiện tại
            today = timezone.now().date()

            if workday_date < today:
                msg = "Ngày đã qua vui lòng chọn ngày tiếp theo."
                return Response({"msg": msg}, status=400)

            # Lấy ra thứ trong tuần (thứ Hai = 2, ..., Chủ Nhật = 8)
            weekday_number = workday_date.isoweekday() + 1
            # Chuyển đổi thành chuỗi
            weekday_str = str(weekday_number)

            # Bắt đầu transaction ở đây
            with transaction.atomic():
                with connection.cursor() as cursor:
                    cursor.execute(SELECT_USER_BY_ID, [user_id])    
                    userExit = cursor.fetchone()

                print('userExit:', userExit[4])
                if(userExit[15] != 'patient'):
                    msg = "Tài khoản hiện tại không phải là bệnh nhân."
                    return Response({"msg": msg}, status=400)
                
                with connection.cursor() as cursor:
                    cursor.execute(SELECT_Schedule_BY_DOCTOR_WORĐAY, [data.doctorID, weekday_str, data.timeOnline, data.timeOnline])
                    result = cursor.fetchone()
                
                # Kiểm tra dữ liệu có tồn tại không
                exists = result is not None
                if not exists:
                    msg = f"Với ngày {workday_date} và giờ {data.timeOnline} không thuộc giờ hành chính của bác sỹ."
                    return Response({"msg": msg}, status=400)
                
                with connection.cursor() as cursor:
                    cursor.execute(CREATE_Schedule, [userExit[4], data.doctorID, data.note])
                    schedule = cursor.fetchone()

                    if schedule == 0:
                        raise ValueError("Server error: Đặt lịch khám.")
                    
                with connection.cursor() as cursor:
                    cursor.execute(CREATE_MedicalRecords, [userExit[4], data.doctorID, schedule, data.diagnosis])
                    
            log_utils.LogEnd(subject)
            return Response({"msg": "Cteate successful"})

        except Exception as e:
            error_message = f"{str(e)}"
            log_utils.LogError(subject, error_message)

            # # Kiểm tra số điện thoại có bị trùng
            # if 'doctors_phonenumber_key' in error_message.lower():
            #     return Response({"msg": "Số điện thoại đã tồn tại"}, status=409) # 409: Conflict  

            # # Kiểm tra số điện thoại có bị trùng
            # if 'doctors_email_key' in error_message.lower():
            #     return Response({"msg": "Email đã tồn tại"}, status=409) # 409: Conflict 
            
            logger.error(f"subject: {str(e)}")
            return Response({"msg": "Internal Server Error"}, status=500)    