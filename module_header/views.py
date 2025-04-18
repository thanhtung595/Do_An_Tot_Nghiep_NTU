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
            # Mặc định là đã đăng nhập
            isToken = "1";     
            
            # Kiểm tra xem người dùng đã đăng nhập chưa
            access_token = request.COOKIES.get('access_token')
            if not access_token:
               isToken = "0";     

            # Truy vấn dữ liệu header từ database
            with connection.cursor() as cursor:
                cursor.execute(GET_ALL_HEADER, [isToken])
                header = cursor.fetchall()

            # Kiểm tra nếu không có dữ liệu
            if not header:
                return Response({"header": []}, status=400)

            # Chuyển đổi dữ liệu từ tuple sang dictionary
            header_list = [
                dict(zip([col[0] for col in cursor.description], row)) 
                for row in header
            ]

            # Trả về danh sách header
            return Response({"header": header_list})
        except Exception as e:
            # Xử lý lỗi và ghi log
            logger.error(f"Login error: {str(e)}")
            return Response({"msg": "Internal Server Error"}, status=500)    