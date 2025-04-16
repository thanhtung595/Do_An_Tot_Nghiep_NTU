import jwt
from django.conf import settings
from rest_framework.views import APIView
from rest_framework.response import Response
from django.db import connection
from querycontans import SELECT_ALL_USER, SELECT_USER_BY_ID, SELECT_USER_DOCTOR_BY_ID
import logging

logger = logging.getLogger(__name__)

class GetUserView(APIView):
    def get(self, request):
        try:
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
            
            if role == 'patient':
                # Truy vấn dữ liệu từ PostgreSQL
                with connection.cursor() as cursor:
                    cursor.execute(SELECT_USER_BY_ID, [user_id])
                    user = cursor.fetchone()

                if not user:
                    return Response({"user": []}, status=400)

                user_map_collum = dict(zip([col[0] for col in cursor.description], user))

                return Response(user_map_collum)
            else:
                 # Truy vấn dữ liệu từ PostgreSQL
                with connection.cursor() as cursor:
                    cursor.execute(SELECT_USER_DOCTOR_BY_ID, [user_id])
                    doctor = cursor.fetchone()

                if not doctor:
                    return Response({"doctor": []}, status=400)

                doctor_map_collum = dict(zip([col[0] for col in cursor.description], doctor))

                return Response(doctor_map_collum)   
        
        except jwt.ExpiredSignatureError:
            return Response({"msg": "Token has expired"}, status=401)
        except jwt.InvalidTokenError:
            return Response({"msg": "Invalid token"}, status=401)
        except Exception as e:
            logger.error(f"Header error: {str(e)}")
            return Response({"msg": "Internal Server Error"}, status=500)
        
class GetAllUsers(APIView):
    def get(self, request):
        try:
            # Truy vấn dữ liệu từ PostgreSQL
            with connection.cursor() as cursor:
                cursor.execute(SELECT_ALL_USER)
                users = cursor.fetchall()

            if not users:
                return Response({"data": []}, status=200)

            data_list = [
                dict(zip([col[0] for col in cursor.description], row)) 
                for row in users
            ]

            return Response({"data": data_list})
        except Exception as e:
            logger.error(f"GetAllUsers error: {str(e)}")
            return Response({"msg": "Internal Server Error"}, status=500)    