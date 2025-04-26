from django.shortcuts import render
from rest_framework.views import APIView
from rest_framework.response import Response
import logging
from .services.header_service import HeaderService

logger = logging.getLogger(__name__)

class HeaderView(APIView):
    def get(self, request):
        try:
            # Kiểm tra xem người dùng đã đăng nhập chưa
            is_logged_in = bool(request.COOKIES.get('access_token'))
            
            header_list, msg, status_code = HeaderService.get_header(is_logged_in)
            
            if header_list is not None:
                return Response({"header": header_list}, status=status_code)
            else:
                return Response({"msg": msg}, status=status_code)
                
        except Exception as e:
            logger.error(f"Get header error: {str(e)}")
            return Response({"msg": "Internal Server Error"}, status=500) 