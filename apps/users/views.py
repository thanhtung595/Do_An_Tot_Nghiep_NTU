from rest_framework.views import APIView
from rest_framework.response import Response
import logging
from .services.users_service import UserService

logger = logging.getLogger(__name__)

class GetUserView(APIView):
    """
    API View để lấy thông tin người dùng hiện tại
    """
    def get(self, request):
        # Lấy access token từ cookie
        access_token = request.COOKIES.get('access_token')
        # Gọi service để lấy thông tin người dùng
        user_data, msg, status_code = UserService.get_user_by_token(access_token)
        
        # Trả về kết quả
        if user_data is not None:
            return Response(user_data, status=status_code)
        else:
            return Response({"msg": msg}, status=status_code)

class GetAllUsers(APIView):
    """
    API View để lấy danh sách tất cả người dùng
    """
    def get(self, request):
        # Gọi service để lấy danh sách người dùng
        user_list, msg, status_code = UserService.get_all_users()
        
        # Trả về kết quả
        if user_list is not None:
            return Response({"data": user_list}, status=status_code)
        else:
            return Response({"msg": msg}, status=status_code) 