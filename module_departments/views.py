# Import các thư viện cần thiết
from django.shortcuts import render
from rest_framework.views import APIView
from rest_framework.response import Response
from django.db import connection
import logging
from querycontans import SELECT_ALL_Departments

# Khởi tạo logger để ghi log
logger = logging.getLogger(__name__)

# Class xử lý việc lấy danh sách các khoa/phòng ban
class DepartmentsGetView(APIView):
    def get(self, request):
        try:
            # Truy vấn dữ liệu từ database để lấy danh sách các khoa/phòng ban
            with connection.cursor() as cursor:
                cursor.execute(SELECT_ALL_Departments)
                departments = cursor.fetchall()

            # Kiểm tra nếu không có dữ liệu trả về danh sách rỗng
            if not departments:
                return Response({"departments": []}, status=200)

            # Chuyển đổi dữ liệu từ tuple sang dictionary để dễ dàng xử lý
            departments_list = [
                dict(zip([col[0] for col in cursor.description], row)) 
                for row in departments
            ]

            # Trả về danh sách các khoa/phòng ban
            return Response({"departments": departments_list})
        except Exception as e:
            # Xử lý lỗi và ghi log
            logger.error(f"Login error: {str(e)}")
            return Response({"msg": "Internal Server Error"}, status=500)    
