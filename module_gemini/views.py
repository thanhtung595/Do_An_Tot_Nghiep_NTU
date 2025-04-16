from rest_framework.views import APIView
from rest_framework.response import Response
import google.generativeai as genai
from rest_framework.response import Response
from django.db import connection
from querycontans import SELECT_DOCTOR_ALL_VI
import json
import re

genai.configure(api_key="AIzaSyDYt8TiuPNNiUE4P77uBohIWXtd8ZIXswE")

model = genai.GenerativeModel(model_name="models/gemini-1.5-pro")
models = genai.list_models()
class DiagnoseAPIView(APIView):
    def post(self, request):
        symptoms = request.data.get("symptoms")
        if not symptoms:
            return Response({"error": "Vui lòng nhập triệu chứng"}, status=400)

        try:
            with connection.cursor() as cursor:
                # Lấy danh sách bác sĩ
                cursor.execute(SELECT_DOCTOR_ALL_VI)
                doctors_raw = cursor.fetchall()
                colnames = [desc[0] for desc in cursor.description]
                doctors = [dict(zip(colnames, row)) for row in doctors_raw]

                # Lấy danh sách chuyên khoa
                cursor.execute("SELECT name FROM Departments")
                departments_raw = cursor.fetchall()
                departments = [row[0].strip() for row in departments_raw]
                departments_lower = [d.lower() for d in departments]

            # Prompt cho AI kèm danh sách chuyên khoa
            departments_str = "\n".join([f"- {dep}" for dep in departments])
            prompt = f"""
            Bạn là một bác sĩ AI.

            Dưới đây là danh sách chuyên khoa hiện có tại bệnh viện:
            {departments_str}

            Dựa trên các triệu chứng sau: "{symptoms}", 
            hãy:
            1. Viết mô tả chẩn đoán (dài, chi tiết, có thể sử dụng \\n, \\t để trình bày).
            2. Trả về kết quả theo đúng định dạng JSON sau:

            [JSON]
            {{
                "chuandoan": "Đoạn mô tả có thể dài, giải thích các triệu chứng liên quan",
                "benh": "Tên bệnh có khả năng cao",
                "chuyenkhoa": "Chọn duy nhất một tên chuyên khoa từ danh sách ở trên, nếu không có chuyên khoa phù hợp hãy đưa ra tên chuyên khoa khác"
            }}

            Không trả về bất kỳ thông tin nào ngoài JSON.
            """

            # Gọi Gemini
            ai_response = model.generate_content(prompt)
            ai_text = ai_response.text.strip()
            print("ai_response.text:", ai_text)

            # Trích xuất JSON từ AI
            json_match = re.search(r"\{[\s\S]*?\}", ai_text)
            if not json_match:
                return Response({
                    "error": "Không tìm thấy JSON trong phản hồi AI",
                    "ai_raw_output": ai_text
                }, status=500)

            json_str = json_match.group(0)
            parsed_result = json.loads(json_str)

            diagnosis = parsed_result.get("chuandoan", "")
            chuyen_khoa = parsed_result.get("chuyenkhoa", "").strip()
            chuyen_khoa_lower = chuyen_khoa.lower()

            # Kiểm tra xem chuyên khoa có trong danh sách không
            if chuyen_khoa_lower not in departments_lower:
                suggested_doctors = None
            else:
                suggested_doctors = [
                    doc for doc in doctors
                    if doc.get("khoa", "").strip().lower() == chuyen_khoa_lower
                ]
                if not suggested_doctors:
                    suggested_doctors = None

            return Response({
                "diagnosis": diagnosis,
                "chuyen_khoa": chuyen_khoa,
                "suggested_doctors": suggested_doctors,
                "full_ai_output": ai_text
            })

        except json.JSONDecodeError as e:
            return Response({
                "error": f"Lỗi phân tích JSON: {str(e)}",
                "ai_raw_output": ai_text
            }, status=500)

        except Exception as e:
            return Response({"error": str(e)}, status=500)







# for m in models:
#     print(m.name, "→", m.supported_generation_methods)    
# return Response({"diagnosis": "ai_response.text"})