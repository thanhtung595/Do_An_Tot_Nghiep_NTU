from django.urls import path
from .views import DoctorView, DoctorViewClient
urlpatterns = [
     path('doctor/', DoctorView.as_view(), name='doctor'),
     path('doctor/appointment/', DoctorViewClient.as_view(), name='doctor-client'),
]