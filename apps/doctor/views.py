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
    def put(self, request):
        try:
            data = DoctorDTO(request.data)
            result, msg, status_code = DoctorService.update_doctor(data)
            return Response({"msg": msg}, status=status_code)
        except Exception as e:
            logger.error(f"Update Doctor error: {str(e)}")
            return Response({"msg": "Internal Server Error"}, status=500)    
    
    def post(self, request):
        try:
            access_token = request.COOKIES.get('access_token')
            if not access_token:
                return Response({"msg": "Token is missing"}, status=401)     
            
            payload = jwt.decode(access_token, settings.SECRET_KEY, algorithms=['HS256'])
            user_id = payload.get('user_id')    

            if not user_id:
                return Response({"msg": "Invalid token"}, status=401)
            
            data = DoctorDTO(request.data)
            result, msg, status_code = DoctorService.create_doctor(data, user_id)
            return Response({"msg": msg}, status=status_code)

        except Exception as e:
            logger.error(f"Create Doctor error: {str(e)}")
            return Response({"msg": "Internal Server Error"}, status=500)

    def get(self, request):
        try:
            doctors_list, msg, status_code = DoctorService.get_all_doctors()
            if doctors_list is not None:
                return Response({"doctors": doctors_list}, status=status_code)
            else:
                return Response({"msg": msg}, status=status_code)
        except Exception as e:
            logger.error(f"Get Doctors error: {str(e)}")
            return Response({"msg": "Internal Server Error"}, status=500)    
        
class DoctorViewClient(APIView):
    def post(self, request):
        try:
            access_token = request.COOKIES.get('access_token')
            if not access_token:
                return Response({"msg": "Token is missing", "status":401}, status=401)     
            
            payload = jwt.decode(access_token, settings.SECRET_KEY, algorithms=['HS256'])
            if not payload:
                return Response({"msg": "Invalid token"}, status=401)
            user_id = payload.get('user_id')    

            if not user_id:
                return Response({"msg": "Invalid token"}, status=401)
            
            data = Appointment(request.data)
            result, msg, status_code = DoctorService.book_appointment(data, user_id)
            return Response({"msg": msg}, status=status_code)

        except Exception as e:
            logger.error(f"Book Appointment error: {str(e)}")
            return Response({"msg": "Internal Server Error"}, status=500) 