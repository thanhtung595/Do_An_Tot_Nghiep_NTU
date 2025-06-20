package nguyenthanhtung.datn.service.feedback.implement;

import lombok.RequiredArgsConstructor;
import nguyenthanhtung.datn.config.requestThreadLocal.RequestThreadLocalContext;
import nguyenthanhtung.datn.config.requestThreadLocal.RequestUserThreadLocal;
import nguyenthanhtung.datn.constants.ContantApplication;
import nguyenthanhtung.datn.dto.base.BaseResponse;
import nguyenthanhtung.datn.dto.feedback.request.CreateFeedbackDTO;
import nguyenthanhtung.datn.repository.feedback.FeedbackRepository;
import nguyenthanhtung.datn.service.feedback.FeedbackService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class ImplFeedbackService implements FeedbackService {

    private final FeedbackRepository feedbackRepository;

    @Override
    public BaseResponse getAll(int limit) {
        BaseResponse response = new BaseResponse();
        try {
            List<Map<String, Object>> data = feedbackRepository.getAll(limit);

            response.setHttpStatusCode(ContantApplication.HttpStatus_OK);
            response.setStatus(ContantApplication.StatusSuccess);
            response.setMessage(ContantApplication.MessageSuccess);
            response.addData("feedback", data);
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            response.setHttpStatusCode(ContantApplication.HttpStatus_Internal_Server_Error);
            response.setStatus(ContantApplication.StatusErrorBugException);
            response.setMessage(e.getMessage());
        }

        return response;
    }

    @Override
    public BaseResponse craeteFeedback(CreateFeedbackDTO createFeedbackDTO) {
        BaseResponse response = new BaseResponse();
        try {
            RequestUserThreadLocal currentUser = RequestThreadLocalContext.get();
            int idUser = currentUser.getUserId();
            createFeedbackDTO.setIdUser(idUser);
            feedbackRepository.craeteFeedback(createFeedbackDTO);

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
}
