from django.contrib import admin
from django.urls import re_path
from django.conf import settings
from django.views.static import serve
from django.urls import path, include
from django.conf.urls.static import static

urlpatterns = [
    path('admin/', admin.site.urls),
    path('api/', include('module_auth.urls')),
    path('api/', include('module_service.urls')),
    path('api/', include('module_header.urls')),
    path('api/', include('module_users.urls')),
    path('api/', include('module_departments.urls')),
    path('api/', include('module_doctor.urls')),
    path('api/', include('module_appointment.urls')),
    path('api/', include('module_gemini.urls')),

    re_path(r'^img/(?P<path>.*)$', serve, {'document_root': settings.IMG_ROOT}),
]

if settings.DEBUG:
    urlpatterns += static('/img/', document_root=settings.IMG_ROOT)