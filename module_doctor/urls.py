from django.urls import path
from .views import DoctorView
urlpatterns = [
     path('doctor/', DoctorView.as_view(), name='doctor'),
]