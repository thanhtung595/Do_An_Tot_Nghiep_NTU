from django.contrib import admin
from django.urls import re_path
from django.conf import settings
from django.views.static import serve
from django.urls import path, include
from django.conf.urls.static import static

urlpatterns = [
    path('admin/', admin.site.urls),
    path('api/', include('apps.users.urls')),
    path('api/', include('apps.appointment.urls')),
    path('api/', include('apps.authentication.urls')),
    path('api/', include('apps.departments.urls')),
    path('api/', include('apps.doctor.urls')),
    path('api/', include('apps.gemini_ai.urls')),
    path('api/', include('apps.header.urls')),
    path('api/', include('apps.services.urls')),

    re_path(r'^img/(?P<path>.*)$', serve, {'document_root': settings.IMG_ROOT}),
]

if settings.DEBUG:
    urlpatterns += static('/img/', document_root=settings.IMG_ROOT)