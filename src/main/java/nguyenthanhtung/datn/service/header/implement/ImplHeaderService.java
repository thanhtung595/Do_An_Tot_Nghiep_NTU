package nguyenthanhtung.datn.service.header.implement;

import lombok.RequiredArgsConstructor;
import nguyenthanhtung.datn.constants.ContantApplication;
import nguyenthanhtung.datn.dto.base.BaseResponse;
import nguyenthanhtung.datn.repository.header.HeaderRepository;
import nguyenthanhtung.datn.service.header.HeaderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class ImplHeaderService implements HeaderService {

    private final HeaderRepository headerRepository;

    @Override
    public BaseResponse getHeader() {
        BaseResponse response = new BaseResponse();

        try {

            List<Map<String, Object>> data = headerRepository.getHeader();

            response.setHttpStatusCode(ContantApplication.HttpStatus_OK);
            response.setStatus(ContantApplication.StatusSuccess);
            response.setMessage(ContantApplication.MessageSuccess);
            response.addData("header", data);
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            response.setHttpStatusCode(ContantApplication.HttpStatus_Internal_Server_Error);
            response.setStatus(ContantApplication.StatusErrorBugException);
            response.setMessage(e.getMessage());
        }

        return response;
    }

    @Override
    public BaseResponse getHeaderAdmin() {
        BaseResponse response = new BaseResponse();

        try {

            List<Map<String, Object>> data = headerRepository.getHeaderAdmin();

            response.setHttpStatusCode(ContantApplication.HttpStatus_OK);
            response.setStatus(ContantApplication.StatusSuccess);
            response.setMessage(ContantApplication.MessageSuccess);
            response.addData("header", data);
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            response.setHttpStatusCode(ContantApplication.HttpStatus_Internal_Server_Error);
            response.setStatus(ContantApplication.StatusErrorBugException);
            response.setMessage(e.getMessage());
        }

        return response;
    }
}
