package nguyenthanhtung.datn.service.auth.implement;

import lombok.RequiredArgsConstructor;
import nguyenthanhtung.datn.constants.ContantApplication;
import nguyenthanhtung.datn.dto.auth.request.RequestLoginDTO;
import nguyenthanhtung.datn.dto.auth.request.RequestRegisterDTO;
import nguyenthanhtung.datn.dto.auth.response.ResponseLogin;
import nguyenthanhtung.datn.dto.base.BaseResponse;
import nguyenthanhtung.datn.entity.auth.LoginEntity;
import nguyenthanhtung.datn.entity.base.RepositoryResult;
import nguyenthanhtung.datn.exception.ConflictException;
import nguyenthanhtung.datn.repository.auth.AuthRepository;
import nguyenthanhtung.datn.service.auth.AuthService;
import nguyenthanhtung.datn.util.Jwt.JwtTokenProvider;
import nguyenthanhtung.datn.util.TextUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class ImplAuthService implements AuthService {

    private final AuthRepository authRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public BaseResponse register(RequestRegisterDTO requestRegisterDTO) {
        BaseResponse response = new BaseResponse();
        RepositoryResult result;
        String message;

        try {
            if (TextUtils.isNullOrBlank(requestRegisterDTO.getUsername())
                || TextUtils.isNullOrBlank(requestRegisterDTO.getEmail())
                || TextUtils.isNullOrBlank(requestRegisterDTO.getFullname())
                || TextUtils.isNullOrBlank(requestRegisterDTO.getPhone())
                || TextUtils.isNullOrBlank(requestRegisterDTO.getPassword())){

                message = "Chưa nhập đầy đủ thông tin.";
                response.setHttpStatusCode(ContantApplication.HttpStatus_BAD_REQUEST);
                response.setStatus(ContantApplication.StatusErrorClient);
                response.setMessage(message);

                return response;
            }

            if (requestRegisterDTO.getPassword().length() < 6){
                response.setHttpStatusCode(ContantApplication.HttpStatus_BAD_REQUEST);
                response.setStatus(ContantApplication.StatusErrorClient);
                response.setMessage("Mật khẩu phải có ít nhất 6 ký tự.");
                return response;
            }

            result = authRepository.register(requestRegisterDTO);
            if (Objects.equals(result.getStatusCode(), ContantApplication.StatusWarning_Conflict)){
                throw new ConflictException(result.getMessage());
            }

            response.setHttpStatusCode(ContantApplication.HttpStatus_OK);
            response.setStatus(ContantApplication.StatusSuccess);
            response.setMessage(ContantApplication.MessageSuccess);
        } catch (ConflictException e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            response.setHttpStatusCode(ContantApplication.HttpStatus_Conflict);
            response.setStatus(ContantApplication.StatusErrorClient);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            response.setHttpStatusCode(ContantApplication.HttpStatus_Internal_Server_Error);
            response.setStatus(ContantApplication.StatusErrorBugException);
            response.setMessage(e.getMessage());
        }

        return response;
    }

    @Override
    public BaseResponse login(RequestLoginDTO requestLoginDTO) {
        BaseResponse response = new BaseResponse();
        String message;

        try {
            LoginEntity loginEntity = authRepository.login(requestLoginDTO);

            if (loginEntity == null){
                message = "Tài khoản hoặc mật khẩu không chính xác.";
                response.setHttpStatusCode(ContantApplication.HttpStatus_NOT_FOUND);
                response.setStatus(ContantApplication.StatusSuccess_No_Data);
                response.setMessage(message);

                return response;
            }

            ResponseLogin responseLogin = new ResponseLogin();
            String validityAc = System.getProperty("VALIDITY_INMILLI_SECONDS_ACCESS", "1");
            String validityRf = System.getProperty("VALIDITY_INMILLI_SECONDS_REFESH", "7");
            Map<String, Object> claims = Map.of(
                    ContantApplication.ClaimUserID, loginEntity.getId(),
                    ContantApplication.ClaimRoleUser, loginEntity.getRole()
            );

            String accessToken = jwtTokenProvider.createToken(claims, validityAc);
            String refeshToken = jwtTokenProvider.createToken(claims, validityRf);

            responseLogin.setAccessToken(accessToken);
            responseLogin.setRefeshToken(refeshToken);

            response.setHttpStatusCode(ContantApplication.HttpStatus_OK);
            response.setStatus(ContantApplication.StatusSuccess);
            response.setMessage(ContantApplication.MessageSuccess);
            response.addData("token", responseLogin);
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            response.setHttpStatusCode(ContantApplication.HttpStatus_Internal_Server_Error);
            response.setStatus(ContantApplication.StatusErrorBugException);
            response.setMessage(e.getMessage());
        }

        return response;
    }
}
