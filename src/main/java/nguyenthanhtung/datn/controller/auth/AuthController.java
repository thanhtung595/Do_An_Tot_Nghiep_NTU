package nguyenthanhtung.datn.controller.auth;

import lombok.RequiredArgsConstructor;
import nguyenthanhtung.datn.config.requestThreadLocal.RequestThreadLocalContext;
import nguyenthanhtung.datn.config.requestThreadLocal.RequestUserThreadLocal;
import nguyenthanhtung.datn.constants.ContantApplication;
import nguyenthanhtung.datn.dto.auth.request.RequestLoginDTO;
import nguyenthanhtung.datn.dto.auth.request.RequestRegisterDTO;
import nguyenthanhtung.datn.dto.base.BaseResponse;
import nguyenthanhtung.datn.service.auth.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<BaseResponse> register(@RequestBody RequestRegisterDTO requestRegisterDTO) {
        String subject = "@Post Auth Register";

        BaseResponse response = authService.register(requestRegisterDTO);
        response.setSubjectFunction(subject);

        return ResponseEntity.status(response.getHttpStatusCode()).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<BaseResponse> login(@RequestBody RequestLoginDTO requestLoginDTO) {
        String subject = "@Post Auth Login";

        BaseResponse response = authService.login(requestLoginDTO);
        response.setSubjectFunction(subject);

        return ResponseEntity.status(response.getHttpStatusCode()).body(response);
    }

    @GetMapping("/required-admin")
    public ResponseEntity<BaseResponse> requiredRoleAdmin(){
        String subject = "@Get Required Role Admin";
        BaseResponse response = new BaseResponse();

        try {
            RequestUserThreadLocal currentUser = RequestThreadLocalContext.get();
            if (currentUser != null && Objects.equals(currentUser.getRole(), ContantApplication.RoleAdmin)){
                response.setSubjectFunction(subject);
                response.setHttpStatusCode(ContantApplication.HttpStatus_OK);
                response.setStatus(ContantApplication.StatusSuccess);
            } else {
                response.setSubjectFunction(subject);
                response.setHttpStatusCode(ContantApplication.HttpStatus_Forbidden);
                response.setStatus(ContantApplication.StatusErrorClient);
            }
        }catch (Exception e){
            response.setSubjectFunction(subject);
            response.setHttpStatusCode(ContantApplication.HttpStatus_Forbidden);
            response.setStatus(ContantApplication.StatusErrorClient);
        }
        return ResponseEntity.status(response.getHttpStatusCode()).body(response);
    }

    @GetMapping("/required-doctor")
    public ResponseEntity<BaseResponse> requiredRoleDoctor(){
        String subject = "@Get Required Role Doctor";
        BaseResponse response = new BaseResponse();

        try {
            RequestUserThreadLocal currentUser = RequestThreadLocalContext.get();
            if (currentUser != null && Objects.equals(currentUser.getRole(), ContantApplication.RoleDoctor)){
                response.setSubjectFunction(subject);
                response.setHttpStatusCode(ContantApplication.HttpStatus_OK);
                response.setStatus(ContantApplication.StatusSuccess);
            } else {
                response.setSubjectFunction(subject);
                response.setHttpStatusCode(ContantApplication.HttpStatus_Forbidden);
                response.setStatus(ContantApplication.StatusErrorClient);
            }
        }catch (Exception e){
            response.setSubjectFunction(subject);
            response.setHttpStatusCode(ContantApplication.HttpStatus_Forbidden);
            response.setStatus(ContantApplication.StatusErrorClient);
        }
        return ResponseEntity.status(response.getHttpStatusCode()).body(response);
    }

    @GetMapping("/required-patient")
    public ResponseEntity<BaseResponse> requiredRolePatient(){
        String subject = "@Get Required Role Patient";
        BaseResponse response = new BaseResponse();

        try {
            RequestUserThreadLocal currentUser = RequestThreadLocalContext.get();
            if (currentUser != null && Objects.equals(currentUser.getRole(), ContantApplication.RolePatient)){
                response.setSubjectFunction(subject);
                response.setHttpStatusCode(ContantApplication.HttpStatus_OK);
                response.setStatus(ContantApplication.StatusSuccess);
            } else {
                response.setSubjectFunction(subject);
                response.setHttpStatusCode(ContantApplication.HttpStatus_Forbidden);
                response.setStatus(ContantApplication.StatusErrorClient);
            }
        }catch (Exception e){
            response.setSubjectFunction(subject);
            response.setHttpStatusCode(ContantApplication.HttpStatus_Forbidden);
            response.setStatus(ContantApplication.StatusErrorClient);
        }
        return ResponseEntity.status(response.getHttpStatusCode()).body(response);
    }
}
