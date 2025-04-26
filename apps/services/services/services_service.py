from django.db import connection
import logging
from core.services_core.logutils import log_utils
from apps.services.constants.services_query_constants import SELECT_ALL_SERVICE

logger = logging.getLogger(__name__)

class ServiceService:
    @staticmethod
    def get_all_services():
        try:
            with connection.cursor() as cursor:
                cursor.execute(SELECT_ALL_SERVICE)
                services = cursor.fetchall()

            if not services:
                return [], "No services found", 200

            service_list = [
                dict(zip([col[0] for col in cursor.description], row)) 
                for row in services
            ]

            return service_list, "Success", 200
        except Exception as e:
            logger.error(f"Get services error: {str(e)}")
            return None, "Internal Server Error", 500 