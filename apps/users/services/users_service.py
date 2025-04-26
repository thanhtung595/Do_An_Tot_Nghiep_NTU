import jwt
from django.conf import settings
from django.db import connection
from apps.users.constants.users_query_constants import SELECT_ALL_USER, SELECT_USER_BY_ID, SELECT_USER_DOCTOR_BY_ID
import logging

logger = logging.getLogger(__name__)

class UserService:
    @staticmethod
    def get_user_by_token(access_token):
        try:
            if not access_token:
                return None, "Token is missing", 401
            
            # Decode token and get user info
            payload = jwt.decode(access_token, settings.SECRET_KEY, algorithms=['HS256'])
            user_id = payload.get('user_id')    
            role = payload.get('role')    
            
            if not user_id:
                return None, "Invalid token", 401
            
            # Handle based on user role
            if role == 'patient':
                with connection.cursor() as cursor:
                    cursor.execute(SELECT_USER_BY_ID, [user_id])
                    user = cursor.fetchone()

                if not user:
                    return None, "User not found", 400

                user_data = dict(zip([col[0] for col in cursor.description], user))
                return user_data, "Success", 200
            else:
                with connection.cursor() as cursor:
                    cursor.execute(SELECT_USER_DOCTOR_BY_ID, [user_id])
                    doctor = cursor.fetchone()

                if not doctor:
                    return None, "Doctor not found", 400

                doctor_data = dict(zip([col[0] for col in cursor.description], doctor))
                return doctor_data, "Success", 200
                
        except jwt.ExpiredSignatureError:
            return None, "Token has expired", 401
        except jwt.InvalidTokenError:
            return None, "Invalid token", 401
        except Exception as e:
            logger.error(f"Token processing error: {str(e)}")
            return None, "Internal Server Error", 500

    @staticmethod
    def get_all_users():
        try:
            with connection.cursor() as cursor:
                cursor.execute(SELECT_ALL_USER)
                users = cursor.fetchall()

            if not users:
                return [], "No users found", 200

            user_list = [
                dict(zip([col[0] for col in cursor.description], row)) 
                for row in users
            ]

            return user_list, "Success", 200
        except Exception as e:
            logger.error(f"Get all users error: {str(e)}")
            return None, "Internal Server Error", 500 