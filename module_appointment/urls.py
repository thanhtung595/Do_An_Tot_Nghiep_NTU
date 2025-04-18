from django.urls import path
from .views import AppointmentView, GetAllAppointment, UpdateAppointment
urlpatterns = [
     path('appointment/', AppointmentView.as_view(), name='Appointment'),
     path('appointment-getall/', GetAllAppointment.as_view(), name='GetAllAppointment'),
     path('appointment-update/', UpdateAppointment.as_view(), name='UpdateAppointment'),
]