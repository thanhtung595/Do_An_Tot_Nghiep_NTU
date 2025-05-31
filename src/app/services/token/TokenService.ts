// auth-storage.service.ts (hoặc bất kỳ service nào bạn dùng để lưu token)
const ACCESS_TOKEN_KEY = 'accessToken';
const REFESH_TOKEN_KEY = 'refeshToken';
const ACCESS_EXPIRY_KEY = 'accessTokenExpiry';
const REFESH_EXPIRY_KEY = 'refeshTokenExpiry';

export function saveAccessToken(token: string) {
  const now = new Date();
  const expiryTime = now.getTime() + 24 * 60 * 60 * 1000; // 1 ngày = 86400000 ms

  localStorage.setItem(ACCESS_TOKEN_KEY, token);
  localStorage.setItem(ACCESS_EXPIRY_KEY, expiryTime.toString());
}

export function saveRefeshToken(token: string) {
  const now = new Date();
  const expiryTime = now.getTime() + 24 * 60 * 60 * 1000; // 1 ngày = 86400000 ms

  localStorage.setItem(REFESH_TOKEN_KEY, token);
  localStorage.setItem(REFESH_EXPIRY_KEY, expiryTime.toString());
}

export function getAccessToken(): string | null {
  const expiry = localStorage.getItem(ACCESS_EXPIRY_KEY);
  const token = localStorage.getItem(ACCESS_TOKEN_KEY);

  if (!expiry || !token) {
    return null;
  }

  const now = new Date().getTime();
  const expiryTime = parseInt(expiry, 10);

  if (now > expiryTime) {
    // Token hết hạn ⇒ xóa
    localStorage.removeItem(ACCESS_TOKEN_KEY);
    localStorage.removeItem(ACCESS_EXPIRY_KEY);
    return null;
  }

  return token;
}

export function clearAccessToken() {
  localStorage.removeItem(ACCESS_TOKEN_KEY);
  localStorage.removeItem(ACCESS_EXPIRY_KEY);
}

