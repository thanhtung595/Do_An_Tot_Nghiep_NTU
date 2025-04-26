# Import các module cần thiết
from django.urls import path
from .views import DoctorView, DoctorViewClient

# Định nghĩa các URL patterns cho ứng dụng doctor
urlpatterns = [
    # API endpoint để quản lý thông tin bác sĩ (thêm, sửa, xem danh sách)
    path('doctor/', DoctorView.as_view(), name='doctor'),
    
    # API endpoint để đặt lịch hẹn với bác sĩ
    path('doctor/appointment/', DoctorViewClient.as_view(), name='doctor-client'),
]