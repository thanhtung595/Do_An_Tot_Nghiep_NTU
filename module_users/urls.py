from django.urls import path
from .views import GetUserView

urlpatterns = [
     path('user/', GetUserView.as_view(), name='get_user_by_id'),
]