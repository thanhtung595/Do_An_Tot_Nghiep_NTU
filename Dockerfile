# Bước 1: Build Angular
FROM node:20-alpine AS build-stage

WORKDIR /app

# Copy package.json và package-lock.json để cài nhanh hơn
COPY package*.json ./

# Cài đặt dependencies
RUN npm install

# Copy toàn bộ mã nguồn vào container
COPY . .

# Build Angular ở chế độ production
RUN npm run build -- --configuration=production

# Bước 2: NGINX phục vụ app Angular đã build
FROM nginx:alpine AS production-stage

# Copy file build vào NGINX
COPY --from=build-stage /app/dist/project-datn /usr/share/nginx/html

# (Tuỳ chọn) Thêm file cấu hình nginx nếu cần redirect, SPA routing
# COPY nginx.conf /etc/nginx/nginx.conf

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]
