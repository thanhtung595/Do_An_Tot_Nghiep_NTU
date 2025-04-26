from django.shortcuts import render
from rest_framework.views import APIView
from rest_framework.response import Response
import logging
from .services.services_service import ServiceService

logger = logging.getLogger(__name__)

class ServiceView(APIView):
    def get(self, request):
        try:
            service_list, msg, status_code = ServiceService.get_all_services()
            
            if service_list is not None:
                return Response({"service": service_list}, status=status_code)
            else:
                return Response({"msg": msg}, status=status_code)
                
        except Exception as e:
            logger.error(f"Get services error: {str(e)}")
            return Response({"msg": "Internal Server Error"}, status=500) 