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
            # Kiểm tra và lấy thông tin từ token
            access_token = request.COOKIES.get('access_token')
            if not access_token:
               return Response({"msg": "Token is missing"}, status=401)     
            
            # Giải mã token và lấy thông tin người dùng
            payload = jwt.decode(access_token, settings.SECRET_KEY, algorithms=['HS256'])
            user_id = payload.get('user_id')    
            role = payload.get('role')    
            print("user_id: ",user_id)
            print("role: ",role)
            if not user_id:
                return Response({"msg": "Invalid token"}, status=401)
            
            # Xử lý theo vai trò người dùng
            if role == 'patient':
                # Lấy thông tin bệnh nhân
                with connection.cursor() as cursor:
                    cursor.execute(SELECT_USER_BY_ID, [user_id])
                    user = cursor.fetchone()

                if not user:
                    return Response({"user": []}, status=400)

                # Chuyển đổi dữ liệu từ tuple sang dictionary
                user_map_collum = dict(zip([col[0] for col in cursor.description], user))

                return Response(user_map_collum)
            else:
                # Lấy thông tin bác sĩ
                with connection.cursor() as cursor:
                    cursor.execute(SELECT_USER_DOCTOR_BY_ID, [user_id])
                    doctor = cursor.fetchone()

                if not doctor:
                    return Response({"doctor": []}, status=400)

                # Chuyển đổi dữ liệu từ tuple sang dictionary
                doctor_map_collum = dict(zip([col[0] for col in cursor.description], doctor))

                return Response(doctor_map_collum)   
        
        # Xử lý các trường hợp lỗi token
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
            # Truy vấn dữ liệu người dùng từ database
            with connection.cursor() as cursor:
                cursor.execute(SELECT_ALL_USER)
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
            logger.error(f"GetAllUsers error: {str(e)}")
            return Response({"msg": "Internal Server Error"}, status=500)    