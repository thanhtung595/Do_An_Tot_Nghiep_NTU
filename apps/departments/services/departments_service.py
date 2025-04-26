from django.db import connection
import logging
from apps.departments.constants.departments_query_constants import SELECT_ALL_Departments

logger = logging.getLogger(__name__)

class DepartmentService:
    @staticmethod
    def get_all_departments():
        try:
            with connection.cursor() as cursor:
                cursor.execute(SELECT_ALL_Departments)
                departments = cursor.fetchall()

            if not departments:
                return [], "No departments found", 200

            departments_list = [
                dict(zip([col[0] for col in cursor.description], row)) 
                for row in departments
            ]

            return departments_list, "Success", 200
        except Exception as e:
            logger.error(f"Get departments error: {str(e)}")
            return None, "Internal Server Error", 500 