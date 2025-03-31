from django.shortcuts import render
from rest_framework.views import APIView
from rest_framework.response import Response
from django.db import connection
import logging
from querycontans import SELECT_ALL

logger = logging.getLogger(__name__)

class DepartmentsGetView(APIView):
    def get(self, request):
        try:
            # Truy vấn dữ liệu từ PostgreSQL
            with connection.cursor() as cursor:
                cursor.execute(SELECT_ALL)
                departments = cursor.fetchall()

            if not departments:
                return Response({"departments": []}, status=200)

            departments_list = [
                dict(zip([col[0] for col in cursor.description], row)) 
                for row in departments
            ]

            return Response({"departments": departments_list})
        except Exception as e:
            logger.error(f"Login error: {str(e)}")
            return Response({"msg": "Internal Server Error"}, status=500)    
