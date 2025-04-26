from django.shortcuts import render
from rest_framework.views import APIView
from rest_framework.response import Response
from rest_framework import status, permissions
import logging
from models import RegisterUserDTO
from .services.auth_service import AuthService

logger = logging.getLogger(__name__)

class LoginView(APIView):
    """
    API View xử lý đăng nhập người dùng
    """
    def post(self, request):
        try:
            # Lấy thông tin đăng nhập từ request
            username = request.data.get("username")
            password = request.data.get("password")
            
            # Gọi service để xử lý đăng nhập
            refresh, msg, status_code = AuthService.login(username, password)
            
            if refresh:
                # Nếu đăng nhập thành công, set cookies cho access token và refresh token
                response = Response({"msg": msg})
                response.set_cookie(
                    key="access_token", 
                    value=str(refresh.access_token),
                    httponly=True, 
                    samesite="Lax", 
                    secure=True
                )
                response.set_cookie(
                    key="refresh_token", 
                    value=str(refresh),
                    httponly=True, 
                    samesite="Lax", 
                    secure=True
                )
                return response
            else:
                return Response({"msg": msg}, status=status_code)

        except Exception as e:
            # Xử lý lỗi khi đăng nhập
            logger.error(f"Login error: {str(e)}")
            return Response({"msg": "Internal Server Error"}, status=500)
        
class RegisterView(APIView):
    """
    API View xử lý đăng ký người dùng mới
    """
    def post(self, request):
        try:
            # Chuyển đổi dữ liệu request thành DTO
            data = RegisterUserDTO(request.data)
            # Gọi service để xử lý đăng ký
            result, msg, status_code = AuthService.register(data)
            return Response({"msg": msg}, status=status_code)

        except Exception as e:
            # Xử lý lỗi khi đăng ký
            logger.error(f"Register error: {str(e)}")
            return Response({"msg": "Internal Server Error"}, status=500)

class LogoutView(APIView):
    """
    API View xử lý đăng xuất người dùng
    """
    def get(self, request):
        try:
            # Xóa cookies chứa token khi đăng xuất
            response = Response({"msg": "Logged out successfully"})
            response.delete_cookie("access_token")
            response.delete_cookie("refresh_token")
            return response
        except Exception as e:
            # Xử lý lỗi khi đăng xuất
            logger.error(f"Logout error: {str(e)}")
            return Response({"msg": "Internal Server Error"}, status=500) 