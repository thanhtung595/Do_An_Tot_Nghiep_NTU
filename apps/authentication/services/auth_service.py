from django.db import connection, transaction
from django.contrib.auth.hashers import make_password, check_password
from rest_framework_simplejwt.tokens import RefreshToken
import logging
from core.services_core.logutils import log_utils
from apps.authentication.constants.auth_query_constants import (
    GET_USER_BY_USERNAME_PASSWORDHASH, 
    CHECK_USER_REGISTER_EXIT, 
    REGISTER_USER_BY_USERNAME_PASSWORDHASH
)
from querycontans import CREATE_PATIENTS
from models import RegisterUserDTO

logger = logging.getLogger(__name__)

class AuthService:
    @staticmethod
    def login(username, password):
        subject = 'Login'
        try:
            log_utils.LogStart(subject, username)

            if not username or not password:
                return None, "Username and password are required", 400

            with connection.cursor() as cursor:
                cursor.execute(GET_USER_BY_USERNAME_PASSWORDHASH, [username])
                user = cursor.fetchone()

            if not user:
                waring_msg = "Invalid username or password"
                log_utils.LogWarning(subject, waring_msg)
                return None, waring_msg, 401

            password_db = str(user[2])
            if check_password(password, password_db):
                refresh = RefreshToken()
                refresh["user_id"] = str(user[0])
                refresh["role"] = str(user[3])
                
                log_utils.LogEnd(subject)
                return refresh, "Login successful", 200
            else:
                waring_msg = "Invalid username or password"
                log_utils.LogWarning(subject, waring_msg)
                return None, waring_msg, 401

        except Exception as e:
            error_message = f"{str(e)}"
            log_utils.log_error(subject, error_message)
            logger.error(f"Login error: {str(e)}")
            return None, "Internal Server Error", 500

    @staticmethod
    def register(data: RegisterUserDTO):
        subject = 'register'
        try:
            msg = "username = %s - password = %s", [data.username, data.password]
            log_utils.LogStart(subject, msg)

            if not data.username or not data.password:
                msg = "Username and password are required"
                log_utils.LogWarning(subject, msg)
                return None, msg, 400

            with transaction.atomic():
                with connection.cursor() as cursor:
                    cursor.execute(CHECK_USER_REGISTER_EXIT, [data.username])
                    userExit = cursor.fetchone()
                    userExit = userExit[0]

                if userExit > 0:
                    msg = "Tài khoản đã tồn tại."
                    log_utils.LogWarning(subject, msg)
                    return None, msg, 409

                hashed_password = make_password(data.password)

                with connection.cursor() as cursor:
                    cursor.execute(REGISTER_USER_BY_USERNAME_PASSWORDHASH, [data.username, hashed_password])
                    userID = cursor.fetchone()

                if userID:
                    with connection.cursor() as cursor:
                        cursor.execute(CREATE_PATIENTS, [data.fullname, data.phone, data.email, userID])
                        patient = cursor.fetchone()

                    log_utils.LogEnd(subject)
                    return patient, "Register successful", 200
                else:
                    raise ValueError("Tạo thất bại do server lỗi.")

        except Exception as e:
            error_message = f"{str(e)}"
            log_utils.LogError(subject, error_message)

            if 'patients_phonenumber_key' in error_message.lower():
                return None, "Số điện thoại đã tồn tại", 409

            if 'patients_email_key' in error_message.lower():
                return None, "Email đã tồn tại", 409
            
            logger.error(f"Login error: {str(e)}")
            return None, "Internal Server Error", 500 