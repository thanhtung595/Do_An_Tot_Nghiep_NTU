package nguyenthanhtung.datn.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nguyenthanhtung.datn.constants.ContantApplication;
import nguyenthanhtung.datn.dto.base.BaseResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {

        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setSubjectFunction("ACCESS_DENIED");
        baseResponse.setHttpStatusCode(ContantApplication.HttpStatus_Forbidden); // 403
        baseResponse.setStatus(ContantApplication.StatusErrorClient);
        baseResponse.setMessage("Bạn không có quyền truy cập tài nguyên này.");

        response.setStatus(ContantApplication.HttpStatus_Forbidden);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        ObjectMapper mapper = new ObjectMapper();
        response.getWriter().write(mapper.writeValueAsString(baseResponse));
    }
}
