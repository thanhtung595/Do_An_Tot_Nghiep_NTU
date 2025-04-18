from django.shortcuts import render
from rest_framework.views import APIView
from rest_framework.response import Response
from django.db import connection
import logging
from django.conf import settings
import jwt
from service.logutils import log_utils
from querycontans import UPDATE_Medicalrecords_Appointment, SELECT_ALL, SELECT_Appointments_BY_ID_Patients, SELECT_Appointments_BY_ID_Doctor
from models.appointment_update_dto import AppointmentUpdate

logger = logging.getLogger(__name__)

class AppointmentView(APIView):
    def get(self, request):
        try:
            print("-----AppointmentView!")
             # Lấy giá trị user_id của cookie `access_token`
            access_token = request.COOKIES.get('access_token')
            if not access_token:
               return Response({"msg": "Token is missing"}, status=401)     
            
             # Giải mã token và lấy user_id
            payload = jwt.decode(access_token, settings.SECRET_KEY, algorithms=['HS256'])
            user_id = payload.get('user_id')    
            role = payload.get('role')    
            print("user_id: ",user_id)
            print("role: ",role)
            if not user_id:
                return Response({"msg": "Invalid token"}, status=401)    

            if role == "patient":
                # Truy vấn dữ liệu từ PostgreSQL
                with connection.cursor() as cursor:
                    cursor.execute(SELECT_Appointments_BY_ID_Patients, [user_id])
                    patientAppointmen = cursor.fetchall()
                    data = [
                    dict(zip([col[0] for col in cursor.description], row)) 
                        for row in patientAppointmen
                    ]
                    return Response({"data": data})
            elif role == "doctor":
                # Truy vấn dữ liệu từ PostgreSQL
                with connection.cursor() as cursor:
                    cursor.execute(SELECT_Appointments_BY_ID_Doctor, [user_id])
                    doctorAppointmen = cursor.fetchall()
                    data = [
                    dict(zip([col[0] for col in cursor.description], row)) 
                        for row in doctorAppointmen
                    ]
                    return Response({"data": data})
        except Exception as e:
            logger.error(f"Login error: {str(e)}")
            return Response({"msg": "Internal Server Error"}, status=500)    


class GetAllAppointment(APIView):
    def get(self, request):
        try:
            # Truy vấn dữ liệu người dùng từ database
            with connection.cursor() as cursor:
                cursor.execute(SELECT_ALL)
                users = cursor.fetchall()

            # Kiểm tra nếu không có dữ liệu
            if not users:
                return Response({"data": []}, status=200)

            # Chuyển đổi dữ liệu từ tuple sang dictionary
            data_list = [
                dict(zip([col[0] for col in cursor.description], row)) 
                for row in users
            ]

            # Trả về danh sách người dùng
            return Response({"data": data_list})
        except Exception as e:
            # Xử lý lỗi và ghi log
            logger.error(f"GetAllAppointment error: {str(e)}")
            return Response({"msg": "Internal Server Error"}, status=500)    

class UpdateAppointment(APIView):
    def post(self, request):
        try:
            # Lấy dữ liệu từ request
            data = AppointmentUpdate(request.data)
            print(data)
            # Truy vấn dữ liệu người dùng từ database
            with connection.cursor() as cursor:
                cursor.execute(
                    "UPDATE Appointments SET status = %s, notes = %s WHERE id = %s",
                    [data.status, data.notes, data.appointmentid]
                )
                cursor.execute(
                    "UPDATE medicalrecords SET diagnosis = %s, symptom = %s WHERE id = %s",
                    [data.diagnosis, data.symptom, data.medicalrecordsid]
                )

            return Response({"msg": "Update successful"})
        except Exception as e:
            # Xử lý lỗi và ghi log
            logger.error(f"GetAllAppointment error: {str(e)}")
            return Response({"msg": "Internal Server Error"}, status=500)           