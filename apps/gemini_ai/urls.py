from django.urls import path
from .views import DiagnoseAPIView

urlpatterns = [
    path("diagnose-ai/", DiagnoseAPIView.as_view()),
]
