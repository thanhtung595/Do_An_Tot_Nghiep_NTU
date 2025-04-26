# Import module AppConfig từ Django
from django.apps import AppConfig


class AppointmentConfig(AppConfig):
    """
    Cấu hình cho ứng dụng appointment
    """
    # Sử dụng BigAutoField làm trường khóa chính mặc định
    default_auto_field = 'django.db.models.BigAutoField'
    # Tên của ứng dụng
    name = 'apps.appointment'
