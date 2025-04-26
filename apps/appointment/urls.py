# Import các module cần thiết
from django.urls import path
from .views import AppointmentView, GetAllAppointment, UpdateAppointment

# Định nghĩa các URL patterns cho ứng dụng appointment
urlpatterns = [
    # API endpoint để lấy danh sách lịch hẹn của người dùng
    path('appointment/', AppointmentView.as_view(), name='Appointment'),
    
    # API endpoint để lấy tất cả lịch hẹn
    path('appointment-getall/', GetAllAppointment.as_view(), name='GetAllAppointment'),
    
    # API endpoint để cập nhật thông tin lịch hẹn
    path('appointment-update/', UpdateAppointment.as_view(), name='UpdateAppointment'),
]