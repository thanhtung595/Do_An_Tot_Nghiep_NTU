from django.contrib import admin
from django.urls import re_path
from django.conf import settings
from django.views.static import serve
from django.urls import path, include
from django.conf.urls.static import static

urlpatterns = [
    path('admin/', admin.site.urls),
    path('api/', include('app.users.urls')),
    path('api/', include('app.appointment.urls')),
    path('api/', include('app.auth.urls')),
    path('api/', include('app.departments.urls')),
    path('api/', include('app.doctor.urls')),
    path('api/', include('app.gemini_ai.urls')),
    path('api/', include('app.header.urls')),
    path('api/', include('app.services.urls')),

    re_path(r'^img/(?P<path>.*)$', serve, {'document_root': settings.IMG_ROOT}),
]

if settings.DEBUG:
    urlpatterns += static('/img/', document_root=settings.IMG_ROOT)