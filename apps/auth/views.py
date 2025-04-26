from django.shortcuts import render
from rest_framework.views import APIView
from rest_framework.response import Response
from rest_framework import status, permissions
from rest_framework_simplejwt.tokens import RefreshToken
from django.db import connection, transaction
from django.contrib.auth.hashers import make_password, check_password
import logging
from ...core.services_core.logutils import log_utils
from .constants.auth_query_constants import GET_USER_BY_USERNAME_PASSWORDHASH, CHECK_USER_REGISTER_EXIT, REGISTER_USER_BY_USERNAME_PASSWORDHASH
from models import RegisterUserDTO
from querycontans import CREATE_PATIENTS
logger = logging.getLogger(__name__)

# Class xử lý đăng nhập
class LoginView(APIView):
    def post(self, request):
        subject = 'Login'
        try:
            # Lấy thông tin username và password từ request
            username = request.data.get("username")
            password = request.data.get("password")
            # Ghi log bắt đầu quá trình đăng nhập
            log_utils.LogStart(subject, username)

            # Kiểm tra thông tin đăng nhập có đầy đủ không
            if not username or not password:
                return Response({"msg": "Username and password are required"}, status=400)

            # Truy vấn thông tin người dùng từ database
            with connection.cursor() as cursor:
                cursor.execute(GET_USER_BY_USERNAME_PASSWORDHASH, [username])
                user = cursor.fetchone()

            # Kiểm tra người dùng có tồn tại không
            if not user:
                # Ghi log cảnh báo
                waring_msg = "Invalid username or password"
                log_utils.LogWarning(subject, waring_msg)
                return Response({"msg": waring_msg}, status=401)

            # Kiểm tra mật khẩu có khớp không
            password_db = str(user[2])
            if check_password(password, password_db):
                # Tạo JWT Token cho phiên đăng nhập
                refresh = RefreshToken()
                refresh["user_id"] = str(user[0])
                refresh["role"] = str(user[3])
                
                response = Response({"msg": "Login successful"})

                # Lưu token vào cookie
                response.set_cookie(
                    key="access_token", value=str(refresh.access_token),
                    httponly=True, samesite="Lax", secure=True
                )
                response.set_cookie(
                    key="refresh_token", value=str(refresh),
                    httponly=True, samesite="Lax", secure=True
                )

                # Ghi log kết thúc quá trình đăng nhập thành công
                log_utils.LogEnd(subject)
                return response
            else:
                # Ghi log cảnh báo mật khẩu không đúng
                waring_msg = "Invalid username or password"
                log_utils.LogWarning(subject, waring_msg)
                return Response({"msg": waring_msg}, status=401)

        except Exception as e:
            # Xử lý lỗi và ghi log
            error_message = f"{str(e)}"
            log_utils.log_error(subject, error_message)
            logger.error(f"Login error: {str(e)}")
            return Response({"msg": "Internal Server Error"}, status=500)
        
# Class xử lý đăng ký tài khoản
class RegisterView(APIView):
    def post(self, request):
        subject = 'register'
        data = RegisterUserDTO(request.data)
        print(data.email)
        try:
            data = RegisterUserDTO(request.data)
            msg = "username = %s - password = %s", [data.username, data.password]
            # Ghi log bắt đầu quá trình đăng ký
            log_utils.LogStart(subject, msg)

            # Kiểm tra thông tin đăng ký có đầy đủ không
            if not data.username or not data.password:
                msg = "Username and password are required"
                log_utils.LogWarning(subject, msg)
                return Response({"msg": msg}, status=400)

            # Bắt đầu transaction để đảm bảo tính toàn vẹn dữ liệu
            with transaction.atomic():
                # Kiểm tra tài khoản đã tồn tại chưa
                with connection.cursor() as cursor:
                    cursor.execute(CHECK_USER_REGISTER_EXIT, [data.username])
                    userExit = cursor.fetchone()
                    userExit = userExit[0]  # Lấy giá trị count từ tuple

                if userExit > 0:
                    msg = "Tài khoản đã tồn tại."
                    log_utils.LogWarning(subject, msg)
                    return Response({"msg": msg}, status=409)  # 409: Conflict    

                # Mã hóa mật khẩu trước khi lưu vào database
                hashed_password = make_password(data.password)

                # Tạo tài khoản người dùng mới
                with connection.cursor() as cursor:
                    cursor.execute(REGISTER_USER_BY_USERNAME_PASSWORDHASH, [data.username, hashed_password])
                    userID = cursor.fetchone()

                # Kiểm tra việc tạo tài khoản có thành công không
                if userID:
                    # Tạo thông tin bệnh nhân
                    with connection.cursor() as cursor:
                        cursor.execute(CREATE_PATIENTS, [data.fullname, data.phone, data.email, userID])
                        patient = cursor.fetchone()

                    log_utils.LogEnd(subject)
                    return Response({"msg": "Register successful"})
                else:
                    raise ValueError("Tạo thất bại do server lỗi.")

        except Exception as e:
            error_message = f"{str(e)}"
            log_utils.LogError(subject, error_message)

            # Xử lý các trường hợp lỗi cụ thể
            if 'patients_phonenumber_key' in error_message.lower():
                return Response({"msg": "Số điện thoại đã tồn tại"}, status=409) # 409: Conflict  

            if 'patients_email_key' in error_message.lower():
                return Response({"msg": "Email đã tồn tại"}, status=409) # 409: Conflict 
            
            logger.error(f"Login error: {str(e)}")
            return Response({"msg": "Internal Server Error"}, status=500)

# Class xử lý đăng xuất
class LogoutView(APIView):
    def get(self, request):
        try:
            # Xóa token và cookie khi đăng xuất
            response = Response({"msg": "Logged out successfully"})
            response.delete_cookie("access_token")
            response.delete_cookie("refresh_token")
            return response
        except Exception as e:
            logger.error(f"Login error: {str(e)}")
            return Response({"msg": "Internal Server Error"}, status=500)                  