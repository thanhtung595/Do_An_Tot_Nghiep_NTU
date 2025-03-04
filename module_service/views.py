from django.shortcuts import render
from rest_framework.views import APIView
from rest_framework.response import Response
from django.db import connection
import logging
from service.logutils import log_utils
from .query_constants import SELECT_ALL_SERVICE

logger = logging.getLogger(__name__)

class ServiceView(APIView):
    def get(self, request):
        subject = 'service'
        try:
            # Write log start
            log_utils.LogStart(subject)
            
            # Truy vấn dữ liệu từ PostgreSQL
            with connection.cursor() as cursor:
                cursor.execute(SELECT_ALL_SERVICE)
                service = cursor.fetchall()

            if not service:
                return Response({"service": []}, status=200)

            service_list = [
                dict(zip([col[0] for col in cursor.description], row)) 
                for row in service
            ]

            # Write log end
            log_utils.LogEnd(subject)
            return Response({"service": service_list})
        except Exception as e:
            error_message = f"{str(e)}"
            log_utils.LogError(subject, error_message)

            logger.error(f"Login error: {str(e)}")
            return Response({"msg": "Internal Server Error"}, status=500)    
