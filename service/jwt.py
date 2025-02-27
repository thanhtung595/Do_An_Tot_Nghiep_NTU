import jwt
from datetime import datetime, timedelta
from django.conf import settings

SECRET_KEY = settings.SECRET_KEY

def create_jwt_token(user_id):
    payload = {
        "user_id": user_id,
        "exp": datetime.utcnow() + timedelta(days=1),
        "iat": datetime.utcnow()
    }
    return jwt.encode(payload, SECRET_KEY, algorithm="HS256")
