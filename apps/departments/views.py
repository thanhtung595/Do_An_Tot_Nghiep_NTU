from django.shortcuts import render
from rest_framework.views import APIView
from rest_framework.response import Response
import logging
from .services.departments_service import DepartmentService

logger = logging.getLogger(__name__)

class DepartmentsGetView(APIView):
    def get(self, request):
        try:
            departments_list, msg, status_code = DepartmentService.get_all_departments()
            
            if departments_list is not None:
                return Response({"departments": departments_list}, status=status_code)
            else:
                return Response({"msg": msg}, status=status_code)
                
        except Exception as e:
            logger.error(f"Get departments error: {str(e)}")
            return Response({"msg": "Internal Server Error"}, status=500) 