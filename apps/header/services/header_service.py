from django.db import connection
import logging
from apps.header.constants.header_query_constants import GET_ALL_HEADER

logger = logging.getLogger(__name__)

class HeaderService:
    @staticmethod
    def get_header(is_logged_in: bool):
        try:
            isToken = "1" if is_logged_in else "0"

            with connection.cursor() as cursor:
                cursor.execute(GET_ALL_HEADER, [isToken])
                header = cursor.fetchall()

            if not header:
                return [], "No header data found", 400

            header_list = [
                dict(zip([col[0] for col in cursor.description], row)) 
                for row in header
            ]

            return header_list, "Success", 200
        except Exception as e:
            logger.error(f"Get header error: {str(e)}")
            return None, "Internal Server Error", 500 