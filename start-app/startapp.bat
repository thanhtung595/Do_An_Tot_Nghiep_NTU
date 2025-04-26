@echo off
setlocal enabledelayedexpansion

:: Di chuyển về thư mục gốc (chứa manage.py)
cd ..

:: Thư mục chứa apps
set APP_DIR=apps

:: Tên app cần tạo
set APP_NAME=services

:: Đường dẫn đầy đủ
set FULL_PATH=%APP_DIR%\%APP_NAME%

:: Tạo thư mục app nếu chưa có
mkdir %APP_DIR%\%APP_NAME%

:: Tạo app bằng lệnh Django
python manage.py startapp %APP_NAME% %FULL_PATH%

:: Tạo các thư mục con constants, dtos, services
mkdir %FULL_PATH%\constants
mkdir %FULL_PATH%\dtos
mkdir %FULL_PATH%\services

:: Tạo các file __init__.py trong các thư mục
type nul > %FULL_PATH%\constants\__init__.py
type nul > %FULL_PATH%\dtos\__init__.py
type nul > %FULL_PATH%\services\__init__.py

:: Tạo các file mẫu constants, dtos, services
type nul > %FULL_PATH%\constants\%APP_NAME%_constants.py
type nul > %FULL_PATH%\constants\%APP_NAME%_query_constants.py
type nul > %FULL_PATH%\dtos\%APP_NAME%_dto.py
type nul > %FULL_PATH%\services\%APP_NAME%_service.py

:: Tạo các file serializers.py và urls.py nếu chưa có
if not exist %FULL_PATH%\serializers.py type nul > %FULL_PATH%\serializers.py
if not exist %FULL_PATH%\urls.py type nul > %FULL_PATH%\urls.py

echo App %APP_NAME% created successfully at %FULL_PATH%
pause
