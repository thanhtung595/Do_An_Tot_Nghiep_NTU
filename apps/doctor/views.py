from django.shortcuts import render
from rest_framework.views import APIView
from rest_framework.response import Response
import logging
from django.conf import settings
import jwt
from .dtos.doctor_dto import DoctorDTO
from models.appointment_dto import Appointment
from .services.doctor_service import DoctorService

logger = logging.getLogger(__name__)

class DoctorView(APIView):
    """
    API View để quản lý thông tin bác sĩ
    """
    def put(self, request):
        """
        Cập nhật thông tin bác sĩ
        """
        try:
            # Chuyển đổi dữ liệu request thành DTO
            data = DoctorDTO(request.data)
            # Gọi service để cập nhật thông tin bác sĩ
            result, msg, status_code = DoctorService.update_doctor(data)
            return Response({"msg": msg}, status=status_code)
        except Exception as e:
            # Xử lý lỗi khi cập nhật thông tin bác sĩ
            logger.error(f"Update Doctor error: {str(e)}")
            return Response({"msg": "Internal Server Error"}, status=500)    
    
    def post(self, request):
        """
        Tạo thông tin bác sĩ mới
        """
        try:
            # Kiểm tra và lấy thông tin từ access token
            access_token = request.COOKIES.get('access_token')
            if not access_token:
                return Response({"msg": "Token is missing"}, status=401)     
            
            # Giải mã token để lấy user_id
            payload = jwt.decode(access_token, settings.SECRET_KEY, algorithms=['HS256'])
            user_id = payload.get('user_id')    

            if not user_id:
                return Response({"msg": "Invalid token"}, status=401)
            
            # Chuyển đổi dữ liệu request thành DTO và tạo bác sĩ mới
            data = DoctorDTO(request.data)
            result, msg, status_code = DoctorService.create_doctor(data, user_id)
            return Response({"msg": msg}, status=status_code)

        except Exception as e:
            # Xử lý lỗi khi tạo bác sĩ mới
            logger.error(f"Create Doctor error: {str(e)}")
            return Response({"msg": "Internal Server Error"}, status=500)

    def get(self, request):
        """
        Lấy danh sách tất cả bác sĩ
        """
        try:
            # Gọi service để lấy danh sách bác sĩ
            doctors_list, msg, status_code = DoctorService.get_all_doctors()
            if doctors_list is not None:
                return Response({"doctors": doctors_list}, status=status_code)
            else:
                return Response({"msg": msg}, status=status_code)
        except Exception as e:
            # Xử lý lỗi khi lấy danh sách bác sĩ
            logger.error(f"Get Doctors error: {str(e)}")
            return Response({"msg": "Internal Server Error"}, status=500)    
        
class DoctorViewClient(APIView):
    """
    API View để đặt lịch hẹn với bác sĩ
    """
    def post(self, request):
        """
        Đặt lịch hẹn với bác sĩ
        """
        try:
            # Kiểm tra và lấy thông tin từ access token
            access_token = request.COOKIES.get('access_token')
            if not access_token:
                return Response({"msg": "Token is missing", "status":401}, status=401)     
            
            # Giải mã token để lấy thông tin người dùng
            payload = jwt.decode(access_token, settings.SECRET_KEY, algorithms=['HS256'])
            if not payload:
                return Response({"msg": "Invalid token"}, status=401)
            user_id = payload.get('user_id')    

            if not user_id:
                return Response({"msg": "Invalid token"}, status=401)
            
            # Chuyển đổi dữ liệu request thành DTO và đặt lịch hẹn
            data = Appointment(request.data)
            result, msg, status_code = DoctorService.book_appointment(data, user_id)
            return Response({"msg": msg}, status=status_code)

        except Exception as e:
            # Xử lý lỗi khi đặt lịch hẹn
            logger.error(f"Book Appointment error: {str(e)}")
            return Response({"msg": "Internal Server Error"}, status=500) 