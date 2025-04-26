from django.db import connection
import logging
from apps.appointment.constants.appointment_query_constants import SELECT_ALL, SELECT_Appointments_BY_ID_Patients, SELECT_Appointments_BY_ID_Doctor
from apps.appointment.dtos.appointment_dto import AppointmentUpdate

logger = logging.getLogger(__name__)

class AppointmentService:
    """
    Lớp dịch vụ xử lý các thao tác liên quan đến lịch hẹn
    
    Cung cấp các phương thức để:
    - Lấy danh sách lịch hẹn theo người dùng (bệnh nhân/bác sĩ)
    - Lấy danh sách tất cả lịch hẹn
    - Cập nhật thông tin lịch hẹn
    """
    
    @staticmethod
    def get_appointments_by_user(user_id, role):
        """
        Lấy danh sách lịch hẹn theo người dùng
        
        Args:
            user_id (str): ID của người dùng
            role (str): Vai trò của người dùng ("patient" hoặc "doctor")
            
        Returns:
            list: Danh sách lịch hẹn dưới dạng danh sách dictionary
            []: Nếu không tìm thấy lịch hẹn
            
        Raises:
            Exception: Nếu có lỗi xảy ra trong quá trình truy vấn
            
        Quy trình:
        1. Kiểm tra vai trò người dùng
        2. Truy vấn lịch hẹn từ database
        3. Chuyển đổi kết quả thành danh sách dictionary
        4. Trả về kết quả
        """
        try:
            if role == "patient":
                with connection.cursor() as cursor:
                    cursor.execute(SELECT_Appointments_BY_ID_Patients, [user_id])
                    patient_appointments = cursor.fetchall()
                    data = [
                        dict(zip([col[0] for col in cursor.description], row)) 
                        for row in patient_appointments
                    ]
                    return data
            elif role == "doctor":
                with connection.cursor() as cursor:
                    cursor.execute(SELECT_Appointments_BY_ID_Doctor, [user_id])
                    doctor_appointments = cursor.fetchall()
                    data = [
                        dict(zip([col[0] for col in cursor.description], row)) 
                        for row in doctor_appointments
                    ]
                    return data
            return []
        except Exception as e:
            logger.error(f"Get appointments error: {str(e)}")
            raise

    @staticmethod
    def get_all_appointments():
        """
        Lấy danh sách tất cả lịch hẹn
        
        Returns:
            list: Danh sách tất cả lịch hẹn dưới dạng danh sách dictionary
            []: Nếu không có lịch hẹn nào
            
        Raises:
            Exception: Nếu có lỗi xảy ra trong quá trình truy vấn
            
        Quy trình:
        1. Truy vấn tất cả lịch hẹn từ database
        2. Chuyển đổi kết quả thành danh sách dictionary
        3. Trả về kết quả
        """
        try:
            with connection.cursor() as cursor:
                cursor.execute(SELECT_ALL)
                appointments = cursor.fetchall()
                if not appointments:
                    return []
                data_list = [
                    dict(zip([col[0] for col in cursor.description], row)) 
                    for row in appointments
                ]
                return data_list
        except Exception as e:
            logger.error(f"Get all appointments error: {str(e)}")
            raise

    @staticmethod
    def update_appointment(data):
        """
        Cập nhật thông tin lịch hẹn
        
        Args:
            data (AppointmentUpdate): Đối tượng chứa thông tin cập nhật
            
        Raises:
            Exception: Nếu có lỗi xảy ra trong quá trình cập nhật
            
        Quy trình:
        1. Cập nhật trạng thái và ghi chú của lịch hẹn
        2. Cập nhật chẩn đoán và triệu chứng trong hồ sơ bệnh án
        """
        try:
            with connection.cursor() as cursor:
                cursor.execute(
                    "UPDATE Appointments SET status = %s, notes = %s WHERE id = %s",
                    [data.status, data.notes, data.appointmentid]
                )
                cursor.execute(
                    "UPDATE medicalrecords SET diagnosis = %s, symptom = %s WHERE id = %s",
                    [data.diagnosis, data.symptom, data.medicalrecordsid]
                )
        except Exception as e:
            logger.error(f"Update appointment error: {str(e)}")
            raise 