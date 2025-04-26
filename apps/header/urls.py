from django.urls import path
from .views import HeaderView

urlpatterns = [
     path('header/', HeaderView.as_view(), name='get_all_header'),
]