package nguyenthanhtung.datn.repository.feedback;

import nguyenthanhtung.datn.dto.feedback.request.CreateFeedbackDTO;

import java.util.List;
import java.util.Map;

public interface FeedbackRepository {
    List<Map<String, Object>> getAll(int limit);
    void craeteFeedback(CreateFeedbackDTO createFeedbackDTO);
}
