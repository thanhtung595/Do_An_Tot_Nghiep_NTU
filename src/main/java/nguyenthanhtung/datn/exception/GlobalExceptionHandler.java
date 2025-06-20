package nguyenthanhtung.datn.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nguyenthanhtung.datn.constants.ContantApplication;
import nguyenthanhtung.datn.dto.base.BaseResponse;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    private void writeErrorResponse(HttpServletResponse response,
                                    int status,
                                    String subject,
                                    String statusCode,
                                    String message) throws IOException {
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setSubjectFunction(subject);
        baseResponse.setHttpStatusCode(status);
        baseResponse.setStatus(statusCode);
        baseResponse.setMessage(message);

        response.setStatus(status);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        objectMapper.writeValue(response.getWriter(), baseResponse);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public void handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                             HttpServletRequest request,
                                             HttpServletResponse response) throws IOException {
        writeErrorResponse(
                response,
                ContantApplication.HttpStatus_BAD_REQUEST,
                "INVALID_REQUEST_BODY",
                ContantApplication.StatusErrorClient,
                "Dữ liệu body không hợp lệ hoặc bị thiếu."
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public void handleValidationError(MethodArgumentNotValidException ex,
                                      HttpServletRequest request,
                                      HttpServletResponse response) throws IOException {
        writeErrorResponse(
                response,
                ContantApplication.HttpStatus_BAD_REQUEST,
                "VALIDATION_ERROR",
                ContantApplication.StatusErrorClient,
                "Lỗi validate dữ liệu đầu vào."
        );
    }

    @ExceptionHandler(Exception.class)
    public void handleGenericException(Exception ex,
                                       HttpServletRequest request,
                                       HttpServletResponse response) throws IOException {
        int status = response.getStatus();

        // Nếu status vẫn là 200 thì đặt mặc định là 500
        if (status == 200) {
            status = ContantApplication.HttpStatus_Internal_Server_Error;
        }

        writeErrorResponse(
                response,
                status,
                "UNEXPECTED_ERROR",
                ContantApplication.StatusErrorServer,
                ex.getMessage()
        );
    }
}