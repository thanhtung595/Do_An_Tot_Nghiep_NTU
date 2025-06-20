package nguyenthanhtung.datn.service.feedback;

import nguyenthanhtung.datn.dto.base.BaseResponse;
import nguyenthanhtung.datn.dto.feedback.request.CreateFeedbackDTO;

public interface FeedbackService {
    BaseResponse getAll(int limit);
    BaseResponse craeteFeedback(CreateFeedbackDTO createFeedbackDTO);
}
