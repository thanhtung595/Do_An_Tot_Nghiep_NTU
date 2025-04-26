from django.shortcuts import render
from rest_framework.views import APIView
from rest_framework.response import Response
from rest_framework import status, permissions
import logging
from models import RegisterUserDTO
from .services.auth_service import AuthService

logger = logging.getLogger(__name__)

class LoginView(APIView):
    def post(self, request):
        try:
            username = request.data.get("username")
            password = request.data.get("password")
            
            refresh, msg, status_code = AuthService.login(username, password)
            
            if refresh:
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
            logger.error(f"Login error: {str(e)}")
            return Response({"msg": "Internal Server Error"}, status=500)
        
class RegisterView(APIView):
    def post(self, request):
        try:
            data = RegisterUserDTO(request.data)
            result, msg, status_code = AuthService.register(data)
            return Response({"msg": msg}, status=status_code)

        except Exception as e:
            logger.error(f"Register error: {str(e)}")
            return Response({"msg": "Internal Server Error"}, status=500)

class LogoutView(APIView):
    def get(self, request):
        try:
            response = Response({"msg": "Logged out successfully"})
            response.delete_cookie("access_token")
            response.delete_cookie("refresh_token")
            return response
        except Exception as e:
            logger.error(f"Logout error: {str(e)}")
            return Response({"msg": "Internal Server Error"}, status=500) 