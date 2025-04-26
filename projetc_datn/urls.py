# Import các module cần thiết
from django.contrib import admin
from django.urls import re_path
from django.conf import settings
from django.views.static import serve
from django.urls import path, include
from django.conf.urls.static import static

# Định nghĩa các URL patterns của ứng dụng
urlpatterns = [
    # URL cho trang quản trị Django
    path('admin/', admin.site.urls),
    
    # Các API endpoints cho các ứng dụng
    path('api/', include('apps.users.urls')),           # API cho quản lý người dùng
    path('api/', include('apps.appointment.urls')),     # API cho quản lý lịch hẹn
    path('api/', include('apps.authentication.urls')),  # API cho xác thực
    path('api/', include('apps.departments.urls')),     # API cho quản lý khoa/phòng
    path('api/', include('apps.doctor.urls')),          # API cho quản lý bác sĩ
    path('api/', include('apps.gemini_ai.urls')),       # API cho tích hợp AI
    path('api/', include('apps.header.urls')),          # API cho header
    path('api/', include('apps.services.urls')),        # API cho dịch vụ

    # URL cho truy cập hình ảnh
    re_path(r'^img/(?P<path>.*)$', serve, {'document_root': settings.IMG_ROOT}),
]

# Trong môi trường development, thêm URL cho truy cập hình ảnh
if settings.DEBUG:
    urlpatterns += static('/img/', document_root=settings.IMG_ROOT)