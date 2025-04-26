# Import các module cần thiết
from django.urls import path
from rest_framework_simplejwt.views import TokenRefreshView
from .views import LoginView, RegisterView, LogoutView

# Định nghĩa các URL patterns cho ứng dụng authentication
urlpatterns = [
    # API endpoint cho đăng nhập
    path('login/', LoginView.as_view(), name='login'),
    
    # API endpoint cho đăng ký
    path('register/', RegisterView.as_view(), name='register'),
    
    # API endpoint cho đăng xuất
    path('logout/', LogoutView.as_view(), name='logout'),
    
    # API endpoint để làm mới token
    path('token/refresh/', TokenRefreshView.as_view(), name='token_refresh'),
    
    # API endpoint cho danh sách người dùng (đã bị comment)
    # path('users/', UserListView.as_view(), name='user_list'),
]
