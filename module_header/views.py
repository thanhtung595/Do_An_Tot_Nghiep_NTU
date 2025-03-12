from django.shortcuts import render
from rest_framework.views import APIView
from rest_framework.response import Response
from django.db import connection
from .query_constants import GET_ALL_HEADER
import logging

logger = logging.getLogger(__name__)

class HeaderView(APIView):
    def get(self, request):
        try:
            isToken = "1";     
             # Lấy giá trị của cookie `access_token`
            access_token = request.COOKIES.get('access_token')
            if not access_token:
               isToken = "0";     

            # Truy vấn dữ liệu từ PostgreSQL
            with connection.cursor() as cursor:
                cursor.execute(GET_ALL_HEADER, [isToken])
                header = cursor.fetchall()

            if not header:
                return Response({"header": []}, status=400)

            header_list = [
                dict(zip([col[0] for col in cursor.description], row)) 
                for row in header
            ]

            return Response({"header": header_list})
        except Exception as e:
            logger.error(f"Login error: {str(e)}")
            return Response({"msg": "Internal Server Error"}, status=500)    