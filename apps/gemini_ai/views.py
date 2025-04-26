from rest_framework.views import APIView
from rest_framework.response import Response
from .services.gemini_ai_service import GeminiService

class DiagnoseAPIView(APIView):
    """
    API View để chẩn đoán bệnh dựa trên triệu chứng sử dụng Gemini AI
    """
    def post(self, request):
        """
        Xử lý yêu cầu POST để chẩn đoán bệnh
        
        Args:
            request: Request object chứa triệu chứng bệnh
            
        Returns:
            Response: Kết quả chẩn đoán hoặc thông báo lỗi
        """
        symptoms = request.data.get("symptoms")
        result, msg, status_code = GeminiService.get_diagnosis(symptoms)
        
        if result is not None:
            return Response(result, status=status_code)
        else:
            return Response({"error": msg}, status=status_code) 