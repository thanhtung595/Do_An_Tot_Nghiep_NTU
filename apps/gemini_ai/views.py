from rest_framework.views import APIView
from rest_framework.response import Response
from .services.gemini_ai_service import GeminiService

class DiagnoseAPIView(APIView):
    def post(self, request):
        symptoms = request.data.get("symptoms")
        result, msg, status_code = GeminiService.get_diagnosis(symptoms)
        
        if result is not None:
            return Response(result, status=status_code)
        else:
            return Response({"error": msg}, status=status_code) 