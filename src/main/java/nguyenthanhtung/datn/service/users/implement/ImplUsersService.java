package nguyenthanhtung.datn.service.users.implement;

import lombok.RequiredArgsConstructor;
import nguyenthanhtung.datn.config.requestThreadLocal.RequestThreadLocalContext;
import nguyenthanhtung.datn.config.requestThreadLocal.RequestUserThreadLocal;
import nguyenthanhtung.datn.constants.ContantApplication;
import nguyenthanhtung.datn.dto.base.BaseResponse;
import nguyenthanhtung.datn.dto.users.request.EditPasswordRequestDTO;
import nguyenthanhtung.datn.dto.users.request.UpdateRequestDTO;
import nguyenthanhtung.datn.entity.base.RepositoryResult;
import nguyenthanhtung.datn.repository.notification.NotificationRepository;
import nguyenthanhtung.datn.repository.users.UsersRepository;
import nguyenthanhtung.datn.service.notification.NotificationService;
import nguyenthanhtung.datn.service.users.UsersService;
import nguyenthanhtung.datn.util.IdQueryUserUtils;
import nguyenthanhtung.datn.util.S3_Service.FileS3Service;
import nguyenthanhtung.datn.util.WebSocket.WebSocketService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.multipart.MultipartFile;

import java.security.SecureRandom;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class ImplUsersService implements UsersService {

    private final UsersRepository usersRepository;
    private final FileS3Service s3Service;

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final SecureRandom random = new SecureRandom();

    @Override
    public BaseResponse getProfile() {
        BaseResponse response = new BaseResponse();
        try {
            Map<String, Object> data = usersRepository.getProfile();

            response.setHttpStatusCode(ContantApplication.HttpStatus_OK);
            response.setStatus(ContantApplication.StatusSuccess);
            response.setMessage(ContantApplication.MessageSuccess);
            response.addData("user", data);
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            response.setHttpStatusCode(ContantApplication.HttpStatus_Internal_Server_Error);
            response.setStatus(ContantApplication.StatusErrorBugException);
            response.setMessage(e.getMessage());
        }

        return response;
    }

    @Override
    public BaseResponse getAllUser() {
        BaseResponse response = new BaseResponse();
        try {
            List<Map<String, Object>> data = usersRepository.getAllUser();

            response.setHttpStatusCode(ContantApplication.HttpStatus_OK);
            response.setStatus(ContantApplication.StatusSuccess);
            response.setMessage(ContantApplication.MessageSuccess);
            response.addData("users", data);
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            response.setHttpStatusCode(ContantApplication.HttpStatus_Internal_Server_Error);
            response.setStatus(ContantApplication.StatusErrorBugException);
            response.setMessage(e.getMessage());
        }

        return response;
    }

    @Override
    public BaseResponse updateUser(UpdateRequestDTO updateRequestDTO) {
        BaseResponse response = new BaseResponse();
        try {
            usersRepository.updateUser(updateRequestDTO);

            response.setHttpStatusCode(ContantApplication.HttpStatus_OK);
            response.setStatus(ContantApplication.StatusSuccess);
            response.setMessage(ContantApplication.MessageSuccess);
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            response.setHttpStatusCode(ContantApplication.HttpStatus_Internal_Server_Error);
            response.setStatus(ContantApplication.StatusErrorBugException);
            response.setMessage(e.getMessage());
        }

        return response;
    }

    @Override
    public BaseResponse editPassword(EditPasswordRequestDTO editPasswordRequestDTO) {
        BaseResponse response = new BaseResponse();
        try {

            if (editPasswordRequestDTO.getNewPassword().length() < 6){
                response.setHttpStatusCode(ContantApplication.HttpStatus_BAD_REQUEST);
                response.setStatus(ContantApplication.StatusErrorClient);
                response.setMessage("Mật khẩu phải có ít nhất 6 ký tự.");
                return response;
            }

            RepositoryResult result = usersRepository.editPassword(editPasswordRequestDTO);
            if (Objects.equals(result.getStatusCode(), ContantApplication.StatusSuccess_No_Data)){
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                response.setHttpStatusCode(ContantApplication.HttpStatus_BAD_REQUEST);
                response.setStatus(ContantApplication.StatusErrorClient);
                response.setMessage(result.getMessage());
                return response;
            }

            response.setHttpStatusCode(ContantApplication.HttpStatus_OK);
            response.setStatus(ContantApplication.StatusSuccess);
            response.setMessage(ContantApplication.MessageSuccess);
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            response.setHttpStatusCode(ContantApplication.HttpStatus_Internal_Server_Error);
            response.setStatus(ContantApplication.StatusErrorBugException);
            response.setMessage(e.getMessage());
        }

        return response;
    }

    @Override
    public BaseResponse uploadImage(MultipartFile file) {
        BaseResponse response = new BaseResponse();
        String path;
        String fileNameNew;
        try {
            RequestUserThreadLocal currentUser = RequestThreadLocalContext.get();

            if(Objects.equals(currentUser.getRole(), ContantApplication.RolePatient)){
                path = System.getProperty("PATH_AVATAR_PATIENTS");
            } else if (Objects.equals(currentUser.getRole(), ContantApplication.RoleDoctor)){
                path = System.getProperty("PATH_AVATAR_DOCTOR");
            } else {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                response.setHttpStatusCode(ContantApplication.HttpStatus_BAD_REQUEST);
                response.setStatus(ContantApplication.StatusErrorBugException);
                response.setMessage("Error Server....");
                return response;
            }

            fileNameNew = "phong-kham-thien-an-" + "25476" + currentUser.getUserId() + "6547";
            String fileName = s3Service.uploadFile(file, fileNameNew, path);

            usersRepository.uploadImage(currentUser.getUserId(), fileName);

            response.setHttpStatusCode(ContantApplication.HttpStatus_OK);
            response.setStatus(ContantApplication.StatusSuccess);
            response.setMessage(ContantApplication.MessageSuccess);
            response.addData("image", System.getProperty("URL_AWS_STORAGE") + path + fileName);

        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            response.setHttpStatusCode(ContantApplication.HttpStatus_Internal_Server_Error);
            response.setStatus(ContantApplication.StatusErrorBugException);
            response.setMessage(e.getMessage());
        }

        return response;
    }

    public static String generateRandomString(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }
        return sb.toString();
    }
}
