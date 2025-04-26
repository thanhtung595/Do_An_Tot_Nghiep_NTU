#!/usr/bin/env python
"""Django's command-line utility for administrative tasks."""
# File quản lý các lệnh dòng lệnh của Django
import os
import sys


def main():
    """Run administrative tasks."""
    # Thiết lập biến môi trường cho module settings của Django
    os.environ.setdefault('DJANGO_SETTINGS_MODULE', 'projetc_datn.settings')
    try:
        # Import module quản lý lệnh của Django
        from django.core.management import execute_from_command_line
    except ImportError as exc:
        # Xử lý lỗi khi không thể import Django
        raise ImportError(
            "Couldn't import Django. Are you sure it's installed and "
            "available on your PYTHONPATH environment variable? Did you "
            "forget to activate a virtual environment?"
        ) from exc
    # Thực thi lệnh từ dòng lệnh
    execute_from_command_line(sys.argv)


if __name__ == '__main__':
    # Chạy hàm main khi file được thực thi trực tiếp
    main()
