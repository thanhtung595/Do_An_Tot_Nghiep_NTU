from django.shortcuts import render
from rest_framework.views import APIView
from rest_framework.response import Response
from rest_framework import status, permissions
from rest_framework_simplejwt.tokens import RefreshToken
from django.db import connection
from django.contrib.auth.hashers import check_password
from django.shortcuts import get_object_or_404
import logging
from service.logutils import log_utils

logger = logging.getLogger(__name__)

# Function login
class LoginView(APIView):
    def post(self, request):
        subject = 'Login'
        try:
            username = request.data.get("username")
            password = request.data.get("password")
            # Write log start
            log_utils.LogStart(subject, username)

            if not username or not password:
                return Response({"error": "Username and password are required"}, status=400)

            # Truy vấn dữ liệu từ PostgreSQL
            with connection.cursor() as cursor:
                cursor.execute("SELECT id, username, password FROM account WHERE username = %s", [username])
                user = cursor.fetchone()

            if not user:
                # Write log warning
                waring_msg = "Invalid username or password"
                log_utils.LogWarning(subject, waring_msg)
                return Response({"error": waring_msg}, status=401)

            # user_id, _, hashed_password = user  # user[0] = id, user[1] = username, user[2] = password

            # if not check_password(password, hashed_password):
            #     return Response({"error": "Invalid username or password"}, status=401)

            # Tạo JWT Token
            refresh = RefreshToken()
            refresh["user_id"] = str(user[0])
            
            response = Response({"message": "Login successful"})

            # Sử dụng cookie để lưu token
            response.set_cookie(
                key="access_token", value=str(refresh.access_token),
                httponly=True, samesite="Lax", secure=True
            )
            response.set_cookie(
                key="refresh_token", value=str(refresh),
                httponly=True, samesite="Lax", secure=True
            )

            # Write log end
            log_utils.LogEnd(subject)
            return response

        except Exception as e:
            error_message = f"{str(e)}"
            log_utils.log_error(subject, error_message)

            logger.error(f"Login error: {str(e)}")
            return Response({"error": "Internal Server Error"}, status=500)