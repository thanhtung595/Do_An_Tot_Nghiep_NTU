# Import module AppConfig từ Django
from django.apps import AppConfig


class AuthConfig(AppConfig):
    """
    Cấu hình cho ứng dụng authentication
    """
    # Sử dụng BigAutoField làm trường khóa chính mặc định
    default_auto_field = 'django.db.models.BigAutoField'
    # Tên của ứng dụng
    name = 'apps.authentication'
