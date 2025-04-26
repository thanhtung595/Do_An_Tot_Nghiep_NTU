from django.shortcuts import render
from rest_framework.views import APIView
from rest_framework.response import Response
import logging
from django.conf import settings
import jwt
from core.services_core.logutils import log_utils
from .dtos.appointment_dto import AppointmentUpdate
from .services.appointment_service import AppointmentService

logger = logging.getLogger(__name__)

class AppointmentView(APIView):
    """
    API View để lấy danh sách lịch hẹn của người dùng
    """
    def get(self, request):
        try:
            print("-----AppointmentView!")
            # Lấy access token từ cookie
            access_token = request.COOKIES.get('access_token')
            if not access_token:
               return Response({"msg": "Token is missing"}, status=401)     
            
            # Giải mã token để lấy thông tin người dùng
            payload = jwt.decode(access_token, settings.SECRET_KEY, algorithms=['HS256'])
            user_id = payload.get('user_id')    
            role = payload.get('role')    
            print("user_id: ",user_id)
            print("role: ",role)
            if not user_id:
                return Response({"msg": "Invalid token"}, status=401)    

            # Lấy danh sách lịch hẹn theo user_id và role
            data = AppointmentService.get_appointments_by_user(user_id, role)
            return Response({"data": data})
        except Exception as e:
            logger.error(f"Login error: {str(e)}")
            return Response({"msg": "Internal Server Error"}, status=500)    

class GetAllAppointment(APIView):
    """
    API View để lấy tất cả lịch hẹn
    """
    def get(self, request):
        try:
            # Lấy danh sách tất cả lịch hẹn
            data_list = AppointmentService.get_all_appointments()
            return Response({"data": data_list})
        except Exception as e:
            logger.error(f"GetAllAppointment error: {str(e)}")
            return Response({"msg": "Internal Server Error"}, status=500)    

class UpdateAppointment(APIView):
    """
    API View để cập nhật thông tin lịch hẹn
    """
    def post(self, request):
        try:
            # Chuyển đổi dữ liệu request thành DTO
            data = AppointmentUpdate(request.data)
            print(data)
            # Cập nhật thông tin lịch hẹn
            AppointmentService.update_appointment(data)
            return Response({"msg": "Update successful"})
        except Exception as e:
            logger.error(f"GetAllAppointment error: {str(e)}")
            return Response({"msg": "Internal Server Error"}, status=500) 