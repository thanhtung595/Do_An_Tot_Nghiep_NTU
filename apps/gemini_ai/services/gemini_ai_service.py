import google.generativeai as genai
from django.db import connection
import json
import re
from apps.gemini_ai.constants.gemini_ai_query_constants import SELECT_DOCTOR_ALL_VI

# Cấu hình API key cho Gemini
genai.configure(api_key="AIzaSyDYt8TiuPNNiUE4P77uBohIWXtd8ZIXswE")

# Khởi tạo model Gemini
model = genai.GenerativeModel(model_name="models/gemini-1.5-pro")

class GeminiService:
    @staticmethod
    def get_diagnosis(symptoms: str):
        try:
            if not symptoms:
                return None, "Vui lòng nhập triệu chứng", 400

            # Truy vấn dữ liệu từ database
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

            # Tạo prompt cho AI với danh sách chuyên khoa
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

            # Gọi API Gemini để nhận kết quả chẩn đoán
            ai_response = model.generate_content(prompt)
            ai_text = ai_response.text.strip()
            print("ai_response.text:", ai_text)

            # Trích xuất JSON từ kết quả AI
            json_match = re.search(r"\{[\s\S]*?\}", ai_text)
            if not json_match:
                return None, "Không tìm thấy JSON trong phản hồi AI", 500

            # Phân tích kết quả JSON
            json_str = json_match.group(0)
            parsed_result = json.loads(json_str)

            diagnosis = parsed_result.get("chuandoan", "")
            chuyen_khoa = parsed_result.get("chuyenkhoa", "").strip()
            chuyen_khoa_lower = chuyen_khoa.lower()

            # Tìm bác sĩ phù hợp với chuyên khoa
            if chuyen_khoa_lower not in departments_lower:
                suggested_doctors = None
            else:
                suggested_doctors = [
                    doc for doc in doctors
                    if doc.get("khoa", "").strip().lower() == chuyen_khoa_lower
                ]
                if not suggested_doctors:
                    suggested_doctors = None

            result = {
                "diagnosis": diagnosis,
                "chuyen_khoa": chuyen_khoa,
                "suggested_doctors": suggested_doctors,
                "full_ai_output": ai_text
            }

            return result, "Success", 200

        except json.JSONDecodeError as e:
            return None, f"Lỗi phân tích JSON: {str(e)}", 500

        except Exception as e:
            return None, str(e), 500 