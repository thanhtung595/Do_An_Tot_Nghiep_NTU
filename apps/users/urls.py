# Import các module cần thiết
from django.urls import path
from .views import GetUserView, GetAllUsers

# Định nghĩa các URL patterns cho ứng dụng users
urlpatterns = [
    # API endpoint để lấy thông tin người dùng hiện tại
    path('user/', GetUserView.as_view(), name='get_user_by_id'),
    
    # API endpoint để lấy danh sách tất cả người dùng
    path('user-all/', GetAllUsers.as_view(), name='get_all_user'),
]