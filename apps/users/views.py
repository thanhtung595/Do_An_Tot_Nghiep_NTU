from rest_framework.views import APIView
from rest_framework.response import Response
import logging
from .services.users_service import UserService

logger = logging.getLogger(__name__)

class GetUserView(APIView):
    def get(self, request):
        access_token = request.COOKIES.get('access_token')
        user_data, msg, status_code = UserService.get_user_by_token(access_token)
        
        if user_data is not None:
            return Response(user_data, status=status_code)
        else:
            return Response({"msg": msg}, status=status_code)

class GetAllUsers(APIView):
    def get(self, request):
        user_list, msg, status_code = UserService.get_all_users()
        
        if user_list is not None:
            return Response({"data": user_list}, status=status_code)
        else:
            return Response({"msg": msg}, status=status_code) 