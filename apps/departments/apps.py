# Import module AppConfig từ Django
from django.apps import AppConfig


class DepartmentsConfig(AppConfig):
    """
    Cấu hình cho ứng dụng departments
    """
    # Sử dụng BigAutoField làm trường khóa chính mặc định
    default_auto_field = 'django.db.models.BigAutoField'
    # Tên của ứng dụng
    name = 'apps.departments'
