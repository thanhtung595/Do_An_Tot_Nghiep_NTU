# Import các thư viện cần thiết
from django.shortcuts import render
from rest_framework.views import APIView
from rest_framework.response import Response
from django.db import connection
import logging
from service.logutils import log_utils
from .constants.services_query_constants import SELECT_ALL_SERVICE

# Khởi tạo logger để ghi log
logger = logging.getLogger(__name__)

# Class xử lý các chức năng liên quan đến dịch vụ
class ServiceView(APIView):
    def get(self, request):
        try:
            # Truy vấn dữ liệu dịch vụ từ database
            with connection.cursor() as cursor:
                cursor.execute(SELECT_ALL_SERVICE)
                service = cursor.fetchall()

            # Kiểm tra nếu không có dữ liệu
            if not service:
                return Response({"service": []}, status=200)

            # Chuyển đổi dữ liệu từ tuple sang dictionary
            service_list = [
                dict(zip([col[0] for col in cursor.description], row)) 
                for row in service
            ]

            # Trả về danh sách dịch vụ
            return Response({"service": service_list})
        except Exception as e:
            # Xử lý lỗi và ghi log
            logger.error(f"Login error: {str(e)}")
            return Response({"msg": "Internal Server Error"}, status=500)    
