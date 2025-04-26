from django.urls import path
from .views import DepartmentsGetView

urlpatterns = [
     path('departments/', DepartmentsGetView.as_view(), name='get_all_departments'),
]