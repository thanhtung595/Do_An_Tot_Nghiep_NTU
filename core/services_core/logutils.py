import logging
import os
from datetime import datetime

class LogUtils:
    def __init__(self, log_dir="logs"):
        # Tự động tạo thư mục logs nếu chưa có
        os.makedirs(log_dir, exist_ok=True)

        # Lưu giờ hiện tại để theo dõi khi cần đổi file log
        self.current_hour = datetime.now().strftime("%Y-%m-%d-%H")
        self.log_dir = log_dir

        # Cấu hình logger
        self.logger = logging.getLogger("LogUtils")
        self.logger.setLevel(logging.INFO)

        # Xóa các handler cũ nếu có (tránh ghi log trùng)
        if self.logger.hasHandlers():
            self.logger.handlers.clear()

        # Tạo handler ghi log vào file
        self._create_log_handler()

    def _create_log_handler(self):
        """Tạo handler mới mỗi khi sang giờ mới"""
        log_filename = f"{self.current_hour}.log"
        log_path = os.path.join(self.log_dir, log_filename)

        # Xóa tất cả handler cũ trước khi thêm mới
        self.logger.handlers.clear()

        # Tạo handler mới
        handler = logging.FileHandler(log_path, encoding="utf-8")
        handler.setFormatter(logging.Formatter("%(asctime)s: %(levelname)s: %(message)s"))
        
        self.logger.addHandler(handler)

    def _check_log_rotation(self):
        """Kiểm tra nếu đã sang giờ mới thì tạo file log mới"""
        new_hour = datetime.now().strftime("%Y-%m-%d-%H")
        if new_hour != self.current_hour:
            self.current_hour = new_hour
            self._create_log_handler()

    def LogInfo(self, subject, message):
        self._check_log_rotation()
        self.logger.info(f"[{subject}] {message}")

    def LogError(self, subject, message):
        self._check_log_rotation()
        self.logger.error(f"[{subject}] {message}")

    def LogWarning(self, subject, message):
        self._check_log_rotation()
        self.logger.warning(f"[{subject}] {message}")

    def LogStart(self, subject, msg="START."):
        self.LogInfo(subject, msg)

    def LogEnd(self, subject, msg="END."):
        self.LogInfo(subject, msg)
        

# Khởi tạo instance chung
log_utils = LogUtils()
