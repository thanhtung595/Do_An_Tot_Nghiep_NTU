from django.urls import path
from .views import GetUserView, GetAllUsers

urlpatterns = [
     path('user/', GetUserView.as_view(), name='get_user_by_id'),
     path('user-all/', GetAllUsers.as_view(), name='get_all_user'),
]