# Import các module cần thiết
from django.urls import path
from .views import DepartmentsGetView

# Định nghĩa các URL patterns cho ứng dụng departments
urlpatterns = [
    # API endpoint để lấy danh sách tất cả các khoa/phòng
    path('departments/', DepartmentsGetView.as_view(), name='get_all_departments'),
]