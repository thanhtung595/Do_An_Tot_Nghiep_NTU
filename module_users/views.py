import jwt
from django.conf import settings
from rest_framework.views import APIView
from rest_framework.response import Response
from django.db import connection
from querycontans import SELECT_USER_BY_ID
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

            if not user_id:
                return Response({"msg": "Invalid token"}, status=401)
            
            # Truy vấn dữ liệu từ PostgreSQL
            with connection.cursor() as cursor:
                cursor.execute(SELECT_USER_BY_ID, [user_id])
                user = cursor.fetchone()

            if not user:
                return Response({"user": []}, status=400)

            user_map_collum = dict(zip([col[0] for col in cursor.description], user))

            return Response(user_map_collum)
        
        except jwt.ExpiredSignatureError:
            return Response({"msg": "Token has expired"}, status=401)
        except jwt.InvalidTokenError:
            return Response({"msg": "Invalid token"}, status=401)
        except Exception as e:
            logger.error(f"Header error: {str(e)}")
            return Response({"msg": "Internal Server Error"}, status=500)